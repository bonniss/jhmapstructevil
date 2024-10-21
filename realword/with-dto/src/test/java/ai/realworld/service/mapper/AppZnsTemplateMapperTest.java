package ai.realworld.service.mapper;

import static ai.realworld.domain.AppZnsTemplateAsserts.*;
import static ai.realworld.domain.AppZnsTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppZnsTemplateMapperTest {

    private AppZnsTemplateMapper appZnsTemplateMapper;

    @BeforeEach
    void setUp() {
        appZnsTemplateMapper = new AppZnsTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppZnsTemplateSample1();
        var actual = appZnsTemplateMapper.toEntity(appZnsTemplateMapper.toDto(expected));
        assertAppZnsTemplateAllPropertiesEquals(expected, actual);
    }
}
