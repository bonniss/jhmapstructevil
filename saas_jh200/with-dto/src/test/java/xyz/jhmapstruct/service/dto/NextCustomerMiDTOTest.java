package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerMiDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerMiDTO.class);
        NextCustomerMiDTO nextCustomerMiDTO1 = new NextCustomerMiDTO();
        nextCustomerMiDTO1.setId(1L);
        NextCustomerMiDTO nextCustomerMiDTO2 = new NextCustomerMiDTO();
        assertThat(nextCustomerMiDTO1).isNotEqualTo(nextCustomerMiDTO2);
        nextCustomerMiDTO2.setId(nextCustomerMiDTO1.getId());
        assertThat(nextCustomerMiDTO1).isEqualTo(nextCustomerMiDTO2);
        nextCustomerMiDTO2.setId(2L);
        assertThat(nextCustomerMiDTO1).isNotEqualTo(nextCustomerMiDTO2);
        nextCustomerMiDTO1.setId(null);
        assertThat(nextCustomerMiDTO1).isNotEqualTo(nextCustomerMiDTO2);
    }
}
