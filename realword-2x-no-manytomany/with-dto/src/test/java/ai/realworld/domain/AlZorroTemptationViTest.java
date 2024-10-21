package ai.realworld.domain;

import static ai.realworld.domain.AlZorroTemptationViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlZorroTemptationViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlZorroTemptationVi.class);
        AlZorroTemptationVi alZorroTemptationVi1 = getAlZorroTemptationViSample1();
        AlZorroTemptationVi alZorroTemptationVi2 = new AlZorroTemptationVi();
        assertThat(alZorroTemptationVi1).isNotEqualTo(alZorroTemptationVi2);

        alZorroTemptationVi2.setId(alZorroTemptationVi1.getId());
        assertThat(alZorroTemptationVi1).isEqualTo(alZorroTemptationVi2);

        alZorroTemptationVi2 = getAlZorroTemptationViSample2();
        assertThat(alZorroTemptationVi1).isNotEqualTo(alZorroTemptationVi2);
    }

    @Test
    void thumbnailTest() {
        AlZorroTemptationVi alZorroTemptationVi = getAlZorroTemptationViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alZorroTemptationVi.setThumbnail(metaverseBack);
        assertThat(alZorroTemptationVi.getThumbnail()).isEqualTo(metaverseBack);

        alZorroTemptationVi.thumbnail(null);
        assertThat(alZorroTemptationVi.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AlZorroTemptationVi alZorroTemptationVi = getAlZorroTemptationViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alZorroTemptationVi.setApplication(johnLennonBack);
        assertThat(alZorroTemptationVi.getApplication()).isEqualTo(johnLennonBack);

        alZorroTemptationVi.application(null);
        assertThat(alZorroTemptationVi.getApplication()).isNull();
    }
}
