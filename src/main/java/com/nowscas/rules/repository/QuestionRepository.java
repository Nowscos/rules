package com.nowscas.rules.repository;

import com.nowscas.rules.model.QuestionEntity;
import com.nowscas.rules.model.StalkerEntity;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Long>, JpaSpecificationExecutor<QuestionEntity> {

}