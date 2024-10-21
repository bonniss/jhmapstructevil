package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPedroTaxDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPedroTaxDTO.class);
        AlPedroTaxDTO alPedroTaxDTO1 = new AlPedroTaxDTO();
        alPedroTaxDTO1.setId(1L);
        AlPedroTaxDTO alPedroTaxDTO2 = new AlPedroTaxDTO();
        assertThat(alPedroTaxDTO1).isNotEqualTo(alPedroTaxDTO2);
        alPedroTaxDTO2.setId(alPedroTaxDTO1.getId());
        assertThat(alPedroTaxDTO1).isEqualTo(alPedroTaxDTO2);
        alPedroTaxDTO2.setId(2L);
        assertThat(alPedroTaxDTO1).isNotEqualTo(alPedroTaxDTO2);
        alPedroTaxDTO1.setId(null);
        assertThat(alPedroTaxDTO1).isNotEqualTo(alPedroTaxDTO2);
    }
}
