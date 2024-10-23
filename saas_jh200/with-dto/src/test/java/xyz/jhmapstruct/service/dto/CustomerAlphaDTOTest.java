package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerAlphaDTO.class);
        CustomerAlphaDTO customerAlphaDTO1 = new CustomerAlphaDTO();
        customerAlphaDTO1.setId(1L);
        CustomerAlphaDTO customerAlphaDTO2 = new CustomerAlphaDTO();
        assertThat(customerAlphaDTO1).isNotEqualTo(customerAlphaDTO2);
        customerAlphaDTO2.setId(customerAlphaDTO1.getId());
        assertThat(customerAlphaDTO1).isEqualTo(customerAlphaDTO2);
        customerAlphaDTO2.setId(2L);
        assertThat(customerAlphaDTO1).isNotEqualTo(customerAlphaDTO2);
        customerAlphaDTO1.setId(null);
        assertThat(customerAlphaDTO1).isNotEqualTo(customerAlphaDTO2);
    }
}
