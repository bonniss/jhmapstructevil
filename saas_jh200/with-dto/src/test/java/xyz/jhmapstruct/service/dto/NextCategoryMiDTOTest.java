package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryMiDTO.class);
        NextCategoryMiDTO nextCategoryMiDTO1 = new NextCategoryMiDTO();
        nextCategoryMiDTO1.setId(1L);
        NextCategoryMiDTO nextCategoryMiDTO2 = new NextCategoryMiDTO();
        assertThat(nextCategoryMiDTO1).isNotEqualTo(nextCategoryMiDTO2);
        nextCategoryMiDTO2.setId(nextCategoryMiDTO1.getId());
        assertThat(nextCategoryMiDTO1).isEqualTo(nextCategoryMiDTO2);
        nextCategoryMiDTO2.setId(2L);
        assertThat(nextCategoryMiDTO1).isNotEqualTo(nextCategoryMiDTO2);
        nextCategoryMiDTO1.setId(null);
        assertThat(nextCategoryMiDTO1).isNotEqualTo(nextCategoryMiDTO2);
    }
}
