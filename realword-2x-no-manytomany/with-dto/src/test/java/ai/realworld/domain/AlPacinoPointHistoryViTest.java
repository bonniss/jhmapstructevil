package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoPointHistoryViTestSamples.*;
import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoPointHistoryViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoPointHistoryVi.class);
        AlPacinoPointHistoryVi alPacinoPointHistoryVi1 = getAlPacinoPointHistoryViSample1();
        AlPacinoPointHistoryVi alPacinoPointHistoryVi2 = new AlPacinoPointHistoryVi();
        assertThat(alPacinoPointHistoryVi1).isNotEqualTo(alPacinoPointHistoryVi2);

        alPacinoPointHistoryVi2.setId(alPacinoPointHistoryVi1.getId());
        assertThat(alPacinoPointHistoryVi1).isEqualTo(alPacinoPointHistoryVi2);

        alPacinoPointHistoryVi2 = getAlPacinoPointHistoryViSample2();
        assertThat(alPacinoPointHistoryVi1).isNotEqualTo(alPacinoPointHistoryVi2);
    }

    @Test
    void customerTest() {
        AlPacinoPointHistoryVi alPacinoPointHistoryVi = getAlPacinoPointHistoryViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoPointHistoryVi.setCustomer(alPacinoBack);
        assertThat(alPacinoPointHistoryVi.getCustomer()).isEqualTo(alPacinoBack);

        alPacinoPointHistoryVi.customer(null);
        assertThat(alPacinoPointHistoryVi.getCustomer()).isNull();
    }

    @Test
    void applicationTest() {
        AlPacinoPointHistoryVi alPacinoPointHistoryVi = getAlPacinoPointHistoryViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPacinoPointHistoryVi.setApplication(johnLennonBack);
        assertThat(alPacinoPointHistoryVi.getApplication()).isEqualTo(johnLennonBack);

        alPacinoPointHistoryVi.application(null);
        assertThat(alPacinoPointHistoryVi.getApplication()).isNull();
    }
}
