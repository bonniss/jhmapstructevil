package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewAlpha;
import xyz.jhmapstruct.repository.ReviewAlphaRepository;
import xyz.jhmapstruct.service.dto.ReviewAlphaDTO;
import xyz.jhmapstruct.service.mapper.ReviewAlphaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewAlpha}.
 */
@Service
@Transactional
public class ReviewAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewAlphaService.class);

    private final ReviewAlphaRepository reviewAlphaRepository;

    private final ReviewAlphaMapper reviewAlphaMapper;

    public ReviewAlphaService(ReviewAlphaRepository reviewAlphaRepository, ReviewAlphaMapper reviewAlphaMapper) {
        this.reviewAlphaRepository = reviewAlphaRepository;
        this.reviewAlphaMapper = reviewAlphaMapper;
    }

    /**
     * Save a reviewAlpha.
     *
     * @param reviewAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewAlphaDTO save(ReviewAlphaDTO reviewAlphaDTO) {
        LOG.debug("Request to save ReviewAlpha : {}", reviewAlphaDTO);
        ReviewAlpha reviewAlpha = reviewAlphaMapper.toEntity(reviewAlphaDTO);
        reviewAlpha = reviewAlphaRepository.save(reviewAlpha);
        return reviewAlphaMapper.toDto(reviewAlpha);
    }

    /**
     * Update a reviewAlpha.
     *
     * @param reviewAlphaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewAlphaDTO update(ReviewAlphaDTO reviewAlphaDTO) {
        LOG.debug("Request to update ReviewAlpha : {}", reviewAlphaDTO);
        ReviewAlpha reviewAlpha = reviewAlphaMapper.toEntity(reviewAlphaDTO);
        reviewAlpha = reviewAlphaRepository.save(reviewAlpha);
        return reviewAlphaMapper.toDto(reviewAlpha);
    }

    /**
     * Partially update a reviewAlpha.
     *
     * @param reviewAlphaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewAlphaDTO> partialUpdate(ReviewAlphaDTO reviewAlphaDTO) {
        LOG.debug("Request to partially update ReviewAlpha : {}", reviewAlphaDTO);

        return reviewAlphaRepository
            .findById(reviewAlphaDTO.getId())
            .map(existingReviewAlpha -> {
                reviewAlphaMapper.partialUpdate(existingReviewAlpha, reviewAlphaDTO);

                return existingReviewAlpha;
            })
            .map(reviewAlphaRepository::save)
            .map(reviewAlphaMapper::toDto);
    }

    /**
     * Get all the reviewAlphas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewAlphaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewAlphaRepository.findAllWithEagerRelationships(pageable).map(reviewAlphaMapper::toDto);
    }

    /**
     * Get one reviewAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewAlphaDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewAlpha : {}", id);
        return reviewAlphaRepository.findOneWithEagerRelationships(id).map(reviewAlphaMapper::toDto);
    }

    /**
     * Delete the reviewAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewAlpha : {}", id);
        reviewAlphaRepository.deleteById(id);
    }
}
