package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerGammaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerGammaDTO.class);
        NextCustomerGammaDTO nextCustomerGammaDTO1 = new NextCustomerGammaDTO();
        nextCustomerGammaDTO1.setId(1L);
        NextCustomerGammaDTO nextCustomerGammaDTO2 = new NextCustomerGammaDTO();
        assertThat(nextCustomerGammaDTO1).isNotEqualTo(nextCustomerGammaDTO2);
        nextCustomerGammaDTO2.setId(nextCustomerGammaDTO1.getId());
        assertThat(nextCustomerGammaDTO1).isEqualTo(nextCustomerGammaDTO2);
        nextCustomerGammaDTO2.setId(2L);
        assertThat(nextCustomerGammaDTO1).isNotEqualTo(nextCustomerGammaDTO2);
        nextCustomerGammaDTO1.setId(null);
        assertThat(nextCustomerGammaDTO1).isNotEqualTo(nextCustomerGammaDTO2);
    }
}
