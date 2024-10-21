package ai.realworld.service.mapper;

import static ai.realworld.domain.AlInquiryViAsserts.*;
import static ai.realworld.domain.AlInquiryViTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlInquiryViMapperTest {

    private AlInquiryViMapper alInquiryViMapper;

    @BeforeEach
    void setUp() {
        alInquiryViMapper = new AlInquiryViMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlInquiryViSample1();
        var actual = alInquiryViMapper.toEntity(alInquiryViMapper.toDto(expected));
        assertAlInquiryViAllPropertiesEquals(expected, actual);
    }
}
