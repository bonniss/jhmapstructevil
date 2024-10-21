package ai.realworld.service;

import ai.realworld.domain.OlAlmantinoMilo;
import ai.realworld.repository.OlAlmantinoMiloRepository;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.service.mapper.OlAlmantinoMiloMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.OlAlmantinoMilo}.
 */
@Service
@Transactional
public class OlAlmantinoMiloService {

    private static final Logger LOG = LoggerFactory.getLogger(OlAlmantinoMiloService.class);

    private final OlAlmantinoMiloRepository olAlmantinoMiloRepository;

    private final OlAlmantinoMiloMapper olAlmantinoMiloMapper;

    public OlAlmantinoMiloService(OlAlmantinoMiloRepository olAlmantinoMiloRepository, OlAlmantinoMiloMapper olAlmantinoMiloMapper) {
        this.olAlmantinoMiloRepository = olAlmantinoMiloRepository;
        this.olAlmantinoMiloMapper = olAlmantinoMiloMapper;
    }

    /**
     * Save a olAlmantinoMilo.
     *
     * @param olAlmantinoMiloDTO the entity to save.
     * @return the persisted entity.
     */
    public OlAlmantinoMiloDTO save(OlAlmantinoMiloDTO olAlmantinoMiloDTO) {
        LOG.debug("Request to save OlAlmantinoMilo : {}", olAlmantinoMiloDTO);
        OlAlmantinoMilo olAlmantinoMilo = olAlmantinoMiloMapper.toEntity(olAlmantinoMiloDTO);
        olAlmantinoMilo = olAlmantinoMiloRepository.save(olAlmantinoMilo);
        return olAlmantinoMiloMapper.toDto(olAlmantinoMilo);
    }

    /**
     * Update a olAlmantinoMilo.
     *
     * @param olAlmantinoMiloDTO the entity to save.
     * @return the persisted entity.
     */
    public OlAlmantinoMiloDTO update(OlAlmantinoMiloDTO olAlmantinoMiloDTO) {
        LOG.debug("Request to update OlAlmantinoMilo : {}", olAlmantinoMiloDTO);
        OlAlmantinoMilo olAlmantinoMilo = olAlmantinoMiloMapper.toEntity(olAlmantinoMiloDTO);
        olAlmantinoMilo = olAlmantinoMiloRepository.save(olAlmantinoMilo);
        return olAlmantinoMiloMapper.toDto(olAlmantinoMilo);
    }

    /**
     * Partially update a olAlmantinoMilo.
     *
     * @param olAlmantinoMiloDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OlAlmantinoMiloDTO> partialUpdate(OlAlmantinoMiloDTO olAlmantinoMiloDTO) {
        LOG.debug("Request to partially update OlAlmantinoMilo : {}", olAlmantinoMiloDTO);

        return olAlmantinoMiloRepository
            .findById(olAlmantinoMiloDTO.getId())
            .map(existingOlAlmantinoMilo -> {
                olAlmantinoMiloMapper.partialUpdate(existingOlAlmantinoMilo, olAlmantinoMiloDTO);

                return existingOlAlmantinoMilo;
            })
            .map(olAlmantinoMiloRepository::save)
            .map(olAlmantinoMiloMapper::toDto);
    }

    /**
     * Get one olAlmantinoMilo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OlAlmantinoMiloDTO> findOne(UUID id) {
        LOG.debug("Request to get OlAlmantinoMilo : {}", id);
        return olAlmantinoMiloRepository.findById(id).map(olAlmantinoMiloMapper::toDto);
    }

    /**
     * Delete the olAlmantinoMilo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete OlAlmantinoMilo : {}", id);
        olAlmantinoMiloRepository.deleteById(id);
    }
}
