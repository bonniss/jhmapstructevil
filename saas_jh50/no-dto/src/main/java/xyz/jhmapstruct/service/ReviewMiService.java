package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ReviewMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
public interface ReviewMiService {
    /**
     * Save a reviewMi.
     *
     * @param reviewMi the entity to save.
     * @return the persisted entity.
     */
    ReviewMi save(ReviewMi reviewMi);

    /**
     * Updates a reviewMi.
     *
     * @param reviewMi the entity to update.
     * @return the persisted entity.
     */
    ReviewMi update(ReviewMi reviewMi);

    /**
     * Partially updates a reviewMi.
     *
     * @param reviewMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewMi> partialUpdate(ReviewMi reviewMi);

    /**
     * Get all the reviewMis.
     *
     * @return the list of entities.
     */
    List<ReviewMi> findAll();

    /**
     * Get all the reviewMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewMi> findOne(Long id);

    /**
     * Delete the "id" reviewMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
