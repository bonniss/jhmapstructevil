package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.repository.ReviewThetaRepository;
import xyz.jhmapstruct.service.dto.ReviewThetaDTO;
import xyz.jhmapstruct.service.mapper.ReviewThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewTheta}.
 */
@Service
@Transactional
public class ReviewThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewThetaService.class);

    private final ReviewThetaRepository reviewThetaRepository;

    private final ReviewThetaMapper reviewThetaMapper;

    public ReviewThetaService(ReviewThetaRepository reviewThetaRepository, ReviewThetaMapper reviewThetaMapper) {
        this.reviewThetaRepository = reviewThetaRepository;
        this.reviewThetaMapper = reviewThetaMapper;
    }

    /**
     * Save a reviewTheta.
     *
     * @param reviewThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewThetaDTO save(ReviewThetaDTO reviewThetaDTO) {
        LOG.debug("Request to save ReviewTheta : {}", reviewThetaDTO);
        ReviewTheta reviewTheta = reviewThetaMapper.toEntity(reviewThetaDTO);
        reviewTheta = reviewThetaRepository.save(reviewTheta);
        return reviewThetaMapper.toDto(reviewTheta);
    }

    /**
     * Update a reviewTheta.
     *
     * @param reviewThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewThetaDTO update(ReviewThetaDTO reviewThetaDTO) {
        LOG.debug("Request to update ReviewTheta : {}", reviewThetaDTO);
        ReviewTheta reviewTheta = reviewThetaMapper.toEntity(reviewThetaDTO);
        reviewTheta = reviewThetaRepository.save(reviewTheta);
        return reviewThetaMapper.toDto(reviewTheta);
    }

    /**
     * Partially update a reviewTheta.
     *
     * @param reviewThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewThetaDTO> partialUpdate(ReviewThetaDTO reviewThetaDTO) {
        LOG.debug("Request to partially update ReviewTheta : {}", reviewThetaDTO);

        return reviewThetaRepository
            .findById(reviewThetaDTO.getId())
            .map(existingReviewTheta -> {
                reviewThetaMapper.partialUpdate(existingReviewTheta, reviewThetaDTO);

                return existingReviewTheta;
            })
            .map(reviewThetaRepository::save)
            .map(reviewThetaMapper::toDto);
    }

    /**
     * Get all the reviewThetas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewThetaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewThetaRepository.findAllWithEagerRelationships(pageable).map(reviewThetaMapper::toDto);
    }

    /**
     * Get one reviewTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewThetaDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewTheta : {}", id);
        return reviewThetaRepository.findOneWithEagerRelationships(id).map(reviewThetaMapper::toDto);
    }

    /**
     * Delete the reviewTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewTheta : {}", id);
        reviewThetaRepository.deleteById(id);
    }
}
