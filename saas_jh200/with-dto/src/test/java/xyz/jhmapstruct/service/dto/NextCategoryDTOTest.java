package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryDTO.class);
        NextCategoryDTO nextCategoryDTO1 = new NextCategoryDTO();
        nextCategoryDTO1.setId(1L);
        NextCategoryDTO nextCategoryDTO2 = new NextCategoryDTO();
        assertThat(nextCategoryDTO1).isNotEqualTo(nextCategoryDTO2);
        nextCategoryDTO2.setId(nextCategoryDTO1.getId());
        assertThat(nextCategoryDTO1).isEqualTo(nextCategoryDTO2);
        nextCategoryDTO2.setId(2L);
        assertThat(nextCategoryDTO1).isNotEqualTo(nextCategoryDTO2);
        nextCategoryDTO1.setId(null);
        assertThat(nextCategoryDTO1).isNotEqualTo(nextCategoryDTO2);
    }
}
