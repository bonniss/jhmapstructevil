package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCategorySigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCategorySigmaDTO.class);
        NextCategorySigmaDTO nextCategorySigmaDTO1 = new NextCategorySigmaDTO();
        nextCategorySigmaDTO1.setId(1L);
        NextCategorySigmaDTO nextCategorySigmaDTO2 = new NextCategorySigmaDTO();
        assertThat(nextCategorySigmaDTO1).isNotEqualTo(nextCategorySigmaDTO2);
        nextCategorySigmaDTO2.setId(nextCategorySigmaDTO1.getId());
        assertThat(nextCategorySigmaDTO1).isEqualTo(nextCategorySigmaDTO2);
        nextCategorySigmaDTO2.setId(2L);
        assertThat(nextCategorySigmaDTO1).isNotEqualTo(nextCategorySigmaDTO2);
        nextCategorySigmaDTO1.setId(null);
        assertThat(nextCategorySigmaDTO1).isNotEqualTo(nextCategorySigmaDTO2);
    }
}
