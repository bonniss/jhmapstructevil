package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ReviewViDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
public interface ReviewViService {
    /**
     * Save a reviewVi.
     *
     * @param reviewViDTO the entity to save.
     * @return the persisted entity.
     */
    ReviewViDTO save(ReviewViDTO reviewViDTO);

    /**
     * Updates a reviewVi.
     *
     * @param reviewViDTO the entity to update.
     * @return the persisted entity.
     */
    ReviewViDTO update(ReviewViDTO reviewViDTO);

    /**
     * Partially updates a reviewVi.
     *
     * @param reviewViDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewViDTO> partialUpdate(ReviewViDTO reviewViDTO);

    /**
     * Get all the reviewVis.
     *
     * @return the list of entities.
     */
    List<ReviewViDTO> findAll();

    /**
     * Get all the reviewVis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewViDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewVi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewViDTO> findOne(Long id);

    /**
     * Delete the "id" reviewVi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
