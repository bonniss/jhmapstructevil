package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.repository.ReviewBetaRepository;
import xyz.jhmapstruct.service.dto.ReviewBetaDTO;
import xyz.jhmapstruct.service.mapper.ReviewBetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewBeta}.
 */
@Service
@Transactional
public class ReviewBetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewBetaService.class);

    private final ReviewBetaRepository reviewBetaRepository;

    private final ReviewBetaMapper reviewBetaMapper;

    public ReviewBetaService(ReviewBetaRepository reviewBetaRepository, ReviewBetaMapper reviewBetaMapper) {
        this.reviewBetaRepository = reviewBetaRepository;
        this.reviewBetaMapper = reviewBetaMapper;
    }

    /**
     * Save a reviewBeta.
     *
     * @param reviewBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewBetaDTO save(ReviewBetaDTO reviewBetaDTO) {
        LOG.debug("Request to save ReviewBeta : {}", reviewBetaDTO);
        ReviewBeta reviewBeta = reviewBetaMapper.toEntity(reviewBetaDTO);
        reviewBeta = reviewBetaRepository.save(reviewBeta);
        return reviewBetaMapper.toDto(reviewBeta);
    }

    /**
     * Update a reviewBeta.
     *
     * @param reviewBetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewBetaDTO update(ReviewBetaDTO reviewBetaDTO) {
        LOG.debug("Request to update ReviewBeta : {}", reviewBetaDTO);
        ReviewBeta reviewBeta = reviewBetaMapper.toEntity(reviewBetaDTO);
        reviewBeta = reviewBetaRepository.save(reviewBeta);
        return reviewBetaMapper.toDto(reviewBeta);
    }

    /**
     * Partially update a reviewBeta.
     *
     * @param reviewBetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewBetaDTO> partialUpdate(ReviewBetaDTO reviewBetaDTO) {
        LOG.debug("Request to partially update ReviewBeta : {}", reviewBetaDTO);

        return reviewBetaRepository
            .findById(reviewBetaDTO.getId())
            .map(existingReviewBeta -> {
                reviewBetaMapper.partialUpdate(existingReviewBeta, reviewBetaDTO);

                return existingReviewBeta;
            })
            .map(reviewBetaRepository::save)
            .map(reviewBetaMapper::toDto);
    }

    /**
     * Get all the reviewBetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewBetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewBetaRepository.findAllWithEagerRelationships(pageable).map(reviewBetaMapper::toDto);
    }

    /**
     * Get one reviewBeta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewBetaDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewBeta : {}", id);
        return reviewBetaRepository.findOneWithEagerRelationships(id).map(reviewBetaMapper::toDto);
    }

    /**
     * Delete the reviewBeta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewBeta : {}", id);
        reviewBetaRepository.deleteById(id);
    }
}
