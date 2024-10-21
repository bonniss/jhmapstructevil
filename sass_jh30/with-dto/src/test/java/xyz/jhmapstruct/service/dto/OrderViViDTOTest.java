package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderViViDTO.class);
        OrderViViDTO orderViViDTO1 = new OrderViViDTO();
        orderViViDTO1.setId(1L);
        OrderViViDTO orderViViDTO2 = new OrderViViDTO();
        assertThat(orderViViDTO1).isNotEqualTo(orderViViDTO2);
        orderViViDTO2.setId(orderViViDTO1.getId());
        assertThat(orderViViDTO1).isEqualTo(orderViViDTO2);
        orderViViDTO2.setId(2L);
        assertThat(orderViViDTO1).isNotEqualTo(orderViViDTO2);
        orderViViDTO1.setId(null);
        assertThat(orderViViDTO1).isNotEqualTo(orderViViDTO2);
    }
}
