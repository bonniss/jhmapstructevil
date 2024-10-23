package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryMiMiDTO.class);
        NextCategoryMiMiDTO nextCategoryMiMiDTO1 = new NextCategoryMiMiDTO();
        nextCategoryMiMiDTO1.setId(1L);
        NextCategoryMiMiDTO nextCategoryMiMiDTO2 = new NextCategoryMiMiDTO();
        assertThat(nextCategoryMiMiDTO1).isNotEqualTo(nextCategoryMiMiDTO2);
        nextCategoryMiMiDTO2.setId(nextCategoryMiMiDTO1.getId());
        assertThat(nextCategoryMiMiDTO1).isEqualTo(nextCategoryMiMiDTO2);
        nextCategoryMiMiDTO2.setId(2L);
        assertThat(nextCategoryMiMiDTO1).isNotEqualTo(nextCategoryMiMiDTO2);
        nextCategoryMiMiDTO1.setId(null);
        assertThat(nextCategoryMiMiDTO1).isNotEqualTo(nextCategoryMiMiDTO2);
    }
}
