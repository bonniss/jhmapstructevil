package ai.realworld.domain;

import static ai.realworld.domain.HandCraftTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.RihannaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RihannaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rihanna.class);
        Rihanna rihanna1 = getRihannaSample1();
        Rihanna rihanna2 = new Rihanna();
        assertThat(rihanna1).isNotEqualTo(rihanna2);

        rihanna2.setId(rihanna1.getId());
        assertThat(rihanna1).isEqualTo(rihanna2);

        rihanna2 = getRihannaSample2();
        assertThat(rihanna1).isNotEqualTo(rihanna2);
    }

    @Test
    void applicationTest() {
        Rihanna rihanna = getRihannaRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        rihanna.setApplication(johnLennonBack);
        assertThat(rihanna.getApplication()).isEqualTo(johnLennonBack);

        rihanna.application(null);
        assertThat(rihanna.getApplication()).isNull();
    }

    @Test
    void agentRolesTest() {
        Rihanna rihanna = getRihannaRandomSampleGenerator();
        HandCraft handCraftBack = getHandCraftRandomSampleGenerator();

        rihanna.addAgentRoles(handCraftBack);
        assertThat(rihanna.getAgentRoles()).containsOnly(handCraftBack);
        assertThat(handCraftBack.getRole()).isEqualTo(rihanna);

        rihanna.removeAgentRoles(handCraftBack);
        assertThat(rihanna.getAgentRoles()).doesNotContain(handCraftBack);
        assertThat(handCraftBack.getRole()).isNull();

        rihanna.agentRoles(new HashSet<>(Set.of(handCraftBack)));
        assertThat(rihanna.getAgentRoles()).containsOnly(handCraftBack);
        assertThat(handCraftBack.getRole()).isEqualTo(rihanna);

        rihanna.setAgentRoles(new HashSet<>());
        assertThat(rihanna.getAgentRoles()).doesNotContain(handCraftBack);
        assertThat(handCraftBack.getRole()).isNull();
    }
}
