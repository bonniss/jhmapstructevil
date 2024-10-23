package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerViDTO.class);
        NextCustomerViDTO nextCustomerViDTO1 = new NextCustomerViDTO();
        nextCustomerViDTO1.setId(1L);
        NextCustomerViDTO nextCustomerViDTO2 = new NextCustomerViDTO();
        assertThat(nextCustomerViDTO1).isNotEqualTo(nextCustomerViDTO2);
        nextCustomerViDTO2.setId(nextCustomerViDTO1.getId());
        assertThat(nextCustomerViDTO1).isEqualTo(nextCustomerViDTO2);
        nextCustomerViDTO2.setId(2L);
        assertThat(nextCustomerViDTO1).isNotEqualTo(nextCustomerViDTO2);
        nextCustomerViDTO1.setId(null);
        assertThat(nextCustomerViDTO1).isNotEqualTo(nextCustomerViDTO2);
    }
}
