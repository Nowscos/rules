package com.nowscas.rules.repository;

import com.nowscas.rules.model.StalkerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface StalkerRepository extends JpaRepository<StalkerEntity, Long>, JpaSpecificationExecutor<StalkerEntity> {

    Optional<StalkerEntity> findByChatId(Long chatId);

    Optional<StalkerEntity> findByStalkerName(String stalkerName);
}