package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewSigmaDTO.class);
        NextReviewSigmaDTO nextReviewSigmaDTO1 = new NextReviewSigmaDTO();
        nextReviewSigmaDTO1.setId(1L);
        NextReviewSigmaDTO nextReviewSigmaDTO2 = new NextReviewSigmaDTO();
        assertThat(nextReviewSigmaDTO1).isNotEqualTo(nextReviewSigmaDTO2);
        nextReviewSigmaDTO2.setId(nextReviewSigmaDTO1.getId());
        assertThat(nextReviewSigmaDTO1).isEqualTo(nextReviewSigmaDTO2);
        nextReviewSigmaDTO2.setId(2L);
        assertThat(nextReviewSigmaDTO1).isNotEqualTo(nextReviewSigmaDTO2);
        nextReviewSigmaDTO1.setId(null);
        assertThat(nextReviewSigmaDTO1).isNotEqualTo(nextReviewSigmaDTO2);
    }
}
