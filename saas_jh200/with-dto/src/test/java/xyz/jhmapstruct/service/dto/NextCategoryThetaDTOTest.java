package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryThetaDTO.class);
        NextCategoryThetaDTO nextCategoryThetaDTO1 = new NextCategoryThetaDTO();
        nextCategoryThetaDTO1.setId(1L);
        NextCategoryThetaDTO nextCategoryThetaDTO2 = new NextCategoryThetaDTO();
        assertThat(nextCategoryThetaDTO1).isNotEqualTo(nextCategoryThetaDTO2);
        nextCategoryThetaDTO2.setId(nextCategoryThetaDTO1.getId());
        assertThat(nextCategoryThetaDTO1).isEqualTo(nextCategoryThetaDTO2);
        nextCategoryThetaDTO2.setId(2L);
        assertThat(nextCategoryThetaDTO1).isNotEqualTo(nextCategoryThetaDTO2);
        nextCategoryThetaDTO1.setId(null);
        assertThat(nextCategoryThetaDTO1).isNotEqualTo(nextCategoryThetaDTO2);
    }
}
