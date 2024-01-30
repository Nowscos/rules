package com.nowscas.rules.service;

import com.nowscas.rules.model.StalkerEntity;
import com.nowscas.rules.repository.StalkerRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Chat;

import static com.nowscas.rules.exception.StalkerException.notFound;
import static com.nowscas.rules.util.Constants.STALKER_STATE_NEW;

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

    public void saveStalker(StalkerEntity stalkerEntity) {
        stalkerRepository.save(stalkerEntity);
    }

    public void deleteByChatId(long chatId) {
        stalkerRepository.delete(getStalkerByChatId(chatId));
    }

    public void delete(StalkerEntity stalkerEntity) {
        stalkerRepository.delete(stalkerEntity);
    }

}
