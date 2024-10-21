package ai.realworld.service;

import ai.realworld.domain.MagharettiVi;
import ai.realworld.repository.MagharettiViRepository;
import ai.realworld.service.dto.MagharettiViDTO;
import ai.realworld.service.mapper.MagharettiViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.MagharettiVi}.
 */
@Service
@Transactional
public class MagharettiViService {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiViService.class);

    private final MagharettiViRepository magharettiViRepository;

    private final MagharettiViMapper magharettiViMapper;

    public MagharettiViService(MagharettiViRepository magharettiViRepository, MagharettiViMapper magharettiViMapper) {
        this.magharettiViRepository = magharettiViRepository;
        this.magharettiViMapper = magharettiViMapper;
    }

    /**
     * Save a magharettiVi.
     *
     * @param magharettiViDTO the entity to save.
     * @return the persisted entity.
     */
    public MagharettiViDTO save(MagharettiViDTO magharettiViDTO) {
        LOG.debug("Request to save MagharettiVi : {}", magharettiViDTO);
        MagharettiVi magharettiVi = magharettiViMapper.toEntity(magharettiViDTO);
        magharettiVi = magharettiViRepository.save(magharettiVi);
        return magharettiViMapper.toDto(magharettiVi);
    }

    /**
     * Update a magharettiVi.
     *
     * @param magharettiViDTO the entity to save.
     * @return the persisted entity.
     */
    public MagharettiViDTO update(MagharettiViDTO magharettiViDTO) {
        LOG.debug("Request to update MagharettiVi : {}", magharettiViDTO);
        MagharettiVi magharettiVi = magharettiViMapper.toEntity(magharettiViDTO);
        magharettiVi = magharettiViRepository.save(magharettiVi);
        return magharettiViMapper.toDto(magharettiVi);
    }

    /**
     * Partially update a magharettiVi.
     *
     * @param magharettiViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MagharettiViDTO> partialUpdate(MagharettiViDTO magharettiViDTO) {
        LOG.debug("Request to partially update MagharettiVi : {}", magharettiViDTO);

        return magharettiViRepository
            .findById(magharettiViDTO.getId())
            .map(existingMagharettiVi -> {
                magharettiViMapper.partialUpdate(existingMagharettiVi, magharettiViDTO);

                return existingMagharettiVi;
            })
            .map(magharettiViRepository::save)
            .map(magharettiViMapper::toDto);
    }

    /**
     * Get one magharettiVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MagharettiViDTO> findOne(Long id) {
        LOG.debug("Request to get MagharettiVi : {}", id);
        return magharettiViRepository.findById(id).map(magharettiViMapper::toDto);
    }

    /**
     * Delete the magharettiVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MagharettiVi : {}", id);
        magharettiViRepository.deleteById(id);
    }
}
