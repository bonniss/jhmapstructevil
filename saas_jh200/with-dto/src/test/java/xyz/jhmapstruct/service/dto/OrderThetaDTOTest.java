package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderThetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderThetaDTO.class);
        OrderThetaDTO orderThetaDTO1 = new OrderThetaDTO();
        orderThetaDTO1.setId(1L);
        OrderThetaDTO orderThetaDTO2 = new OrderThetaDTO();
        assertThat(orderThetaDTO1).isNotEqualTo(orderThetaDTO2);
        orderThetaDTO2.setId(orderThetaDTO1.getId());
        assertThat(orderThetaDTO1).isEqualTo(orderThetaDTO2);
        orderThetaDTO2.setId(2L);
        assertThat(orderThetaDTO1).isNotEqualTo(orderThetaDTO2);
        orderThetaDTO1.setId(null);
        assertThat(orderThetaDTO1).isNotEqualTo(orderThetaDTO2);
    }
}
