package ai.realworld.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppZnsTemplateDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppZnsTemplateDTO.class);
        AppZnsTemplateDTO appZnsTemplateDTO1 = new AppZnsTemplateDTO();
        appZnsTemplateDTO1.setId(1L);
        AppZnsTemplateDTO appZnsTemplateDTO2 = new AppZnsTemplateDTO();
        assertThat(appZnsTemplateDTO1).isNotEqualTo(appZnsTemplateDTO2);
        appZnsTemplateDTO2.setId(appZnsTemplateDTO1.getId());
        assertThat(appZnsTemplateDTO1).isEqualTo(appZnsTemplateDTO2);
        appZnsTemplateDTO2.setId(2L);
        assertThat(appZnsTemplateDTO1).isNotEqualTo(appZnsTemplateDTO2);
        appZnsTemplateDTO1.setId(null);
        assertThat(appZnsTemplateDTO1).isNotEqualTo(appZnsTemplateDTO2);
    }
}
