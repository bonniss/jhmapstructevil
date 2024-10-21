package ai.realworld.service.mapper;

import static ai.realworld.domain.AppMessageTemplateAsserts.*;
import static ai.realworld.domain.AppMessageTemplateTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppMessageTemplateMapperTest {

    private AppMessageTemplateMapper appMessageTemplateMapper;

    @BeforeEach
    void setUp() {
        appMessageTemplateMapper = new AppMessageTemplateMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAppMessageTemplateSample1();
        var actual = appMessageTemplateMapper.toEntity(appMessageTemplateMapper.toDto(expected));
        assertAppMessageTemplateAllPropertiesEquals(expected, actual);
    }
}
