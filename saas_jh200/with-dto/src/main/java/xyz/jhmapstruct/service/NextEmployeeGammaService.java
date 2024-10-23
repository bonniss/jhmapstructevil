package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeGammaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeGammaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeGamma}.
 */
@Service
@Transactional
public class NextEmployeeGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeGammaService.class);

    private final NextEmployeeGammaRepository nextEmployeeGammaRepository;

    private final NextEmployeeGammaMapper nextEmployeeGammaMapper;

    public NextEmployeeGammaService(
        NextEmployeeGammaRepository nextEmployeeGammaRepository,
        NextEmployeeGammaMapper nextEmployeeGammaMapper
    ) {
        this.nextEmployeeGammaRepository = nextEmployeeGammaRepository;
        this.nextEmployeeGammaMapper = nextEmployeeGammaMapper;
    }

    /**
     * Save a nextEmployeeGamma.
     *
     * @param nextEmployeeGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeGammaDTO save(NextEmployeeGammaDTO nextEmployeeGammaDTO) {
        LOG.debug("Request to save NextEmployeeGamma : {}", nextEmployeeGammaDTO);
        NextEmployeeGamma nextEmployeeGamma = nextEmployeeGammaMapper.toEntity(nextEmployeeGammaDTO);
        nextEmployeeGamma = nextEmployeeGammaRepository.save(nextEmployeeGamma);
        return nextEmployeeGammaMapper.toDto(nextEmployeeGamma);
    }

    /**
     * Update a nextEmployeeGamma.
     *
     * @param nextEmployeeGammaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeGammaDTO update(NextEmployeeGammaDTO nextEmployeeGammaDTO) {
        LOG.debug("Request to update NextEmployeeGamma : {}", nextEmployeeGammaDTO);
        NextEmployeeGamma nextEmployeeGamma = nextEmployeeGammaMapper.toEntity(nextEmployeeGammaDTO);
        nextEmployeeGamma = nextEmployeeGammaRepository.save(nextEmployeeGamma);
        return nextEmployeeGammaMapper.toDto(nextEmployeeGamma);
    }

    /**
     * Partially update a nextEmployeeGamma.
     *
     * @param nextEmployeeGammaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeGammaDTO> partialUpdate(NextEmployeeGammaDTO nextEmployeeGammaDTO) {
        LOG.debug("Request to partially update NextEmployeeGamma : {}", nextEmployeeGammaDTO);

        return nextEmployeeGammaRepository
            .findById(nextEmployeeGammaDTO.getId())
            .map(existingNextEmployeeGamma -> {
                nextEmployeeGammaMapper.partialUpdate(existingNextEmployeeGamma, nextEmployeeGammaDTO);

                return existingNextEmployeeGamma;
            })
            .map(nextEmployeeGammaRepository::save)
            .map(nextEmployeeGammaMapper::toDto);
    }

    /**
     * Get one nextEmployeeGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeGammaDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeGamma : {}", id);
        return nextEmployeeGammaRepository.findById(id).map(nextEmployeeGammaMapper::toDto);
    }

    /**
     * Delete the nextEmployeeGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeGamma : {}", id);
        nextEmployeeGammaRepository.deleteById(id);
    }
}
