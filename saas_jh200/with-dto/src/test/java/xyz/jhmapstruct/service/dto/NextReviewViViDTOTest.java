package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewViViDTO.class);
        NextReviewViViDTO nextReviewViViDTO1 = new NextReviewViViDTO();
        nextReviewViViDTO1.setId(1L);
        NextReviewViViDTO nextReviewViViDTO2 = new NextReviewViViDTO();
        assertThat(nextReviewViViDTO1).isNotEqualTo(nextReviewViViDTO2);
        nextReviewViViDTO2.setId(nextReviewViViDTO1.getId());
        assertThat(nextReviewViViDTO1).isEqualTo(nextReviewViViDTO2);
        nextReviewViViDTO2.setId(2L);
        assertThat(nextReviewViViDTO1).isNotEqualTo(nextReviewViViDTO2);
        nextReviewViViDTO1.setId(null);
        assertThat(nextReviewViViDTO1).isNotEqualTo(nextReviewViViDTO2);
    }
}
