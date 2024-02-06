package com.nowscas.rules.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StalkerDto implements Serializable {

    private String userName;

    private String stalkerName;

    private String groupName;

    private String state;

    private String firstName;

    private String lastName;

    private Long chatId;

    private int lastMessageId;

    private String messageText;

    private String lastThreadName;

    private Integer currentAnswers;

    private Integer attempts;

    private String[] passedQuestions;

    private ZonedDateTime tested;

    private ZonedDateTime created;

    private boolean actual;

    private UUID uuid;

    private Long id;

}
