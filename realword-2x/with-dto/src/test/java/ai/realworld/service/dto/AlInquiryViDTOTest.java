package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AlInquiryViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlInquiryViDTO.class);
        AlInquiryViDTO alInquiryViDTO1 = new AlInquiryViDTO();
        alInquiryViDTO1.setId(UUID.randomUUID());
        AlInquiryViDTO alInquiryViDTO2 = new AlInquiryViDTO();
        assertThat(alInquiryViDTO1).isNotEqualTo(alInquiryViDTO2);
        alInquiryViDTO2.setId(alInquiryViDTO1.getId());
        assertThat(alInquiryViDTO1).isEqualTo(alInquiryViDTO2);
        alInquiryViDTO2.setId(UUID.randomUUID());
        assertThat(alInquiryViDTO1).isNotEqualTo(alInquiryViDTO2);
        alInquiryViDTO1.setId(null);
        assertThat(alInquiryViDTO1).isNotEqualTo(alInquiryViDTO2);
    }
}
