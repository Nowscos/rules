package com.nowscas.rules.util;

import java.util.HashMap;
import java.util.Map;

public final class Constants {

    public static final String START_COMMAND = "/start";
    public static final String HELP_COMMAND = "/help";
    public static final String MY_INFO_COMMAND = "/info";
//    public static final String DELETE_MYSELF_TEMPORARY_COMMAND = "/deleteMyself";
    public static final String HELP_FRIEND_COMMAND = "/helpFriend";

    public static final String CLOSE_TESTING_ADMIN_COMMAND = "/closeTesting";
    public static final String OPEN_TESTING_ADMIN_COMMAND = "/openTesting";
    public static final String DOWNLOAD_RESULTS_ADMIN_COMMAND = "/downloadResults";

    public static final String STALKER_STATE_NEW = "NEW";
    public static final String STALKER_STATE_WAIT_FOR_GROUP = "WAIT_FOR_GROUP";
    public static final String STALKER_STATE_FILLED = "FILLED";
    public static final String STALKER_STATE_PREPARE_TO_TEST = "PREPARE_TO_TEST";
    public static final String TESTING = "TESTING";
    public static final String FINISH_TEST = "FINISH_TEST";

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

    public static final String START_TESTING_RUS = "Начать";
    public static final String LATER_RUS = "Позже";
    public static final String REGISTER_SUCCESS_RUS = "Регистрация успешно завершена";
    public static final String START_TESTING_TEXT_RUS = "Для начала тестирования веди команду /start";
    public static final String NOT_VALID_GROUP_RUS = "Указана некорректная группировка";
    public static final String START_REGISTRATION_RUS = "Начинаем регистрацию\n\nВведи свой позывной";
    public static final String ALREADY_REGISTERED_RUS = "Привет, сталкер! Ты зарегистрирован под позывным %s в группировке %s";
    public static final String RULES_SUCCESS_INFO_RUS = "Ты успешно сдал правила. Попыток: %s";
    public static final String RULES_FAIL_INFO_RUS = "Ты еще не сдал правила. Попыток: %s";
    public static final String CHOOSE_YOUR_GROUP_RUS = "Выбери свою группировку";
    public static final String TESTING_OFFER_RUS = "Начнем сдачу правил? На каждый вопрос дается 30 секунд. Не мешкай!";
    public static final String TESTING_START_RUS = "Начнем. Помни, что на вопрос у тебе 30 секунд!";
    public static final String WAIT_FOR_RETEST_RUS = "Недавно ты уже неудачно сдал правила. Пойди подучи и возвращайся. Минут до открытия пересдачи: %d";
    public static final String QUESTION_CALLBACK_TEXT = "%s\nВаш ответ: %s";
    public static final String TESTING_EXIT_RUS = "Ты отложил сдачу правил. Когда будешь готов напиши /start";
    public static final String DO_NOT_ANSWER_START = "Ответь на вопрос в блоке выше";
    public static final String YOU_DID_NOT_ANSWER = "Вы не ответили";

    public static final String QUESTION_UPDATED = "Список вопросов обновлен";
    public static final String QUESTIONS_NOT_EXIST = "Не найдено подходящих вопросов";
    public static final String BAD_TESTING_MESSAGE = "Ты не прошел тестирование. Ошибок: %d. Попробуй еще";
    public static final String SUCCESS_TESTING_MESSAGE = "Ты успешно сдал правила!";
//    public static final String DELETE_YOURSELF_MESSAGE = "Вы успешно удалили свою запись";
    public static final String TEST_DISABLE_MESSAGE = "Сдача правил временно закрыта. Ожидайте уведомления";
    public static final String TEST_ALREADY_OPENED_MESSAGE = "Сдача правил уже открыта";
    public static final String TEST_ALREADY_CLOSED_MESSAGE = "Сдача правил уже закрыта";
    public static final String TESTING_START = "Сдача правил на предстоящую игру открыта!\nДля начала введи команду /start";
    public static final String TESTING_END = "Сдача правил на предстоящую игру закрывается!";

    public static final String LONG_STALKER_NAME_MESSAGE = "Позывной не может быть длиннее 30 символов. Введи корректный позывной";
    public static final String BAD_STALKER_NAME_MESSAGE = "Позывной не может начинаться с \'/\'\nВведи корректный позывной";
    public static final String NOT_REGISTERED_MESSAGE = "Ты еще не зарегистрировался. Введи команду /start для регистрации";
    public static final String REGISTRATION_NOT_FINISHED_MESSAGE = "Ты не завершил регистрацию. Выбери одну из вышеуказанных группировок";
    public static final String REGISTRATION_CONTINUES_MESSAGE = "Ты уже начал регистрацию! Выбери группировку";
    public static final String TESTING_IS_FINISHED_MESSAGE = "Ты уже сдал правила для предстоящей игры!";
    public static final String TESTING_IS_RUNNING = "Ты уже в процессе тестирования! Заверши его";
    public static final String INVALID_COMMAND_ERROR = "Команда не поддерживается!";
    public static final String INVALID_FILE_UPLOAD_ERROR = "Отправка файлов не поддерживается!";
    public static final String SEND_MESSAGE_EXCEPTION = "Ошибка отправки сообщения в чат: %d";
    public static final String SEND_EDIT_MESSAGE_EXCEPTION = "Ошибка отправки Edit сообщения в чат: %d";
    public static final String SEND_DELETE_MESSAGE_EXCEPTION = "Ошибка отправки Delete сообщения в чат: %d";
    public static final String SET_MENU_EXCEPTION = "Ошибка создания меню";

    public static final String FRIEND_HELPED = "Друг спасен";
    public static final String FRIEND_NOT_FOUND = "Друг не найден";

    public static final String NR = "№   ";
    public static final String STALKER_NAME = "ПОЗЫВНОЙ";
    public static final String STALKER_GROUP_NAME = "ГРУППИРОВКА";
    public static final String ATTEMPTS = "ПОПЫТОК";
    public static final String TG_TAG = "ТЕЛЕГРАМ ТЭГ";

    public static final String RESULT = "result";
    public static final String FILE_PATH = "file_path";
    public static final String ENTER = "\n";

    public static final String START_BUTTON_TEXT = "Начать работу";
    public static final String HELP_BUTTON_TEXT = "Помощь";
    public static final String MY_INFO_BUTTON_TEXT = "Моя информация";
    public static final String HELP_TEXT = "Данный бот предназначен для сдачи правил на проекте Сталкер Северо-Запад\n\n" +
            "Вы можете выполнить следующие команды:\n" +
            "/start для регистрации и тестирования\n" +
            "/help для просмотра доступых команд\n" +
            "/info для просмотра своих результатов";
//            "/info для просмотра своих результатов\n" +
//            "/deleteMyself временная команда для удаления своей записи во время тестирования";
    public static final String ADMIN_HELP_TEXT = "Данный бот предназначен для сдачи правил на проекте Сталкер Северо-Запад\n\n" +
            "Вы можете выполнить следующие команды:\n" +
            "/start для регистрации и тестирования\n" +
            "/help для просмотра доступых команд\n" +
            "/info для просмотра своих результатов\n" +
//            "/deleteMyself временная команда для удаления своей записи во время тестирования\n" +
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
