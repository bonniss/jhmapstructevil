package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.repository.NextEmployeeSigmaRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeSigmaDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeSigmaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeSigma}.
 */
@Service
@Transactional
public class NextEmployeeSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeSigmaService.class);

    private final NextEmployeeSigmaRepository nextEmployeeSigmaRepository;

    private final NextEmployeeSigmaMapper nextEmployeeSigmaMapper;

    public NextEmployeeSigmaService(
        NextEmployeeSigmaRepository nextEmployeeSigmaRepository,
        NextEmployeeSigmaMapper nextEmployeeSigmaMapper
    ) {
        this.nextEmployeeSigmaRepository = nextEmployeeSigmaRepository;
        this.nextEmployeeSigmaMapper = nextEmployeeSigmaMapper;
    }

    /**
     * Save a nextEmployeeSigma.
     *
     * @param nextEmployeeSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeSigmaDTO save(NextEmployeeSigmaDTO nextEmployeeSigmaDTO) {
        LOG.debug("Request to save NextEmployeeSigma : {}", nextEmployeeSigmaDTO);
        NextEmployeeSigma nextEmployeeSigma = nextEmployeeSigmaMapper.toEntity(nextEmployeeSigmaDTO);
        nextEmployeeSigma = nextEmployeeSigmaRepository.save(nextEmployeeSigma);
        return nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);
    }

    /**
     * Update a nextEmployeeSigma.
     *
     * @param nextEmployeeSigmaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeSigmaDTO update(NextEmployeeSigmaDTO nextEmployeeSigmaDTO) {
        LOG.debug("Request to update NextEmployeeSigma : {}", nextEmployeeSigmaDTO);
        NextEmployeeSigma nextEmployeeSigma = nextEmployeeSigmaMapper.toEntity(nextEmployeeSigmaDTO);
        nextEmployeeSigma = nextEmployeeSigmaRepository.save(nextEmployeeSigma);
        return nextEmployeeSigmaMapper.toDto(nextEmployeeSigma);
    }

    /**
     * Partially update a nextEmployeeSigma.
     *
     * @param nextEmployeeSigmaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeSigmaDTO> partialUpdate(NextEmployeeSigmaDTO nextEmployeeSigmaDTO) {
        LOG.debug("Request to partially update NextEmployeeSigma : {}", nextEmployeeSigmaDTO);

        return nextEmployeeSigmaRepository
            .findById(nextEmployeeSigmaDTO.getId())
            .map(existingNextEmployeeSigma -> {
                nextEmployeeSigmaMapper.partialUpdate(existingNextEmployeeSigma, nextEmployeeSigmaDTO);

                return existingNextEmployeeSigma;
            })
            .map(nextEmployeeSigmaRepository::save)
            .map(nextEmployeeSigmaMapper::toDto);
    }

    /**
     * Get one nextEmployeeSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeSigmaDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeSigma : {}", id);
        return nextEmployeeSigmaRepository.findById(id).map(nextEmployeeSigmaMapper::toDto);
    }

    /**
     * Delete the nextEmployeeSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeSigma : {}", id);
        nextEmployeeSigmaRepository.deleteById(id);
    }
}
