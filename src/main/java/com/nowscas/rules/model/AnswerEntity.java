package com.nowscas.rules.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answers")
public class AnswerEntity implements Serializable {

    @Column(name = "answer")
    private String answer;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private QuestionEntity question;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @UpdateTimestamp
    @Column(name = "updated", nullable = false)
    private ZonedDateTime updated;

    @Column(name = "actual", nullable = false)
    private boolean actual;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
