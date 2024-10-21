package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HandCraftViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HandCraftViDTO.class);
        HandCraftViDTO handCraftViDTO1 = new HandCraftViDTO();
        handCraftViDTO1.setId(1L);
        HandCraftViDTO handCraftViDTO2 = new HandCraftViDTO();
        assertThat(handCraftViDTO1).isNotEqualTo(handCraftViDTO2);
        handCraftViDTO2.setId(handCraftViDTO1.getId());
        assertThat(handCraftViDTO1).isEqualTo(handCraftViDTO2);
        handCraftViDTO2.setId(2L);
        assertThat(handCraftViDTO1).isNotEqualTo(handCraftViDTO2);
        handCraftViDTO1.setId(null);
        assertThat(handCraftViDTO1).isNotEqualTo(handCraftViDTO2);
    }
}
