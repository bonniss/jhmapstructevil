package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewGammaDTO.class);
        ReviewGammaDTO reviewGammaDTO1 = new ReviewGammaDTO();
        reviewGammaDTO1.setId(1L);
        ReviewGammaDTO reviewGammaDTO2 = new ReviewGammaDTO();
        assertThat(reviewGammaDTO1).isNotEqualTo(reviewGammaDTO2);
        reviewGammaDTO2.setId(reviewGammaDTO1.getId());
        assertThat(reviewGammaDTO1).isEqualTo(reviewGammaDTO2);
        reviewGammaDTO2.setId(2L);
        assertThat(reviewGammaDTO1).isNotEqualTo(reviewGammaDTO2);
        reviewGammaDTO1.setId(null);
        assertThat(reviewGammaDTO1).isNotEqualTo(reviewGammaDTO2);
    }
}
