package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerBetaDTO.class);
        CustomerBetaDTO customerBetaDTO1 = new CustomerBetaDTO();
        customerBetaDTO1.setId(1L);
        CustomerBetaDTO customerBetaDTO2 = new CustomerBetaDTO();
        assertThat(customerBetaDTO1).isNotEqualTo(customerBetaDTO2);
        customerBetaDTO2.setId(customerBetaDTO1.getId());
        assertThat(customerBetaDTO1).isEqualTo(customerBetaDTO2);
        customerBetaDTO2.setId(2L);
        assertThat(customerBetaDTO1).isNotEqualTo(customerBetaDTO2);
        customerBetaDTO1.setId(null);
        assertThat(customerBetaDTO1).isNotEqualTo(customerBetaDTO2);
    }
}
