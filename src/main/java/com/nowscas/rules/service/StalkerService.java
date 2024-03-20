package com.nowscas.rules.service;

import com.nowscas.rules.model.StalkerEntity;
import com.nowscas.rules.repository.StalkerRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import static com.nowscas.rules.exception.StalkerException.notFound;
import static com.nowscas.rules.util.Constants.ATTEMPTS;
import static com.nowscas.rules.util.Constants.FINISH_TEST;
import static com.nowscas.rules.util.Constants.NR;
import static com.nowscas.rules.util.Constants.STALKER_GROUP_NAME;
import static com.nowscas.rules.util.Constants.STALKER_NAME;
import static com.nowscas.rules.util.Constants.STALKER_STATE_NEW;
import static com.nowscas.rules.util.Constants.TG_TAG;

@Service
@RequiredArgsConstructor
public class StalkerService {

    private final StalkerRepository stalkerRepository;

    public List<StalkerEntity> getAllStalkers() {
        return stalkerRepository.findAll();
    }

    public void saveNewStalker(Chat chat) {
        StalkerEntity newEntity = StalkerEntity.builder()
                .userName(chat.getUserName())
                .firstName(chat.getFirstName())
                .lastName(chat.getLastName())
                .chatId(chat.getId())
                .uuid(UUID.randomUUID())
                .state(STALKER_STATE_NEW)
                .build();
        stalkerRepository.save(newEntity);
    }

    public StalkerEntity getStalkerByChatId(Long chatId) {
        return stalkerRepository.findByChatId(chatId)
                .orElseThrow(() -> notFound("Сталкер с chatId {0} не найден", chatId));
    }

    public StalkerEntity getStalkerByStalkerName(String stalkerName) {
        return stalkerRepository.findByStalkerName(stalkerName)
                .orElseThrow(() -> notFound("Сталкер с позывным {0} не найден", stalkerName));
    }

    public void saveStalker(StalkerEntity stalkerEntity) {
        stalkerRepository.save(stalkerEntity);
    }

    public void deleteByChatId(long chatId) {
        stalkerRepository.delete(getStalkerByChatId(chatId));
    }

    public SendDocument getResultFileSend(long chatId) throws IOException {
        List<StalkerEntity> allStalkers = getAllStalkers();
        List<StalkerEntity> filtered = allStalkers.stream().filter(s -> FINISH_TEST.equals(s.getState())).toList();
        File tempFile = File.createTempFile("result", ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile, true));
        bw.append(NR);
        bw.append(getColumnLine(STALKER_NAME, 31));
        bw.append(getColumnLine(STALKER_GROUP_NAME, 21));
        bw.append(getColumnLine(ATTEMPTS, 21));
        bw.append(TG_TAG);
        bw.append("\n");
        for (int i = 0; i < filtered.size(); i++) {
            bw.append("\n");
            bw.append(getColumnLine(String.valueOf(i + 1), 4));
            bw.append(getColumnLine(filtered.get(i).getStalkerName(), 31));
            bw.append(getColumnLine(filtered.get(i).getGroupName(), 21));
            bw.append(getColumnLine(String.valueOf(filtered.get(i).getAttempts()), 21));
            bw.append(filtered.get(i).getUserName());
        }

        bw.close();
        InputFile inputFile = new InputFile();
        inputFile.setMedia(tempFile);
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setDocument(inputFile);
        tempFile.deleteOnExit();
        return sendDocument;
    }

    private String getColumnLine(String body, int spaceSize) {
        return body + " ".repeat(Math.max(0, spaceSize - body.length()));
    }

}
