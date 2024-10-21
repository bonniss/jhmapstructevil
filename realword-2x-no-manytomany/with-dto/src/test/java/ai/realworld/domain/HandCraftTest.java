package ai.realworld.domain;

import static ai.realworld.domain.EdSheeranTestSamples.*;
import static ai.realworld.domain.HandCraftTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.RihannaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HandCraftTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HandCraft.class);
        HandCraft handCraft1 = getHandCraftSample1();
        HandCraft handCraft2 = new HandCraft();
        assertThat(handCraft1).isNotEqualTo(handCraft2);

        handCraft2.setId(handCraft1.getId());
        assertThat(handCraft1).isEqualTo(handCraft2);

        handCraft2 = getHandCraftSample2();
        assertThat(handCraft1).isNotEqualTo(handCraft2);
    }

    @Test
    void agentTest() {
        HandCraft handCraft = getHandCraftRandomSampleGenerator();
        EdSheeran edSheeranBack = getEdSheeranRandomSampleGenerator();

        handCraft.setAgent(edSheeranBack);
        assertThat(handCraft.getAgent()).isEqualTo(edSheeranBack);

        handCraft.agent(null);
        assertThat(handCraft.getAgent()).isNull();
    }

    @Test
    void roleTest() {
        HandCraft handCraft = getHandCraftRandomSampleGenerator();
        Rihanna rihannaBack = getRihannaRandomSampleGenerator();

        handCraft.setRole(rihannaBack);
        assertThat(handCraft.getRole()).isEqualTo(rihannaBack);

        handCraft.role(null);
        assertThat(handCraft.getRole()).isNull();
    }

    @Test
    void applicationTest() {
        HandCraft handCraft = getHandCraftRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        handCraft.setApplication(johnLennonBack);
        assertThat(handCraft.getApplication()).isEqualTo(johnLennonBack);

        handCraft.application(null);
        assertThat(handCraft.getApplication()).isNull();
    }
}
