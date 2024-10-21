package ai.realworld.domain;

import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.HandCraftTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EdSheeranTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EdSheeran.class);
        EdSheeran edSheeran1 = getEdSheeranSample1();
        EdSheeran edSheeran2 = new EdSheeran();
        assertThat(edSheeran1).isNotEqualTo(edSheeran2);

        edSheeran2.setId(edSheeran1.getId());
        assertThat(edSheeran1).isEqualTo(edSheeran2);

        edSheeran2 = getEdSheeranSample2();
        assertThat(edSheeran1).isNotEqualTo(edSheeran2);
    }

    @Test
    void agencyTest() {
        EdSheeran edSheeran = getEdSheeranRandomSampleGenerator();
        AlApple alAppleBack = getAlAppleRandomSampleGenerator();

        edSheeran.setAgency(alAppleBack);
        assertThat(edSheeran.getAgency()).isEqualTo(alAppleBack);

        edSheeran.agency(null);
        assertThat(edSheeran.getAgency()).isNull();
    }

    @Test
    void avatarTest() {
        EdSheeran edSheeran = getEdSheeranRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        edSheeran.setAvatar(metaverseBack);
        assertThat(edSheeran.getAvatar()).isEqualTo(metaverseBack);

        edSheeran.avatar(null);
        assertThat(edSheeran.getAvatar()).isNull();
    }

    @Test
    void appUserTest() {
        EdSheeran edSheeran = getEdSheeranRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        edSheeran.setAppUser(alPacinoBack);
        assertThat(edSheeran.getAppUser()).isEqualTo(alPacinoBack);

        edSheeran.appUser(null);
        assertThat(edSheeran.getAppUser()).isNull();
    }

    @Test
    void applicationTest() {
        EdSheeran edSheeran = getEdSheeranRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        edSheeran.setApplication(johnLennonBack);
        assertThat(edSheeran.getApplication()).isEqualTo(johnLennonBack);

        edSheeran.application(null);
        assertThat(edSheeran.getApplication()).isNull();
    }

    @Test
    void agentRolesTest() {
        EdSheeran edSheeran = getEdSheeranRandomSampleGenerator();
        HandCraft handCraftBack = getHandCraftRandomSampleGenerator();

        edSheeran.addAgentRoles(handCraftBack);
        assertThat(edSheeran.getAgentRoles()).containsOnly(handCraftBack);
        assertThat(handCraftBack.getAgent()).isEqualTo(edSheeran);

        edSheeran.removeAgentRoles(handCraftBack);
        assertThat(edSheeran.getAgentRoles()).doesNotContain(handCraftBack);
        assertThat(handCraftBack.getAgent()).isNull();

        edSheeran.agentRoles(new HashSet<>(Set.of(handCraftBack)));
        assertThat(edSheeran.getAgentRoles()).containsOnly(handCraftBack);
        assertThat(handCraftBack.getAgent()).isEqualTo(edSheeran);

        edSheeran.setAgentRoles(new HashSet<>());
        assertThat(edSheeran.getAgentRoles()).doesNotContain(handCraftBack);
        assertThat(handCraftBack.getAgent()).isNull();
    }
}
