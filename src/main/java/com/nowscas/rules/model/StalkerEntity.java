package com.nowscas.rules.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stalkers")
public class StalkerEntity implements Serializable {

    @Column(name = "user_name")
    private String userName;

    @Column(name = "stalker_name")
    private String stalkerName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "state")
    private String state;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "last_message_id")
    private int lastMessageId;

    @Column(name = "message_text")
    private String messageText;

    @Column(name = "last_thread_name")
    private String lastThreadName;

    @Column(name = "current_answers")
    private int currentAnswers;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "passed_questions")
    private String[] passedQuestions;

    @Column(name = "tested")
    private ZonedDateTime tested;

    @CreationTimestamp
    @Column(name = "created", nullable = false)
    private ZonedDateTime created;

    @Column(name = "actual", nullable = false)
    private boolean actual;

    @Column(name = "uuid", nullable = false)
    private UUID uuid;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
