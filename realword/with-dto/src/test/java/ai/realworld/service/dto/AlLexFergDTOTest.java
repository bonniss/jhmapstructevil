package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLexFergDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLexFergDTO.class);
        AlLexFergDTO alLexFergDTO1 = new AlLexFergDTO();
        alLexFergDTO1.setId(1L);
        AlLexFergDTO alLexFergDTO2 = new AlLexFergDTO();
        assertThat(alLexFergDTO1).isNotEqualTo(alLexFergDTO2);
        alLexFergDTO2.setId(alLexFergDTO1.getId());
        assertThat(alLexFergDTO1).isEqualTo(alLexFergDTO2);
        alLexFergDTO2.setId(2L);
        assertThat(alLexFergDTO1).isNotEqualTo(alLexFergDTO2);
        alLexFergDTO1.setId(null);
        assertThat(alLexFergDTO1).isNotEqualTo(alLexFergDTO2);
    }
}
