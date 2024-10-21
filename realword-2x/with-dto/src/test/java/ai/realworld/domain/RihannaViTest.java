package ai.realworld.domain;

import static ai.realworld.domain.HandCraftViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.RihannaViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RihannaViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RihannaVi.class);
        RihannaVi rihannaVi1 = getRihannaViSample1();
        RihannaVi rihannaVi2 = new RihannaVi();
        assertThat(rihannaVi1).isNotEqualTo(rihannaVi2);

        rihannaVi2.setId(rihannaVi1.getId());
        assertThat(rihannaVi1).isEqualTo(rihannaVi2);

        rihannaVi2 = getRihannaViSample2();
        assertThat(rihannaVi1).isNotEqualTo(rihannaVi2);
    }

    @Test
    void applicationTest() {
        RihannaVi rihannaVi = getRihannaViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        rihannaVi.setApplication(johnLennonBack);
        assertThat(rihannaVi.getApplication()).isEqualTo(johnLennonBack);

        rihannaVi.application(null);
        assertThat(rihannaVi.getApplication()).isNull();
    }

    @Test
    void agentRolesTest() {
        RihannaVi rihannaVi = getRihannaViRandomSampleGenerator();
        HandCraftVi handCraftViBack = getHandCraftViRandomSampleGenerator();

        rihannaVi.addAgentRoles(handCraftViBack);
        assertThat(rihannaVi.getAgentRoles()).containsOnly(handCraftViBack);
        assertThat(handCraftViBack.getRole()).isEqualTo(rihannaVi);

        rihannaVi.removeAgentRoles(handCraftViBack);
        assertThat(rihannaVi.getAgentRoles()).doesNotContain(handCraftViBack);
        assertThat(handCraftViBack.getRole()).isNull();

        rihannaVi.agentRoles(new HashSet<>(Set.of(handCraftViBack)));
        assertThat(rihannaVi.getAgentRoles()).containsOnly(handCraftViBack);
        assertThat(handCraftViBack.getRole()).isEqualTo(rihannaVi);

        rihannaVi.setAgentRoles(new HashSet<>());
        assertThat(rihannaVi.getAgentRoles()).doesNotContain(handCraftViBack);
        assertThat(handCraftViBack.getRole()).isNull();
    }
}
