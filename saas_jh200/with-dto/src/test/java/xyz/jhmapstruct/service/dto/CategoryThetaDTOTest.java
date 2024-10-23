package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryThetaDTO.class);
        CategoryThetaDTO categoryThetaDTO1 = new CategoryThetaDTO();
        categoryThetaDTO1.setId(1L);
        CategoryThetaDTO categoryThetaDTO2 = new CategoryThetaDTO();
        assertThat(categoryThetaDTO1).isNotEqualTo(categoryThetaDTO2);
        categoryThetaDTO2.setId(categoryThetaDTO1.getId());
        assertThat(categoryThetaDTO1).isEqualTo(categoryThetaDTO2);
        categoryThetaDTO2.setId(2L);
        assertThat(categoryThetaDTO1).isNotEqualTo(categoryThetaDTO2);
        categoryThetaDTO1.setId(null);
        assertThat(categoryThetaDTO1).isNotEqualTo(categoryThetaDTO2);
    }
}
