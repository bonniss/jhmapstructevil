package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderViDTO.class);
        OrderViDTO orderViDTO1 = new OrderViDTO();
        orderViDTO1.setId(1L);
        OrderViDTO orderViDTO2 = new OrderViDTO();
        assertThat(orderViDTO1).isNotEqualTo(orderViDTO2);
        orderViDTO2.setId(orderViDTO1.getId());
        assertThat(orderViDTO1).isEqualTo(orderViDTO2);
        orderViDTO2.setId(2L);
        assertThat(orderViDTO1).isNotEqualTo(orderViDTO2);
        orderViDTO1.setId(null);
        assertThat(orderViDTO1).isNotEqualTo(orderViDTO2);
    }
}
