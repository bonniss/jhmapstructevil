package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlPacinoVoucherTestSamples.*;
import static ai.realworld.domain.AlVueVueTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AlPacinoVoucherTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlPacinoVoucher.class);
        AlPacinoVoucher alPacinoVoucher1 = getAlPacinoVoucherSample1();
        AlPacinoVoucher alPacinoVoucher2 = new AlPacinoVoucher();
        assertThat(alPacinoVoucher1).isNotEqualTo(alPacinoVoucher2);

        alPacinoVoucher2.setId(alPacinoVoucher1.getId());
        assertThat(alPacinoVoucher1).isEqualTo(alPacinoVoucher2);

        alPacinoVoucher2 = getAlPacinoVoucherSample2();
        assertThat(alPacinoVoucher1).isNotEqualTo(alPacinoVoucher2);
    }

    @Test
    void userTest() {
        AlPacinoVoucher alPacinoVoucher = getAlPacinoVoucherRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alPacinoVoucher.setUser(alPacinoBack);
        assertThat(alPacinoVoucher.getUser()).isEqualTo(alPacinoBack);

        alPacinoVoucher.user(null);
        assertThat(alPacinoVoucher.getUser()).isNull();
    }

    @Test
    void voucherTest() {
        AlPacinoVoucher alPacinoVoucher = getAlPacinoVoucherRandomSampleGenerator();
        AlVueVue alVueVueBack = getAlVueVueRandomSampleGenerator();

        alPacinoVoucher.setVoucher(alVueVueBack);
        assertThat(alPacinoVoucher.getVoucher()).isEqualTo(alVueVueBack);

        alPacinoVoucher.voucher(null);
        assertThat(alPacinoVoucher.getVoucher()).isNull();
    }

    @Test
    void applicationTest() {
        AlPacinoVoucher alPacinoVoucher = getAlPacinoVoucherRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alPacinoVoucher.setApplication(johnLennonBack);
        assertThat(alPacinoVoucher.getApplication()).isEqualTo(johnLennonBack);

        alPacinoVoucher.application(null);
        assertThat(alPacinoVoucher.getApplication()).isNull();
    }
}
