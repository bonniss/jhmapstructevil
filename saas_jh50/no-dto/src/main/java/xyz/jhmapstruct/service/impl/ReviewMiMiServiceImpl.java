package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.ReviewMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
@Service
@Transactional
public class ReviewMiMiServiceImpl implements ReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiServiceImpl.class);

    private final ReviewMiMiRepository reviewMiMiRepository;

    public ReviewMiMiServiceImpl(ReviewMiMiRepository reviewMiMiRepository) {
        this.reviewMiMiRepository = reviewMiMiRepository;
    }

    @Override
    public ReviewMiMi save(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to save ReviewMiMi : {}", reviewMiMi);
        return reviewMiMiRepository.save(reviewMiMi);
    }

    @Override
    public ReviewMiMi update(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to update ReviewMiMi : {}", reviewMiMi);
        return reviewMiMiRepository.save(reviewMiMi);
    }

    @Override
    public Optional<ReviewMiMi> partialUpdate(ReviewMiMi reviewMiMi) {
        LOG.debug("Request to partially update ReviewMiMi : {}", reviewMiMi);

        return reviewMiMiRepository
            .findById(reviewMiMi.getId())
            .map(existingReviewMiMi -> {
                if (reviewMiMi.getRating() != null) {
                    existingReviewMiMi.setRating(reviewMiMi.getRating());
                }
                if (reviewMiMi.getComment() != null) {
                    existingReviewMiMi.setComment(reviewMiMi.getComment());
                }
                if (reviewMiMi.getReviewDate() != null) {
                    existingReviewMiMi.setReviewDate(reviewMiMi.getReviewDate());
                }

                return existingReviewMiMi;
            })
            .map(reviewMiMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewMiMi> findAll() {
        LOG.debug("Request to get all ReviewMiMis");
        return reviewMiMiRepository.findAll();
    }

    public Page<ReviewMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewMiMi> findOne(Long id) {
        LOG.debug("Request to get ReviewMiMi : {}", id);
        return reviewMiMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMiMi : {}", id);
        reviewMiMiRepository.deleteById(id);
    }
}
