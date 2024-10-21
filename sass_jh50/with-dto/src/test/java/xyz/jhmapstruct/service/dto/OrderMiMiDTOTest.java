package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderMiMiDTO.class);
        OrderMiMiDTO orderMiMiDTO1 = new OrderMiMiDTO();
        orderMiMiDTO1.setId(1L);
        OrderMiMiDTO orderMiMiDTO2 = new OrderMiMiDTO();
        assertThat(orderMiMiDTO1).isNotEqualTo(orderMiMiDTO2);
        orderMiMiDTO2.setId(orderMiMiDTO1.getId());
        assertThat(orderMiMiDTO1).isEqualTo(orderMiMiDTO2);
        orderMiMiDTO2.setId(2L);
        assertThat(orderMiMiDTO1).isNotEqualTo(orderMiMiDTO2);
        orderMiMiDTO1.setId(null);
        assertThat(orderMiMiDTO1).isNotEqualTo(orderMiMiDTO2);
    }
}
