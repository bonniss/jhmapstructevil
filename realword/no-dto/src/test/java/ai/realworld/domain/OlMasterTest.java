package ai.realworld.domain;

import static ai.realworld.domain.AndreiRightHandTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.OlMasterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OlMasterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OlMaster.class);
        OlMaster olMaster1 = getOlMasterSample1();
        OlMaster olMaster2 = new OlMaster();
        assertThat(olMaster1).isNotEqualTo(olMaster2);

        olMaster2.setId(olMaster1.getId());
        assertThat(olMaster1).isEqualTo(olMaster2);

        olMaster2 = getOlMasterSample2();
        assertThat(olMaster1).isNotEqualTo(olMaster2);
    }

    @Test
    void addressTest() {
        OlMaster olMaster = getOlMasterRandomSampleGenerator();
        AndreiRightHand andreiRightHandBack = getAndreiRightHandRandomSampleGenerator();

        olMaster.setAddress(andreiRightHandBack);
        assertThat(olMaster.getAddress()).isEqualTo(andreiRightHandBack);

        olMaster.address(null);
        assertThat(olMaster.getAddress()).isNull();
    }

    @Test
    void applicationsTest() {
        OlMaster olMaster = getOlMasterRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        olMaster.addApplications(johnLennonBack);
        assertThat(olMaster.getApplications()).containsOnly(johnLennonBack);
        assertThat(johnLennonBack.getOrganization()).isEqualTo(olMaster);

        olMaster.removeApplications(johnLennonBack);
        assertThat(olMaster.getApplications()).doesNotContain(johnLennonBack);
        assertThat(johnLennonBack.getOrganization()).isNull();

        olMaster.applications(new HashSet<>(Set.of(johnLennonBack)));
        assertThat(olMaster.getApplications()).containsOnly(johnLennonBack);
        assertThat(johnLennonBack.getOrganization()).isEqualTo(olMaster);

        olMaster.setApplications(new HashSet<>());
        assertThat(olMaster.getApplications()).doesNotContain(johnLennonBack);
        assertThat(johnLennonBack.getOrganization()).isNull();
    }
}
