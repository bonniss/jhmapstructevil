package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerMiMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerMiMiDTO.class);
        NextCustomerMiMiDTO nextCustomerMiMiDTO1 = new NextCustomerMiMiDTO();
        nextCustomerMiMiDTO1.setId(1L);
        NextCustomerMiMiDTO nextCustomerMiMiDTO2 = new NextCustomerMiMiDTO();
        assertThat(nextCustomerMiMiDTO1).isNotEqualTo(nextCustomerMiMiDTO2);
        nextCustomerMiMiDTO2.setId(nextCustomerMiMiDTO1.getId());
        assertThat(nextCustomerMiMiDTO1).isEqualTo(nextCustomerMiMiDTO2);
        nextCustomerMiMiDTO2.setId(2L);
        assertThat(nextCustomerMiMiDTO1).isNotEqualTo(nextCustomerMiMiDTO2);
        nextCustomerMiMiDTO1.setId(null);
        assertThat(nextCustomerMiMiDTO1).isNotEqualTo(nextCustomerMiMiDTO2);
    }
}
