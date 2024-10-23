package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategorySigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorySigmaDTO.class);
        CategorySigmaDTO categorySigmaDTO1 = new CategorySigmaDTO();
        categorySigmaDTO1.setId(1L);
        CategorySigmaDTO categorySigmaDTO2 = new CategorySigmaDTO();
        assertThat(categorySigmaDTO1).isNotEqualTo(categorySigmaDTO2);
        categorySigmaDTO2.setId(categorySigmaDTO1.getId());
        assertThat(categorySigmaDTO1).isEqualTo(categorySigmaDTO2);
        categorySigmaDTO2.setId(2L);
        assertThat(categorySigmaDTO1).isNotEqualTo(categorySigmaDTO2);
        categorySigmaDTO1.setId(null);
        assertThat(categorySigmaDTO1).isNotEqualTo(categorySigmaDTO2);
    }
}
