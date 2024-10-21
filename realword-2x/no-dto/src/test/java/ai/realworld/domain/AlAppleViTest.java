package ai.realworld.domain;

import static ai.realworld.domain.AlAlexTypeViTestSamples.*;
import static ai.realworld.domain.AlAppleViTestSamples.*;
import static ai.realworld.domain.AndreiRightHandViTestSamples.*;
import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlAppleViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlAppleVi.class);
        AlAppleVi alAppleVi1 = getAlAppleViSample1();
        AlAppleVi alAppleVi2 = new AlAppleVi();
        assertThat(alAppleVi1).isNotEqualTo(alAppleVi2);

        alAppleVi2.setId(alAppleVi1.getId());
        assertThat(alAppleVi1).isEqualTo(alAppleVi2);

        alAppleVi2 = getAlAppleViSample2();
        assertThat(alAppleVi1).isNotEqualTo(alAppleVi2);
    }

    @Test
    void addressTest() {
        AlAppleVi alAppleVi = getAlAppleViRandomSampleGenerator();
        AndreiRightHandVi andreiRightHandViBack = getAndreiRightHandViRandomSampleGenerator();

        alAppleVi.setAddress(andreiRightHandViBack);
        assertThat(alAppleVi.getAddress()).isEqualTo(andreiRightHandViBack);

        alAppleVi.address(null);
        assertThat(alAppleVi.getAddress()).isNull();
    }

    @Test
    void agencyTypeTest() {
        AlAppleVi alAppleVi = getAlAppleViRandomSampleGenerator();
        AlAlexTypeVi alAlexTypeViBack = getAlAlexTypeViRandomSampleGenerator();

        alAppleVi.setAgencyType(alAlexTypeViBack);
        assertThat(alAppleVi.getAgencyType()).isEqualTo(alAlexTypeViBack);

        alAppleVi.agencyType(null);
        assertThat(alAppleVi.getAgencyType()).isNull();
    }

    @Test
    void logoTest() {
        AlAppleVi alAppleVi = getAlAppleViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        alAppleVi.setLogo(metaverseBack);
        assertThat(alAppleVi.getLogo()).isEqualTo(metaverseBack);

        alAppleVi.logo(null);
        assertThat(alAppleVi.getLogo()).isNull();
    }

    @Test
    void applicationTest() {
        AlAppleVi alAppleVi = getAlAppleViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alAppleVi.setApplication(johnLennonBack);
        assertThat(alAppleVi.getApplication()).isEqualTo(johnLennonBack);

        alAppleVi.application(null);
        assertThat(alAppleVi.getApplication()).isNull();
    }

    @Test
    void agentsTest() {
        AlAppleVi alAppleVi = getAlAppleViRandomSampleGenerator();
        EdSheeranVi edSheeranViBack = getEdSheeranViRandomSampleGenerator();

        alAppleVi.addAgents(edSheeranViBack);
        assertThat(alAppleVi.getAgents()).containsOnly(edSheeranViBack);
        assertThat(edSheeranViBack.getAgency()).isEqualTo(alAppleVi);

        alAppleVi.removeAgents(edSheeranViBack);
        assertThat(alAppleVi.getAgents()).doesNotContain(edSheeranViBack);
        assertThat(edSheeranViBack.getAgency()).isNull();

        alAppleVi.agents(new HashSet<>(Set.of(edSheeranViBack)));
        assertThat(alAppleVi.getAgents()).containsOnly(edSheeranViBack);
        assertThat(edSheeranViBack.getAgency()).isEqualTo(alAppleVi);

        alAppleVi.setAgents(new HashSet<>());
        assertThat(alAppleVi.getAgents()).doesNotContain(edSheeranViBack);
        assertThat(edSheeranViBack.getAgency()).isNull();
    }
}
