package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerViViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerViViDTO.class);
        NextCustomerViViDTO nextCustomerViViDTO1 = new NextCustomerViViDTO();
        nextCustomerViViDTO1.setId(1L);
        NextCustomerViViDTO nextCustomerViViDTO2 = new NextCustomerViViDTO();
        assertThat(nextCustomerViViDTO1).isNotEqualTo(nextCustomerViViDTO2);
        nextCustomerViViDTO2.setId(nextCustomerViViDTO1.getId());
        assertThat(nextCustomerViViDTO1).isEqualTo(nextCustomerViViDTO2);
        nextCustomerViViDTO2.setId(2L);
        assertThat(nextCustomerViViDTO1).isNotEqualTo(nextCustomerViViDTO2);
        nextCustomerViViDTO1.setId(null);
        assertThat(nextCustomerViViDTO1).isNotEqualTo(nextCustomerViViDTO2);
    }
}
