package com.nowscas.rules.repository;

import com.nowscas.rules.model.AnswerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long>, JpaSpecificationExecutor<AnswerEntity> {

    Optional<AnswerEntity> findByUuid(UUID uuid);

}