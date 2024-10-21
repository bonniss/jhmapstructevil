package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductViTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewVi.class);
        ReviewVi reviewVi1 = getReviewViSample1();
        ReviewVi reviewVi2 = new ReviewVi();
        assertThat(reviewVi1).isNotEqualTo(reviewVi2);

        reviewVi2.setId(reviewVi1.getId());
        assertThat(reviewVi1).isEqualTo(reviewVi2);

        reviewVi2 = getReviewViSample2();
        assertThat(reviewVi1).isNotEqualTo(reviewVi2);
    }

    @Test
    void productTest() {
        ReviewVi reviewVi = getReviewViRandomSampleGenerator();
        ProductVi productViBack = getProductViRandomSampleGenerator();

        reviewVi.setProduct(productViBack);
        assertThat(reviewVi.getProduct()).isEqualTo(productViBack);

        reviewVi.product(null);
        assertThat(reviewVi.getProduct()).isNull();
    }
}
