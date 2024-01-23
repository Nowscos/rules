package com.nowscas.rules.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowscas.rules.config.BotProperties;
import com.nowscas.rules.exception.StalkerException;
import com.nowscas.rules.model.StalkerEntity;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.nowscas.rules.util.Constants.ALREADY_REGISTERED_RUS;
import static com.nowscas.rules.util.Constants.BAD_TESTING_MESSAGE;
import static com.nowscas.rules.util.Constants.CHOOSE_YOUR_GROUP_RUS;
import static com.nowscas.rules.util.Constants.FILE_PATH;
import static com.nowscas.rules.util.Constants.FINISH_TEST;
import static com.nowscas.rules.util.Constants.INVALID_COMMAND_ERROR;
import static com.nowscas.rules.util.Constants.INVALID_FILE_UPLOAD_ERROR;
import static com.nowscas.rules.util.Constants.QUESTIONS_NOT_EXIST;
import static com.nowscas.rules.util.Constants.QUESTION_UPDATED;
import static com.nowscas.rules.util.Constants.REGISTER_CONTINUE_BUTTON;
import static com.nowscas.rules.util.Constants.REGISTER_EXIT_BUTTON;
import static com.nowscas.rules.util.Constants.REGISTER_OFFER_RUS;
import static com.nowscas.rules.util.Constants.REGISTER_SUCCESS_RUS;
import static com.nowscas.rules.util.Constants.REGISTRATION_CONTINUES_MESSAGE;
import static com.nowscas.rules.util.Constants.RESULT;
import static com.nowscas.rules.util.Constants.SEND_EDIT_MESSAGE_EXCEPTION;
import static com.nowscas.rules.util.Constants.SEND_MESSAGE_EXCEPTION;
import static com.nowscas.rules.util.Constants.STALKER_STATE_FILLED;
import static com.nowscas.rules.util.Constants.STALKER_STATE_NEW;
import static com.nowscas.rules.util.Constants.STALKER_STATE_WAIT_FOR_GROUP;
import static com.nowscas.rules.util.Constants.SUCCESS_TESTING_MESSAGE;
import static com.nowscas.rules.util.Constants.TESTING;
import static com.nowscas.rules.util.Constants.TESTING_CONTINUE_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_EXIT_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_IS_FINISHED_MESSAGE;
import static com.nowscas.rules.util.Constants.TESTING_OFFER_RUS;

@Slf4j
@Component
@RequiredArgsConstructor
public class RulesBot extends TelegramLongPollingBot {

    @Value("${app.bot.admins}")
    private List<Long> admins;

    private final BotProperties botProperties;
    private final StalkerService stalkerService;
    private final InitialService rulesService;
    private final QuestionService questionService;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            long chatId = update.getMessage().getChatId();
            if (update.getMessage().getDocument() != null) {
                if (admins.contains(chatId)) {
                    uploadFile(update.getMessage().getDocument().getFileId(), chatId);
                } else {
                    sendMessage(chatId, INVALID_FILE_UPLOAD_ERROR, null);
                }
            } else if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                try {
                    StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
                    if (STALKER_STATE_NEW.equals(stalkerByChatId.getState())) {
                        stalkerByChatId.setStalkerName(messageText);
                        stalkerByChatId.setState(STALKER_STATE_WAIT_FOR_GROUP);
                        stalkerService.saveStalker(stalkerByChatId);
                        sendReceiveGroupMessage(chatId);
                        return;
                    }
                } catch (StalkerException ignored) {
                }

