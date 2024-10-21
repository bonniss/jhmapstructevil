package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppMessageTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppMessageTemplateDTO.class);
        AppMessageTemplateDTO appMessageTemplateDTO1 = new AppMessageTemplateDTO();
        appMessageTemplateDTO1.setId(1L);
        AppMessageTemplateDTO appMessageTemplateDTO2 = new AppMessageTemplateDTO();
        assertThat(appMessageTemplateDTO1).isNotEqualTo(appMessageTemplateDTO2);
        appMessageTemplateDTO2.setId(appMessageTemplateDTO1.getId());
        assertThat(appMessageTemplateDTO1).isEqualTo(appMessageTemplateDTO2);
        appMessageTemplateDTO2.setId(2L);
        assertThat(appMessageTemplateDTO1).isNotEqualTo(appMessageTemplateDTO2);
        appMessageTemplateDTO1.setId(null);
        assertThat(appMessageTemplateDTO1).isNotEqualTo(appMessageTemplateDTO2);
    }
}
