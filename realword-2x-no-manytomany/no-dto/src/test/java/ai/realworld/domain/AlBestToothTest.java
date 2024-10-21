package ai.realworld.domain;

import static ai.realworld.domain.AlBestToothTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlBestToothTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlBestTooth.class);
        AlBestTooth alBestTooth1 = getAlBestToothSample1();
        AlBestTooth alBestTooth2 = new AlBestTooth();
        assertThat(alBestTooth1).isNotEqualTo(alBestTooth2);

        alBestTooth2.setId(alBestTooth1.getId());
        assertThat(alBestTooth1).isEqualTo(alBestTooth2);

        alBestTooth2 = getAlBestToothSample2();
        assertThat(alBestTooth1).isNotEqualTo(alBestTooth2);
    }

    @Test
    void applicationTest() {
        AlBestTooth alBestTooth = getAlBestToothRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alBestTooth.setApplication(johnLennonBack);
        assertThat(alBestTooth.getApplication()).isEqualTo(johnLennonBack);

        alBestTooth.application(null);
        assertThat(alBestTooth.getApplication()).isNull();
    }
}
