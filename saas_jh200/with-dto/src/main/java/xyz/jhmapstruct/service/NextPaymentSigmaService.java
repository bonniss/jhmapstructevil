package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPaymentSigma;
import xyz.jhmapstruct.repository.NextPaymentSigmaRepository;
import xyz.jhmapstruct.service.dto.NextPaymentSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPaymentSigma}.
 */
@Service
@Transactional
public class NextPaymentSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentSigmaService.class);

    private final NextPaymentSigmaRepository nextPaymentSigmaRepository;

    private final NextPaymentSigmaMapper nextPaymentSigmaMapper;

    public NextPaymentSigmaService(NextPaymentSigmaRepository nextPaymentSigmaRepository, NextPaymentSigmaMapper nextPaymentSigmaMapper) {
        this.nextPaymentSigmaRepository = nextPaymentSigmaRepository;
        this.nextPaymentSigmaMapper = nextPaymentSigmaMapper;
    }

    /**
     * Save a nextPaymentSigma.
     *
     * @param nextPaymentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentSigmaDTO save(NextPaymentSigmaDTO nextPaymentSigmaDTO) {
        LOG.debug("Request to save NextPaymentSigma : {}", nextPaymentSigmaDTO);
        NextPaymentSigma nextPaymentSigma = nextPaymentSigmaMapper.toEntity(nextPaymentSigmaDTO);
        nextPaymentSigma = nextPaymentSigmaRepository.save(nextPaymentSigma);
        return nextPaymentSigmaMapper.toDto(nextPaymentSigma);
    }

    /**
     * Update a nextPaymentSigma.
     *
     * @param nextPaymentSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentSigmaDTO update(NextPaymentSigmaDTO nextPaymentSigmaDTO) {
        LOG.debug("Request to update NextPaymentSigma : {}", nextPaymentSigmaDTO);
        NextPaymentSigma nextPaymentSigma = nextPaymentSigmaMapper.toEntity(nextPaymentSigmaDTO);
        nextPaymentSigma = nextPaymentSigmaRepository.save(nextPaymentSigma);
        return nextPaymentSigmaMapper.toDto(nextPaymentSigma);
    }

    /**
     * Partially update a nextPaymentSigma.
     *
     * @param nextPaymentSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentSigmaDTO> partialUpdate(NextPaymentSigmaDTO nextPaymentSigmaDTO) {
        LOG.debug("Request to partially update NextPaymentSigma : {}", nextPaymentSigmaDTO);

        return nextPaymentSigmaRepository
            .findById(nextPaymentSigmaDTO.getId())
            .map(existingNextPaymentSigma -> {
                nextPaymentSigmaMapper.partialUpdate(existingNextPaymentSigma, nextPaymentSigmaDTO);

                return existingNextPaymentSigma;
            })
            .map(nextPaymentSigmaRepository::save)
            .map(nextPaymentSigmaMapper::toDto);
    }

    /**
     * Get one nextPaymentSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextPaymentSigma : {}", id);
        return nextPaymentSigmaRepository.findById(id).map(nextPaymentSigmaMapper::toDto);
    }

    /**
     * Delete the nextPaymentSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPaymentSigma : {}", id);
        nextPaymentSigmaRepository.deleteById(id);
    }
}
