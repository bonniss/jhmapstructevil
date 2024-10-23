package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.repository.NextReviewRepository;
import xyz.jhmapstruct.service.dto.NextReviewDTO;
import xyz.jhmapstruct.service.mapper.NextReviewMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextReview}.
 */
@Service
@Transactional
public class NextReviewService {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewService.class);

    private final NextReviewRepository nextReviewRepository;

    private final NextReviewMapper nextReviewMapper;

    public NextReviewService(NextReviewRepository nextReviewRepository, NextReviewMapper nextReviewMapper) {
        this.nextReviewRepository = nextReviewRepository;
        this.nextReviewMapper = nextReviewMapper;
    }

    /**
     * Save a nextReview.
     *
     * @param nextReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewDTO save(NextReviewDTO nextReviewDTO) {
        LOG.debug("Request to save NextReview : {}", nextReviewDTO);
        NextReview nextReview = nextReviewMapper.toEntity(nextReviewDTO);
        nextReview = nextReviewRepository.save(nextReview);
        return nextReviewMapper.toDto(nextReview);
    }

    /**
     * Update a nextReview.
     *
     * @param nextReviewDTO the entity to save.
     * @return the persisted entity.
     */
    public NextReviewDTO update(NextReviewDTO nextReviewDTO) {
        LOG.debug("Request to update NextReview : {}", nextReviewDTO);
        NextReview nextReview = nextReviewMapper.toEntity(nextReviewDTO);
        nextReview = nextReviewRepository.save(nextReview);
        return nextReviewMapper.toDto(nextReview);
    }

    /**
     * Partially update a nextReview.
     *
     * @param nextReviewDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextReviewDTO> partialUpdate(NextReviewDTO nextReviewDTO) {
        LOG.debug("Request to partially update NextReview : {}", nextReviewDTO);

        return nextReviewRepository
            .findById(nextReviewDTO.getId())
            .map(existingNextReview -> {
                nextReviewMapper.partialUpdate(existingNextReview, nextReviewDTO);

                return existingNextReview;
            })
            .map(nextReviewRepository::save)
            .map(nextReviewMapper::toDto);
    }

    /**
     * Get all the nextReviews with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NextReviewDTO> findAllWithEagerRelationships(Pageable pageable) {
        return nextReviewRepository.findAllWithEagerRelationships(pageable).map(nextReviewMapper::toDto);
    }

    /**
     * Get one nextReview by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextReviewDTO> findOne(Long id) {
        LOG.debug("Request to get NextReview : {}", id);
        return nextReviewRepository.findOneWithEagerRelationships(id).map(nextReviewMapper::toDto);
    }

    /**
     * Delete the nextReview by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextReview : {}", id);
        nextReviewRepository.deleteById(id);
    }
}
