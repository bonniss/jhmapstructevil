package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.Review;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.Review}.
 */
public interface ReviewService {
    /**
     * Save a review.
     *
     * @param review the entity to save.
     * @return the persisted entity.
     */
    Review save(Review review);

    /**
     * Updates a review.
     *
     * @param review the entity to update.
     * @return the persisted entity.
     */
    Review update(Review review);

    /**
     * Partially updates a review.
     *
     * @param review the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Review> partialUpdate(Review review);

    /**
     * Get all the reviews.
     *
     * @return the list of entities.
     */
    List<Review> findAll();

    /**
     * Get all the reviews with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Review> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" review.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Review> findOne(Long id);

    /**
     * Delete the "id" review.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
