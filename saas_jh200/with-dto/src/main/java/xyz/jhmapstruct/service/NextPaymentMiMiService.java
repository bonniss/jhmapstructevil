package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentMiMi;
import xyz.jhmapstruct.repository.NextPaymentMiMiRepository;
import xyz.jhmapstruct.service.dto.NextPaymentMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentMiMi}.
 */
@Service
@Transactional
public class NextPaymentMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiMiService.class);

    private final NextPaymentMiMiRepository nextPaymentMiMiRepository;

    private final NextPaymentMiMiMapper nextPaymentMiMiMapper;

    public NextPaymentMiMiService(NextPaymentMiMiRepository nextPaymentMiMiRepository, NextPaymentMiMiMapper nextPaymentMiMiMapper) {
        this.nextPaymentMiMiRepository = nextPaymentMiMiRepository;
        this.nextPaymentMiMiMapper = nextPaymentMiMiMapper;
    }

    /**
     * Save a nextPaymentMiMi.
     *
     * @param nextPaymentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiMiDTO save(NextPaymentMiMiDTO nextPaymentMiMiDTO) {
        LOG.debug("Request to save NextPaymentMiMi : {}", nextPaymentMiMiDTO);
        NextPaymentMiMi nextPaymentMiMi = nextPaymentMiMiMapper.toEntity(nextPaymentMiMiDTO);
        nextPaymentMiMi = nextPaymentMiMiRepository.save(nextPaymentMiMi);
        return nextPaymentMiMiMapper.toDto(nextPaymentMiMi);
    }

    /**
     * Update a nextPaymentMiMi.
     *
     * @param nextPaymentMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentMiMiDTO update(NextPaymentMiMiDTO nextPaymentMiMiDTO) {
        LOG.debug("Request to update NextPaymentMiMi : {}", nextPaymentMiMiDTO);
        NextPaymentMiMi nextPaymentMiMi = nextPaymentMiMiMapper.toEntity(nextPaymentMiMiDTO);
        nextPaymentMiMi = nextPaymentMiMiRepository.save(nextPaymentMiMi);
        return nextPaymentMiMiMapper.toDto(nextPaymentMiMi);
    }

    /**
     * Partially update a nextPaymentMiMi.
     *
     * @param nextPaymentMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentMiMiDTO> partialUpdate(NextPaymentMiMiDTO nextPaymentMiMiDTO) {
        LOG.debug("Request to partially update NextPaymentMiMi : {}", nextPaymentMiMiDTO);

        return nextPaymentMiMiRepository
            .findById(nextPaymentMiMiDTO.getId())
            .map(existingNextPaymentMiMi -> {
                nextPaymentMiMiMapper.partialUpdate(existingNextPaymentMiMi, nextPaymentMiMiDTO);

                return existingNextPaymentMiMi;
            })
            .map(nextPaymentMiMiRepository::save)
            .map(nextPaymentMiMiMapper::toDto);
    }

    /**
     * Get one nextPaymentMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentMiMi : {}", id);
        return nextPaymentMiMiRepository.findById(id).map(nextPaymentMiMiMapper::toDto);
    }

    /**
     * Delete the nextPaymentMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentMiMi : {}", id);
        nextPaymentMiMiRepository.deleteById(id);
    }
}
