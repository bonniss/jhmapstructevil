package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeMiMi;
import xyz.jhmapstruct.repository.NextEmployeeMiMiRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeMiMiDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeMiMi}.
 */
@Service
@Transactional
public class NextEmployeeMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiMiService.class);

    private final NextEmployeeMiMiRepository nextEmployeeMiMiRepository;

    private final NextEmployeeMiMiMapper nextEmployeeMiMiMapper;

    public NextEmployeeMiMiService(NextEmployeeMiMiRepository nextEmployeeMiMiRepository, NextEmployeeMiMiMapper nextEmployeeMiMiMapper) {
        this.nextEmployeeMiMiRepository = nextEmployeeMiMiRepository;
        this.nextEmployeeMiMiMapper = nextEmployeeMiMiMapper;
    }

    /**
     * Save a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiMiDTO save(NextEmployeeMiMiDTO nextEmployeeMiMiDTO) {
        LOG.debug("Request to save NextEmployeeMiMi : {}", nextEmployeeMiMiDTO);
        NextEmployeeMiMi nextEmployeeMiMi = nextEmployeeMiMiMapper.toEntity(nextEmployeeMiMiDTO);
        nextEmployeeMiMi = nextEmployeeMiMiRepository.save(nextEmployeeMiMi);
        return nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);
    }

    /**
     * Update a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiMiDTO update(NextEmployeeMiMiDTO nextEmployeeMiMiDTO) {
        LOG.debug("Request to update NextEmployeeMiMi : {}", nextEmployeeMiMiDTO);
        NextEmployeeMiMi nextEmployeeMiMi = nextEmployeeMiMiMapper.toEntity(nextEmployeeMiMiDTO);
        nextEmployeeMiMi = nextEmployeeMiMiRepository.save(nextEmployeeMiMi);
        return nextEmployeeMiMiMapper.toDto(nextEmployeeMiMi);
    }

    /**
     * Partially update a nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeMiMiDTO> partialUpdate(NextEmployeeMiMiDTO nextEmployeeMiMiDTO) {
        LOG.debug("Request to partially update NextEmployeeMiMi : {}", nextEmployeeMiMiDTO);

        return nextEmployeeMiMiRepository
            .findById(nextEmployeeMiMiDTO.getId())
            .map(existingNextEmployeeMiMi -> {
                nextEmployeeMiMiMapper.partialUpdate(existingNextEmployeeMiMi, nextEmployeeMiMiDTO);

                return existingNextEmployeeMiMi;
            })
            .map(nextEmployeeMiMiRepository::save)
            .map(nextEmployeeMiMiMapper::toDto);
    }

    /**
     * Get one nextEmployeeMiMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeMiMi : {}", id);
        return nextEmployeeMiMiRepository.findById(id).map(nextEmployeeMiMiMapper::toDto);
    }

    /**
     * Delete the nextEmployeeMiMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeMiMi : {}", id);
        nextEmployeeMiMiRepository.deleteById(id);
    }
}