                switch (messageText) {
                    case "/start":
                        try {
                            StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
                            if (STALKER_STATE_WAIT_FOR_GROUP.equals(stalkerByChatId.getState())) {
                                sendMessage(chatId, REGISTRATION_CONTINUES_MESSAGE, null);
                            } else if (STALKER_STATE_FILLED.equals(stalkerByChatId.getState())) {
                                sendAuthMessage(chatId, stalkerByChatId);
                            } else if (FINISH_TEST.equals(stalkerByChatId.getState())) {
                                sendMessage(chatId, TESTING_IS_FINISHED_MESSAGE, null);
                            }
                        } catch (StalkerException e) {
                            sendRegisterMessage(chatId);
                        }
                        break;
                    case "/fillQuestions":
                        if (admins.contains(chatId)) {
                            sendMessage(chatId, QUESTION_UPDATED, null);
                            break;
                        } else {
                            sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                        }
                    default:
                        sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Chat chat = update.getCallbackQuery().getMessage().getChat();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            EditMessageText message;
            if (REGISTER_CONTINUE_BUTTON.equals(callbackData)) {
                message = rulesService.processContinueRegisterButton(chatId, messageId);
                stalkerService.saveNewStalker(chat);
                sendEditMessage(message, chatId);
            }
            if (REGISTER_EXIT_BUTTON.equals(callbackData)) {
                message = rulesService.processExitRegisterButton(chatId, messageId);
                sendEditMessage(message, chatId);
            }

            StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
            if (TESTING_CONTINUE_BUTTON.equals(callbackData)) {
                message = rulesService.processStartTestingButton(chatId, messageId);
                sendEditMessage(message, chatId);
                stalkerByChatId.setState(TESTING);
                stalkerService.saveStalker(stalkerByChatId);

                sendQuestionMessage(stalkerByChatId, chatId);
            }
            if (TESTING_EXIT_BUTTON.equals(callbackData)) {
                message = rulesService.processExitTestingButton(chatId, messageId);
                sendEditMessage(message, chatId);
            }

            if (STALKER_STATE_WAIT_FOR_GROUP.equals(stalkerByChatId.getState())) {
                message = rulesService.processGroupChoiceMessage(update.getCallbackQuery());
                if (REGISTER_SUCCESS_RUS.equals(message.getText())) {
                    sendEditMessage(message, chatId);
                    InlineKeyboardMarkup inlineKeyboardMarkup = rulesService.prepareTestingMarkup();
                    sendMessage(chatId, TESTING_OFFER_RUS, inlineKeyboardMarkup);
                } else {
                    sendEditMessage(message, chatId);
                    sendReceiveGroupMessage(chatId);
                }
            } else if (TESTING.equals(stalkerByChatId.getState())) {
                boolean needMore = questionService.processAnswerMessage(update.getCallbackQuery(), stalkerByChatId);
                if (needMore) {
                    sendQuestionMessage(stalkerByChatId, chatId);
                } else {
                    if (stalkerByChatId.getCurrentAnswers() < 2) {
                        stalkerByChatId.setState(FINISH_TEST);
                        sendBadTestingMessage(chatId);
                    } else {
                        stalkerByChatId.setState(FINISH_TEST);
                        sendSuccessTestingMessage(chatId);
                    }
                    stalkerByChatId.setAttempts(stalkerByChatId.getAttempts() + 1);
                    stalkerByChatId.setCurrentAnswers(0);
                    stalkerByChatId.setPassedQuestions(null);
                    stalkerService.saveStalker(stalkerByChatId);
                }
            }
        }

    }

    private void sendAuthMessage(long chatId, StalkerEntity stalkerEntity) {
        String answer = String.format(ALREADY_REGISTERED_RUS, stalkerEntity.getStalkerName(), stalkerEntity.getGroupName());
        sendMessage(chatId, answer, null);
        InlineKeyboardMarkup inlineKeyboardMarkup = rulesService.prepareTestingMarkup();
        sendMessage(chatId, TESTING_OFFER_RUS, inlineKeyboardMarkup);
    }

    private void sendSuccessTestingMessage(long chatId) {
        sendMessage(chatId, SUCCESS_TESTING_MESSAGE, null);
    }

    private void sendBadTestingMessage(long chatId) {
        sendMessage(chatId, BAD_TESTING_MESSAGE, null);
        InlineKeyboardMarkup inlineKeyboardMarkup = rulesService.prepareTestingMarkup();
        sendMessage(chatId, TESTING_OFFER_RUS, inlineKeyboardMarkup);
    }

    private void sendRegisterMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = rulesService.prepareRegisterMarkup();
        sendMessage(chatId, REGISTER_OFFER_RUS, inlineKeyboardMarkup);
    }

    private void sendReceiveGroupMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = rulesService.prepareReceiveGroupMarkup();
        sendMessage(chatId, CHOOSE_YOUR_GROUP_RUS, inlineKeyboardMarkup);
    }

    private void sendQuestionMessage(StalkerEntity stalkerByChatId, Long chatId) {
        Pair<String, InlineKeyboardMarkup> question = questionService.prepareQuestionMarkup(stalkerByChatId);
        if (question == null) {
            sendMessage(chatId, QUESTIONS_NOT_EXIST, null);
        } else {
            sendMessage(chatId, question.getLeft(), question.getRight());
        }
    }

    private void sendMessage(long chatId, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        if (inlineKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.info(String.format(SEND_MESSAGE_EXCEPTION, chatId));
            throw new StalkerException(HttpStatus.INTERNAL_SERVER_ERROR, String.format(SEND_MESSAGE_EXCEPTION, chatId));
        }
    }

    private void sendEditMessage(EditMessageText editMessage, long chatId) {
        try {
            execute(editMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            log.info(String.format(SEND_EDIT_MESSAGE_EXCEPTION, chatId));
            throw new StalkerException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void uploadFile(String fileId, Long chatId) throws IOException {
        URL url = new URL("https://api.telegram.org/bot" + getBotToken() + "/getFile?file_id=" + fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
        String text = bufferedReader.readLine();

        JsonNode jsonNode = objectMapper.readValue(text, JsonNode.class);
        JsonNode result = jsonNode.get(RESULT);
        JsonNode filePath = result.get(FILE_PATH);
        String path = filePath.textValue();

        InputStream is = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + path).openStream();
        byte[] bytes = is.readAllBytes();

        is.close();
        bufferedReader.close();
        File file = new File("questions.json");
        FileUtils.writeByteArrayToFile(file, bytes);
        String questionsJsonString = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        questionService.fillQuestions(questionsJsonString);
        sendMessage(chatId, QUESTION_UPDATED, null);
    }
}
