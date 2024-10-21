package xyz.jhmapstruct.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static xyz.jhmapstruct.domain.ProductViViTestSamples.*;
import static xyz.jhmapstruct.domain.ReviewViViTestSamples.*;

import org.junit.jupiter.api.Test;
import xyz.jhmapstruct.web.rest.TestUtil;

class ReviewViViTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewViVi.class);
        ReviewViVi reviewViVi1 = getReviewViViSample1();
        ReviewViVi reviewViVi2 = new ReviewViVi();
        assertThat(reviewViVi1).isNotEqualTo(reviewViVi2);

        reviewViVi2.setId(reviewViVi1.getId());
        assertThat(reviewViVi1).isEqualTo(reviewViVi2);

        reviewViVi2 = getReviewViViSample2();
        assertThat(reviewViVi1).isNotEqualTo(reviewViVi2);
    }

    @Test
    void productTest() {
        ReviewViVi reviewViVi = getReviewViViRandomSampleGenerator();
        ProductViVi productViViBack = getProductViViRandomSampleGenerator();

        reviewViVi.setProduct(productViViBack);
        assertThat(reviewViVi.getProduct()).isEqualTo(productViViBack);

        reviewViVi.product(null);
        assertThat(reviewViVi.getProduct()).isNull();
    }
}
