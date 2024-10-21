package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerViDTO.class);
        CustomerViDTO customerViDTO1 = new CustomerViDTO();
        customerViDTO1.setId(1L);
        CustomerViDTO customerViDTO2 = new CustomerViDTO();
        assertThat(customerViDTO1).isNotEqualTo(customerViDTO2);
        customerViDTO2.setId(customerViDTO1.getId());
        assertThat(customerViDTO1).isEqualTo(customerViDTO2);
        customerViDTO2.setId(2L);
        assertThat(customerViDTO1).isNotEqualTo(customerViDTO2);
        customerViDTO1.setId(null);
        assertThat(customerViDTO1).isNotEqualTo(customerViDTO2);
    }
}
