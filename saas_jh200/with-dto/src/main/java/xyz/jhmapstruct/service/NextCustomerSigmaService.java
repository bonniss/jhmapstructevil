package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.repository.NextCustomerSigmaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerSigma}.
 */
@Service
@Transactional
public class NextCustomerSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerSigmaService.class);

    private final NextCustomerSigmaRepository nextCustomerSigmaRepository;

    private final NextCustomerSigmaMapper nextCustomerSigmaMapper;

    public NextCustomerSigmaService(
        NextCustomerSigmaRepository nextCustomerSigmaRepository,
        NextCustomerSigmaMapper nextCustomerSigmaMapper
    ) {
        this.nextCustomerSigmaRepository = nextCustomerSigmaRepository;
        this.nextCustomerSigmaMapper = nextCustomerSigmaMapper;
    }

    /**
     * Save a nextCustomerSigma.
     *
     * @param nextCustomerSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerSigmaDTO save(NextCustomerSigmaDTO nextCustomerSigmaDTO) {
        LOG.debug("Request to save NextCustomerSigma : {}", nextCustomerSigmaDTO);
        NextCustomerSigma nextCustomerSigma = nextCustomerSigmaMapper.toEntity(nextCustomerSigmaDTO);
        nextCustomerSigma = nextCustomerSigmaRepository.save(nextCustomerSigma);
        return nextCustomerSigmaMapper.toDto(nextCustomerSigma);
    }

    /**
     * Update a nextCustomerSigma.
     *
     * @param nextCustomerSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerSigmaDTO update(NextCustomerSigmaDTO nextCustomerSigmaDTO) {
        LOG.debug("Request to update NextCustomerSigma : {}", nextCustomerSigmaDTO);
        NextCustomerSigma nextCustomerSigma = nextCustomerSigmaMapper.toEntity(nextCustomerSigmaDTO);
        nextCustomerSigma = nextCustomerSigmaRepository.save(nextCustomerSigma);
        return nextCustomerSigmaMapper.toDto(nextCustomerSigma);
    }

    /**
     * Partially update a nextCustomerSigma.
     *
     * @param nextCustomerSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerSigmaDTO> partialUpdate(NextCustomerSigmaDTO nextCustomerSigmaDTO) {
        LOG.debug("Request to partially update NextCustomerSigma : {}", nextCustomerSigmaDTO);

        return nextCustomerSigmaRepository
            .findById(nextCustomerSigmaDTO.getId())
            .map(existingNextCustomerSigma -> {
                nextCustomerSigmaMapper.partialUpdate(existingNextCustomerSigma, nextCustomerSigmaDTO);

                return existingNextCustomerSigma;
            })
            .map(nextCustomerSigmaRepository::save)
            .map(nextCustomerSigmaMapper::toDto);
    }

    /**
     * Get one nextCustomerSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerSigma : {}", id);
        return nextCustomerSigmaRepository.findById(id).map(nextCustomerSigmaMapper::toDto);
    }

    /**
     * Delete the nextCustomerSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerSigma : {}", id);
        nextCustomerSigmaRepository.deleteById(id);
    }
}
