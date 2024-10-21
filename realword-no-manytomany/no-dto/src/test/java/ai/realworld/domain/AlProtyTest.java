package ai.realworld.domain;

import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AlProProTestSamples.*;
import static ai.realworld.domain.AlProtyTestSamples.*;
import static ai.realworld.domain.AlProtyTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlProtyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProty.class);
        AlProty alProty1 = getAlProtySample1();
        AlProty alProty2 = new AlProty();
        assertThat(alProty1).isNotEqualTo(alProty2);

        alProty2.setId(alProty1.getId());
        assertThat(alProty1).isEqualTo(alProty2);

        alProty2 = getAlProtySample2();
        assertThat(alProty1).isNotEqualTo(alProty2);
    }

    @Test
    void parentTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        AlProty alProtyBack = getAlProtyRandomSampleGenerator();

        alProty.setParent(alProtyBack);
        assertThat(alProty.getParent()).isEqualTo(alProtyBack);

        alProty.parent(null);
        assertThat(alProty.getParent()).isNull();
    }

    @Test
    void operatorTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        AlApple alAppleBack = getAlAppleRandomSampleGenerator();

        alProty.setOperator(alAppleBack);
        assertThat(alProty.getOperator()).isEqualTo(alAppleBack);

        alProty.operator(null);
        assertThat(alProty.getOperator()).isNull();
    }

    @Test
    void propertyProfileTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        AlProPro alProProBack = getAlProProRandomSampleGenerator();

        alProty.setPropertyProfile(alProProBack);
        assertThat(alProty.getPropertyProfile()).isEqualTo(alProProBack);

        alProty.propertyProfile(null);
        assertThat(alProty.getPropertyProfile()).isNull();
    }

    @Test
    void avatarTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alProty.setAvatar(metaverseBack);
        assertThat(alProty.getAvatar()).isEqualTo(metaverseBack);

        alProty.avatar(null);
        assertThat(alProty.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alProty.setApplication(johnLennonBack);
        assertThat(alProty.getApplication()).isEqualTo(johnLennonBack);

        alProty.application(null);
        assertThat(alProty.getApplication()).isNull();
    }

    @Test
    void childrenTest() {
        AlProty alProty = getAlProtyRandomSampleGenerator();
        AlProty alProtyBack = getAlProtyRandomSampleGenerator();

        alProty.addChildren(alProtyBack);
        assertThat(alProty.getChildren()).containsOnly(alProtyBack);
        assertThat(alProtyBack.getParent()).isEqualTo(alProty);

        alProty.removeChildren(alProtyBack);
        assertThat(alProty.getChildren()).doesNotContain(alProtyBack);
        assertThat(alProtyBack.getParent()).isNull();

        alProty.children(new HashSet<>(Set.of(alProtyBack)));
        assertThat(alProty.getChildren()).containsOnly(alProtyBack);
        assertThat(alProtyBack.getParent()).isEqualTo(alProty);

        alProty.setChildren(new HashSet<>());
        assertThat(alProty.getChildren()).doesNotContain(alProtyBack);
        assertThat(alProtyBack.getParent()).isNull();
    }
}
