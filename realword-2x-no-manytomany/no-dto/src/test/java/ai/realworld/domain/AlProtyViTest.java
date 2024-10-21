package ai.realworld.domain;

import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AlProProViTestSamples.*;
import static ai.realworld.domain.AlProtyViTestSamples.*;
import static ai.realworld.domain.AlProtyViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlProtyViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlProtyVi.class);
        AlProtyVi alProtyVi1 = getAlProtyViSample1();
        AlProtyVi alProtyVi2 = new AlProtyVi();
        assertThat(alProtyVi1).isNotEqualTo(alProtyVi2);

        alProtyVi2.setId(alProtyVi1.getId());
        assertThat(alProtyVi1).isEqualTo(alProtyVi2);

        alProtyVi2 = getAlProtyViSample2();
        assertThat(alProtyVi1).isNotEqualTo(alProtyVi2);
    }

    @Test
    void parentTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        AlProtyVi alProtyViBack = getAlProtyViRandomSampleGenerator();

        alProtyVi.setParent(alProtyViBack);
        assertThat(alProtyVi.getParent()).isEqualTo(alProtyViBack);

        alProtyVi.parent(null);
        assertThat(alProtyVi.getParent()).isNull();
    }

    @Test
    void operatorTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        AlAppleVi alAppleViBack = getAlAppleViRandomSampleGenerator();

        alProtyVi.setOperator(alAppleViBack);
        assertThat(alProtyVi.getOperator()).isEqualTo(alAppleViBack);

        alProtyVi.operator(null);
        assertThat(alProtyVi.getOperator()).isNull();
    }

    @Test
    void propertyProfileTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        AlProProVi alProProViBack = getAlProProViRandomSampleGenerator();

        alProtyVi.setPropertyProfile(alProProViBack);
        assertThat(alProtyVi.getPropertyProfile()).isEqualTo(alProProViBack);

        alProtyVi.propertyProfile(null);
        assertThat(alProtyVi.getPropertyProfile()).isNull();
    }

    @Test
    void avatarTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alProtyVi.setAvatar(metaverseBack);
        assertThat(alProtyVi.getAvatar()).isEqualTo(metaverseBack);

        alProtyVi.avatar(null);
        assertThat(alProtyVi.getAvatar()).isNull();
    }

    @Test
    void applicationTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alProtyVi.setApplication(johnLennonBack);
        assertThat(alProtyVi.getApplication()).isEqualTo(johnLennonBack);

        alProtyVi.application(null);
        assertThat(alProtyVi.getApplication()).isNull();
    }

    @Test
    void childrenTest() {
        AlProtyVi alProtyVi = getAlProtyViRandomSampleGenerator();
        AlProtyVi alProtyViBack = getAlProtyViRandomSampleGenerator();

        alProtyVi.addChildren(alProtyViBack);
        assertThat(alProtyVi.getChildren()).containsOnly(alProtyViBack);
        assertThat(alProtyViBack.getParent()).isEqualTo(alProtyVi);

        alProtyVi.removeChildren(alProtyViBack);
        assertThat(alProtyVi.getChildren()).doesNotContain(alProtyViBack);
        assertThat(alProtyViBack.getParent()).isNull();

        alProtyVi.children(new HashSet<>(Set.of(alProtyViBack)));
        assertThat(alProtyVi.getChildren()).containsOnly(alProtyViBack);
        assertThat(alProtyViBack.getParent()).isEqualTo(alProtyVi);

        alProtyVi.setChildren(new HashSet<>());
        assertThat(alProtyVi.getChildren()).doesNotContain(alProtyViBack);
        assertThat(alProtyViBack.getParent()).isNull();
    }
}
