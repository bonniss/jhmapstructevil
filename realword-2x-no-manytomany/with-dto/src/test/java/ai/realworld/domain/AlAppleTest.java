package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeTestSamples.*;
import static ai.realworld.domain.AlAppleTestSamples.*;
import static ai.realworld.domain.AndreiRightHandTestSamples.*;
import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlAppleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlApple.class);
        AlApple alApple1 = getAlAppleSample1();
        AlApple alApple2 = new AlApple();
        assertThat(alApple1).isNotEqualTo(alApple2);

        alApple2.setId(alApple1.getId());
        assertThat(alApple1).isEqualTo(alApple2);

        alApple2 = getAlAppleSample2();
        assertThat(alApple1).isNotEqualTo(alApple2);
    }

    @Test
    void addressTest() {
        AlApple alApple = getAlAppleRandomSampleGenerator();
        AndreiRightHand andreiRightHandBack = getAndreiRightHandRandomSampleGenerator();

        alApple.setAddress(andreiRightHandBack);
        assertThat(alApple.getAddress()).isEqualTo(andreiRightHandBack);

        alApple.address(null);
        assertThat(alApple.getAddress()).isNull();
    }

    @Test
    void agencyTypeTest() {
        AlApple alApple = getAlAppleRandomSampleGenerator();
        AlAlexType alAlexTypeBack = getAlAlexTypeRandomSampleGenerator();

        alApple.setAgencyType(alAlexTypeBack);
        assertThat(alApple.getAgencyType()).isEqualTo(alAlexTypeBack);

        alApple.agencyType(null);
        assertThat(alApple.getAgencyType()).isNull();
    }

    @Test
    void logoTest() {
        AlApple alApple = getAlAppleRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alApple.setLogo(metaverseBack);
        assertThat(alApple.getLogo()).isEqualTo(metaverseBack);

        alApple.logo(null);
        assertThat(alApple.getLogo()).isNull();
    }

    @Test
    void applicationTest() {
        AlApple alApple = getAlAppleRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alApple.setApplication(johnLennonBack);
        assertThat(alApple.getApplication()).isEqualTo(johnLennonBack);

        alApple.application(null);
        assertThat(alApple.getApplication()).isNull();
    }

    @Test
    void agentsTest() {
        AlApple alApple = getAlAppleRandomSampleGenerator();
        EdSheeran edSheeranBack = getEdSheeranRandomSampleGenerator();

        alApple.addAgents(edSheeranBack);
        assertThat(alApple.getAgents()).containsOnly(edSheeranBack);
        assertThat(edSheeranBack.getAgency()).isEqualTo(alApple);

        alApple.removeAgents(edSheeranBack);
        assertThat(alApple.getAgents()).doesNotContain(edSheeranBack);
        assertThat(edSheeranBack.getAgency()).isNull();

        alApple.agents(new HashSet<>(Set.of(edSheeranBack)));
        assertThat(alApple.getAgents()).containsOnly(edSheeranBack);
        assertThat(edSheeranBack.getAgency()).isEqualTo(alApple);

        alApple.setAgents(new HashSet<>());
        assertThat(alApple.getAgents()).doesNotContain(edSheeranBack);
        assertThat(edSheeranBack.getAgency()).isNull();
    }
}
