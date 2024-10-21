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
import xyz.jhmapstruct.domain.ReviewVi;
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.ReviewViService;
import xyz.jhmapstruct.service.dto.ReviewViDTO;
import xyz.jhmapstruct.service.mapper.ReviewViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
@Service
@Transactional
public class ReviewViServiceImpl implements ReviewViService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViServiceImpl.class);

    private final ReviewViRepository reviewViRepository;

    private final ReviewViMapper reviewViMapper;

    public ReviewViServiceImpl(ReviewViRepository reviewViRepository, ReviewViMapper reviewViMapper) {
        this.reviewViRepository = reviewViRepository;
        this.reviewViMapper = reviewViMapper;
    }

    @Override
    public ReviewViDTO save(ReviewViDTO reviewViDTO) {
        LOG.debug("Request to save ReviewVi : {}", reviewViDTO);
        ReviewVi reviewVi = reviewViMapper.toEntity(reviewViDTO);
        reviewVi = reviewViRepository.save(reviewVi);
        return reviewViMapper.toDto(reviewVi);
    }

    @Override
    public ReviewViDTO update(ReviewViDTO reviewViDTO) {
        LOG.debug("Request to update ReviewVi : {}", reviewViDTO);
        ReviewVi reviewVi = reviewViMapper.toEntity(reviewViDTO);
        reviewVi = reviewViRepository.save(reviewVi);
        return reviewViMapper.toDto(reviewVi);
    }

    @Override
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

    @Override
    @Transactional(readOnly = true)
    public List<ReviewViDTO> findAll() {
        LOG.debug("Request to get all ReviewVis");
        return reviewViRepository.findAll().stream().map(reviewViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ReviewViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewViRepository.findAllWithEagerRelationships(pageable).map(reviewViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewViDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewVi : {}", id);
        return reviewViRepository.findOneWithEagerRelationships(id).map(reviewViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewVi : {}", id);
        reviewViRepository.deleteById(id);
    }
}
