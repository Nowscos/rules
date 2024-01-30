package com.nowscas.rules.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nowscas.rules.model.AnswerEntity;
import com.nowscas.rules.model.AnswerResponse;
import com.nowscas.rules.model.QuestionDto;
import com.nowscas.rules.model.QuestionEntity;
import com.nowscas.rules.model.StalkerEntity;
import com.nowscas.rules.repository.AnswerRepository;
import com.nowscas.rules.repository.QuestionRepository;
import com.nowscas.rules.repository.StalkerRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import static com.nowscas.rules.util.Constants.QUESTION_CALLBACK_TEXT;

@Service
@RequiredArgsConstructor
public class QuestionService {

    @Value("${app.bot.limit-answers}")
    private Integer limitAnswers;

    private final StalkerRepository stalkerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final ObjectMapper objectMapper;

    // Подготовка блока с вопросом
    public Pair<String, InlineKeyboardMarkup> prepareQuestionMarkup(StalkerEntity stalker) {
        List<String> passedIds = new ArrayList<>();
        String[] passedQuestions = stalker.getPassedQuestions();
        if (passedQuestions != null) {
            passedIds.addAll(Arrays.asList(passedQuestions));
        }
        Long randomId = Long.parseLong(getRandomId(passedIds));
        Optional<QuestionEntity> question = questionRepository.findById(randomId);
        if (question.isPresent()) {
            String passedId = String.valueOf(question.get().getId());
            passedIds.add(passedId);
            String[] updatedIds = passedIds.stream().map(String::valueOf).distinct().toArray(String[]::new);
            stalker.setPassedQuestions(updatedIds);
            stalkerRepository.save(stalker);
        } else {
            return null;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> inlineRows = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow = new ArrayList<>();
        List<InlineKeyboardButton> inlineRow2 = new ArrayList<>();
        int counter = 0;

        for (AnswerEntity answer : question.get().getAnswers()) {
            InlineKeyboardButton answerButton = new InlineKeyboardButton();
            answerButton.setText(answer.getAnswer());
            answerButton.setCallbackData(answer.getUuid().toString());
            if (counter < 2) {
                inlineRow.add(answerButton);
                counter++;
            } else {
                inlineRow2.add(answerButton);
            }
        }
        inlineRows.add(inlineRow);
        if (counter > 1) {
            inlineRows.add(inlineRow2);
        }
        inlineKeyboardMarkup.setKeyboard(inlineRows);
        return Pair.of(question.get().getQuestion(), inlineKeyboardMarkup);
    }

    // Разбор ответа по вопросу
    public AnswerResponse processAnswerMessage(CallbackQuery callbackQuery, StalkerEntity stalker, long chatId, int messageId) {
        AnswerResponse answerResponse = new AnswerResponse();
        String callbackData = callbackQuery.getData();
        AnswerEntity answer = answerRepository.findByUuid(UUID.fromString(callbackData)).orElse(null);
        if (answer.getAnswer().equals(answer.getQuestion().getRightAnswer())) {
            stalker.setCurrentAnswers(stalker.getCurrentAnswers() + 1);
        }
        List<String> ids = Arrays.asList(stalker.getPassedQuestions());

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(String.format(QUESTION_CALLBACK_TEXT, answer.getQuestion().getQuestion(), answer.getAnswer()));

        answerResponse.setNeedMore(ids.size() < limitAnswers);
        answerResponse.setMessageText(message);
        return answerResponse;
    }

    // Заполнение списка вопросов
    public void fillQuestions(String questions) throws JsonProcessingException {
        answerRepository.deleteAll();
        questionRepository.deleteAll();

        List<QuestionDto> questionDtoList = objectMapper.readValue(questions, new TypeReference<>(){});
        for (QuestionDto question : questionDtoList) {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setUuid(UUID.randomUUID());
            questionEntity.setQuestion(question.getQuestion());
            questionEntity.setRightAnswer(question.getRightAnswer());
            QuestionEntity save = questionRepository.save(questionEntity);

            Set<AnswerEntity> answerEntitySet = new HashSet<>();
            for (String answer : question.getAnswers()) {
                AnswerEntity answerEntity = new AnswerEntity();
                answerEntity.setUuid(UUID.randomUUID());
                answerEntity.setAnswer(answer);
                answerEntity.setQuestion(save);
                answerEntitySet.add(answerEntity);
            }
            answerRepository.saveAll(answerEntitySet);
        }
    }

    // Получение случайного id вопроса, на который еще не отвечали
    private String getRandomId(List<String> passedIds) {
        List<String> ids = questionRepository.findAll().stream().map(q -> String.valueOf(q.getId())).collect(Collectors.toList());
        ids.removeAll(passedIds);
        int random = (int) ((Math.random() * (ids.size())) + 0);
        return ids.get(random);
    }
}
