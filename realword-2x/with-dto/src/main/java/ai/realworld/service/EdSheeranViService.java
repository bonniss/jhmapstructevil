package ai.realworld.service;

import ai.realworld.domain.EdSheeranVi;
import ai.realworld.repository.EdSheeranViRepository;
import ai.realworld.service.dto.EdSheeranViDTO;
import ai.realworld.service.mapper.EdSheeranViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.EdSheeranVi}.
 */
@Service
@Transactional
public class EdSheeranViService {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranViService.class);

    private final EdSheeranViRepository edSheeranViRepository;

    private final EdSheeranViMapper edSheeranViMapper;

    public EdSheeranViService(EdSheeranViRepository edSheeranViRepository, EdSheeranViMapper edSheeranViMapper) {
        this.edSheeranViRepository = edSheeranViRepository;
        this.edSheeranViMapper = edSheeranViMapper;
    }

    /**
     * Save a edSheeranVi.
     *
     * @param edSheeranViDTO the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranViDTO save(EdSheeranViDTO edSheeranViDTO) {
        LOG.debug("Request to save EdSheeranVi : {}", edSheeranViDTO);
        EdSheeranVi edSheeranVi = edSheeranViMapper.toEntity(edSheeranViDTO);
        edSheeranVi = edSheeranViRepository.save(edSheeranVi);
        return edSheeranViMapper.toDto(edSheeranVi);
    }

    /**
     * Update a edSheeranVi.
     *
     * @param edSheeranViDTO the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranViDTO update(EdSheeranViDTO edSheeranViDTO) {
        LOG.debug("Request to update EdSheeranVi : {}", edSheeranViDTO);
        EdSheeranVi edSheeranVi = edSheeranViMapper.toEntity(edSheeranViDTO);
        edSheeranVi = edSheeranViRepository.save(edSheeranVi);
        return edSheeranViMapper.toDto(edSheeranVi);
    }

    /**
     * Partially update a edSheeranVi.
     *
     * @param edSheeranViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EdSheeranViDTO> partialUpdate(EdSheeranViDTO edSheeranViDTO) {
        LOG.debug("Request to partially update EdSheeranVi : {}", edSheeranViDTO);

        return edSheeranViRepository
            .findById(edSheeranViDTO.getId())
            .map(existingEdSheeranVi -> {
                edSheeranViMapper.partialUpdate(existingEdSheeranVi, edSheeranViDTO);

                return existingEdSheeranVi;
            })
            .map(edSheeranViRepository::save)
            .map(edSheeranViMapper::toDto);
    }

    /**
     * Get all the edSheeranVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EdSheeranViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return edSheeranViRepository.findAllWithEagerRelationships(pageable).map(edSheeranViMapper::toDto);
    }

    /**
     * Get one edSheeranVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EdSheeranViDTO> findOne(Long id) {
        LOG.debug("Request to get EdSheeranVi : {}", id);
        return edSheeranViRepository.findOneWithEagerRelationships(id).map(edSheeranViMapper::toDto);
    }

    /**
     * Delete the edSheeranVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EdSheeranVi : {}", id);
        edSheeranViRepository.deleteById(id);
    }
}
