package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.dto.ReviewMiMiDTO;
import xyz.jhmapstruct.service.mapper.ReviewMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
@Service
@Transactional
public class ReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiService.class);

    private final ReviewMiMiRepository reviewMiMiRepository;

    private final ReviewMiMiMapper reviewMiMiMapper;

    public ReviewMiMiService(ReviewMiMiRepository reviewMiMiRepository, ReviewMiMiMapper reviewMiMiMapper) {
        this.reviewMiMiRepository = reviewMiMiRepository;
        this.reviewMiMiMapper = reviewMiMiMapper;
    }

    /**
     * Save a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiMiDTO save(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to save ReviewMiMi : {}", reviewMiMiDTO);
        ReviewMiMi reviewMiMi = reviewMiMiMapper.toEntity(reviewMiMiDTO);
        reviewMiMi = reviewMiMiRepository.save(reviewMiMi);
        return reviewMiMiMapper.toDto(reviewMiMi);
    }

    /**
     * Update a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewMiMiDTO update(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to update ReviewMiMi : {}", reviewMiMiDTO);
        ReviewMiMi reviewMiMi = reviewMiMiMapper.toEntity(reviewMiMiDTO);
        reviewMiMi = reviewMiMiRepository.save(reviewMiMi);
        return reviewMiMiMapper.toDto(reviewMiMi);
    }

    /**
     * Partially update a reviewMiMi.
     *
     * @param reviewMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewMiMiDTO> partialUpdate(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to partially update ReviewMiMi : {}", reviewMiMiDTO);

        return reviewMiMiRepository
            .findById(reviewMiMiDTO.getId())
            .map(existingReviewMiMi -> {
                reviewMiMiMapper.partialUpdate(existingReviewMiMi, reviewMiMiDTO);

                return existingReviewMiMi;
            })
            .map(reviewMiMiRepository::save)
            .map(reviewMiMiMapper::toDto);
    }

    /**
     * Get all the reviewMiMis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiMiRepository.findAllWithEagerRelationships(pageable).map(reviewMiMiMapper::toDto);
    }

    /**
     * Get one reviewMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewMiMi : {}", id);
        return reviewMiMiRepository.findOneWithEagerRelationships(id).map(reviewMiMiMapper::toDto);
    }

    /**
     * Delete the reviewMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMiMi : {}", id);
        reviewMiMiRepository.deleteById(id);
    }
}
