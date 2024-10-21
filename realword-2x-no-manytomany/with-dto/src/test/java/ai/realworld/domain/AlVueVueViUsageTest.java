package ai.realworld.domain;

import static ai.realworld.domain.AlPacinoTestSamples.*;
import static ai.realworld.domain.AlVueVueViTestSamples.*;
import static ai.realworld.domain.AlVueVueViUsageTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AlVueVueViUsageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AlVueVueViUsage.class);
        AlVueVueViUsage alVueVueViUsage1 = getAlVueVueViUsageSample1();
        AlVueVueViUsage alVueVueViUsage2 = new AlVueVueViUsage();
        assertThat(alVueVueViUsage1).isNotEqualTo(alVueVueViUsage2);

        alVueVueViUsage2.setId(alVueVueViUsage1.getId());
        assertThat(alVueVueViUsage1).isEqualTo(alVueVueViUsage2);

        alVueVueViUsage2 = getAlVueVueViUsageSample2();
        assertThat(alVueVueViUsage1).isNotEqualTo(alVueVueViUsage2);
    }

    @Test
    void applicationTest() {
        AlVueVueViUsage alVueVueViUsage = getAlVueVueViUsageRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        alVueVueViUsage.setApplication(johnLennonBack);
        assertThat(alVueVueViUsage.getApplication()).isEqualTo(johnLennonBack);

        alVueVueViUsage.application(null);
        assertThat(alVueVueViUsage.getApplication()).isNull();
    }

    @Test
    void voucherTest() {
        AlVueVueViUsage alVueVueViUsage = getAlVueVueViUsageRandomSampleGenerator();
        AlVueVueVi alVueVueViBack = getAlVueVueViRandomSampleGenerator();

        alVueVueViUsage.addVoucher(alVueVueViBack);
        assertThat(alVueVueViUsage.getVouchers()).containsOnly(alVueVueViBack);
        assertThat(alVueVueViBack.getAlVueVueViUsage()).isEqualTo(alVueVueViUsage);

        alVueVueViUsage.removeVoucher(alVueVueViBack);
        assertThat(alVueVueViUsage.getVouchers()).doesNotContain(alVueVueViBack);
        assertThat(alVueVueViBack.getAlVueVueViUsage()).isNull();

        alVueVueViUsage.vouchers(new HashSet<>(Set.of(alVueVueViBack)));
        assertThat(alVueVueViUsage.getVouchers()).containsOnly(alVueVueViBack);
        assertThat(alVueVueViBack.getAlVueVueViUsage()).isEqualTo(alVueVueViUsage);

        alVueVueViUsage.setVouchers(new HashSet<>());
        assertThat(alVueVueViUsage.getVouchers()).doesNotContain(alVueVueViBack);
        assertThat(alVueVueViBack.getAlVueVueViUsage()).isNull();
    }

    @Test
    void customerTest() {
        AlVueVueViUsage alVueVueViUsage = getAlVueVueViUsageRandomSampleGenerator();
        AlPacino alPacinoBack = getAlPacinoRandomSampleGenerator();

        alVueVueViUsage.addCustomer(alPacinoBack);
        assertThat(alVueVueViUsage.getCustomers()).containsOnly(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueViUsage()).isEqualTo(alVueVueViUsage);

        alVueVueViUsage.removeCustomer(alPacinoBack);
        assertThat(alVueVueViUsage.getCustomers()).doesNotContain(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueViUsage()).isNull();

        alVueVueViUsage.customers(new HashSet<>(Set.of(alPacinoBack)));
        assertThat(alVueVueViUsage.getCustomers()).containsOnly(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueViUsage()).isEqualTo(alVueVueViUsage);

        alVueVueViUsage.setCustomers(new HashSet<>());
        assertThat(alVueVueViUsage.getCustomers()).doesNotContain(alPacinoBack);
        assertThat(alPacinoBack.getAlVueVueViUsage()).isNull();
    }
}
