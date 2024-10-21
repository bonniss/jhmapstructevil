package ai.realworld.domain;

import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.HandCraftViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EdSheeranViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EdSheeranVi.class);
        EdSheeranVi edSheeranVi1 = getEdSheeranViSample1();
        EdSheeranVi edSheeranVi2 = new EdSheeranVi();
        assertThat(edSheeranVi1).isNotEqualTo(edSheeranVi2);

        edSheeranVi2.setId(edSheeranVi1.getId());
        assertThat(edSheeranVi1).isEqualTo(edSheeranVi2);

        edSheeranVi2 = getEdSheeranViSample2();
        assertThat(edSheeranVi1).isNotEqualTo(edSheeranVi2);
    }

    @Test
    void agencyTest() {
        EdSheeranVi edSheeranVi = getEdSheeranViRandomSampleGenerator();
        AlAppleVi alAppleViBack = getAlAppleViRandomSampleGenerator();

        edSheeranVi.setAgency(alAppleViBack);
        assertThat(edSheeranVi.getAgency()).isEqualTo(alAppleViBack);

        edSheeranVi.agency(null);
        assertThat(edSheeranVi.getAgency()).isNull();
    }

    @Test
    void avatarTest() {
        EdSheeranVi edSheeranVi = getEdSheeranViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        edSheeranVi.setAvatar(metaverseBack);
        assertThat(edSheeranVi.getAvatar()).isEqualTo(metaverseBack);

        edSheeranVi.avatar(null);
        assertThat(edSheeranVi.getAvatar()).isNull();
    }

    @Test
    void appUserTest() {
        EdSheeranVi edSheeranVi = getEdSheeranViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        edSheeranVi.setAppUser(alPacinoBack);
        assertThat(edSheeranVi.getAppUser()).isEqualTo(alPacinoBack);

        edSheeranVi.appUser(null);
        assertThat(edSheeranVi.getAppUser()).isNull();
    }

    @Test
    void applicationTest() {
        EdSheeranVi edSheeranVi = getEdSheeranViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        edSheeranVi.setApplication(johnLennonBack);
        assertThat(edSheeranVi.getApplication()).isEqualTo(johnLennonBack);

        edSheeranVi.application(null);
        assertThat(edSheeranVi.getApplication()).isNull();
    }

    @Test
    void agentRolesTest() {
        EdSheeranVi edSheeranVi = getEdSheeranViRandomSampleGenerator();
        HandCraftVi handCraftViBack = getHandCraftViRandomSampleGenerator();

        edSheeranVi.addAgentRoles(handCraftViBack);
        assertThat(edSheeranVi.getAgentRoles()).containsOnly(handCraftViBack);
        assertThat(handCraftViBack.getAgent()).isEqualTo(edSheeranVi);

        edSheeranVi.removeAgentRoles(handCraftViBack);
        assertThat(edSheeranVi.getAgentRoles()).doesNotContain(handCraftViBack);
        assertThat(handCraftViBack.getAgent()).isNull();

        edSheeranVi.agentRoles(new HashSet<>(Set.of(handCraftViBack)));
        assertThat(edSheeranVi.getAgentRoles()).containsOnly(handCraftViBack);
        assertThat(handCraftViBack.getAgent()).isEqualTo(edSheeranVi);

        edSheeranVi.setAgentRoles(new HashSet<>());
        assertThat(edSheeranVi.getAgentRoles()).doesNotContain(handCraftViBack);
        assertThat(handCraftViBack.getAgent()).isNull();
    }
}
