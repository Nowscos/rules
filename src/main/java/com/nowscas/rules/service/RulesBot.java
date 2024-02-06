package com.nowscas.rules.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowscas.rules.config.BotProperties;
import com.nowscas.rules.exception.StalkerException;
import com.nowscas.rules.model.AnswerResponse;
import com.nowscas.rules.model.StalkerEntity;
import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static com.nowscas.rules.util.Constants.ADMIN_HELP_TEXT;
import static com.nowscas.rules.util.Constants.ALREADY_REGISTERED_RUS;
import static com.nowscas.rules.util.Constants.BAD_STALKER_NAME_MESSAGE;
import static com.nowscas.rules.util.Constants.BAD_TESTING_MESSAGE;
import static com.nowscas.rules.util.Constants.CHOOSE_YOUR_GROUP_RUS;
import static com.nowscas.rules.util.Constants.CLOSE_TESTING_ADMIN_COMMAND;
import static com.nowscas.rules.util.Constants.DELETE_MYSELF_TEMPORARY_COMMAND;
import static com.nowscas.rules.util.Constants.DELETE_YOURSELF_MESSAGE;
import static com.nowscas.rules.util.Constants.DOWNLOAD_RESULTS_ADMIN_COMMAND;
import static com.nowscas.rules.util.Constants.DO_NOT_ANSWER_START;
import static com.nowscas.rules.util.Constants.ENTER;
import static com.nowscas.rules.util.Constants.FILE_PATH;
import static com.nowscas.rules.util.Constants.FINISH_TEST;
import static com.nowscas.rules.util.Constants.HELP_BUTTON_TEXT;
import static com.nowscas.rules.util.Constants.HELP_COMMAND;
import static com.nowscas.rules.util.Constants.HELP_TEXT;
import static com.nowscas.rules.util.Constants.INVALID_COMMAND_ERROR;
import static com.nowscas.rules.util.Constants.INVALID_FILE_UPLOAD_ERROR;
import static com.nowscas.rules.util.Constants.LONG_STALKER_NAME_MESSAGE;
import static com.nowscas.rules.util.Constants.MY_INFO_BUTTON_TEXT;
import static com.nowscas.rules.util.Constants.MY_INFO_COMMAND;
import static com.nowscas.rules.util.Constants.NOT_REGISTERED_MESSAGE;
import static com.nowscas.rules.util.Constants.OPEN_TESTING_ADMIN_COMMAND;
import static com.nowscas.rules.util.Constants.QUESTIONS_NOT_EXIST;
import static com.nowscas.rules.util.Constants.QUESTION_UPDATED;
import static com.nowscas.rules.util.Constants.REGISTER_SUCCESS_RUS;
import static com.nowscas.rules.util.Constants.REGISTRATION_CONTINUES_MESSAGE;
import static com.nowscas.rules.util.Constants.REGISTRATION_NOT_FINISHED_MESSAGE;
import static com.nowscas.rules.util.Constants.RESULT;
import static com.nowscas.rules.util.Constants.RULES_FAIL_INFO_RUS;
import static com.nowscas.rules.util.Constants.RULES_SUCCESS_INFO_RUS;
import static com.nowscas.rules.util.Constants.SEND_DELETE_MESSAGE_EXCEPTION;
import static com.nowscas.rules.util.Constants.SEND_EDIT_MESSAGE_EXCEPTION;
import static com.nowscas.rules.util.Constants.SEND_MESSAGE_EXCEPTION;
import static com.nowscas.rules.util.Constants.SET_MENU_EXCEPTION;
import static com.nowscas.rules.util.Constants.STALKER_STATE_FILLED;
import static com.nowscas.rules.util.Constants.STALKER_STATE_NEW;
import static com.nowscas.rules.util.Constants.STALKER_STATE_PREPARE_TO_TEST;
import static com.nowscas.rules.util.Constants.STALKER_STATE_WAIT_FOR_GROUP;
import static com.nowscas.rules.util.Constants.START_BUTTON_TEXT;
import static com.nowscas.rules.util.Constants.START_COMMAND;
import static com.nowscas.rules.util.Constants.START_REGISTRATION_RUS;
import static com.nowscas.rules.util.Constants.START_TESTING_TEXT_RUS;
import static com.nowscas.rules.util.Constants.SUCCESS_TESTING_MESSAGE;
import static com.nowscas.rules.util.Constants.TESTING;
import static com.nowscas.rules.util.Constants.TESTING_CONTINUE_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_END;
import static com.nowscas.rules.util.Constants.TESTING_EXIT_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_IS_FINISHED_MESSAGE;
import static com.nowscas.rules.util.Constants.TESTING_IS_RUNNING;
import static com.nowscas.rules.util.Constants.TESTING_OFFER_RUS;
import static com.nowscas.rules.util.Constants.TESTING_START;
import static com.nowscas.rules.util.Constants.TEST_ALREADY_CLOSED_MESSAGE;
import static com.nowscas.rules.util.Constants.TEST_ALREADY_OPENED_MESSAGE;
import static com.nowscas.rules.util.Constants.TEST_DISABLE_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RulesBot extends TelegramLongPollingBot {

    @Value("${app.bot.admins}")
    private List<Long> admins;
    @Value("${app.bot.limit-answers}")
    private Integer limitAnswers;
    @Value("${app.bot.test-enable}")
    private boolean testEnable;
    @Value("${app.bot.retest-seconds}")
    private int retestSeconds;

    private final BotProperties botProperties;
    private final StalkerService stalkerService;
    private final InitialService initialService;
    private final QuestionService questionService;
    private final ObjectMapper objectMapper;

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    @PostConstruct
    public void init() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand(START_COMMAND, START_BUTTON_TEXT));
        listOfCommands.add(new BotCommand(HELP_COMMAND, HELP_BUTTON_TEXT));
        listOfCommands.add(new BotCommand(MY_INFO_COMMAND, MY_INFO_BUTTON_TEXT));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.info(SET_MENU_EXCEPTION);
            log.info(e.getMessage());
        }

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
            } else if (update.getMessage().hasAnimation() || update.getMessage().hasPhoto() || update.getMessage().hasAudio()) {
                sendMessage(chatId, INVALID_FILE_UPLOAD_ERROR, null);
            } else if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                try {
                    StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
                    if (STALKER_STATE_NEW.equals(stalkerByChatId.getState())) {
                        if (messageText.startsWith("/")) {
                            sendMessage(chatId, BAD_STALKER_NAME_MESSAGE, null);
                            return;
                        }
                        if (messageText.length() > 30) {
                            sendMessage(chatId, LONG_STALKER_NAME_MESSAGE, null);
                            return;
                        }
                        stalkerByChatId.setStalkerName(messageText);
                        stalkerByChatId.setState(STALKER_STATE_WAIT_FOR_GROUP);
                        stalkerService.saveStalker(stalkerByChatId);
                        sendReceiveGroupMessage(chatId);
                        return;
                    }
                } catch (StalkerException ignored) {
                }

                switch (messageText) {
                    case START_COMMAND:
                        try {
                            StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
                            if (STALKER_STATE_WAIT_FOR_GROUP.equals(stalkerByChatId.getState())) {
                                sendMessage(chatId, REGISTRATION_CONTINUES_MESSAGE, null);
                            } else if (STALKER_STATE_FILLED.equals(stalkerByChatId.getState())) {
                                if (testEnable) {
                                    sendAuthMessage(chatId, stalkerByChatId);
                                } else {
                                    sendMessage(chatId, TEST_DISABLE_MESSAGE, null);
                                }
                            } else if (STALKER_STATE_PREPARE_TO_TEST.equals(stalkerByChatId.getState())) {
                                if (testEnable) {
                                    sendMessage(chatId, DO_NOT_ANSWER_START, null);
                                } else {
                                    sendMessage(chatId, TEST_DISABLE_MESSAGE, null);
                                }
                            } else if (FINISH_TEST.equals(stalkerByChatId.getState())) {
                                sendMessage(chatId, TESTING_IS_FINISHED_MESSAGE, null);
                            } else if (TESTING.equals(stalkerByChatId.getState())) {
                                if (testEnable) {
                                    sendMessage(chatId, TESTING_IS_RUNNING, null);
                                } else {
                                    sendMessage(chatId, TEST_DISABLE_MESSAGE, null);
                                }
                            }
                        } catch (StalkerException e) {
                            sendRegisterMessage(update.getMessage().getChat());
                        }
                        break;
                    case HELP_COMMAND:
                        if (admins.contains(chatId)) {
                            sendMessage(chatId, ADMIN_HELP_TEXT, null);
                        } else {
                            sendMessage(chatId, HELP_TEXT, null);
                        }
                        break;
                    case MY_INFO_COMMAND:
                        sendInfoMessage(chatId);
                        break;
                    case CLOSE_TESTING_ADMIN_COMMAND:
                        if (admins.contains(chatId)) {
                            if (!testEnable) {
                                sendMessage(chatId, TEST_ALREADY_CLOSED_MESSAGE, null);
                                return;
                            }
                            this.testEnable = false;
                            notifyClosingTesting();
                        } else {
                            sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                        }
                        break;
                    case OPEN_TESTING_ADMIN_COMMAND:
                        if (admins.contains(chatId)) {
                            if (testEnable) {
                                sendMessage(chatId, TEST_ALREADY_OPENED_MESSAGE, null);
                                return;
                            }
                            this.testEnable = true;
                            List<StalkerEntity> allStalkers = stalkerService.getAllStalkers();
                            for (StalkerEntity stalker : allStalkers) {
                                Integer lastMessageId = stalker.getLastMessageId();
                                initialService.clearQuestionHistory(stalker);
                                sendDeleteMessage(chatId, lastMessageId);
                            }
                            notifyOpenTesting(allStalkers);
                        } else {
                            sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                        }
                        break;
                    case DOWNLOAD_RESULTS_ADMIN_COMMAND:
                        if (admins.contains(chatId)) {
                            SendDocument resultFileSend = stalkerService.getResultFileSend(chatId);
                            execute(resultFileSend);
                        } else {
                            sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                        }
                        break;
                    case DELETE_MYSELF_TEMPORARY_COMMAND:
                        stalkerService.deleteByChatId(chatId);
                        sendMessage(chatId, DELETE_YOURSELF_MESSAGE, null);
                        break;
                    default:
                        sendMessage(chatId, INVALID_COMMAND_ERROR, null);
                }
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();

            EditMessageText message;
            StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
            if (TESTING_CONTINUE_BUTTON.equals(callbackData)) {
                if (testEnable) {
                    ZonedDateTime tested = stalkerByChatId.getTested();
                    if (tested != null) {
                        ZonedDateTime now = ZonedDateTime.now();
                        Duration between = Duration.between(tested, now);
                        long secondsBetween = between.getSeconds();
                        if (secondsBetween <= retestSeconds) {
                            long minutesToReady = retestSeconds - secondsBetween < 60 ? 1 : (retestSeconds - secondsBetween) / 60;
                            message = initialService.processStartTestingButtonWithRetestTime(chatId, messageId, minutesToReady);

                            stalkerByChatId.setState(STALKER_STATE_FILLED);
                            stalkerService.saveStalker(stalkerByChatId);
                            sendEditMessage(message, chatId);
                            return;
                        }
                    }
                    message = initialService.processStartTestingButton(chatId, messageId);
                    sendEditMessage(message, chatId);
                    stalkerByChatId.setState(TESTING);
                    stalkerService.saveStalker(stalkerByChatId);

                    sendQuestionMessage(stalkerByChatId, chatId);
                } else {
                    message = initialService.processStartTestingButtonWhenTestingClosed(chatId, messageId);
                    stalkerByChatId.setState(STALKER_STATE_FILLED);
                    stalkerService.saveStalker(stalkerByChatId);
                    sendEditMessage(message, chatId);
                }
                return;
            }
            if (TESTING_EXIT_BUTTON.equals(callbackData)) {
                message = initialService.processExitTestingButton(chatId, messageId);
                stalkerByChatId.setState(STALKER_STATE_FILLED);
                stalkerService.saveStalker(stalkerByChatId);
                sendEditMessage(message, chatId);
                return;
            }

            if (STALKER_STATE_WAIT_FOR_GROUP.equals(stalkerByChatId.getState())) {
                message = initialService.processGroupChoiceMessage(update.getCallbackQuery());
                if (REGISTER_SUCCESS_RUS.equals(message.getText())) {
                    sendEditMessage(message, chatId);
                    sendMessage(chatId, START_TESTING_TEXT_RUS, null);
                } else {
                    sendEditMessage(message, chatId);
                    sendReceiveGroupMessage(chatId);
                }
            } else if (TESTING.equals(stalkerByChatId.getState())) {
                if (testEnable) {
                    AnswerResponse answerResponse = questionService.processAnswerMessage(update.getCallbackQuery(), stalkerByChatId, chatId, messageId);
                    sendEditMessage(answerResponse.getMessageText(), chatId);
                    if (answerResponse.isNeedMore()) {
                        sendQuestionMessage(stalkerByChatId, chatId);
                    } else {
                        if (stalkerByChatId.getCurrentAnswers() < limitAnswers) {
                            stalkerByChatId.setState(STALKER_STATE_FILLED);
                            sendBadTestingMessage(chatId, limitAnswers - stalkerByChatId.getCurrentAnswers(), stalkerByChatId);
                        } else {
                            stalkerByChatId.setState(FINISH_TEST);
                            sendSuccessTestingMessage(chatId);
                        }
                        stalkerByChatId.setAttempts(stalkerByChatId.getAttempts() + 1);
                        stalkerByChatId.setCurrentAnswers(0);
                        stalkerByChatId.setPassedQuestions(null);
                        stalkerByChatId.setTested(ZonedDateTime.now());
                        stalkerService.saveStalker(stalkerByChatId);
                    }
                } else {
                    sendMessage(chatId, TEST_DISABLE_MESSAGE, null);
                }
            }
        }
    }

    private void sendInfoMessage(long chatId) {
        try {
            StalkerEntity stalkerEntity = stalkerService.getStalkerByChatId(chatId);
            if (STALKER_STATE_WAIT_FOR_GROUP.equals(stalkerEntity.getState())) {
                sendMessage(chatId, REGISTRATION_NOT_FINISHED_MESSAGE, null);
            } else {
                String message = String.format(ALREADY_REGISTERED_RUS, stalkerEntity.getStalkerName(), stalkerEntity.getGroupName());
                String info;
                if (FINISH_TEST.equals(stalkerEntity.getState())) {
                    info = String.format(ENTER + RULES_SUCCESS_INFO_RUS, stalkerEntity.getAttempts());
                } else {
                    info = String.format(ENTER + RULES_FAIL_INFO_RUS, stalkerEntity.getAttempts());
                }
                sendMessage(chatId, message.concat(info), null);
            }
        } catch (StalkerException e) {
            sendMessage(chatId, NOT_REGISTERED_MESSAGE, null);
        }
    }

    private void sendAuthMessage(long chatId, StalkerEntity stalkerEntity) {
        String answer = String.format(ALREADY_REGISTERED_RUS, stalkerEntity.getStalkerName(), stalkerEntity.getGroupName());
        sendMessage(chatId, answer, null);
        InlineKeyboardMarkup inlineKeyboardMarkup = initialService.prepareTestingMarkup();
        int messageId = sendMessage(chatId, TESTING_OFFER_RUS, inlineKeyboardMarkup);
        stalkerEntity.setState(STALKER_STATE_PREPARE_TO_TEST);
        stalkerEntity.setLastMessageId(messageId);
        stalkerService.saveStalker(stalkerEntity);
    }

    private void sendSuccessTestingMessage(long chatId) {
        sendMessage(chatId, SUCCESS_TESTING_MESSAGE, null);
    }

    private void sendBadTestingMessage(long chatId, int errors, StalkerEntity stalkerEntity) {
        String answer = String.format(BAD_TESTING_MESSAGE, errors);
        sendMessage(chatId, answer, null);
        InlineKeyboardMarkup inlineKeyboardMarkup = initialService.prepareTestingMarkup();
        int messageId = sendMessage(chatId, TESTING_OFFER_RUS, inlineKeyboardMarkup);
        stalkerEntity.setState(STALKER_STATE_PREPARE_TO_TEST);
        stalkerEntity.setLastMessageId(messageId);
        stalkerService.saveStalker(stalkerEntity);
    }

    private void sendRegisterMessage(Chat chat) {
        stalkerService.saveNewStalker(chat);
        sendMessage(chat.getId(), START_REGISTRATION_RUS, null);
    }

    private void sendReceiveGroupMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = initialService.prepareReceiveGroupMarkup();
        sendMessage(chatId, CHOOSE_YOUR_GROUP_RUS, inlineKeyboardMarkup);
    }

    private void sendQuestionMessage(StalkerEntity stalkerByChatId, Long chatId) {
        Pair<String, InlineKeyboardMarkup> question = questionService.prepareQuestionMarkup(stalkerByChatId);
        int messageId;
        if (question == null) {
            messageId = sendMessage(chatId, QUESTIONS_NOT_EXIST, null);
        } else {
            messageId = sendMessage(chatId, question.getLeft(), question.getRight());
        }
        stalkerByChatId.setLastMessageId(messageId);
        stalkerService.saveStalker(stalkerByChatId);
    }

    private int sendMessage(long chatId, String message, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        if (inlineKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        }

        try {
            return execute(sendMessage).getMessageId();
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

    private void sendDeleteMessage(Long chatId, Integer lastMessageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setMessageId(lastMessageId);
        deleteMessage.setChatId(chatId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
            log.info(String.format(SEND_DELETE_MESSAGE_EXCEPTION, chatId));
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

    private void notifyOpenTesting(List<StalkerEntity> allStalkers) {
        for (StalkerEntity stalker : allStalkers) {
            sendMessage(stalker.getChatId(), TESTING_START, null);
        }
    }

    private void notifyClosingTesting() {
        List<StalkerEntity> allStalkers = stalkerService.getAllStalkers();
        for (StalkerEntity stalker : allStalkers) {
            String info;
            sendMessage(stalker.getChatId(), TESTING_END, null);
            if (FINISH_TEST.equals(stalker.getState())) {
                info = String.format(RULES_SUCCESS_INFO_RUS, stalker.getAttempts());
            } else {
                info = String.format(RULES_FAIL_INFO_RUS, stalker.getAttempts());
            }
            sendMessage(stalker.getChatId(), info, null);
        }
    }
}
