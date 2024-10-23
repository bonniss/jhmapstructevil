package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewAlphaDTO.class);
        NextReviewAlphaDTO nextReviewAlphaDTO1 = new NextReviewAlphaDTO();
        nextReviewAlphaDTO1.setId(1L);
        NextReviewAlphaDTO nextReviewAlphaDTO2 = new NextReviewAlphaDTO();
        assertThat(nextReviewAlphaDTO1).isNotEqualTo(nextReviewAlphaDTO2);
        nextReviewAlphaDTO2.setId(nextReviewAlphaDTO1.getId());
        assertThat(nextReviewAlphaDTO1).isEqualTo(nextReviewAlphaDTO2);
        nextReviewAlphaDTO2.setId(2L);
        assertThat(nextReviewAlphaDTO1).isNotEqualTo(nextReviewAlphaDTO2);
        nextReviewAlphaDTO1.setId(null);
        assertThat(nextReviewAlphaDTO1).isNotEqualTo(nextReviewAlphaDTO2);
    }
}
