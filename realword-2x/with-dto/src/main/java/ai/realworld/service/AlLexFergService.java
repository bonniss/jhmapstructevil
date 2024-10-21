package ai.realworld.service;

import ai.realworld.domain.AlLexFerg;
import ai.realworld.repository.AlLexFergRepository;
import ai.realworld.service.dto.AlLexFergDTO;
import ai.realworld.service.mapper.AlLexFergMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLexFerg}.
 */
@Service
@Transactional
public class AlLexFergService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergService.class);

    private final AlLexFergRepository alLexFergRepository;

    private final AlLexFergMapper alLexFergMapper;

    public AlLexFergService(AlLexFergRepository alLexFergRepository, AlLexFergMapper alLexFergMapper) {
        this.alLexFergRepository = alLexFergRepository;
        this.alLexFergMapper = alLexFergMapper;
    }

    /**
     * Save a alLexFerg.
     *
     * @param alLexFergDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergDTO save(AlLexFergDTO alLexFergDTO) {
        LOG.debug("Request to save AlLexFerg : {}", alLexFergDTO);
        AlLexFerg alLexFerg = alLexFergMapper.toEntity(alLexFergDTO);
        alLexFerg = alLexFergRepository.save(alLexFerg);
        return alLexFergMapper.toDto(alLexFerg);
    }

    /**
     * Update a alLexFerg.
     *
     * @param alLexFergDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergDTO update(AlLexFergDTO alLexFergDTO) {
        LOG.debug("Request to update AlLexFerg : {}", alLexFergDTO);
        AlLexFerg alLexFerg = alLexFergMapper.toEntity(alLexFergDTO);
        alLexFerg = alLexFergRepository.save(alLexFerg);
        return alLexFergMapper.toDto(alLexFerg);
    }

    /**
     * Partially update a alLexFerg.
     *
     * @param alLexFergDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLexFergDTO> partialUpdate(AlLexFergDTO alLexFergDTO) {
        LOG.debug("Request to partially update AlLexFerg : {}", alLexFergDTO);

        return alLexFergRepository
            .findById(alLexFergDTO.getId())
            .map(existingAlLexFerg -> {
                alLexFergMapper.partialUpdate(existingAlLexFerg, alLexFergDTO);

                return existingAlLexFerg;
            })
            .map(alLexFergRepository::save)
            .map(alLexFergMapper::toDto);
    }

    /**
     * Get all the alLexFergs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AlLexFergDTO> findAllWithEagerRelationships(Pageable pageable) {
        return alLexFergRepository.findAllWithEagerRelationships(pageable).map(alLexFergMapper::toDto);
    }

    /**
     * Get one alLexFerg by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLexFergDTO> findOne(Long id) {
        LOG.debug("Request to get AlLexFerg : {}", id);
        return alLexFergRepository.findOneWithEagerRelationships(id).map(alLexFergMapper::toDto);
    }

    /**
     * Delete the alLexFerg by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlLexFerg : {}", id);
        alLexFergRepository.deleteById(id);
    }
}
