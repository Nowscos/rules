package com.nowscas.rules.service;

import com.nowscas.rules.model.StalkerEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.nowscas.rules.util.Constants.BANDITS_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.BANDITS_GROUP_RUS;
import static com.nowscas.rules.util.Constants.BEGINNERS_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.BEGINNERS_GROUP_RUS;
import static com.nowscas.rules.util.Constants.CANCEL_RUS;
import static com.nowscas.rules.util.Constants.CONTINUE_RUS;
import static com.nowscas.rules.util.Constants.DUTY_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.DUTY_GROUP_RUS;
import static com.nowscas.rules.util.Constants.EXIT_REGISTRATION_RUS;
import static com.nowscas.rules.util.Constants.FREEDOM_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.FREEDOM_GROUP_RUS;
import static com.nowscas.rules.util.Constants.LATER_RUS;
import static com.nowscas.rules.util.Constants.MERCENARIES_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.MERCENARIES_GROUP_RUS;
import static com.nowscas.rules.util.Constants.MONOLITH_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.MONOLITH_GROUP_RUS;
import static com.nowscas.rules.util.Constants.NEW_ZEL_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.NEW_ZEL_GROUP_RUS;
import static com.nowscas.rules.util.Constants.NOT_VALID_GROUP_RUS;
import static com.nowscas.rules.util.Constants.REGISTER_CONTINUE_BUTTON;
import static com.nowscas.rules.util.Constants.REGISTER_EXIT_BUTTON;
import static com.nowscas.rules.util.Constants.REGISTER_SUCCESS_RUS;
import static com.nowscas.rules.util.Constants.SINGLES_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.SINGLES_GROUP_RUS;
import static com.nowscas.rules.util.Constants.SOP_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.SOP_GROUP_RUS;
import static com.nowscas.rules.util.Constants.STALKER_STATE_FILLED;
import static com.nowscas.rules.util.Constants.STALKER_STATE_NEW;
import static com.nowscas.rules.util.Constants.STALKER_STATE_WAIT_FOR_GROUP;
import static com.nowscas.rules.util.Constants.START_REGISTRATION_RUS;
import static com.nowscas.rules.util.Constants.START_TESTING_RUS;
import static com.nowscas.rules.util.Constants.TECH_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.TECH_GROUP_RUS;
import static com.nowscas.rules.util.Constants.TESTING_CONTINUE_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_EXIT_BUTTON;
import static com.nowscas.rules.util.Constants.TESTING_EXIT_RUS;
import static com.nowscas.rules.util.Constants.TESTING_START_RUS;
import static com.nowscas.rules.util.Constants.TEST_DISABLE_MESSAGE;
import static com.nowscas.rules.util.Constants.UN_GROUP_BUTTON;
import static com.nowscas.rules.util.Constants.UN_GROUP_RUS;
import static com.nowscas.rules.util.Constants.getGroupsNameMap;

@Service
@RequiredArgsConstructor
public class InitialService {

    private final StalkerService stalkerService;

//    // Подготовка ответа на начало регистрации
//    public EditMessageText processContinueRegisterButton(Long chatId, Integer messageId) {
//        EditMessageText message = new EditMessageText();
//        message.setChatId(chatId);
//        message.setMessageId(messageId);
//        message.setText(START_REGISTRATION_RUS);
//        return message;
//    }
//
//    // Подготовка ответа на отказ от регистрации
//    public EditMessageText processExitRegisterButton(Long chatId, Integer messageId) {
//        EditMessageText message = new EditMessageText();
//        message.setChatId(chatId);
//        message.setMessageId(messageId);
//        message.setText(EXIT_REGISTRATION_RUS);
//        return message;
//    }

    // Подготовка ответа на начало тестирования
    public EditMessageText processStartTestingButton(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(TESTING_START_RUS);
        return message;
    }

    // Подготовка ответа на начало тестирования когда тестирование закрыто
    public EditMessageText processStartTestingButtonWhenTestingClosed(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(TEST_DISABLE_MESSAGE);
        return message;
    }

    // Подготовка ответа на отказ тестирования
    public EditMessageText processExitTestingButton(Long chatId, Integer messageId) {
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(TESTING_EXIT_RUS);
        return message;
    }

    // Разбор ответа из блока выбора группировки
    public EditMessageText processGroupChoiceMessage(CallbackQuery callbackQuery) {
        Long chatId = callbackQuery.getMessage().getChatId();
        Integer messageId = callbackQuery.getMessage().getMessageId();
        String selectedGroup = callbackQuery.getData();

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);

