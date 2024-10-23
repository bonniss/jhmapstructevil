package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerSigmaDTO.class);
        NextCustomerSigmaDTO nextCustomerSigmaDTO1 = new NextCustomerSigmaDTO();
        nextCustomerSigmaDTO1.setId(1L);
        NextCustomerSigmaDTO nextCustomerSigmaDTO2 = new NextCustomerSigmaDTO();
        assertThat(nextCustomerSigmaDTO1).isNotEqualTo(nextCustomerSigmaDTO2);
        nextCustomerSigmaDTO2.setId(nextCustomerSigmaDTO1.getId());
        assertThat(nextCustomerSigmaDTO1).isEqualTo(nextCustomerSigmaDTO2);
        nextCustomerSigmaDTO2.setId(2L);
        assertThat(nextCustomerSigmaDTO1).isNotEqualTo(nextCustomerSigmaDTO2);
        nextCustomerSigmaDTO1.setId(null);
        assertThat(nextCustomerSigmaDTO1).isNotEqualTo(nextCustomerSigmaDTO2);
    }
}
