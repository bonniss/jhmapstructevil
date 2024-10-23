package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.dto.ReviewViViDTO;
import xyz.jhmapstruct.service.mapper.ReviewViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
@Service
@Transactional
public class ReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViService.class);

    private final ReviewViViRepository reviewViViRepository;

    private final ReviewViViMapper reviewViViMapper;

    public ReviewViViService(ReviewViViRepository reviewViViRepository, ReviewViViMapper reviewViViMapper) {
        this.reviewViViRepository = reviewViViRepository;
        this.reviewViViMapper = reviewViViMapper;
    }

    /**
     * Save a reviewViVi.
     *
     * @param reviewViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewViViDTO save(ReviewViViDTO reviewViViDTO) {
        LOG.debug("Request to save ReviewViVi : {}", reviewViViDTO);
        ReviewViVi reviewViVi = reviewViViMapper.toEntity(reviewViViDTO);
        reviewViVi = reviewViViRepository.save(reviewViVi);
        return reviewViViMapper.toDto(reviewViVi);
    }

    /**
     * Update a reviewViVi.
     *
     * @param reviewViViDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewViViDTO update(ReviewViViDTO reviewViViDTO) {
        LOG.debug("Request to update ReviewViVi : {}", reviewViViDTO);
        ReviewViVi reviewViVi = reviewViViMapper.toEntity(reviewViViDTO);
        reviewViVi = reviewViViRepository.save(reviewViVi);
        return reviewViViMapper.toDto(reviewViVi);
    }

    /**
     * Partially update a reviewViVi.
     *
     * @param reviewViViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewViViDTO> partialUpdate(ReviewViViDTO reviewViViDTO) {
        LOG.debug("Request to partially update ReviewViVi : {}", reviewViViDTO);

        return reviewViViRepository
            .findById(reviewViViDTO.getId())
            .map(existingReviewViVi -> {
                reviewViViMapper.partialUpdate(existingReviewViVi, reviewViViDTO);

                return existingReviewViVi;
            })
            .map(reviewViViRepository::save)
            .map(reviewViViMapper::toDto);
    }

    /**
     * Get all the reviewViVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViViRepository.findAllWithEagerRelationships(pageable).map(reviewViViMapper::toDto);
    }

    /**
     * Get one reviewViVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewViViDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewViVi : {}", id);
        return reviewViViRepository.findOneWithEagerRelationships(id).map(reviewViViMapper::toDto);
    }

    /**
     * Delete the reviewViVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewViVi : {}", id);
        reviewViViRepository.deleteById(id);
    }
}
