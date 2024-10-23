package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewViDTO.class);
        NextReviewViDTO nextReviewViDTO1 = new NextReviewViDTO();
        nextReviewViDTO1.setId(1L);
        NextReviewViDTO nextReviewViDTO2 = new NextReviewViDTO();
        assertThat(nextReviewViDTO1).isNotEqualTo(nextReviewViDTO2);
        nextReviewViDTO2.setId(nextReviewViDTO1.getId());
        assertThat(nextReviewViDTO1).isEqualTo(nextReviewViDTO2);
        nextReviewViDTO2.setId(2L);
        assertThat(nextReviewViDTO1).isNotEqualTo(nextReviewViDTO2);
        nextReviewViDTO1.setId(null);
        assertThat(nextReviewViDTO1).isNotEqualTo(nextReviewViDTO2);
    }
}
