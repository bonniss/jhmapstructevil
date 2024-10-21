package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlLexFergViDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlLexFergViDTO.class);
        AlLexFergViDTO alLexFergViDTO1 = new AlLexFergViDTO();
        alLexFergViDTO1.setId(1L);
        AlLexFergViDTO alLexFergViDTO2 = new AlLexFergViDTO();
        assertThat(alLexFergViDTO1).isNotEqualTo(alLexFergViDTO2);
        alLexFergViDTO2.setId(alLexFergViDTO1.getId());
        assertThat(alLexFergViDTO1).isEqualTo(alLexFergViDTO2);
        alLexFergViDTO2.setId(2L);
        assertThat(alLexFergViDTO1).isNotEqualTo(alLexFergViDTO2);
        alLexFergViDTO1.setId(null);
        assertThat(alLexFergViDTO1).isNotEqualTo(alLexFergViDTO2);
    }
}
