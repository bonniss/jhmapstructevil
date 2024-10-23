package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewDTO.class);
        NextReviewDTO nextReviewDTO1 = new NextReviewDTO();
        nextReviewDTO1.setId(1L);
        NextReviewDTO nextReviewDTO2 = new NextReviewDTO();
        assertThat(nextReviewDTO1).isNotEqualTo(nextReviewDTO2);
        nextReviewDTO2.setId(nextReviewDTO1.getId());
        assertThat(nextReviewDTO1).isEqualTo(nextReviewDTO2);
        nextReviewDTO2.setId(2L);
        assertThat(nextReviewDTO1).isNotEqualTo(nextReviewDTO2);
        nextReviewDTO1.setId(null);
        assertThat(nextReviewDTO1).isNotEqualTo(nextReviewDTO2);
    }
}
