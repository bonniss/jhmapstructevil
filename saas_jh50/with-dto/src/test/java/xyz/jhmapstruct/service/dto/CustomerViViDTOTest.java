package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerViViDTO.class);
        CustomerViViDTO customerViViDTO1 = new CustomerViViDTO();
        customerViViDTO1.setId(1L);
        CustomerViViDTO customerViViDTO2 = new CustomerViViDTO();
        assertThat(customerViViDTO1).isNotEqualTo(customerViViDTO2);
        customerViViDTO2.setId(customerViViDTO1.getId());
        assertThat(customerViViDTO1).isEqualTo(customerViViDTO2);
        customerViViDTO2.setId(2L);
        assertThat(customerViViDTO1).isNotEqualTo(customerViViDTO2);
        customerViViDTO1.setId(null);
        assertThat(customerViViDTO1).isNotEqualTo(customerViViDTO2);
    }
}
