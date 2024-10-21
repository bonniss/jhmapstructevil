package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPedroTaxViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPedroTaxViDTO.class);
        AlPedroTaxViDTO alPedroTaxViDTO1 = new AlPedroTaxViDTO();
        alPedroTaxViDTO1.setId(1L);
        AlPedroTaxViDTO alPedroTaxViDTO2 = new AlPedroTaxViDTO();
        assertThat(alPedroTaxViDTO1).isNotEqualTo(alPedroTaxViDTO2);
        alPedroTaxViDTO2.setId(alPedroTaxViDTO1.getId());
        assertThat(alPedroTaxViDTO1).isEqualTo(alPedroTaxViDTO2);
        alPedroTaxViDTO2.setId(2L);
        assertThat(alPedroTaxViDTO1).isNotEqualTo(alPedroTaxViDTO2);
        alPedroTaxViDTO1.setId(null);
        assertThat(alPedroTaxViDTO1).isNotEqualTo(alPedroTaxViDTO2);
    }
}
