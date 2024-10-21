package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ReviewViVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
public interface ReviewViViService {
    /**
     * Save a reviewViVi.
     *
     * @param reviewViVi the entity to save.
     * @return the persisted entity.
     */
    ReviewViVi save(ReviewViVi reviewViVi);

    /**
     * Updates a reviewViVi.
     *
     * @param reviewViVi the entity to update.
     * @return the persisted entity.
     */
    ReviewViVi update(ReviewViVi reviewViVi);

    /**
     * Partially updates a reviewViVi.
     *
     * @param reviewViVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewViVi> partialUpdate(ReviewViVi reviewViVi);

    /**
     * Get all the reviewViVis.
     *
     * @return the list of entities.
     */
    List<ReviewViVi> findAll();

    /**
     * Get all the reviewViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewViVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewViVi> findOne(Long id);

    /**
     * Delete the "id" reviewViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
