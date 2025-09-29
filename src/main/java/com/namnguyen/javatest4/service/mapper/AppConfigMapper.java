package com.namnguyen.javatest4.service.mapper;

import com.namnguyen.javatest4.domain.AppConfig;
import com.namnguyen.javatest4.service.dto.AppConfigDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AppConfig} and its DTO {@link AppConfigDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppConfigMapper extends EntityMapper<AppConfigDTO, AppConfig> {}
