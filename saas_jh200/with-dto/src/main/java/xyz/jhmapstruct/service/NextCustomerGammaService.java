package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomerGamma;
import xyz.jhmapstruct.repository.NextCustomerGammaRepository;
import xyz.jhmapstruct.service.dto.NextCustomerGammaDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomerGamma}.
 */
@Service
@Transactional
public class NextCustomerGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerGammaService.class);

    private final NextCustomerGammaRepository nextCustomerGammaRepository;

    private final NextCustomerGammaMapper nextCustomerGammaMapper;

    public NextCustomerGammaService(
        NextCustomerGammaRepository nextCustomerGammaRepository,
        NextCustomerGammaMapper nextCustomerGammaMapper
    ) {
        this.nextCustomerGammaRepository = nextCustomerGammaRepository;
        this.nextCustomerGammaMapper = nextCustomerGammaMapper;
    }

    /**
     * Save a nextCustomerGamma.
     *
     * @param nextCustomerGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerGammaDTO save(NextCustomerGammaDTO nextCustomerGammaDTO) {
        LOG.debug("Request to save NextCustomerGamma : {}", nextCustomerGammaDTO);
        NextCustomerGamma nextCustomerGamma = nextCustomerGammaMapper.toEntity(nextCustomerGammaDTO);
        nextCustomerGamma = nextCustomerGammaRepository.save(nextCustomerGamma);
        return nextCustomerGammaMapper.toDto(nextCustomerGamma);
    }

    /**
     * Update a nextCustomerGamma.
     *
     * @param nextCustomerGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerGammaDTO update(NextCustomerGammaDTO nextCustomerGammaDTO) {
        LOG.debug("Request to update NextCustomerGamma : {}", nextCustomerGammaDTO);
        NextCustomerGamma nextCustomerGamma = nextCustomerGammaMapper.toEntity(nextCustomerGammaDTO);
        nextCustomerGamma = nextCustomerGammaRepository.save(nextCustomerGamma);
        return nextCustomerGammaMapper.toDto(nextCustomerGamma);
    }

    /**
     * Partially update a nextCustomerGamma.
     *
     * @param nextCustomerGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerGammaDTO> partialUpdate(NextCustomerGammaDTO nextCustomerGammaDTO) {
        LOG.debug("Request to partially update NextCustomerGamma : {}", nextCustomerGammaDTO);

        return nextCustomerGammaRepository
            .findById(nextCustomerGammaDTO.getId())
            .map(existingNextCustomerGamma -> {
                nextCustomerGammaMapper.partialUpdate(existingNextCustomerGamma, nextCustomerGammaDTO);

                return existingNextCustomerGamma;
            })
            .map(nextCustomerGammaRepository::save)
            .map(nextCustomerGammaMapper::toDto);
    }

    /**
     * Get one nextCustomerGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomerGamma : {}", id);
        return nextCustomerGammaRepository.findById(id).map(nextCustomerGammaMapper::toDto);
    }

    /**
     * Delete the nextCustomerGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomerGamma : {}", id);
        nextCustomerGammaRepository.deleteById(id);
    }
}
