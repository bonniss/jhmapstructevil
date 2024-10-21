package ai.realworld.domain;

import static ai.realworld.domain.AllMassageThaiTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllMassageThaiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllMassageThai.class);
        AllMassageThai allMassageThai1 = getAllMassageThaiSample1();
        AllMassageThai allMassageThai2 = new AllMassageThai();
        assertThat(allMassageThai1).isNotEqualTo(allMassageThai2);

        allMassageThai2.setId(allMassageThai1.getId());
        assertThat(allMassageThai1).isEqualTo(allMassageThai2);

        allMassageThai2 = getAllMassageThaiSample2();
        assertThat(allMassageThai1).isNotEqualTo(allMassageThai2);
    }

    @Test
    void thumbnailTest() {
        AllMassageThai allMassageThai = getAllMassageThaiRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        allMassageThai.setThumbnail(metaverseBack);
        assertThat(allMassageThai.getThumbnail()).isEqualTo(metaverseBack);

        allMassageThai.thumbnail(null);
        assertThat(allMassageThai.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AllMassageThai allMassageThai = getAllMassageThaiRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        allMassageThai.setApplication(johnLennonBack);
        assertThat(allMassageThai.getApplication()).isEqualTo(johnLennonBack);

        allMassageThai.application(null);
        assertThat(allMassageThai.getApplication()).isNull();
    }
}
