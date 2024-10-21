package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.ReviewMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
@Service
@Transactional
public class ReviewMiServiceImpl implements ReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiServiceImpl.class);

    private final ReviewMiRepository reviewMiRepository;

    public ReviewMiServiceImpl(ReviewMiRepository reviewMiRepository) {
        this.reviewMiRepository = reviewMiRepository;
    }

    @Override
    public ReviewMi save(ReviewMi reviewMi) {
        LOG.debug("Request to save ReviewMi : {}", reviewMi);
        return reviewMiRepository.save(reviewMi);
    }

    @Override
    public ReviewMi update(ReviewMi reviewMi) {
        LOG.debug("Request to update ReviewMi : {}", reviewMi);
        return reviewMiRepository.save(reviewMi);
    }

    @Override
    public Optional<ReviewMi> partialUpdate(ReviewMi reviewMi) {
        LOG.debug("Request to partially update ReviewMi : {}", reviewMi);

        return reviewMiRepository
            .findById(reviewMi.getId())
            .map(existingReviewMi -> {
                if (reviewMi.getRating() != null) {
                    existingReviewMi.setRating(reviewMi.getRating());
                }
                if (reviewMi.getComment() != null) {
                    existingReviewMi.setComment(reviewMi.getComment());
                }
                if (reviewMi.getReviewDate() != null) {
                    existingReviewMi.setReviewDate(reviewMi.getReviewDate());
                }

                return existingReviewMi;
            })
            .map(reviewMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewMi> findAll() {
        LOG.debug("Request to get all ReviewMis");
        return reviewMiRepository.findAll();
    }

    public Page<ReviewMi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewMi> findOne(Long id) {
        LOG.debug("Request to get ReviewMi : {}", id);
        return reviewMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMi : {}", id);
        reviewMiRepository.deleteById(id);
    }
}
