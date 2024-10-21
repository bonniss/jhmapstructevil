package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
public interface ReviewMiService {
    /**
     * Save a reviewMi.
     *
     * @param reviewMiDTO the entity to save.
     * @return the persisted entity.
     */
    ReviewMiDTO save(ReviewMiDTO reviewMiDTO);

    /**
     * Updates a reviewMi.
     *
     * @param reviewMiDTO the entity to update.
     * @return the persisted entity.
     */
    ReviewMiDTO update(ReviewMiDTO reviewMiDTO);

    /**
     * Partially updates a reviewMi.
     *
     * @param reviewMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewMiDTO> partialUpdate(ReviewMiDTO reviewMiDTO);

    /**
     * Get all the reviewMis.
     *
     * @return the list of entities.
     */
    List<ReviewMiDTO> findAll();

    /**
     * Get all the reviewMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewMiDTO> findOne(Long id);

    /**
     * Delete the "id" reviewMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
