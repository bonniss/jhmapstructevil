package ai.realworld.service;

import ai.realworld.domain.EdSheeran;
import ai.realworld.repository.EdSheeranRepository;
import ai.realworld.service.dto.EdSheeranDTO;
import ai.realworld.service.mapper.EdSheeranMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.EdSheeran}.
 */
@Service
@Transactional
public class EdSheeranService {

    private static final Logger LOG = LoggerFactory.getLogger(EdSheeranService.class);

    private final EdSheeranRepository edSheeranRepository;

    private final EdSheeranMapper edSheeranMapper;

    public EdSheeranService(EdSheeranRepository edSheeranRepository, EdSheeranMapper edSheeranMapper) {
        this.edSheeranRepository = edSheeranRepository;
        this.edSheeranMapper = edSheeranMapper;
    }

    /**
     * Save a edSheeran.
     *
     * @param edSheeranDTO the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranDTO save(EdSheeranDTO edSheeranDTO) {
        LOG.debug("Request to save EdSheeran : {}", edSheeranDTO);
        EdSheeran edSheeran = edSheeranMapper.toEntity(edSheeranDTO);
        edSheeran = edSheeranRepository.save(edSheeran);
        return edSheeranMapper.toDto(edSheeran);
    }

    /**
     * Update a edSheeran.
     *
     * @param edSheeranDTO the entity to save.
     * @return the persisted entity.
     */
    public EdSheeranDTO update(EdSheeranDTO edSheeranDTO) {
        LOG.debug("Request to update EdSheeran : {}", edSheeranDTO);
        EdSheeran edSheeran = edSheeranMapper.toEntity(edSheeranDTO);
        edSheeran = edSheeranRepository.save(edSheeran);
        return edSheeranMapper.toDto(edSheeran);
    }

    /**
     * Partially update a edSheeran.
     *
     * @param edSheeranDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EdSheeranDTO> partialUpdate(EdSheeranDTO edSheeranDTO) {
        LOG.debug("Request to partially update EdSheeran : {}", edSheeranDTO);

        return edSheeranRepository
            .findById(edSheeranDTO.getId())
            .map(existingEdSheeran -> {
                edSheeranMapper.partialUpdate(existingEdSheeran, edSheeranDTO);

                return existingEdSheeran;
            })
            .map(edSheeranRepository::save)
            .map(edSheeranMapper::toDto);
    }

    /**
     * Get all the edSheerans with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EdSheeranDTO> findAllWithEagerRelationships(Pageable pageable) {
        return edSheeranRepository.findAllWithEagerRelationships(pageable).map(edSheeranMapper::toDto);
    }

    /**
     * Get one edSheeran by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EdSheeranDTO> findOne(Long id) {
        LOG.debug("Request to get EdSheeran : {}", id);
        return edSheeranRepository.findOneWithEagerRelationships(id).map(edSheeranMapper::toDto);
    }

    /**
     * Delete the edSheeran by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete EdSheeran : {}", id);
        edSheeranRepository.deleteById(id);
    }
}