        Map<String, String> groups = getGroupsNameMap();
        String groupNameRus = groups.get(selectedGroup);
        if (groupNameRus != null) {
            StalkerEntity stalkerByChatId = stalkerService.getStalkerByChatId(chatId);
            stalkerByChatId.setGroupName(groupNameRus);
            stalkerByChatId.setState(STALKER_STATE_FILLED);
            stalkerByChatId.setActual(true);
            stalkerService.saveStalker(stalkerByChatId);
            message.setText(REGISTER_SUCCESS_RUS);
        } else {
            message.setText(NOT_VALID_GROUP_RUS);
        }
        return message;
    }

    // Подготовка блока начала регистрации
    public InlineKeyboardMarkup prepareRegisterMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton continueButton = new InlineKeyboardButton();
        continueButton.setText(CONTINUE_RUS);
        continueButton.setCallbackData(REGISTER_CONTINUE_BUTTON);

        InlineKeyboardButton exitButton = new InlineKeyboardButton();
        exitButton.setText(CANCEL_RUS);
        exitButton.setCallbackData(REGISTER_EXIT_BUTTON);

        inlineRow.add(continueButton);
        inlineRow.add(exitButton);

        inlineRows.add(inlineRow);
        inlineKeyboardMarkup.setKeyboard(inlineRows);
        return inlineKeyboardMarkup;
    }

    // Подготовка блока подтверждения начала опроса
    public InlineKeyboardMarkup prepareTestingMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow = new ArrayList<>();

        InlineKeyboardButton continueButton = new InlineKeyboardButton();
        continueButton.setText(START_TESTING_RUS);
        continueButton.setCallbackData(TESTING_CONTINUE_BUTTON);

        InlineKeyboardButton exitButton = new InlineKeyboardButton();
        exitButton.setText(LATER_RUS);
        exitButton.setCallbackData(TESTING_EXIT_BUTTON);

        inlineRow.add(continueButton);
        inlineRow.add(exitButton);

        inlineRows.add(inlineRow);
        inlineKeyboardMarkup.setKeyboard(inlineRows);
        return inlineKeyboardMarkup;
    }

    //Подготовка блока выбора группировки
    public InlineKeyboardMarkup prepareReceiveGroupMarkup() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();

        List<InlineKeyboardButton> inlineRow1 = new ArrayList<>();
        InlineKeyboardButton beginnersButton = new InlineKeyboardButton();
        beginnersButton.setText(BEGINNERS_GROUP_RUS);
        beginnersButton.setCallbackData(BEGINNERS_GROUP_BUTTON);
        InlineKeyboardButton singlesButton = new InlineKeyboardButton();
        singlesButton.setText(SINGLES_GROUP_RUS);
        singlesButton.setCallbackData(SINGLES_GROUP_BUTTON);
        InlineKeyboardButton banditsButton = new InlineKeyboardButton();
        banditsButton.setText(BANDITS_GROUP_RUS);
        banditsButton.setCallbackData(BANDITS_GROUP_BUTTON);
        inlineRow1.add(beginnersButton);
        inlineRow1.add(singlesButton);
        inlineRow1.add(banditsButton);

        List<InlineKeyboardButton> inlineRow2 = new ArrayList<>();
        InlineKeyboardButton dutyButton = new InlineKeyboardButton();
        dutyButton.setText(DUTY_GROUP_RUS);
        dutyButton.setCallbackData(DUTY_GROUP_BUTTON);
        InlineKeyboardButton freedomButton = new InlineKeyboardButton();
        freedomButton.setText(FREEDOM_GROUP_RUS);
        freedomButton.setCallbackData(FREEDOM_GROUP_BUTTON);
        InlineKeyboardButton monolithButton = new InlineKeyboardButton();
        monolithButton.setText(MONOLITH_GROUP_RUS);
        monolithButton.setCallbackData(MONOLITH_GROUP_BUTTON);
        inlineRow2.add(dutyButton);
        inlineRow2.add(freedomButton);
        inlineRow2.add(monolithButton);

        List<InlineKeyboardButton> inlineRow3 = new ArrayList<>();
        InlineKeyboardButton unButton = new InlineKeyboardButton();
        unButton.setText(UN_GROUP_RUS);
        unButton.setCallbackData(UN_GROUP_BUTTON);
        InlineKeyboardButton newZelButton = new InlineKeyboardButton();
        newZelButton.setText(NEW_ZEL_GROUP_RUS);
        newZelButton.setCallbackData(NEW_ZEL_GROUP_BUTTON);
        InlineKeyboardButton mercenariesButton = new InlineKeyboardButton();
        mercenariesButton.setText(MERCENARIES_GROUP_RUS);
        mercenariesButton.setCallbackData(MERCENARIES_GROUP_BUTTON);
        inlineRow3.add(unButton);
        inlineRow3.add(newZelButton);
        inlineRow3.add(mercenariesButton);

        List<InlineKeyboardButton> inlineRow4 = new ArrayList<>();
        InlineKeyboardButton sopButton = new InlineKeyboardButton();
        sopButton.setText(SOP_GROUP_RUS);
        sopButton.setCallbackData(SOP_GROUP_BUTTON);
        InlineKeyboardButton techButton = new InlineKeyboardButton();
        techButton.setText(TECH_GROUP_RUS);
        techButton.setCallbackData(TECH_GROUP_BUTTON);
        inlineRow4.add(sopButton);
        inlineRow4.add(techButton);

        inlineRows.add(inlineRow1);
        inlineRows.add(inlineRow2);
        inlineRows.add(inlineRow3);
        inlineRows.add(inlineRow4);
        inlineKeyboardMarkup.setKeyboard(inlineRows);

        return inlineKeyboardMarkup;
    }

    public void clearQuestionHistory(List<StalkerEntity> allStalkers) {
        for (StalkerEntity stalker : allStalkers) {
            if (!STALKER_STATE_NEW.equals(stalker.getState()) && !STALKER_STATE_WAIT_FOR_GROUP.equals(stalker.getState())) {
                stalker.setState(STALKER_STATE_FILLED);
            }
            stalker.setAttempts(0);
            stalker.setCurrentAnswers(0);
            stalker.setPassedQuestions(null);
            stalkerService.saveStalker(stalker);
        }
    }
}
