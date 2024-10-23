package xyz.jhmapstruct.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class NextProductSigmaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NextProductSigmaDTO.class);
        NextProductSigmaDTO nextProductSigmaDTO1 = new NextProductSigmaDTO();
        nextProductSigmaDTO1.setId(1L);
        NextProductSigmaDTO nextProductSigmaDTO2 = new NextProductSigmaDTO();
        assertThat(nextProductSigmaDTO1).isNotEqualTo(nextProductSigmaDTO2);
        nextProductSigmaDTO2.setId(nextProductSigmaDTO1.getId());
        assertThat(nextProductSigmaDTO1).isEqualTo(nextProductSigmaDTO2);
        nextProductSigmaDTO2.setId(2L);
        assertThat(nextProductSigmaDTO1).isNotEqualTo(nextProductSigmaDTO2);
        nextProductSigmaDTO1.setId(null);
        assertThat(nextProductSigmaDTO1).isNotEqualTo(nextProductSigmaDTO2);
    }
}
