package ai.realworld.domain;

import static ai.realworld.domain.InitiumTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static ai.realworld.domain.OlAlmantinoMiloTestSamples.*;
import static ai.realworld.domain.OlMasterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JohnLennonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JohnLennon.class);
        JohnLennon johnLennon1 = getJohnLennonSample1();
        JohnLennon johnLennon2 = new JohnLennon();
        assertThat(johnLennon1).isNotEqualTo(johnLennon2);

        johnLennon2.setId(johnLennon1.getId());
        assertThat(johnLennon1).isEqualTo(johnLennon2);

        johnLennon2 = getJohnLennonSample2();
        assertThat(johnLennon1).isNotEqualTo(johnLennon2);
    }

    @Test
    void logoTest() {
        JohnLennon johnLennon = getJohnLennonRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        johnLennon.setLogo(metaverseBack);
        assertThat(johnLennon.getLogo()).isEqualTo(metaverseBack);

        johnLennon.logo(null);
        assertThat(johnLennon.getLogo()).isNull();
    }

    @Test
    void appManagerTest() {
        JohnLennon johnLennon = getJohnLennonRandomSampleGenerator();
        OlAlmantinoMilo olAlmantinoMiloBack = getOlAlmantinoMiloRandomSampleGenerator();

        johnLennon.setAppManager(olAlmantinoMiloBack);
        assertThat(johnLennon.getAppManager()).isEqualTo(olAlmantinoMiloBack);

        johnLennon.appManager(null);
        assertThat(johnLennon.getAppManager()).isNull();
    }

    @Test
    void organizationTest() {
        JohnLennon johnLennon = getJohnLennonRandomSampleGenerator();
        OlMaster olMasterBack = getOlMasterRandomSampleGenerator();

        johnLennon.setOrganization(olMasterBack);
        assertThat(johnLennon.getOrganization()).isEqualTo(olMasterBack);

        johnLennon.organization(null);
        assertThat(johnLennon.getOrganization()).isNull();
    }

    @Test
    void jelloInitiumTest() {
        JohnLennon johnLennon = getJohnLennonRandomSampleGenerator();
        Initium initiumBack = getInitiumRandomSampleGenerator();

        johnLennon.setJelloInitium(initiumBack);
        assertThat(johnLennon.getJelloInitium()).isEqualTo(initiumBack);

        johnLennon.jelloInitium(null);
        assertThat(johnLennon.getJelloInitium()).isNull();
    }

    @Test
    void inhouseInitiumTest() {
        JohnLennon johnLennon = getJohnLennonRandomSampleGenerator();
        Initium initiumBack = getInitiumRandomSampleGenerator();

        johnLennon.setInhouseInitium(initiumBack);
        assertThat(johnLennon.getInhouseInitium()).isEqualTo(initiumBack);

        johnLennon.inhouseInitium(null);
        assertThat(johnLennon.getInhouseInitium()).isNull();
    }
}
