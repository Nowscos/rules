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
public class AnswerDto implements Serializable {

    private String answer;

    private ZonedDateTime created;

    private ZonedDateTime updated;

    private boolean actual;

    private UUID uuid;

    private Long id;

}
