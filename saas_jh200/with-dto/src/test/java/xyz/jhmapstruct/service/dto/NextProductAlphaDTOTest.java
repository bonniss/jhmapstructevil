package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductAlphaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductAlphaDTO.class);
        NextProductAlphaDTO nextProductAlphaDTO1 = new NextProductAlphaDTO();
        nextProductAlphaDTO1.setId(1L);
        NextProductAlphaDTO nextProductAlphaDTO2 = new NextProductAlphaDTO();
        assertThat(nextProductAlphaDTO1).isNotEqualTo(nextProductAlphaDTO2);
        nextProductAlphaDTO2.setId(nextProductAlphaDTO1.getId());
        assertThat(nextProductAlphaDTO1).isEqualTo(nextProductAlphaDTO2);
        nextProductAlphaDTO2.setId(2L);
        assertThat(nextProductAlphaDTO1).isNotEqualTo(nextProductAlphaDTO2);
        nextProductAlphaDTO1.setId(null);
        assertThat(nextProductAlphaDTO1).isNotEqualTo(nextProductAlphaDTO2);
    }
}
