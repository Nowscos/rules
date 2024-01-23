package com.nowscas.rules.mapper;

import com.nowscas.rules.model.StalkerDto;
import com.nowscas.rules.model.StalkerEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StalkerMapper {

    StalkerEntity map(StalkerDto dto);

    StalkerDto map(StalkerEntity entity);
}
