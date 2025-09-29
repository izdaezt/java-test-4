package com.namnguyen.javatest4.service.mapper;

import static com.namnguyen.javatest4.domain.AppConfigAsserts.*;
import static com.namnguyen.javatest4.domain.AppConfigTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppConfigMapperTest {

    private AppConfigMapper appConfigMapper;

    @BeforeEach
    void setUp() {
        appConfigMapper = new AppConfigMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppConfigSample1();
        var actual = appConfigMapper.toEntity(appConfigMapper.toDto(expected));
        assertAppConfigAllPropertiesEquals(expected, actual);
    }
}
