package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextOrderAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextOrderAlphaDTO.class);
        NextOrderAlphaDTO nextOrderAlphaDTO1 = new NextOrderAlphaDTO();
        nextOrderAlphaDTO1.setId(1L);
        NextOrderAlphaDTO nextOrderAlphaDTO2 = new NextOrderAlphaDTO();
        assertThat(nextOrderAlphaDTO1).isNotEqualTo(nextOrderAlphaDTO2);
        nextOrderAlphaDTO2.setId(nextOrderAlphaDTO1.getId());
        assertThat(nextOrderAlphaDTO1).isEqualTo(nextOrderAlphaDTO2);
        nextOrderAlphaDTO2.setId(2L);
        assertThat(nextOrderAlphaDTO1).isNotEqualTo(nextOrderAlphaDTO2);
        nextOrderAlphaDTO1.setId(null);
        assertThat(nextOrderAlphaDTO1).isNotEqualTo(nextOrderAlphaDTO2);
    }
}
