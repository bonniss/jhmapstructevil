package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class OrderAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderAlphaDTO.class);
        OrderAlphaDTO orderAlphaDTO1 = new OrderAlphaDTO();
        orderAlphaDTO1.setId(1L);
        OrderAlphaDTO orderAlphaDTO2 = new OrderAlphaDTO();
        assertThat(orderAlphaDTO1).isNotEqualTo(orderAlphaDTO2);
        orderAlphaDTO2.setId(orderAlphaDTO1.getId());
        assertThat(orderAlphaDTO1).isEqualTo(orderAlphaDTO2);
        orderAlphaDTO2.setId(2L);
        assertThat(orderAlphaDTO1).isNotEqualTo(orderAlphaDTO2);
        orderAlphaDTO1.setId(null);
        assertThat(orderAlphaDTO1).isNotEqualTo(orderAlphaDTO2);
    }
}
