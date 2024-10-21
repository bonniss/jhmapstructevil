package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryMiMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryMiMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryMiMi.class);
        CategoryMiMi categoryMiMi1 = getCategoryMiMiSample1();
        CategoryMiMi categoryMiMi2 = new CategoryMiMi();
        assertThat(categoryMiMi1).isNotEqualTo(categoryMiMi2);

        categoryMiMi2.setId(categoryMiMi1.getId());
        assertThat(categoryMiMi1).isEqualTo(categoryMiMi2);

        categoryMiMi2 = getCategoryMiMiSample2();
        assertThat(categoryMiMi1).isNotEqualTo(categoryMiMi2);
    }
}
