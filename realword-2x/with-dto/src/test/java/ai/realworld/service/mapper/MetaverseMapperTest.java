package ai.realworld.service.mapper;

import static ai.realworld.domain.MetaverseAsserts.*;
import static ai.realworld.domain.MetaverseTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MetaverseMapperTest {

    private MetaverseMapper metaverseMapper;

    @BeforeEach
    void setUp() {
        metaverseMapper = new MetaverseMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMetaverseSample1();
        var actual = metaverseMapper.toEntity(metaverseMapper.toDto(expected));
        assertMetaverseAllPropertiesEquals(expected, actual);
    }
}
