package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderSigmaDTO.class);
        OrderSigmaDTO orderSigmaDTO1 = new OrderSigmaDTO();
        orderSigmaDTO1.setId(1L);
        OrderSigmaDTO orderSigmaDTO2 = new OrderSigmaDTO();
        assertThat(orderSigmaDTO1).isNotEqualTo(orderSigmaDTO2);
        orderSigmaDTO2.setId(orderSigmaDTO1.getId());
        assertThat(orderSigmaDTO1).isEqualTo(orderSigmaDTO2);
        orderSigmaDTO2.setId(2L);
        assertThat(orderSigmaDTO1).isNotEqualTo(orderSigmaDTO2);
        orderSigmaDTO1.setId(null);
        assertThat(orderSigmaDTO1).isNotEqualTo(orderSigmaDTO2);
    }
}
