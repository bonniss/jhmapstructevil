package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;
import xyz.jhmapstruct.service.mapper.ReviewMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
@Service
@Transactional
public class ReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiService.class);

    private final ReviewMiRepository reviewMiRepository;

    private final ReviewMiMapper reviewMiMapper;

    public ReviewMiService(ReviewMiRepository reviewMiRepository, ReviewMiMapper reviewMiMapper) {
        this.reviewMiRepository = reviewMiRepository;
        this.reviewMiMapper = reviewMiMapper;
    }

    /**
     * Save a reviewMi.
     *
     * @param reviewMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiDTO save(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to save ReviewMi : {}", reviewMiDTO);
        ReviewMi reviewMi = reviewMiMapper.toEntity(reviewMiDTO);
        reviewMi = reviewMiRepository.save(reviewMi);
        return reviewMiMapper.toDto(reviewMi);
    }

    /**
     * Update a reviewMi.
     *
     * @param reviewMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiDTO update(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to update ReviewMi : {}", reviewMiDTO);
        ReviewMi reviewMi = reviewMiMapper.toEntity(reviewMiDTO);
        reviewMi = reviewMiRepository.save(reviewMi);
        return reviewMiMapper.toDto(reviewMi);
    }

    /**
     * Partially update a reviewMi.
     *
     * @param reviewMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewMiDTO> partialUpdate(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to partially update ReviewMi : {}", reviewMiDTO);

        return reviewMiRepository
            .findById(reviewMiDTO.getId())
            .map(existingReviewMi -> {
                reviewMiMapper.partialUpdate(existingReviewMi, reviewMiDTO);

                return existingReviewMi;
            })
            .map(reviewMiRepository::save)
            .map(reviewMiMapper::toDto);
    }

    /**
     * Get all the reviewMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiRepository.findAllWithEagerRelationships(pageable).map(reviewMiMapper::toDto);
    }

    /**
     * Get one reviewMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewMiDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewMi : {}", id);
        return reviewMiRepository.findOneWithEagerRelationships(id).map(reviewMiMapper::toDto);
    }

    /**
     * Delete the reviewMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMi : {}", id);
        reviewMiRepository.deleteById(id);
    }
}
