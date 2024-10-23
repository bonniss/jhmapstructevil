package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategoryBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategoryBetaDTO.class);
        NextCategoryBetaDTO nextCategoryBetaDTO1 = new NextCategoryBetaDTO();
        nextCategoryBetaDTO1.setId(1L);
        NextCategoryBetaDTO nextCategoryBetaDTO2 = new NextCategoryBetaDTO();
        assertThat(nextCategoryBetaDTO1).isNotEqualTo(nextCategoryBetaDTO2);
        nextCategoryBetaDTO2.setId(nextCategoryBetaDTO1.getId());
        assertThat(nextCategoryBetaDTO1).isEqualTo(nextCategoryBetaDTO2);
        nextCategoryBetaDTO2.setId(2L);
        assertThat(nextCategoryBetaDTO1).isNotEqualTo(nextCategoryBetaDTO2);
        nextCategoryBetaDTO1.setId(null);
        assertThat(nextCategoryBetaDTO1).isNotEqualTo(nextCategoryBetaDTO2);
    }
}
