package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerAlphaDTO.class);
        NextCustomerAlphaDTO nextCustomerAlphaDTO1 = new NextCustomerAlphaDTO();
        nextCustomerAlphaDTO1.setId(1L);
        NextCustomerAlphaDTO nextCustomerAlphaDTO2 = new NextCustomerAlphaDTO();
        assertThat(nextCustomerAlphaDTO1).isNotEqualTo(nextCustomerAlphaDTO2);
        nextCustomerAlphaDTO2.setId(nextCustomerAlphaDTO1.getId());
        assertThat(nextCustomerAlphaDTO1).isEqualTo(nextCustomerAlphaDTO2);
        nextCustomerAlphaDTO2.setId(2L);
        assertThat(nextCustomerAlphaDTO1).isNotEqualTo(nextCustomerAlphaDTO2);
        nextCustomerAlphaDTO1.setId(null);
        assertThat(nextCustomerAlphaDTO1).isNotEqualTo(nextCustomerAlphaDTO2);
    }
}
