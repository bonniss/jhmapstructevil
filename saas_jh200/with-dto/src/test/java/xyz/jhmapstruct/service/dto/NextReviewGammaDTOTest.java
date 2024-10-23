package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewGammaDTO.class);
        NextReviewGammaDTO nextReviewGammaDTO1 = new NextReviewGammaDTO();
        nextReviewGammaDTO1.setId(1L);
        NextReviewGammaDTO nextReviewGammaDTO2 = new NextReviewGammaDTO();
        assertThat(nextReviewGammaDTO1).isNotEqualTo(nextReviewGammaDTO2);
        nextReviewGammaDTO2.setId(nextReviewGammaDTO1.getId());
        assertThat(nextReviewGammaDTO1).isEqualTo(nextReviewGammaDTO2);
        nextReviewGammaDTO2.setId(2L);
        assertThat(nextReviewGammaDTO1).isNotEqualTo(nextReviewGammaDTO2);
        nextReviewGammaDTO1.setId(null);
        assertThat(nextReviewGammaDTO1).isNotEqualTo(nextReviewGammaDTO2);
    }
}
