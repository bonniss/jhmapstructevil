package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewMiMiDTO.class);
        NextReviewMiMiDTO nextReviewMiMiDTO1 = new NextReviewMiMiDTO();
        nextReviewMiMiDTO1.setId(1L);
        NextReviewMiMiDTO nextReviewMiMiDTO2 = new NextReviewMiMiDTO();
        assertThat(nextReviewMiMiDTO1).isNotEqualTo(nextReviewMiMiDTO2);
        nextReviewMiMiDTO2.setId(nextReviewMiMiDTO1.getId());
        assertThat(nextReviewMiMiDTO1).isEqualTo(nextReviewMiMiDTO2);
        nextReviewMiMiDTO2.setId(2L);
        assertThat(nextReviewMiMiDTO1).isNotEqualTo(nextReviewMiMiDTO2);
        nextReviewMiMiDTO1.setId(null);
        assertThat(nextReviewMiMiDTO1).isNotEqualTo(nextReviewMiMiDTO2);
    }
}
