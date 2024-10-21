package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlVueVueTestSamples.*;
import static ai.realworld.domain.AlVueVueUsageTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlVueVueUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueUsage.class);
        AlVueVueUsage alVueVueUsage1 = getAlVueVueUsageSample1();
        AlVueVueUsage alVueVueUsage2 = new AlVueVueUsage();
        assertThat(alVueVueUsage1).isNotEqualTo(alVueVueUsage2);

        alVueVueUsage2.setId(alVueVueUsage1.getId());
        assertThat(alVueVueUsage1).isEqualTo(alVueVueUsage2);

        alVueVueUsage2 = getAlVueVueUsageSample2();
        assertThat(alVueVueUsage1).isNotEqualTo(alVueVueUsage2);
    }

    @Test
    void applicationTest() {
        AlVueVueUsage alVueVueUsage = getAlVueVueUsageRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVueUsage.setApplication(johnLennonBack);
        assertThat(alVueVueUsage.getApplication()).isEqualTo(johnLennonBack);

        alVueVueUsage.application(null);
        assertThat(alVueVueUsage.getApplication()).isNull();
    }

    @Test
    void voucherTest() {
        AlVueVueUsage alVueVueUsage = getAlVueVueUsageRandomSampleGenerator();
        AlVueVue alVueVueBack = getAlVueVueRandomSampleGenerator();

        alVueVueUsage.addVoucher(alVueVueBack);
        assertThat(alVueVueUsage.getVouchers()).containsOnly(alVueVueBack);
        assertThat(alVueVueBack.getAlVueVueUsage()).isEqualTo(alVueVueUsage);

        alVueVueUsage.removeVoucher(alVueVueBack);
        assertThat(alVueVueUsage.getVouchers()).doesNotContain(alVueVueBack);
        assertThat(alVueVueBack.getAlVueVueUsage()).isNull();

        alVueVueUsage.vouchers(new HashSet<>(Set.of(alVueVueBack)));
        assertThat(alVueVueUsage.getVouchers()).containsOnly(alVueVueBack);
        assertThat(alVueVueBack.getAlVueVueUsage()).isEqualTo(alVueVueUsage);

        alVueVueUsage.setVouchers(new HashSet<>());
        assertThat(alVueVueUsage.getVouchers()).doesNotContain(alVueVueBack);
        assertThat(alVueVueBack.getAlVueVueUsage()).isNull();
    }

    @Test
    void customerTest() {
        AlVueVueUsage alVueVueUsage = getAlVueVueUsageRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alVueVueUsage.addCustomer(alPacinoBack);
        assertThat(alVueVueUsage.getCustomers()).containsOnly(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueUsage()).isEqualTo(alVueVueUsage);

        alVueVueUsage.removeCustomer(alPacinoBack);
        assertThat(alVueVueUsage.getCustomers()).doesNotContain(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueUsage()).isNull();

        alVueVueUsage.customers(new HashSet<>(Set.of(alPacinoBack)));
        assertThat(alVueVueUsage.getCustomers()).containsOnly(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueUsage()).isEqualTo(alVueVueUsage);

        alVueVueUsage.setCustomers(new HashSet<>());
        assertThat(alVueVueUsage.getCustomers()).doesNotContain(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueUsage()).isNull();
    }
}
