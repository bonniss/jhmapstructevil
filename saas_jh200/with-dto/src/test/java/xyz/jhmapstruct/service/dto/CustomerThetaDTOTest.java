package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CustomerThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerThetaDTO.class);
        CustomerThetaDTO customerThetaDTO1 = new CustomerThetaDTO();
        customerThetaDTO1.setId(1L);
        CustomerThetaDTO customerThetaDTO2 = new CustomerThetaDTO();
        assertThat(customerThetaDTO1).isNotEqualTo(customerThetaDTO2);
        customerThetaDTO2.setId(customerThetaDTO1.getId());
        assertThat(customerThetaDTO1).isEqualTo(customerThetaDTO2);
        customerThetaDTO2.setId(2L);
        assertThat(customerThetaDTO1).isNotEqualTo(customerThetaDTO2);
        customerThetaDTO1.setId(null);
        assertThat(customerThetaDTO1).isNotEqualTo(customerThetaDTO2);
    }
}
