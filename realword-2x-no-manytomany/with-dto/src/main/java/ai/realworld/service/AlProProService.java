package ai.realworld.service;

import ai.realworld.domain.AlProPro;
import ai.realworld.repository.AlProProRepository;
import ai.realworld.service.dto.AlProProDTO;
import ai.realworld.service.mapper.AlProProMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlProPro}.
 */
@Service
@Transactional
public class AlProProService {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProService.class);

    private final AlProProRepository alProProRepository;

    private final AlProProMapper alProProMapper;

    public AlProProService(AlProProRepository alProProRepository, AlProProMapper alProProMapper) {
        this.alProProRepository = alProProRepository;
        this.alProProMapper = alProProMapper;
    }

    /**
     * Save a alProPro.
     *
     * @param alProProDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProProDTO save(AlProProDTO alProProDTO) {
        LOG.debug("Request to save AlProPro : {}", alProProDTO);
        AlProPro alProPro = alProProMapper.toEntity(alProProDTO);
        alProPro = alProProRepository.save(alProPro);
        return alProProMapper.toDto(alProPro);
    }

    /**
     * Update a alProPro.
     *
     * @param alProProDTO the entity to save.
     * @return the persisted entity.
     */
    public AlProProDTO update(AlProProDTO alProProDTO) {
        LOG.debug("Request to update AlProPro : {}", alProProDTO);
        AlProPro alProPro = alProProMapper.toEntity(alProProDTO);
        alProPro = alProProRepository.save(alProPro);
        return alProProMapper.toDto(alProPro);
    }

    /**
     * Partially update a alProPro.
     *
     * @param alProProDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlProProDTO> partialUpdate(AlProProDTO alProProDTO) {
        LOG.debug("Request to partially update AlProPro : {}", alProProDTO);

        return alProProRepository
            .findById(alProProDTO.getId())
            .map(existingAlProPro -> {
                alProProMapper.partialUpdate(existingAlProPro, alProProDTO);

                return existingAlProPro;
            })
            .map(alProProRepository::save)
            .map(alProProMapper::toDto);
    }

    /**
     * Get one alProPro by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlProProDTO> findOne(UUID id) {
        LOG.debug("Request to get AlProPro : {}", id);
        return alProProRepository.findById(id).map(alProProMapper::toDto);
    }

    /**
     * Delete the alProPro by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlProPro : {}", id);
        alProProRepository.deleteById(id);
    }
}
