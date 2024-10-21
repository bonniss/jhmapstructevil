package ai.realworld.domain;

import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.OlAlmantinoMiloTestSamples.*;
import static ai.realworld.domain.OlMasterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OlAlmantinoMiloTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OlAlmantinoMilo.class);
        OlAlmantinoMilo olAlmantinoMilo1 = getOlAlmantinoMiloSample1();
        OlAlmantinoMilo olAlmantinoMilo2 = new OlAlmantinoMilo();
        assertThat(olAlmantinoMilo1).isNotEqualTo(olAlmantinoMilo2);

        olAlmantinoMilo2.setId(olAlmantinoMilo1.getId());
        assertThat(olAlmantinoMilo1).isEqualTo(olAlmantinoMilo2);

        olAlmantinoMilo2 = getOlAlmantinoMiloSample2();
        assertThat(olAlmantinoMilo1).isNotEqualTo(olAlmantinoMilo2);
    }

    @Test
    void organizationTest() {
        OlAlmantinoMilo olAlmantinoMilo = getOlAlmantinoMiloRandomSampleGenerator();
        OlMaster olMasterBack = getOlMasterRandomSampleGenerator();

        olAlmantinoMilo.setOrganization(olMasterBack);
        assertThat(olAlmantinoMilo.getOrganization()).isEqualTo(olMasterBack);

        olAlmantinoMilo.organization(null);
        assertThat(olAlmantinoMilo.getOrganization()).isNull();
    }

    @Test
    void applicationsTest() {
        OlAlmantinoMilo olAlmantinoMilo = getOlAlmantinoMiloRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        olAlmantinoMilo.addApplications(johnLennonBack);
        assertThat(olAlmantinoMilo.getApplications()).containsOnly(johnLennonBack);
        assertThat(johnLennonBack.getAppManager()).isEqualTo(olAlmantinoMilo);

        olAlmantinoMilo.removeApplications(johnLennonBack);
        assertThat(olAlmantinoMilo.getApplications()).doesNotContain(johnLennonBack);
        assertThat(johnLennonBack.getAppManager()).isNull();

        olAlmantinoMilo.applications(new HashSet<>(Set.of(johnLennonBack)));
        assertThat(olAlmantinoMilo.getApplications()).containsOnly(johnLennonBack);
        assertThat(johnLennonBack.getAppManager()).isEqualTo(olAlmantinoMilo);

        olAlmantinoMilo.setApplications(new HashSet<>());
        assertThat(olAlmantinoMilo.getApplications()).doesNotContain(johnLennonBack);
        assertThat(johnLennonBack.getAppManager()).isNull();
    }
}
