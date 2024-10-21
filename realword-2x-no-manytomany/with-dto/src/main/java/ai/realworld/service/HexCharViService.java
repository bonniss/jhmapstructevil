package ai.realworld.service;

import ai.realworld.domain.HexCharVi;
import ai.realworld.repository.HexCharViRepository;
import ai.realworld.service.dto.HexCharViDTO;
import ai.realworld.service.mapper.HexCharViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HexCharVi}.
 */
@Service
@Transactional
public class HexCharViService {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharViService.class);

    private final HexCharViRepository hexCharViRepository;

    private final HexCharViMapper hexCharViMapper;

    public HexCharViService(HexCharViRepository hexCharViRepository, HexCharViMapper hexCharViMapper) {
        this.hexCharViRepository = hexCharViRepository;
        this.hexCharViMapper = hexCharViMapper;
    }

    /**
     * Save a hexCharVi.
     *
     * @param hexCharViDTO the entity to save.
     * @return the persisted entity.
     */
    public HexCharViDTO save(HexCharViDTO hexCharViDTO) {
        LOG.debug("Request to save HexCharVi : {}", hexCharViDTO);
        HexCharVi hexCharVi = hexCharViMapper.toEntity(hexCharViDTO);
        hexCharVi = hexCharViRepository.save(hexCharVi);
        return hexCharViMapper.toDto(hexCharVi);
    }

    /**
     * Update a hexCharVi.
     *
     * @param hexCharViDTO the entity to save.
     * @return the persisted entity.
     */
    public HexCharViDTO update(HexCharViDTO hexCharViDTO) {
        LOG.debug("Request to update HexCharVi : {}", hexCharViDTO);
        HexCharVi hexCharVi = hexCharViMapper.toEntity(hexCharViDTO);
        hexCharVi = hexCharViRepository.save(hexCharVi);
        return hexCharViMapper.toDto(hexCharVi);
    }

    /**
     * Partially update a hexCharVi.
     *
     * @param hexCharViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HexCharViDTO> partialUpdate(HexCharViDTO hexCharViDTO) {
        LOG.debug("Request to partially update HexCharVi : {}", hexCharViDTO);

        return hexCharViRepository
            .findById(hexCharViDTO.getId())
            .map(existingHexCharVi -> {
                hexCharViMapper.partialUpdate(existingHexCharVi, hexCharViDTO);

                return existingHexCharVi;
            })
            .map(hexCharViRepository::save)
            .map(hexCharViMapper::toDto);
    }

    /**
     * Get all the hexCharVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HexCharViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return hexCharViRepository.findAllWithEagerRelationships(pageable).map(hexCharViMapper::toDto);
    }

    /**
     * Get one hexCharVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HexCharViDTO> findOne(Long id) {
        LOG.debug("Request to get HexCharVi : {}", id);
        return hexCharViRepository.findOneWithEagerRelationships(id).map(hexCharViMapper::toDto);
    }

    /**
     * Delete the hexCharVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HexCharVi : {}", id);
        hexCharViRepository.deleteById(id);
    }
}
