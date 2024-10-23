package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerSigmaDTO.class);
        CustomerSigmaDTO customerSigmaDTO1 = new CustomerSigmaDTO();
        customerSigmaDTO1.setId(1L);
        CustomerSigmaDTO customerSigmaDTO2 = new CustomerSigmaDTO();
        assertThat(customerSigmaDTO1).isNotEqualTo(customerSigmaDTO2);
        customerSigmaDTO2.setId(customerSigmaDTO1.getId());
        assertThat(customerSigmaDTO1).isEqualTo(customerSigmaDTO2);
        customerSigmaDTO2.setId(2L);
        assertThat(customerSigmaDTO1).isNotEqualTo(customerSigmaDTO2);
        customerSigmaDTO1.setId(null);
        assertThat(customerSigmaDTO1).isNotEqualTo(customerSigmaDTO2);
    }
}
