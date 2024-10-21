package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllMassageThaiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllMassageThaiDTO.class);
        AllMassageThaiDTO allMassageThaiDTO1 = new AllMassageThaiDTO();
        allMassageThaiDTO1.setId(1L);
        AllMassageThaiDTO allMassageThaiDTO2 = new AllMassageThaiDTO();
        assertThat(allMassageThaiDTO1).isNotEqualTo(allMassageThaiDTO2);
        allMassageThaiDTO2.setId(allMassageThaiDTO1.getId());
        assertThat(allMassageThaiDTO1).isEqualTo(allMassageThaiDTO2);
        allMassageThaiDTO2.setId(2L);
        assertThat(allMassageThaiDTO1).isNotEqualTo(allMassageThaiDTO2);
        allMassageThaiDTO1.setId(null);
        assertThat(allMassageThaiDTO1).isNotEqualTo(allMassageThaiDTO2);
    }
}
