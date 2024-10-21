package ai.realworld.service;

import ai.realworld.domain.Initium;
import ai.realworld.repository.InitiumRepository;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.mapper.InitiumMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.Initium}.
 */
@Service
@Transactional
public class InitiumService {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumService.class);

    private final InitiumRepository initiumRepository;

    private final InitiumMapper initiumMapper;

    public InitiumService(InitiumRepository initiumRepository, InitiumMapper initiumMapper) {
        this.initiumRepository = initiumRepository;
        this.initiumMapper = initiumMapper;
    }

    /**
     * Save a initium.
     *
     * @param initiumDTO the entity to save.
     * @return the persisted entity.
     */
    public InitiumDTO save(InitiumDTO initiumDTO) {
        LOG.debug("Request to save Initium : {}", initiumDTO);
        Initium initium = initiumMapper.toEntity(initiumDTO);
        initium = initiumRepository.save(initium);
        return initiumMapper.toDto(initium);
    }

    /**
     * Update a initium.
     *
     * @param initiumDTO the entity to save.
     * @return the persisted entity.
     */
    public InitiumDTO update(InitiumDTO initiumDTO) {
        LOG.debug("Request to update Initium : {}", initiumDTO);
        Initium initium = initiumMapper.toEntity(initiumDTO);
        initium = initiumRepository.save(initium);
        return initiumMapper.toDto(initium);
    }

    /**
     * Partially update a initium.
     *
     * @param initiumDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InitiumDTO> partialUpdate(InitiumDTO initiumDTO) {
        LOG.debug("Request to partially update Initium : {}", initiumDTO);

        return initiumRepository
            .findById(initiumDTO.getId())
            .map(existingInitium -> {
                initiumMapper.partialUpdate(existingInitium, initiumDTO);

                return existingInitium;
            })
            .map(initiumRepository::save)
            .map(initiumMapper::toDto);
    }

    /**
     * Get one initium by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InitiumDTO> findOne(Long id) {
        LOG.debug("Request to get Initium : {}", id);
        return initiumRepository.findById(id).map(initiumMapper::toDto);
    }

    /**
     * Delete the initium by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Initium : {}", id);
        initiumRepository.deleteById(id);
    }
}
