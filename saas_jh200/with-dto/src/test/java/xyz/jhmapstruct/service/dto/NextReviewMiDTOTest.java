package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewMiDTO.class);
        NextReviewMiDTO nextReviewMiDTO1 = new NextReviewMiDTO();
        nextReviewMiDTO1.setId(1L);
        NextReviewMiDTO nextReviewMiDTO2 = new NextReviewMiDTO();
        assertThat(nextReviewMiDTO1).isNotEqualTo(nextReviewMiDTO2);
        nextReviewMiDTO2.setId(nextReviewMiDTO1.getId());
        assertThat(nextReviewMiDTO1).isEqualTo(nextReviewMiDTO2);
        nextReviewMiDTO2.setId(2L);
        assertThat(nextReviewMiDTO1).isNotEqualTo(nextReviewMiDTO2);
        nextReviewMiDTO1.setId(null);
        assertThat(nextReviewMiDTO1).isNotEqualTo(nextReviewMiDTO2);
    }
}
