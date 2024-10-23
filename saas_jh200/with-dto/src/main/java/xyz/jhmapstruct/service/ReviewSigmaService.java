package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.repository.ReviewSigmaRepository;
import xyz.jhmapstruct.service.dto.ReviewSigmaDTO;
import xyz.jhmapstruct.service.mapper.ReviewSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewSigma}.
 */
@Service
@Transactional
public class ReviewSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewSigmaService.class);

    private final ReviewSigmaRepository reviewSigmaRepository;

    private final ReviewSigmaMapper reviewSigmaMapper;

    public ReviewSigmaService(ReviewSigmaRepository reviewSigmaRepository, ReviewSigmaMapper reviewSigmaMapper) {
        this.reviewSigmaRepository = reviewSigmaRepository;
        this.reviewSigmaMapper = reviewSigmaMapper;
    }

    /**
     * Save a reviewSigma.
     *
     * @param reviewSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewSigmaDTO save(ReviewSigmaDTO reviewSigmaDTO) {
        LOG.debug("Request to save ReviewSigma : {}", reviewSigmaDTO);
        ReviewSigma reviewSigma = reviewSigmaMapper.toEntity(reviewSigmaDTO);
        reviewSigma = reviewSigmaRepository.save(reviewSigma);
        return reviewSigmaMapper.toDto(reviewSigma);
    }

    /**
     * Update a reviewSigma.
     *
     * @param reviewSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewSigmaDTO update(ReviewSigmaDTO reviewSigmaDTO) {
        LOG.debug("Request to update ReviewSigma : {}", reviewSigmaDTO);
        ReviewSigma reviewSigma = reviewSigmaMapper.toEntity(reviewSigmaDTO);
        reviewSigma = reviewSigmaRepository.save(reviewSigma);
        return reviewSigmaMapper.toDto(reviewSigma);
    }

    /**
     * Partially update a reviewSigma.
     *
     * @param reviewSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewSigmaDTO> partialUpdate(ReviewSigmaDTO reviewSigmaDTO) {
        LOG.debug("Request to partially update ReviewSigma : {}", reviewSigmaDTO);

        return reviewSigmaRepository
            .findById(reviewSigmaDTO.getId())
            .map(existingReviewSigma -> {
                reviewSigmaMapper.partialUpdate(existingReviewSigma, reviewSigmaDTO);

                return existingReviewSigma;
            })
            .map(reviewSigmaRepository::save)
            .map(reviewSigmaMapper::toDto);
    }

    /**
     * Get all the reviewSigmas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewSigmaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewSigmaRepository.findAllWithEagerRelationships(pageable).map(reviewSigmaMapper::toDto);
    }

    /**
     * Get one reviewSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewSigma : {}", id);
        return reviewSigmaRepository.findOneWithEagerRelationships(id).map(reviewSigmaMapper::toDto);
    }

    /**
     * Delete the reviewSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewSigma : {}", id);
        reviewSigmaRepository.deleteById(id);
    }
}
