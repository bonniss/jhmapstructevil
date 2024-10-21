package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.ReviewViVi;
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.ReviewViViService;
import xyz.jhmapstruct.service.dto.ReviewViViDTO;
import xyz.jhmapstruct.service.mapper.ReviewViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
@Service
@Transactional
public class ReviewViViServiceImpl implements ReviewViViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViServiceImpl.class);

    private final ReviewViViRepository reviewViViRepository;

    private final ReviewViViMapper reviewViViMapper;

    public ReviewViViServiceImpl(ReviewViViRepository reviewViViRepository, ReviewViViMapper reviewViViMapper) {
        this.reviewViViRepository = reviewViViRepository;
        this.reviewViViMapper = reviewViViMapper;
    }

    @Override
    public ReviewViViDTO save(ReviewViViDTO reviewViViDTO) {
        LOG.debug("Request to save ReviewViVi : {}", reviewViViDTO);
        ReviewViVi reviewViVi = reviewViViMapper.toEntity(reviewViViDTO);
        reviewViVi = reviewViViRepository.save(reviewViVi);
        return reviewViViMapper.toDto(reviewViVi);
    }

    @Override
    public ReviewViViDTO update(ReviewViViDTO reviewViViDTO) {
        LOG.debug("Request to update ReviewViVi : {}", reviewViViDTO);
        ReviewViVi reviewViVi = reviewViViMapper.toEntity(reviewViViDTO);
        reviewViVi = reviewViViRepository.save(reviewViVi);
        return reviewViViMapper.toDto(reviewViVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ReviewViViDTO> findAll() {
        LOG.debug("Request to get all ReviewViVis");
        return reviewViViRepository.findAll().stream().map(reviewViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ReviewViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViViRepository.findAllWithEagerRelationships(pageable).map(reviewViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewViViDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewViVi : {}", id);
        return reviewViViRepository.findOneWithEagerRelationships(id).map(reviewViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewViVi : {}", id);
        reviewViViRepository.deleteById(id);
    }
}
