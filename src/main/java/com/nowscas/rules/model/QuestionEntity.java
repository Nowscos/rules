package com.nowscas.rules.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;
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
@Table(name = "questions")
public class QuestionEntity implements Serializable {

    @Column(name = "question")
    private String question;

    @Column(name = "right_answer")
    private String rightAnswer;

    @ToString.Exclude
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AnswerEntity> answers;

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
