package ai.realworld.service;

import ai.realworld.domain.OlMaster;
import ai.realworld.repository.OlMasterRepository;
import ai.realworld.service.dto.OlMasterDTO;
import ai.realworld.service.mapper.OlMasterMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.OlMaster}.
 */
@Service
@Transactional
public class OlMasterService {

    private static final Logger LOG = LoggerFactory.getLogger(OlMasterService.class);

    private final OlMasterRepository olMasterRepository;

    private final OlMasterMapper olMasterMapper;

    public OlMasterService(OlMasterRepository olMasterRepository, OlMasterMapper olMasterMapper) {
        this.olMasterRepository = olMasterRepository;
        this.olMasterMapper = olMasterMapper;
    }

    /**
     * Save a olMaster.
     *
     * @param olMasterDTO the entity to save.
     * @return the persisted entity.
     */
    public OlMasterDTO save(OlMasterDTO olMasterDTO) {
        LOG.debug("Request to save OlMaster : {}", olMasterDTO);
        OlMaster olMaster = olMasterMapper.toEntity(olMasterDTO);
        olMaster = olMasterRepository.save(olMaster);
        return olMasterMapper.toDto(olMaster);
    }

    /**
     * Update a olMaster.
     *
     * @param olMasterDTO the entity to save.
     * @return the persisted entity.
     */
    public OlMasterDTO update(OlMasterDTO olMasterDTO) {
        LOG.debug("Request to update OlMaster : {}", olMasterDTO);
        OlMaster olMaster = olMasterMapper.toEntity(olMasterDTO);
        olMaster = olMasterRepository.save(olMaster);
        return olMasterMapper.toDto(olMaster);
    }

    /**
     * Partially update a olMaster.
     *
     * @param olMasterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OlMasterDTO> partialUpdate(OlMasterDTO olMasterDTO) {
        LOG.debug("Request to partially update OlMaster : {}", olMasterDTO);

        return olMasterRepository
            .findById(olMasterDTO.getId())
            .map(existingOlMaster -> {
                olMasterMapper.partialUpdate(existingOlMaster, olMasterDTO);

                return existingOlMaster;
            })
            .map(olMasterRepository::save)
            .map(olMasterMapper::toDto);
    }

    /**
     * Get one olMaster by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OlMasterDTO> findOne(UUID id) {
        LOG.debug("Request to get OlMaster : {}", id);
        return olMasterRepository.findById(id).map(olMasterMapper::toDto);
    }

    /**
     * Delete the olMaster by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete OlMaster : {}", id);
        olMasterRepository.deleteById(id);
    }
}
