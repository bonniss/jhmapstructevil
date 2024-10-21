package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerMiMiDTO.class);
        CustomerMiMiDTO customerMiMiDTO1 = new CustomerMiMiDTO();
        customerMiMiDTO1.setId(1L);
        CustomerMiMiDTO customerMiMiDTO2 = new CustomerMiMiDTO();
        assertThat(customerMiMiDTO1).isNotEqualTo(customerMiMiDTO2);
        customerMiMiDTO2.setId(customerMiMiDTO1.getId());
        assertThat(customerMiMiDTO1).isEqualTo(customerMiMiDTO2);
        customerMiMiDTO2.setId(2L);
        assertThat(customerMiMiDTO1).isNotEqualTo(customerMiMiDTO2);
        customerMiMiDTO1.setId(null);
        assertThat(customerMiMiDTO1).isNotEqualTo(customerMiMiDTO2);
    }
}
