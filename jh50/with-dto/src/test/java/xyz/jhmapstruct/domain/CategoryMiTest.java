package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.CategoryMiTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class CategoryMiTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryMi.class);
        CategoryMi categoryMi1 = getCategoryMiSample1();
        CategoryMi categoryMi2 = new CategoryMi();
        assertThat(categoryMi1).isNotEqualTo(categoryMi2);

        categoryMi2.setId(categoryMi1.getId());
        assertThat(categoryMi1).isEqualTo(categoryMi2);

        categoryMi2 = getCategoryMiSample2();
        assertThat(categoryMi1).isNotEqualTo(categoryMi2);
    }
}
