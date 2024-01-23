package com.nowscas.rules.model;

import java.io.Serializable;
import java.util.List;
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
public class QuestionDto implements Serializable {

    private String question;

    private String rightAnswer;

    private List<String> answers;

}
