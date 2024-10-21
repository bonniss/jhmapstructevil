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
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.ReviewMiService;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;
import xyz.jhmapstruct.service.mapper.ReviewMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
@Service
@Transactional
public class ReviewMiServiceImpl implements ReviewMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiServiceImpl.class);

    private final ReviewMiRepository reviewMiRepository;

    private final ReviewMiMapper reviewMiMapper;

    public ReviewMiServiceImpl(ReviewMiRepository reviewMiRepository, ReviewMiMapper reviewMiMapper) {
        this.reviewMiRepository = reviewMiRepository;
        this.reviewMiMapper = reviewMiMapper;
    }

    @Override
    public ReviewMiDTO save(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to save ReviewMi : {}", reviewMiDTO);
        ReviewMi reviewMi = reviewMiMapper.toEntity(reviewMiDTO);
        reviewMi = reviewMiRepository.save(reviewMi);
        return reviewMiMapper.toDto(reviewMi);
    }

    @Override
    public ReviewMiDTO update(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to update ReviewMi : {}", reviewMiDTO);
        ReviewMi reviewMi = reviewMiMapper.toEntity(reviewMiDTO);
        reviewMi = reviewMiRepository.save(reviewMi);
        return reviewMiMapper.toDto(reviewMi);
    }

    @Override
    public Optional<ReviewMiDTO> partialUpdate(ReviewMiDTO reviewMiDTO) {
        LOG.debug("Request to partially update ReviewMi : {}", reviewMiDTO);

        return reviewMiRepository
            .findById(reviewMiDTO.getId())
            .map(existingReviewMi -> {
                reviewMiMapper.partialUpdate(existingReviewMi, reviewMiDTO);

                return existingReviewMi;
            })
            .map(reviewMiRepository::save)
            .map(reviewMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewMiDTO> findAll() {
        LOG.debug("Request to get all ReviewMis");
        return reviewMiRepository.findAll().stream().map(reviewMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ReviewMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiRepository.findAllWithEagerRelationships(pageable).map(reviewMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewMiDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewMi : {}", id);
        return reviewMiRepository.findOneWithEagerRelationships(id).map(reviewMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMi : {}", id);
        reviewMiRepository.deleteById(id);
    }
}
