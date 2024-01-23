package com.nowscas.rules.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {AnswerMapper.class})
public interface QuestionMapper {

}
