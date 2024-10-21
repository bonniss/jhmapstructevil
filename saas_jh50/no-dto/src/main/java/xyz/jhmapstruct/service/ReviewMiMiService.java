package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.domain.ReviewMiMi;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
public interface ReviewMiMiService {
    /**
     * Save a reviewMiMi.
     *
     * @param reviewMiMi the entity to save.
     * @return the persisted entity.
     */
    ReviewMiMi save(ReviewMiMi reviewMiMi);

    /**
     * Updates a reviewMiMi.
     *
     * @param reviewMiMi the entity to update.
     * @return the persisted entity.
     */
    ReviewMiMi update(ReviewMiMi reviewMiMi);

    /**
     * Partially updates a reviewMiMi.
     *
     * @param reviewMiMi the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewMiMi> partialUpdate(ReviewMiMi reviewMiMi);

    /**
     * Get all the reviewMiMis.
     *
     * @return the list of entities.
     */
    List<ReviewMiMi> findAll();

    /**
     * Get all the reviewMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewMiMi> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewMiMi> findOne(Long id);

    /**
     * Delete the "id" reviewMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
