package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewAlphaDTO.class);
        ReviewAlphaDTO reviewAlphaDTO1 = new ReviewAlphaDTO();
        reviewAlphaDTO1.setId(1L);
        ReviewAlphaDTO reviewAlphaDTO2 = new ReviewAlphaDTO();
        assertThat(reviewAlphaDTO1).isNotEqualTo(reviewAlphaDTO2);
        reviewAlphaDTO2.setId(reviewAlphaDTO1.getId());
        assertThat(reviewAlphaDTO1).isEqualTo(reviewAlphaDTO2);
        reviewAlphaDTO2.setId(2L);
        assertThat(reviewAlphaDTO1).isNotEqualTo(reviewAlphaDTO2);
        reviewAlphaDTO1.setId(null);
        assertThat(reviewAlphaDTO1).isNotEqualTo(reviewAlphaDTO2);
    }
}
