package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderSigmaDTO.class);
        NextOrderSigmaDTO nextOrderSigmaDTO1 = new NextOrderSigmaDTO();
        nextOrderSigmaDTO1.setId(1L);
        NextOrderSigmaDTO nextOrderSigmaDTO2 = new NextOrderSigmaDTO();
        assertThat(nextOrderSigmaDTO1).isNotEqualTo(nextOrderSigmaDTO2);
        nextOrderSigmaDTO2.setId(nextOrderSigmaDTO1.getId());
        assertThat(nextOrderSigmaDTO1).isEqualTo(nextOrderSigmaDTO2);
        nextOrderSigmaDTO2.setId(2L);
        assertThat(nextOrderSigmaDTO1).isNotEqualTo(nextOrderSigmaDTO2);
        nextOrderSigmaDTO1.setId(null);
        assertThat(nextOrderSigmaDTO1).isNotEqualTo(nextOrderSigmaDTO2);
    }
}
