package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextReviewThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextReviewThetaDTO.class);
        NextReviewThetaDTO nextReviewThetaDTO1 = new NextReviewThetaDTO();
        nextReviewThetaDTO1.setId(1L);
        NextReviewThetaDTO nextReviewThetaDTO2 = new NextReviewThetaDTO();
        assertThat(nextReviewThetaDTO1).isNotEqualTo(nextReviewThetaDTO2);
        nextReviewThetaDTO2.setId(nextReviewThetaDTO1.getId());
        assertThat(nextReviewThetaDTO1).isEqualTo(nextReviewThetaDTO2);
        nextReviewThetaDTO2.setId(2L);
        assertThat(nextReviewThetaDTO1).isNotEqualTo(nextReviewThetaDTO2);
        nextReviewThetaDTO1.setId(null);
        assertThat(nextReviewThetaDTO1).isNotEqualTo(nextReviewThetaDTO2);
    }
}
