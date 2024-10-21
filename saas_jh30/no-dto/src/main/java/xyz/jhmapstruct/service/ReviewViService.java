package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ReviewVi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
public interface ReviewViService {
    /**
     * Save a reviewVi.
     *
     * @param reviewVi the entity to save.
     * @return the persisted entity.
     */
    ReviewVi save(ReviewVi reviewVi);

    /**
     * Updates a reviewVi.
     *
     * @param reviewVi the entity to update.
     * @return the persisted entity.
     */
    ReviewVi update(ReviewVi reviewVi);

    /**
     * Partially updates a reviewVi.
     *
     * @param reviewVi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewVi> partialUpdate(ReviewVi reviewVi);

    /**
     * Get all the reviewVis.
     *
     * @return the list of entities.
     */
    List<ReviewVi> findAll();

    /**
     * Get all the reviewVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewVi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewVi> findOne(Long id);

    /**
     * Delete the "id" reviewVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
