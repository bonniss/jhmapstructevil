package ai.realworld.domain;

import static ai.realworld.domain.EdSheeranViTestSamples.*;
import static ai.realworld.domain.HandCraftViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.RihannaViTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HandCraftViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HandCraftVi.class);
        HandCraftVi handCraftVi1 = getHandCraftViSample1();
        HandCraftVi handCraftVi2 = new HandCraftVi();
        assertThat(handCraftVi1).isNotEqualTo(handCraftVi2);

        handCraftVi2.setId(handCraftVi1.getId());
        assertThat(handCraftVi1).isEqualTo(handCraftVi2);

        handCraftVi2 = getHandCraftViSample2();
        assertThat(handCraftVi1).isNotEqualTo(handCraftVi2);
    }

    @Test
    void agentTest() {
        HandCraftVi handCraftVi = getHandCraftViRandomSampleGenerator();
        EdSheeranVi edSheeranViBack = getEdSheeranViRandomSampleGenerator();

        handCraftVi.setAgent(edSheeranViBack);
        assertThat(handCraftVi.getAgent()).isEqualTo(edSheeranViBack);

        handCraftVi.agent(null);
        assertThat(handCraftVi.getAgent()).isNull();
    }

    @Test
    void roleTest() {
        HandCraftVi handCraftVi = getHandCraftViRandomSampleGenerator();
        RihannaVi rihannaViBack = getRihannaViRandomSampleGenerator();

        handCraftVi.setRole(rihannaViBack);
        assertThat(handCraftVi.getRole()).isEqualTo(rihannaViBack);

        handCraftVi.role(null);
        assertThat(handCraftVi.getRole()).isNull();
    }

    @Test
    void applicationTest() {
        HandCraftVi handCraftVi = getHandCraftViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        handCraftVi.setApplication(johnLennonBack);
        assertThat(handCraftVi.getApplication()).isEqualTo(johnLennonBack);

        handCraftVi.application(null);
        assertThat(handCraftVi.getApplication()).isNull();
    }
}
