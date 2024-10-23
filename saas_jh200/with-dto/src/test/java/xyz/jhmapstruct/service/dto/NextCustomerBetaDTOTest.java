package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerBetaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerBetaDTO.class);
        NextCustomerBetaDTO nextCustomerBetaDTO1 = new NextCustomerBetaDTO();
        nextCustomerBetaDTO1.setId(1L);
        NextCustomerBetaDTO nextCustomerBetaDTO2 = new NextCustomerBetaDTO();
        assertThat(nextCustomerBetaDTO1).isNotEqualTo(nextCustomerBetaDTO2);
        nextCustomerBetaDTO2.setId(nextCustomerBetaDTO1.getId());
        assertThat(nextCustomerBetaDTO1).isEqualTo(nextCustomerBetaDTO2);
        nextCustomerBetaDTO2.setId(2L);
        assertThat(nextCustomerBetaDTO1).isNotEqualTo(nextCustomerBetaDTO2);
        nextCustomerBetaDTO1.setId(null);
        assertThat(nextCustomerBetaDTO1).isNotEqualTo(nextCustomerBetaDTO2);
    }
}
