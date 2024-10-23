package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryAlphaDTO.class);
        NextCategoryAlphaDTO nextCategoryAlphaDTO1 = new NextCategoryAlphaDTO();
        nextCategoryAlphaDTO1.setId(1L);
        NextCategoryAlphaDTO nextCategoryAlphaDTO2 = new NextCategoryAlphaDTO();
        assertThat(nextCategoryAlphaDTO1).isNotEqualTo(nextCategoryAlphaDTO2);
        nextCategoryAlphaDTO2.setId(nextCategoryAlphaDTO1.getId());
        assertThat(nextCategoryAlphaDTO1).isEqualTo(nextCategoryAlphaDTO2);
        nextCategoryAlphaDTO2.setId(2L);
        assertThat(nextCategoryAlphaDTO1).isNotEqualTo(nextCategoryAlphaDTO2);
        nextCategoryAlphaDTO1.setId(null);
        assertThat(nextCategoryAlphaDTO1).isNotEqualTo(nextCategoryAlphaDTO2);
    }
}
