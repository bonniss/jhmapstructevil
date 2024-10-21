package ai.realworld.domain;

import static ai.realworld.domain.AllMassageThaiViTestSamples.*;
import static ai.realworld.domain.JohnLennonTestSamples.*;
import static ai.realworld.domain.MetaverseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import ai.realworld.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AllMassageThaiViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AllMassageThaiVi.class);
        AllMassageThaiVi allMassageThaiVi1 = getAllMassageThaiViSample1();
        AllMassageThaiVi allMassageThaiVi2 = new AllMassageThaiVi();
        assertThat(allMassageThaiVi1).isNotEqualTo(allMassageThaiVi2);

        allMassageThaiVi2.setId(allMassageThaiVi1.getId());
        assertThat(allMassageThaiVi1).isEqualTo(allMassageThaiVi2);

        allMassageThaiVi2 = getAllMassageThaiViSample2();
        assertThat(allMassageThaiVi1).isNotEqualTo(allMassageThaiVi2);
    }

    @Test
    void thumbnailTest() {
        AllMassageThaiVi allMassageThaiVi = getAllMassageThaiViRandomSampleGenerator();
        Metaverse metaverseBack = getMetaverseRandomSampleGenerator();

        allMassageThaiVi.setThumbnail(metaverseBack);
        assertThat(allMassageThaiVi.getThumbnail()).isEqualTo(metaverseBack);

        allMassageThaiVi.thumbnail(null);
        assertThat(allMassageThaiVi.getThumbnail()).isNull();
    }

    @Test
    void applicationTest() {
        AllMassageThaiVi allMassageThaiVi = getAllMassageThaiViRandomSampleGenerator();
        JohnLennon johnLennonBack = getJohnLennonRandomSampleGenerator();

        allMassageThaiVi.setApplication(johnLennonBack);
        assertThat(allMassageThaiVi.getApplication()).isEqualTo(johnLennonBack);

        allMassageThaiVi.application(null);
        assertThat(allMassageThaiVi.getApplication()).isNull();
    }
}
