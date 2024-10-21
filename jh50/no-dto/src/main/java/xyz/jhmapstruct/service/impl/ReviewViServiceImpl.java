package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.ReviewViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
@Service
@Transactional
public class ReviewViServiceImpl implements ReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViServiceImpl.class);

    private final ReviewViRepository reviewViRepository;

    public ReviewViServiceImpl(ReviewViRepository reviewViRepository) {
        this.reviewViRepository = reviewViRepository;
    }

    @Override
    public ReviewVi save(ReviewVi reviewVi) {
        LOG.debug("Request to save ReviewVi : {}", reviewVi);
        return reviewViRepository.save(reviewVi);
    }

    @Override
    public ReviewVi update(ReviewVi reviewVi) {
        LOG.debug("Request to update ReviewVi : {}", reviewVi);
        return reviewViRepository.save(reviewVi);
    }

    @Override
    public Optional<ReviewVi> partialUpdate(ReviewVi reviewVi) {
        LOG.debug("Request to partially update ReviewVi : {}", reviewVi);

        return reviewViRepository
            .findById(reviewVi.getId())
            .map(existingReviewVi -> {
                if (reviewVi.getRating() != null) {
                    existingReviewVi.setRating(reviewVi.getRating());
                }
                if (reviewVi.getComment() != null) {
                    existingReviewVi.setComment(reviewVi.getComment());
                }
                if (reviewVi.getReviewDate() != null) {
                    existingReviewVi.setReviewDate(reviewVi.getReviewDate());
                }

                return existingReviewVi;
            })
            .map(reviewViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewVi> findAll() {
        LOG.debug("Request to get all ReviewVis");
        return reviewViRepository.findAll();
    }

    public Page<ReviewVi> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewVi> findOne(Long id) {
        LOG.debug("Request to get ReviewVi : {}", id);
        return reviewViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewVi : {}", id);
        reviewViRepository.deleteById(id);
    }
}
