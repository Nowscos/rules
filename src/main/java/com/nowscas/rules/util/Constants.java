package com.nowscas.rules.util;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String MY_INFO_COMMAND = "/info";
    public static final String DELETE_MYSELF_TEMPORARY_COMMAND = "/deleteMyself";

    public static final String CLOSE_TESTING_ADMIN_COMMAND = "/closeTesting";
    public static final String OPEN_TESTING_ADMIN_COMMAND = "/openTesting";
    public static final String DOWNLOAD_RESULTS_ADMIN_COMMAND = "/downloadResults";

    public static final String STALKER_STATE_NEW = "NEW";
    public static final String STALKER_STATE_WAIT_FOR_GROUP = "WAIT_FOR_GROUP";
    public static final String STALKER_STATE_FILLED = "FILLED";
    public static final String TESTING = "TESTING";
    public static final String FINISH_TEST = "FINISH_TEST";

    public static final String REGISTER_CONTINUE_BUTTON = "REGISTER_CONTINUE_BUTTON";
    public static final String REGISTER_EXIT_BUTTON = "REGISTER_EXIT_BUTTON";
    public static final String TESTING_CONTINUE_BUTTON = "TESTING_CONTINUE_BUTTON";
    public static final String TESTING_EXIT_BUTTON = "TESTING_EXIT_BUTTON";

    public static final String BEGINNERS_GROUP_BUTTON = "BEGINNERS_GROUP_BUTTON";
    public static final String SINGLES_GROUP_BUTTON = "SINGLES_GROUP_BUTTON";
    public static final String BANDITS_GROUP_BUTTON = "BANDITS_GROUP_BUTTON";
    public static final String DUTY_GROUP_BUTTON = "DUTY_GROUP_BUTTON";
    public static final String FREEDOM_GROUP_BUTTON = "FREEDOM_GROUP_BUTTON";
    public static final String MONOLITH_GROUP_BUTTON = "MONOLITH_GROUP_BUTTON";
    public static final String UN_GROUP_BUTTON = "UN_GROUP_BUTTON";
    public static final String NEW_ZEL_GROUP_BUTTON = "NEW_ZEL_GROUP_BUTTON";
    public static final String MERCENARIES_GROUP_BUTTON = "MERCENARIES_GROUP_BUTTON";
    public static final String SOP_GROUP_BUTTON = "SOP_GROUP_BUTTON";
    public static final String TECH_GROUP_BUTTON = "TECH_GROUP_BUTTON";

    public static final String BEGINNERS_GROUP_RUS = "Новички";
    public static final String SINGLES_GROUP_RUS = "Одиночки";
    public static final String BANDITS_GROUP_RUS = "Бандиты";
    public static final String DUTY_GROUP_RUS = "Долг";
    public static final String FREEDOM_GROUP_RUS = "Свобода";
    public static final String MONOLITH_GROUP_RUS = "Монолит";
    public static final String UN_GROUP_RUS = "Контингент ООН";
    public static final String NEW_ZEL_GROUP_RUS = "Контингент НЗ";
    public static final String MERCENARIES_GROUP_RUS = "Наемники";
    public static final String SOP_GROUP_RUS = "СОП";
    public static final String TECH_GROUP_RUS = "Игротех";

    public static final String CONTINUE_RUS = "Продолжить";
    public static final String CANCEL_RUS = "Отмена";
    public static final String START_TESTING_RUS = "Начать тестирование";
    public static final String LATER_RUS = "Позже";
    public static final String REGISTER_SUCCESS_RUS = "Регистрация успешно завершена";
    public static final String NOT_VALID_GROUP_RUS = "Указана некорректная группировка";
    public static final String START_REGISTRATION_RUS = "Начинаем регистрацию\n\nВведите свой позывной";
    public static final String EXIT_REGISTRATION_RUS = "Вы отказались от регистрации";
    public static final String ALREADY_REGISTERED_RUS = "Привет, сталкер! Ты зарегистрирован под позывным %s в группировке %s.";
    public static final String REGISTER_OFFER_RUS = "Привет, сталкер! Ты еще не зарегистрирован. Продолжить?";
    public static final String CHOOSE_YOUR_GROUP_RUS = "Выберите свою группировку";
    public static final String TESTING_OFFER_RUS = "Начнем сдачу правил?";
    public static final String TESTING_START_RUS = "Начнем!";
    public static final String TESTING_EXIT_RUS = "Вы отложили сдачу правил. Когда будете готовы напишите /start";

    public static final String QUESTION_UPDATED = "Список вопросов обновлен";
    public static final String QUESTIONS_NOT_EXIST = "Не найдено подходящих вопросов";
    public static final String BAD_TESTING_MESSAGE = "Вы не прошли тестирование. Попробуйте еще";
    public static final String SUCCESS_TESTING_MESSAGE = "Вы успешно сдали правила!";

    public static final String REGISTRATION_CONTINUES_MESSAGE = "Вы уже начали регистрацию! Выберите группировку.";
    public static final String TESTING_IS_FINISHED_MESSAGE = "Вы уже сдали правила для предстоящей игры!";
    public static final String TESTING_IS_RUNNING = "Вы уже в процессе тестирования! Завершите его.";
    public static final String INVALID_COMMAND_ERROR = "Команда не поддерживается!";
    public static final String INVALID_FILE_UPLOAD_ERROR = "Отправка файлов не поддерживается!";
    public static final String SEND_MESSAGE_EXCEPTION = "Ошибка отправки сообщения в чат: %d";
    public static final String SEND_EDIT_MESSAGE_EXCEPTION = "Ошибка отправки Edit сообщения в чат: %d";
    public static final String SET_MENU_EXCEPTION = "Ошибка создания меню";

    public static final String RESULT = "result";
    public static final String FILE_PATH = "file_path";

    public static final String START_BUTTON_TEXT = "Начать работу";
    public static final String HELP_BUTTON_TEXT = "Помощь";
    public static final String MY_INFO_BUTTON_TEXT = "Моя информация";
    public static final String HELP_TEXT = "Данный бот предназначен для сдачи правил на проекте Сталкер Северо-Запад\n\n" +
            "Вы можете выполнить следующие команды:\n" +
            "/start для регистрации и тестирования\n" +
            "/help для просмотра доступых команд\n" +
            "/info для просмотра своих результатов\n" +
            "/deleteMyself временная команда для удаления своей записи во время тестирования";
    public static final String ADMIN_HELP_TEXT = "Данный бот предназначен для сдачи правил на проекте Сталкер Северо-Запад\n\n" +
            "Вы можете выполнить следующие команды:\n" +
            "/start для регистрации и тестирования\n" +
            "/help для просмотра доступых команд\n" +
            "/info для просмотра своих результатов\n" +
            "/deleteMyself временная команда для удаления своей записи во время тестирования\n\n" +
            "Команды администратора:\n" +
            "/closeTesting закрыть тестирование на предстоящую игру\n" +
            "/openTesting открыть регистрацию на следующую игру\n" +
            "/downloadResults выгрузить результаты на предстоящую игру";

    public static Map<String, String> getGroupsNameMap() {
        Map<String, String> groupsNameMap = new HashMap<>();
        groupsNameMap.put(BEGINNERS_GROUP_BUTTON, BEGINNERS_GROUP_RUS);
        groupsNameMap.put(SINGLES_GROUP_BUTTON, SINGLES_GROUP_RUS);
        groupsNameMap.put(BANDITS_GROUP_BUTTON, BANDITS_GROUP_RUS);
        groupsNameMap.put(DUTY_GROUP_BUTTON, DUTY_GROUP_RUS);
        groupsNameMap.put(FREEDOM_GROUP_BUTTON, FREEDOM_GROUP_RUS);
        groupsNameMap.put(MONOLITH_GROUP_BUTTON, MONOLITH_GROUP_RUS);
        groupsNameMap.put(UN_GROUP_BUTTON, UN_GROUP_RUS);
        groupsNameMap.put(NEW_ZEL_GROUP_BUTTON, NEW_ZEL_GROUP_RUS);
        groupsNameMap.put(MERCENARIES_GROUP_BUTTON, MERCENARIES_GROUP_RUS);
        groupsNameMap.put(SOP_GROUP_BUTTON, SOP_GROUP_RUS);
        groupsNameMap.put(TECH_GROUP_BUTTON, TECH_GROUP_RUS);
        return groupsNameMap;
    }
}
