package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderGammaDTO.class);
        OrderGammaDTO orderGammaDTO1 = new OrderGammaDTO();
        orderGammaDTO1.setId(1L);
        OrderGammaDTO orderGammaDTO2 = new OrderGammaDTO();
        assertThat(orderGammaDTO1).isNotEqualTo(orderGammaDTO2);
        orderGammaDTO2.setId(orderGammaDTO1.getId());
        assertThat(orderGammaDTO1).isEqualTo(orderGammaDTO2);
        orderGammaDTO2.setId(2L);
        assertThat(orderGammaDTO1).isNotEqualTo(orderGammaDTO2);
        orderGammaDTO1.setId(null);
        assertThat(orderGammaDTO1).isNotEqualTo(orderGammaDTO2);
    }
}
