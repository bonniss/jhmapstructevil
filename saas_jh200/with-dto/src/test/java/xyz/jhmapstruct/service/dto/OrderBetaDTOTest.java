package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderBetaDTO.class);
        OrderBetaDTO orderBetaDTO1 = new OrderBetaDTO();
        orderBetaDTO1.setId(1L);
        OrderBetaDTO orderBetaDTO2 = new OrderBetaDTO();
        assertThat(orderBetaDTO1).isNotEqualTo(orderBetaDTO2);
        orderBetaDTO2.setId(orderBetaDTO1.getId());
        assertThat(orderBetaDTO1).isEqualTo(orderBetaDTO2);
        orderBetaDTO2.setId(2L);
        assertThat(orderBetaDTO1).isNotEqualTo(orderBetaDTO2);
        orderBetaDTO1.setId(null);
        assertThat(orderBetaDTO1).isNotEqualTo(orderBetaDTO2);
    }
}
