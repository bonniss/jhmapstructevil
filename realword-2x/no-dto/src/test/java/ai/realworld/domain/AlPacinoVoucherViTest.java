package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlPacinoVoucherViTestSamples.*;
import static ai.realworld.domain.AlVueVueViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoVoucherVi.class);
        AlPacinoVoucherVi alPacinoVoucherVi1 = getAlPacinoVoucherViSample1();
        AlPacinoVoucherVi alPacinoVoucherVi2 = new AlPacinoVoucherVi();
        assertThat(alPacinoVoucherVi1).isNotEqualTo(alPacinoVoucherVi2);

        alPacinoVoucherVi2.setId(alPacinoVoucherVi1.getId());
        assertThat(alPacinoVoucherVi1).isEqualTo(alPacinoVoucherVi2);

        alPacinoVoucherVi2 = getAlPacinoVoucherViSample2();
        assertThat(alPacinoVoucherVi1).isNotEqualTo(alPacinoVoucherVi2);
    }

    @Test
    void userTest() {
        AlPacinoVoucherVi alPacinoVoucherVi = getAlPacinoVoucherViRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoVoucherVi.setUser(alPacinoBack);
        assertThat(alPacinoVoucherVi.getUser()).isEqualTo(alPacinoBack);

        alPacinoVoucherVi.user(null);
        assertThat(alPacinoVoucherVi.getUser()).isNull();
    }

    @Test
    void voucherTest() {
        AlPacinoVoucherVi alPacinoVoucherVi = getAlPacinoVoucherViRandomSampleGenerator();
        AlVueVueVi alVueVueViBack = getAlVueVueViRandomSampleGenerator();

        alPacinoVoucherVi.setVoucher(alVueVueViBack);
        assertThat(alPacinoVoucherVi.getVoucher()).isEqualTo(alVueVueViBack);

        alPacinoVoucherVi.voucher(null);
        assertThat(alPacinoVoucherVi.getVoucher()).isNull();
    }

    @Test
    void applicationTest() {
        AlPacinoVoucherVi alPacinoVoucherVi = getAlPacinoVoucherViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPacinoVoucherVi.setApplication(johnLennonBack);
        assertThat(alPacinoVoucherVi.getApplication()).isEqualTo(johnLennonBack);

        alPacinoVoucherVi.application(null);
        assertThat(alPacinoVoucherVi.getApplication()).isNull();
    }
}
