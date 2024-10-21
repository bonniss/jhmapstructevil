package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HandCraftDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HandCraftDTO.class);
        HandCraftDTO handCraftDTO1 = new HandCraftDTO();
        handCraftDTO1.setId(1L);
        HandCraftDTO handCraftDTO2 = new HandCraftDTO();
        assertThat(handCraftDTO1).isNotEqualTo(handCraftDTO2);
        handCraftDTO2.setId(handCraftDTO1.getId());
        assertThat(handCraftDTO1).isEqualTo(handCraftDTO2);
        handCraftDTO2.setId(2L);
        assertThat(handCraftDTO1).isNotEqualTo(handCraftDTO2);
        handCraftDTO1.setId(null);
        assertThat(handCraftDTO1).isNotEqualTo(handCraftDTO2);
    }
}
