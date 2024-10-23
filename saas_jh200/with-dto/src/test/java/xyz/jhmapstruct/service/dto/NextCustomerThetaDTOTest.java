package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerThetaDTO.class);
        NextCustomerThetaDTO nextCustomerThetaDTO1 = new NextCustomerThetaDTO();
        nextCustomerThetaDTO1.setId(1L);
        NextCustomerThetaDTO nextCustomerThetaDTO2 = new NextCustomerThetaDTO();
        assertThat(nextCustomerThetaDTO1).isNotEqualTo(nextCustomerThetaDTO2);
        nextCustomerThetaDTO2.setId(nextCustomerThetaDTO1.getId());
        assertThat(nextCustomerThetaDTO1).isEqualTo(nextCustomerThetaDTO2);
        nextCustomerThetaDTO2.setId(2L);
        assertThat(nextCustomerThetaDTO1).isNotEqualTo(nextCustomerThetaDTO2);
        nextCustomerThetaDTO1.setId(null);
        assertThat(nextCustomerThetaDTO1).isNotEqualTo(nextCustomerThetaDTO2);
    }
}
