package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.ReviewViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
@Service
@Transactional
public class ReviewViViServiceImpl implements ReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViServiceImpl.class);

    private final ReviewViViRepository reviewViViRepository;

    public ReviewViViServiceImpl(ReviewViViRepository reviewViViRepository) {
        this.reviewViViRepository = reviewViViRepository;
    }

    @Override
    public ReviewViVi save(ReviewViVi reviewViVi) {
        LOG.debug("Request to save ReviewViVi : {}", reviewViVi);
        return reviewViViRepository.save(reviewViVi);
    }

    @Override
    public ReviewViVi update(ReviewViVi reviewViVi) {
        LOG.debug("Request to update ReviewViVi : {}", reviewViVi);
        return reviewViViRepository.save(reviewViVi);
    }

    @Override
    public Optional<ReviewViVi> partialUpdate(ReviewViVi reviewViVi) {
        LOG.debug("Request to partially update ReviewViVi : {}", reviewViVi);

        return reviewViViRepository
            .findById(reviewViVi.getId())
            .map(existingReviewViVi -> {
                if (reviewViVi.getRating() != null) {
                    existingReviewViVi.setRating(reviewViVi.getRating());
                }
                if (reviewViVi.getComment() != null) {
                    existingReviewViVi.setComment(reviewViVi.getComment());
                }
                if (reviewViVi.getReviewDate() != null) {
                    existingReviewViVi.setReviewDate(reviewViVi.getReviewDate());
                }

                return existingReviewViVi;
            })
            .map(reviewViViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewViVi> findAll() {
        LOG.debug("Request to get all ReviewViVis");
        return reviewViViRepository.findAll();
    }

    public Page<ReviewViVi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewViVi> findOne(Long id) {
        LOG.debug("Request to get ReviewViVi : {}", id);
        return reviewViViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewViVi : {}", id);
        reviewViViRepository.deleteById(id);
    }
}
