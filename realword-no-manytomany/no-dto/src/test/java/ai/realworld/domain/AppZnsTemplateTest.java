package ai.realworld.domain;

import static ai.realworld.domain.AppZnsTemplateTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppZnsTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppZnsTemplate.class);
        AppZnsTemplate appZnsTemplate1 = getAppZnsTemplateSample1();
        AppZnsTemplate appZnsTemplate2 = new AppZnsTemplate();
        assertThat(appZnsTemplate1).isNotEqualTo(appZnsTemplate2);

        appZnsTemplate2.setId(appZnsTemplate1.getId());
        assertThat(appZnsTemplate1).isEqualTo(appZnsTemplate2);

        appZnsTemplate2 = getAppZnsTemplateSample2();
        assertThat(appZnsTemplate1).isNotEqualTo(appZnsTemplate2);
    }

    @Test
    void thumbnailTest() {
        AppZnsTemplate appZnsTemplate = getAppZnsTemplateRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        appZnsTemplate.setThumbnail(metaverseBack);
        assertThat(appZnsTemplate.getThumbnail()).isEqualTo(metaverseBack);

        appZnsTemplate.thumbnail(null);
        assertThat(appZnsTemplate.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AppZnsTemplate appZnsTemplate = getAppZnsTemplateRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        appZnsTemplate.setApplication(johnLennonBack);
        assertThat(appZnsTemplate.getApplication()).isEqualTo(johnLennonBack);

        appZnsTemplate.application(null);
        assertThat(appZnsTemplate.getApplication()).isNull();
    }
}
