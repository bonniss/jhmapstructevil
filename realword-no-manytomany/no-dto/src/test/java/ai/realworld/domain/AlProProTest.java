package ai.realworld.domain;

import static ai.realworld.domain.AlLadyGagaTestSamples.*;
import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlProProTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProPro.class);
        AlProPro alProPro1 = getAlProProSample1();
        AlProPro alProPro2 = new AlProPro();
        assertThat(alProPro1).isNotEqualTo(alProPro2);

        alProPro2.setId(alProPro1.getId());
        assertThat(alProPro1).isEqualTo(alProPro2);

        alProPro2 = getAlProProSample2();
        assertThat(alProPro1).isNotEqualTo(alProPro2);
    }

    @Test
    void parentTest() {
        AlProPro alProPro = getAlProProRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        alProPro.setParent(alProProBack);
        assertThat(alProPro.getParent()).isEqualTo(alProProBack);

        alProPro.parent(null);
        assertThat(alProPro.getParent()).isNull();
    }

    @Test
    void projectTest() {
        AlProPro alProPro = getAlProProRandomSampleGenerator();
        AlLadyGaga alLadyGagaBack = getAlLadyGagaRandomSampleGenerator();

        alProPro.setProject(alLadyGagaBack);
        assertThat(alProPro.getProject()).isEqualTo(alLadyGagaBack);

        alProPro.project(null);
        assertThat(alProPro.getProject()).isNull();
    }

    @Test
    void avatarTest() {
        AlProPro alProPro = getAlProProRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alProPro.setAvatar(metaverseBack);
        assertThat(alProPro.getAvatar()).isEqualTo(metaverseBack);

        alProPro.avatar(null);
        assertThat(alProPro.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlProPro alProPro = getAlProProRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alProPro.setApplication(johnLennonBack);
        assertThat(alProPro.getApplication()).isEqualTo(johnLennonBack);

        alProPro.application(null);
        assertThat(alProPro.getApplication()).isNull();
    }

    @Test
    void childrenTest() {
        AlProPro alProPro = getAlProProRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        alProPro.addChildren(alProProBack);
        assertThat(alProPro.getChildren()).containsOnly(alProProBack);
        assertThat(alProProBack.getParent()).isEqualTo(alProPro);

        alProPro.removeChildren(alProProBack);
        assertThat(alProPro.getChildren()).doesNotContain(alProProBack);
        assertThat(alProProBack.getParent()).isNull();

        alProPro.children(new HashSet<>(Set.of(alProProBack)));
        assertThat(alProPro.getChildren()).containsOnly(alProProBack);
        assertThat(alProProBack.getParent()).isEqualTo(alProPro);

        alProPro.setChildren(new HashSet<>());
        assertThat(alProPro.getChildren()).doesNotContain(alProProBack);
        assertThat(alProProBack.getParent()).isNull();
    }
}
