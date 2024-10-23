package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryViViDTO.class);
        NextCategoryViViDTO nextCategoryViViDTO1 = new NextCategoryViViDTO();
        nextCategoryViViDTO1.setId(1L);
        NextCategoryViViDTO nextCategoryViViDTO2 = new NextCategoryViViDTO();
        assertThat(nextCategoryViViDTO1).isNotEqualTo(nextCategoryViViDTO2);
        nextCategoryViViDTO2.setId(nextCategoryViViDTO1.getId());
        assertThat(nextCategoryViViDTO1).isEqualTo(nextCategoryViViDTO2);
        nextCategoryViViDTO2.setId(2L);
        assertThat(nextCategoryViViDTO1).isNotEqualTo(nextCategoryViViDTO2);
        nextCategoryViViDTO1.setId(null);
        assertThat(nextCategoryViViDTO1).isNotEqualTo(nextCategoryViViDTO2);
    }
}
