package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextCustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextCustomerDTO.class);
        NextCustomerDTO nextCustomerDTO1 = new NextCustomerDTO();
        nextCustomerDTO1.setId(1L);
        NextCustomerDTO nextCustomerDTO2 = new NextCustomerDTO();
        assertThat(nextCustomerDTO1).isNotEqualTo(nextCustomerDTO2);
        nextCustomerDTO2.setId(nextCustomerDTO1.getId());
        assertThat(nextCustomerDTO1).isEqualTo(nextCustomerDTO2);
        nextCustomerDTO2.setId(2L);
        assertThat(nextCustomerDTO1).isNotEqualTo(nextCustomerDTO2);
        nextCustomerDTO1.setId(null);
        assertThat(nextCustomerDTO1).isNotEqualTo(nextCustomerDTO2);
    }
}
