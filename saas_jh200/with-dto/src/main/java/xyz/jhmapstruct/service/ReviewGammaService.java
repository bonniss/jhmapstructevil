package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.repository.ReviewGammaRepository;
import xyz.jhmapstruct.service.dto.ReviewGammaDTO;
import xyz.jhmapstruct.service.mapper.ReviewGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewGamma}.
 */
@Service
@Transactional
public class ReviewGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewGammaService.class);

    private final ReviewGammaRepository reviewGammaRepository;

    private final ReviewGammaMapper reviewGammaMapper;

    public ReviewGammaService(ReviewGammaRepository reviewGammaRepository, ReviewGammaMapper reviewGammaMapper) {
        this.reviewGammaRepository = reviewGammaRepository;
        this.reviewGammaMapper = reviewGammaMapper;
    }

    /**
     * Save a reviewGamma.
     *
     * @param reviewGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewGammaDTO save(ReviewGammaDTO reviewGammaDTO) {
        LOG.debug("Request to save ReviewGamma : {}", reviewGammaDTO);
        ReviewGamma reviewGamma = reviewGammaMapper.toEntity(reviewGammaDTO);
        reviewGamma = reviewGammaRepository.save(reviewGamma);
        return reviewGammaMapper.toDto(reviewGamma);
    }

    /**
     * Update a reviewGamma.
     *
     * @param reviewGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewGammaDTO update(ReviewGammaDTO reviewGammaDTO) {
        LOG.debug("Request to update ReviewGamma : {}", reviewGammaDTO);
        ReviewGamma reviewGamma = reviewGammaMapper.toEntity(reviewGammaDTO);
        reviewGamma = reviewGammaRepository.save(reviewGamma);
        return reviewGammaMapper.toDto(reviewGamma);
    }

    /**
     * Partially update a reviewGamma.
     *
     * @param reviewGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewGammaDTO> partialUpdate(ReviewGammaDTO reviewGammaDTO) {
        LOG.debug("Request to partially update ReviewGamma : {}", reviewGammaDTO);

        return reviewGammaRepository
            .findById(reviewGammaDTO.getId())
            .map(existingReviewGamma -> {
                reviewGammaMapper.partialUpdate(existingReviewGamma, reviewGammaDTO);

                return existingReviewGamma;
            })
            .map(reviewGammaRepository::save)
            .map(reviewGammaMapper::toDto);
    }

    /**
     * Get all the reviewGammas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewGammaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewGammaRepository.findAllWithEagerRelationships(pageable).map(reviewGammaMapper::toDto);
    }

    /**
     * Get one reviewGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewGammaDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewGamma : {}", id);
        return reviewGammaRepository.findOneWithEagerRelationships(id).map(reviewGammaMapper::toDto);
    }

    /**
     * Delete the reviewGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewGamma : {}", id);
        reviewGammaRepository.deleteById(id);
    }
}
