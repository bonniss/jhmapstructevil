package ai.realworld.domain;

import static ai.realworld.domain.AppMessageTemplateTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppMessageTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppMessageTemplate.class);
        AppMessageTemplate appMessageTemplate1 = getAppMessageTemplateSample1();
        AppMessageTemplate appMessageTemplate2 = new AppMessageTemplate();
        assertThat(appMessageTemplate1).isNotEqualTo(appMessageTemplate2);

        appMessageTemplate2.setId(appMessageTemplate1.getId());
        assertThat(appMessageTemplate1).isEqualTo(appMessageTemplate2);

        appMessageTemplate2 = getAppMessageTemplateSample2();
        assertThat(appMessageTemplate1).isNotEqualTo(appMessageTemplate2);
    }

    @Test
    void thumbnailTest() {
        AppMessageTemplate appMessageTemplate = getAppMessageTemplateRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        appMessageTemplate.setThumbnail(metaverseBack);
        assertThat(appMessageTemplate.getThumbnail()).isEqualTo(metaverseBack);

        appMessageTemplate.thumbnail(null);
        assertThat(appMessageTemplate.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AppMessageTemplate appMessageTemplate = getAppMessageTemplateRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        appMessageTemplate.setApplication(johnLennonBack);
        assertThat(appMessageTemplate.getApplication()).isEqualTo(johnLennonBack);

        appMessageTemplate.application(null);
        assertThat(appMessageTemplate.getApplication()).isNull();
    }
}
