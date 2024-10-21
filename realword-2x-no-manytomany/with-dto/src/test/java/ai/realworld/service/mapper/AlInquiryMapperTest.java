package ai.realworld.service.mapper;

import static ai.realworld.domain.AlInquiryAsserts.*;
import static ai.realworld.domain.AlInquiryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AlInquiryMapperTest {

    private AlInquiryMapper alInquiryMapper;

    @BeforeEach
    void setUp() {
        alInquiryMapper = new AlInquiryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAlInquirySample1();
        var actual = alInquiryMapper.toEntity(alInquiryMapper.toDto(expected));
        assertAlInquiryAllPropertiesEquals(expected, actual);
    }
}
