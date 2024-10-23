package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryGammaDTO.class);
        NextCategoryGammaDTO nextCategoryGammaDTO1 = new NextCategoryGammaDTO();
        nextCategoryGammaDTO1.setId(1L);
        NextCategoryGammaDTO nextCategoryGammaDTO2 = new NextCategoryGammaDTO();
        assertThat(nextCategoryGammaDTO1).isNotEqualTo(nextCategoryGammaDTO2);
        nextCategoryGammaDTO2.setId(nextCategoryGammaDTO1.getId());
        assertThat(nextCategoryGammaDTO1).isEqualTo(nextCategoryGammaDTO2);
        nextCategoryGammaDTO2.setId(2L);
        assertThat(nextCategoryGammaDTO1).isNotEqualTo(nextCategoryGammaDTO2);
        nextCategoryGammaDTO1.setId(null);
        assertThat(nextCategoryGammaDTO1).isNotEqualTo(nextCategoryGammaDTO2);
    }
}
