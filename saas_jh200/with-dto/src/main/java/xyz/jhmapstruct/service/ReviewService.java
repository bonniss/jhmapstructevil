package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Review;
import xyz.jhmapstruct.repository.ReviewRepository;
import xyz.jhmapstruct.service.dto.ReviewDTO;
import xyz.jhmapstruct.service.mapper.ReviewMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Review}.
 */
@Service
@Transactional
public class ReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    /**
     * Save a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewDTO save(ReviewDTO reviewDTO) {
        LOG.debug("Request to save Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        review = reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    /**
     * Update a review.
     *
     * @param reviewDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewDTO update(ReviewDTO reviewDTO) {
        LOG.debug("Request to update Review : {}", reviewDTO);
        Review review = reviewMapper.toEntity(reviewDTO);
        review = reviewRepository.save(review);
        return reviewMapper.toDto(review);
    }

    /**
     * Partially update a review.
     *
     * @param reviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewDTO> partialUpdate(ReviewDTO reviewDTO) {
        LOG.debug("Request to partially update Review : {}", reviewDTO);

        return reviewRepository
            .findById(reviewDTO.getId())
            .map(existingReview -> {
                reviewMapper.partialUpdate(existingReview, reviewDTO);

                return existingReview;
            })
            .map(reviewRepository::save)
            .map(reviewMapper::toDto);
    }

    /**
     * Get all the reviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewRepository.findAllWithEagerRelationships(pageable).map(reviewMapper::toDto);
    }

    /**
     * Get one review by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewDTO> findOne(Long id) {
        LOG.debug("Request to get Review : {}", id);
        return reviewRepository.findOneWithEagerRelationships(id).map(reviewMapper::toDto);
    }

    /**
     * Delete the review by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Review : {}", id);
        reviewRepository.deleteById(id);
    }
}
