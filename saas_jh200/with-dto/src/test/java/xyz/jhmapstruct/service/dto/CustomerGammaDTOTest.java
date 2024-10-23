package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerGammaDTO.class);
        CustomerGammaDTO customerGammaDTO1 = new CustomerGammaDTO();
        customerGammaDTO1.setId(1L);
        CustomerGammaDTO customerGammaDTO2 = new CustomerGammaDTO();
        assertThat(customerGammaDTO1).isNotEqualTo(customerGammaDTO2);
        customerGammaDTO2.setId(customerGammaDTO1.getId());
        assertThat(customerGammaDTO1).isEqualTo(customerGammaDTO2);
        customerGammaDTO2.setId(2L);
        assertThat(customerGammaDTO1).isNotEqualTo(customerGammaDTO2);
        customerGammaDTO1.setId(null);
        assertThat(customerGammaDTO1).isNotEqualTo(customerGammaDTO2);
    }
}
