package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryViDTO.class);
        NextCategoryViDTO nextCategoryViDTO1 = new NextCategoryViDTO();
        nextCategoryViDTO1.setId(1L);
        NextCategoryViDTO nextCategoryViDTO2 = new NextCategoryViDTO();
        assertThat(nextCategoryViDTO1).isNotEqualTo(nextCategoryViDTO2);
        nextCategoryViDTO2.setId(nextCategoryViDTO1.getId());
        assertThat(nextCategoryViDTO1).isEqualTo(nextCategoryViDTO2);
        nextCategoryViDTO2.setId(2L);
        assertThat(nextCategoryViDTO1).isNotEqualTo(nextCategoryViDTO2);
        nextCategoryViDTO1.setId(null);
        assertThat(nextCategoryViDTO1).isNotEqualTo(nextCategoryViDTO2);
    }
}
