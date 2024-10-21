package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerMiDTO.class);
        CustomerMiDTO customerMiDTO1 = new CustomerMiDTO();
        customerMiDTO1.setId(1L);
        CustomerMiDTO customerMiDTO2 = new CustomerMiDTO();
        assertThat(customerMiDTO1).isNotEqualTo(customerMiDTO2);
        customerMiDTO2.setId(customerMiDTO1.getId());
        assertThat(customerMiDTO1).isEqualTo(customerMiDTO2);
        customerMiDTO2.setId(2L);
        assertThat(customerMiDTO1).isNotEqualTo(customerMiDTO2);
        customerMiDTO1.setId(null);
        assertThat(customerMiDTO1).isNotEqualTo(customerMiDTO2);
    }
}
