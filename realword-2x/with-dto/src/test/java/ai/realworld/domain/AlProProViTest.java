package ai.realworld.domain;

import static ai.realworld.domain.AlLadyGagaViTestSamples.*;
import static ai.realworld.domain.AlMenityViTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlProProViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProProVi.class);
        AlProProVi alProProVi1 = getAlProProViSample1();
        AlProProVi alProProVi2 = new AlProProVi();
        assertThat(alProProVi1).isNotEqualTo(alProProVi2);

        alProProVi2.setId(alProProVi1.getId());
        assertThat(alProProVi1).isEqualTo(alProProVi2);

        alProProVi2 = getAlProProViSample2();
        assertThat(alProProVi1).isNotEqualTo(alProProVi2);
    }

    @Test
    void parentTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        alProProVi.setParent(alProProViBack);
        assertThat(alProProVi.getParent()).isEqualTo(alProProViBack);

        alProProVi.parent(null);
        assertThat(alProProVi.getParent()).isNull();
    }

    @Test
    void projectTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        AlLadyGagaVi alLadyGagaViBack = getAlLadyGagaViRandomSampleGenerator();

        alProProVi.setProject(alLadyGagaViBack);
        assertThat(alProProVi.getProject()).isEqualTo(alLadyGagaViBack);

        alProProVi.project(null);
        assertThat(alProProVi.getProject()).isNull();
    }

    @Test
    void avatarTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alProProVi.setAvatar(metaverseBack);
        assertThat(alProProVi.getAvatar()).isEqualTo(metaverseBack);

        alProProVi.avatar(null);
        assertThat(alProProVi.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alProProVi.setApplication(johnLennonBack);
        assertThat(alProProVi.getApplication()).isEqualTo(johnLennonBack);

        alProProVi.application(null);
        assertThat(alProProVi.getApplication()).isNull();
    }

    @Test
    void amenityTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        AlMenityVi alMenityViBack = getAlMenityViRandomSampleGenerator();

        alProProVi.addAmenity(alMenityViBack);
        assertThat(alProProVi.getAmenities()).containsOnly(alMenityViBack);

        alProProVi.removeAmenity(alMenityViBack);
        assertThat(alProProVi.getAmenities()).doesNotContain(alMenityViBack);

        alProProVi.amenities(new HashSet<>(Set.of(alMenityViBack)));
        assertThat(alProProVi.getAmenities()).containsOnly(alMenityViBack);

        alProProVi.setAmenities(new HashSet<>());
        assertThat(alProProVi.getAmenities()).doesNotContain(alMenityViBack);
    }

    @Test
    void imageTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alProProVi.addImage(metaverseBack);
        assertThat(alProProVi.getImages()).containsOnly(metaverseBack);

        alProProVi.removeImage(metaverseBack);
        assertThat(alProProVi.getImages()).doesNotContain(metaverseBack);

        alProProVi.images(new HashSet<>(Set.of(metaverseBack)));
        assertThat(alProProVi.getImages()).containsOnly(metaverseBack);

        alProProVi.setImages(new HashSet<>());
        assertThat(alProProVi.getImages()).doesNotContain(metaverseBack);
    }

    @Test
    void childrenTest() {
        AlProProVi alProProVi = getAlProProViRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        alProProVi.addChildren(alProProViBack);
        assertThat(alProProVi.getChildren()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getParent()).isEqualTo(alProProVi);

        alProProVi.removeChildren(alProProViBack);
        assertThat(alProProVi.getChildren()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getParent()).isNull();

        alProProVi.children(new HashSet<>(Set.of(alProProViBack)));
        assertThat(alProProVi.getChildren()).containsOnly(alProProViBack);
        assertThat(alProProViBack.getParent()).isEqualTo(alProProVi);

        alProProVi.setChildren(new HashSet<>());
        assertThat(alProProVi.getChildren()).doesNotContain(alProProViBack);
        assertThat(alProProViBack.getParent()).isNull();
    }
}
