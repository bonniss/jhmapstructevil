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
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.ReviewMiMiService;
import xyz.jhmapstruct.service.dto.ReviewMiMiDTO;
import xyz.jhmapstruct.service.mapper.ReviewMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
@Service
@Transactional
public class ReviewMiMiServiceImpl implements ReviewMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiServiceImpl.class);

    private final ReviewMiMiRepository reviewMiMiRepository;

    private final ReviewMiMiMapper reviewMiMiMapper;

    public ReviewMiMiServiceImpl(ReviewMiMiRepository reviewMiMiRepository, ReviewMiMiMapper reviewMiMiMapper) {
        this.reviewMiMiRepository = reviewMiMiRepository;
        this.reviewMiMiMapper = reviewMiMiMapper;
    }

    @Override
    public ReviewMiMiDTO save(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to save ReviewMiMi : {}", reviewMiMiDTO);
        ReviewMiMi reviewMiMi = reviewMiMiMapper.toEntity(reviewMiMiDTO);
        reviewMiMi = reviewMiMiRepository.save(reviewMiMi);
        return reviewMiMiMapper.toDto(reviewMiMi);
    }

    @Override
    public ReviewMiMiDTO update(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to update ReviewMiMi : {}", reviewMiMiDTO);
        ReviewMiMi reviewMiMi = reviewMiMiMapper.toEntity(reviewMiMiDTO);
        reviewMiMi = reviewMiMiRepository.save(reviewMiMi);
        return reviewMiMiMapper.toDto(reviewMiMi);
    }

    @Override
    public Optional<ReviewMiMiDTO> partialUpdate(ReviewMiMiDTO reviewMiMiDTO) {
        LOG.debug("Request to partially update ReviewMiMi : {}", reviewMiMiDTO);

        return reviewMiMiRepository
            .findById(reviewMiMiDTO.getId())
            .map(existingReviewMiMi -> {
                reviewMiMiMapper.partialUpdate(existingReviewMiMi, reviewMiMiDTO);

                return existingReviewMiMi;
            })
            .map(reviewMiMiRepository::save)
            .map(reviewMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewMiMiDTO> findAll() {
        LOG.debug("Request to get all ReviewMiMis");
        return reviewMiMiRepository.findAll().stream().map(reviewMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<ReviewMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return reviewMiMiRepository.findAllWithEagerRelationships(pageable).map(reviewMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReviewMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get ReviewMiMi : {}", id);
        return reviewMiMiRepository.findOneWithEagerRelationships(id).map(reviewMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ReviewMiMi : {}", id);
        reviewMiMiRepository.deleteById(id);
    }
}
