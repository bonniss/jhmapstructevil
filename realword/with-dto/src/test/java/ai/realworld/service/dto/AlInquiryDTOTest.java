package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlInquiryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlInquiryDTO.class);
        AlInquiryDTO alInquiryDTO1 = new AlInquiryDTO();
        alInquiryDTO1.setId(UUID.randomUUID());
        AlInquiryDTO alInquiryDTO2 = new AlInquiryDTO();
        assertThat(alInquiryDTO1).isNotEqualTo(alInquiryDTO2);
        alInquiryDTO2.setId(alInquiryDTO1.getId());
        assertThat(alInquiryDTO1).isEqualTo(alInquiryDTO2);
        alInquiryDTO2.setId(UUID.randomUUID());
        assertThat(alInquiryDTO1).isNotEqualTo(alInquiryDTO2);
        alInquiryDTO1.setId(null);
        assertThat(alInquiryDTO1).isNotEqualTo(alInquiryDTO2);
    }
}
