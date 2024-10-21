package ai.realworld.domain;

import static ai.realworld.domain.AlZorroTemptationTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlZorroTemptationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlZorroTemptation.class);
        AlZorroTemptation alZorroTemptation1 = getAlZorroTemptationSample1();
        AlZorroTemptation alZorroTemptation2 = new AlZorroTemptation();
        assertThat(alZorroTemptation1).isNotEqualTo(alZorroTemptation2);

        alZorroTemptation2.setId(alZorroTemptation1.getId());
        assertThat(alZorroTemptation1).isEqualTo(alZorroTemptation2);

        alZorroTemptation2 = getAlZorroTemptationSample2();
        assertThat(alZorroTemptation1).isNotEqualTo(alZorroTemptation2);
    }

    @Test
    void thumbnailTest() {
        AlZorroTemptation alZorroTemptation = getAlZorroTemptationRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alZorroTemptation.setThumbnail(metaverseBack);
        assertThat(alZorroTemptation.getThumbnail()).isEqualTo(metaverseBack);

        alZorroTemptation.thumbnail(null);
        assertThat(alZorroTemptation.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AlZorroTemptation alZorroTemptation = getAlZorroTemptationRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alZorroTemptation.setApplication(johnLennonBack);
        assertThat(alZorroTemptation.getApplication()).isEqualTo(johnLennonBack);

        alZorroTemptation.application(null);
        assertThat(alZorroTemptation.getApplication()).isNull();
    }
}
