package ai.realworld.service;

import ai.realworld.domain.RihannaVi;
import ai.realworld.repository.RihannaViRepository;
import ai.realworld.service.dto.RihannaViDTO;
import ai.realworld.service.mapper.RihannaViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.RihannaVi}.
 */
@Service
@Transactional
public class RihannaViService {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaViService.class);

    private final RihannaViRepository rihannaViRepository;

    private final RihannaViMapper rihannaViMapper;

    public RihannaViService(RihannaViRepository rihannaViRepository, RihannaViMapper rihannaViMapper) {
        this.rihannaViRepository = rihannaViRepository;
        this.rihannaViMapper = rihannaViMapper;
    }

    /**
     * Save a rihannaVi.
     *
     * @param rihannaViDTO the entity to save.
     * @return the persisted entity.
     */
    public RihannaViDTO save(RihannaViDTO rihannaViDTO) {
        LOG.debug("Request to save RihannaVi : {}", rihannaViDTO);
        RihannaVi rihannaVi = rihannaViMapper.toEntity(rihannaViDTO);
        rihannaVi = rihannaViRepository.save(rihannaVi);
        return rihannaViMapper.toDto(rihannaVi);
    }

    /**
     * Update a rihannaVi.
     *
     * @param rihannaViDTO the entity to save.
     * @return the persisted entity.
     */
    public RihannaViDTO update(RihannaViDTO rihannaViDTO) {
        LOG.debug("Request to update RihannaVi : {}", rihannaViDTO);
        RihannaVi rihannaVi = rihannaViMapper.toEntity(rihannaViDTO);
        rihannaVi = rihannaViRepository.save(rihannaVi);
        return rihannaViMapper.toDto(rihannaVi);
    }

    /**
     * Partially update a rihannaVi.
     *
     * @param rihannaViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RihannaViDTO> partialUpdate(RihannaViDTO rihannaViDTO) {
        LOG.debug("Request to partially update RihannaVi : {}", rihannaViDTO);

        return rihannaViRepository
            .findById(rihannaViDTO.getId())
            .map(existingRihannaVi -> {
                rihannaViMapper.partialUpdate(existingRihannaVi, rihannaViDTO);

                return existingRihannaVi;
            })
            .map(rihannaViRepository::save)
            .map(rihannaViMapper::toDto);
    }

    /**
     * Get one rihannaVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RihannaViDTO> findOne(Long id) {
        LOG.debug("Request to get RihannaVi : {}", id);
        return rihannaViRepository.findById(id).map(rihannaViMapper::toDto);
    }

    /**
     * Delete the rihannaVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RihannaVi : {}", id);
        rihannaViRepository.deleteById(id);
    }
}
