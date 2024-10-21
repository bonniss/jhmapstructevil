package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderMiDTO.class);
        OrderMiDTO orderMiDTO1 = new OrderMiDTO();
        orderMiDTO1.setId(1L);
        OrderMiDTO orderMiDTO2 = new OrderMiDTO();
        assertThat(orderMiDTO1).isNotEqualTo(orderMiDTO2);
        orderMiDTO2.setId(orderMiDTO1.getId());
        assertThat(orderMiDTO1).isEqualTo(orderMiDTO2);
        orderMiDTO2.setId(2L);
        assertThat(orderMiDTO1).isNotEqualTo(orderMiDTO2);
        orderMiDTO1.setId(null);
        assertThat(orderMiDTO1).isNotEqualTo(orderMiDTO2);
    }
}
