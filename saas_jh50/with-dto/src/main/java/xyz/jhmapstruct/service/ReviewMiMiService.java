package xyz.jhmapstruct.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import xyz.jhmapstruct.service.dto.ReviewMiMiDTO;

/**
 * Service Interface for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
public interface ReviewMiMiService {
    /**
     * Save a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    ReviewMiMiDTO save(ReviewMiMiDTO reviewMiMiDTO);

    /**
     * Updates a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to update.
     * @return the persisted entity.
     */
    ReviewMiMiDTO update(ReviewMiMiDTO reviewMiMiDTO);

    /**
     * Partially updates a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReviewMiMiDTO> partialUpdate(ReviewMiMiDTO reviewMiMiDTO);

    /**
     * Get all the reviewMiMis.
     *
     * @return the list of entities.
     */
    List<ReviewMiMiDTO> findAll();

    /**
     * Get all the reviewMiMis with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReviewMiMiDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" reviewMiMi.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReviewMiMiDTO> findOne(Long id);

    /**
     * Delete the "id" reviewMiMi.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
