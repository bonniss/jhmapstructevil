package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.dto.ReviewViDTO;
import xyz.jhmapstruct.service.mapper.ReviewViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
@Service
@Transactional
public class ReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViService.class);

    private final ReviewViRepository reviewViRepository;

    private final ReviewViMapper reviewViMapper;

    public ReviewViService(ReviewViRepository reviewViRepository, ReviewViMapper reviewViMapper) {
        this.reviewViRepository = reviewViRepository;
        this.reviewViMapper = reviewViMapper;
    }

    /**
     * Save a reviewVi.
     *
     * @param reviewViDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewViDTO save(ReviewViDTO reviewViDTO) {
        LOG.debug("Request to save ReviewVi : {}", reviewViDTO);
        ReviewVi reviewVi = reviewViMapper.toEntity(reviewViDTO);
        reviewVi = reviewViRepository.save(reviewVi);
        return reviewViMapper.toDto(reviewVi);
    }

    /**
     * Update a reviewVi.
     *
     * @param reviewViDTO the entity to save.
     * @return the persisted entity.
     */
    public ReviewViDTO update(ReviewViDTO reviewViDTO) {
        LOG.debug("Request to update ReviewVi : {}", reviewViDTO);
        ReviewVi reviewVi = reviewViMapper.toEntity(reviewViDTO);
        reviewVi = reviewViRepository.save(reviewVi);
        return reviewViMapper.toDto(reviewVi);
    }

    /**
     * Partially update a reviewVi.
     *
     * @param reviewViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReviewViDTO> partialUpdate(ReviewViDTO reviewViDTO) {
        LOG.debug("Request to partially update ReviewVi : {}", reviewViDTO);

        return reviewViRepository
            .findById(reviewViDTO.getId())
            .map(existingReviewVi -> {
                reviewViMapper.partialUpdate(existingReviewVi, reviewViDTO);

                return existingReviewVi;
            })
            .map(reviewViRepository::save)
            .map(reviewViMapper::toDto);
    }

    /**
     * Get all the reviewVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ReviewViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViRepository.findAllWithEagerRelationships(pageable).map(reviewViMapper::toDto);
    }

    /**
     * Get one reviewVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReviewViDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewVi : {}", id);
        return reviewViRepository.findOneWithEagerRelationships(id).map(reviewViMapper::toDto);
    }

    /**
     * Delete the reviewVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewVi : {}", id);
        reviewViRepository.deleteById(id);
    }
}
