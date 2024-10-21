package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ReviewViViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
public interface ReviewViViService {
    /**
     * Save a reviewViVi.
     *
     * @param reviewViViDTO the entity to save.
     * @return the persisted entity.
     */
    ReviewViViDTO save(ReviewViViDTO reviewViViDTO);

    /**
     * Updates a reviewViVi.
     *
     * @param reviewViViDTO the entity to update.
     * @return the persisted entity.
     */
    ReviewViViDTO update(ReviewViViDTO reviewViViDTO);

    /**
     * Partially updates a reviewViVi.
     *
     * @param reviewViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewViViDTO> partialUpdate(ReviewViViDTO reviewViViDTO);

    /**
     * Get all the reviewViVis.
     *
     * @return the list of entities.
     */
    List<ReviewViViDTO> findAll();

    /**
     * Get all the reviewViVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewViViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewViVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewViViDTO> findOne(Long id);

    /**
     * Delete the "id" reviewViVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
