package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllMassageThaiViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllMassageThaiViDTO.class);
        AllMassageThaiViDTO allMassageThaiViDTO1 = new AllMassageThaiViDTO();
        allMassageThaiViDTO1.setId(1L);
        AllMassageThaiViDTO allMassageThaiViDTO2 = new AllMassageThaiViDTO();
        assertThat(allMassageThaiViDTO1).isNotEqualTo(allMassageThaiViDTO2);
        allMassageThaiViDTO2.setId(allMassageThaiViDTO1.getId());
        assertThat(allMassageThaiViDTO1).isEqualTo(allMassageThaiViDTO2);
        allMassageThaiViDTO2.setId(2L);
        assertThat(allMassageThaiViDTO1).isNotEqualTo(allMassageThaiViDTO2);
        allMassageThaiViDTO1.setId(null);
        assertThat(allMassageThaiViDTO1).isNotEqualTo(allMassageThaiViDTO2);
    }
}
