package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoPointHistoryTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoPointHistory.class);
        AlPacinoPointHistory alPacinoPointHistory1 = getAlPacinoPointHistorySample1();
        AlPacinoPointHistory alPacinoPointHistory2 = new AlPacinoPointHistory();
        assertThat(alPacinoPointHistory1).isNotEqualTo(alPacinoPointHistory2);

        alPacinoPointHistory2.setId(alPacinoPointHistory1.getId());
        assertThat(alPacinoPointHistory1).isEqualTo(alPacinoPointHistory2);

        alPacinoPointHistory2 = getAlPacinoPointHistorySample2();
        assertThat(alPacinoPointHistory1).isNotEqualTo(alPacinoPointHistory2);
    }

    @Test
    void customerTest() {
        AlPacinoPointHistory alPacinoPointHistory = getAlPacinoPointHistoryRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoPointHistory.setCustomer(alPacinoBack);
        assertThat(alPacinoPointHistory.getCustomer()).isEqualTo(alPacinoBack);

        alPacinoPointHistory.customer(null);
        assertThat(alPacinoPointHistory.getCustomer()).isNull();
    }

    @Test
    void applicationTest() {
        AlPacinoPointHistory alPacinoPointHistory = getAlPacinoPointHistoryRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPacinoPointHistory.setApplication(johnLennonBack);
        assertThat(alPacinoPointHistory.getApplication()).isEqualTo(johnLennonBack);

        alPacinoPointHistory.application(null);
        assertThat(alPacinoPointHistory.getApplication()).isNull();
    }
}
