package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.repository.NextEmployeeMiRepository;
import xyz.jhmapstruct.service.dto.NextEmployeeMiDTO;
import xyz.jhmapstruct.service.mapper.NextEmployeeMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeMi}.
 */
@Service
@Transactional
public class NextEmployeeMiService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiService.class);

    private final NextEmployeeMiRepository nextEmployeeMiRepository;

    private final NextEmployeeMiMapper nextEmployeeMiMapper;

    public NextEmployeeMiService(NextEmployeeMiRepository nextEmployeeMiRepository, NextEmployeeMiMapper nextEmployeeMiMapper) {
        this.nextEmployeeMiRepository = nextEmployeeMiRepository;
        this.nextEmployeeMiMapper = nextEmployeeMiMapper;
    }

    /**
     * Save a nextEmployeeMi.
     *
     * @param nextEmployeeMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiDTO save(NextEmployeeMiDTO nextEmployeeMiDTO) {
        LOG.debug("Request to save NextEmployeeMi : {}", nextEmployeeMiDTO);
        NextEmployeeMi nextEmployeeMi = nextEmployeeMiMapper.toEntity(nextEmployeeMiDTO);
        nextEmployeeMi = nextEmployeeMiRepository.save(nextEmployeeMi);
        return nextEmployeeMiMapper.toDto(nextEmployeeMi);
    }

    /**
     * Update a nextEmployeeMi.
     *
     * @param nextEmployeeMiDTO the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeMiDTO update(NextEmployeeMiDTO nextEmployeeMiDTO) {
        LOG.debug("Request to update NextEmployeeMi : {}", nextEmployeeMiDTO);
        NextEmployeeMi nextEmployeeMi = nextEmployeeMiMapper.toEntity(nextEmployeeMiDTO);
        nextEmployeeMi = nextEmployeeMiRepository.save(nextEmployeeMi);
        return nextEmployeeMiMapper.toDto(nextEmployeeMi);
    }

    /**
     * Partially update a nextEmployeeMi.
     *
     * @param nextEmployeeMiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeMiDTO> partialUpdate(NextEmployeeMiDTO nextEmployeeMiDTO) {
        LOG.debug("Request to partially update NextEmployeeMi : {}", nextEmployeeMiDTO);

        return nextEmployeeMiRepository
            .findById(nextEmployeeMiDTO.getId())
            .map(existingNextEmployeeMi -> {
                nextEmployeeMiMapper.partialUpdate(existingNextEmployeeMi, nextEmployeeMiDTO);

                return existingNextEmployeeMi;
            })
            .map(nextEmployeeMiRepository::save)
            .map(nextEmployeeMiMapper::toDto);
    }

    /**
     * Get one nextEmployeeMi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeMiDTO> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeMi : {}", id);
        return nextEmployeeMiRepository.findById(id).map(nextEmployeeMiMapper::toDto);
    }

    /**
     * Delete the nextEmployeeMi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeMi : {}", id);
        nextEmployeeMiRepository.deleteById(id);
    }
}
