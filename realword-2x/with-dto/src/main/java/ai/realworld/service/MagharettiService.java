package ai.realworld.service;

import ai.realworld.domain.Magharetti;
import ai.realworld.repository.MagharettiRepository;
import ai.realworld.service.dto.MagharettiDTO;
import ai.realworld.service.mapper.MagharettiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Magharetti}.
 */
@Service
@Transactional
public class MagharettiService {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiService.class);

    private final MagharettiRepository magharettiRepository;

    private final MagharettiMapper magharettiMapper;

    public MagharettiService(MagharettiRepository magharettiRepository, MagharettiMapper magharettiMapper) {
        this.magharettiRepository = magharettiRepository;
        this.magharettiMapper = magharettiMapper;
    }

    /**
     * Save a magharetti.
     *
     * @param magharettiDTO the entity to save.
     * @return the persisted entity.
     */
    public MagharettiDTO save(MagharettiDTO magharettiDTO) {
        LOG.debug("Request to save Magharetti : {}", magharettiDTO);
        Magharetti magharetti = magharettiMapper.toEntity(magharettiDTO);
        magharetti = magharettiRepository.save(magharetti);
        return magharettiMapper.toDto(magharetti);
    }

    /**
     * Update a magharetti.
     *
     * @param magharettiDTO the entity to save.
     * @return the persisted entity.
     */
    public MagharettiDTO update(MagharettiDTO magharettiDTO) {
        LOG.debug("Request to update Magharetti : {}", magharettiDTO);
        Magharetti magharetti = magharettiMapper.toEntity(magharettiDTO);
        magharetti = magharettiRepository.save(magharetti);
        return magharettiMapper.toDto(magharetti);
    }

    /**
     * Partially update a magharetti.
     *
     * @param magharettiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MagharettiDTO> partialUpdate(MagharettiDTO magharettiDTO) {
        LOG.debug("Request to partially update Magharetti : {}", magharettiDTO);

        return magharettiRepository
            .findById(magharettiDTO.getId())
            .map(existingMagharetti -> {
                magharettiMapper.partialUpdate(existingMagharetti, magharettiDTO);

                return existingMagharetti;
            })
            .map(magharettiRepository::save)
            .map(magharettiMapper::toDto);
    }

    /**
     * Get one magharetti by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MagharettiDTO> findOne(Long id) {
        LOG.debug("Request to get Magharetti : {}", id);
        return magharettiRepository.findById(id).map(magharettiMapper::toDto);
    }

    /**
     * Delete the magharetti by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Magharetti : {}", id);
        magharettiRepository.deleteById(id);
    }
}
