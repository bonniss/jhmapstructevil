package ai.realworld.service;

import ai.realworld.domain.InitiumVi;
import ai.realworld.repository.InitiumViRepository;
import ai.realworld.service.dto.InitiumViDTO;
import ai.realworld.service.mapper.InitiumViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.InitiumVi}.
 */
@Service
@Transactional
public class InitiumViService {

    private static final Logger LOG = LoggerFactory.getLogger(InitiumViService.class);

    private final InitiumViRepository initiumViRepository;

    private final InitiumViMapper initiumViMapper;

    public InitiumViService(InitiumViRepository initiumViRepository, InitiumViMapper initiumViMapper) {
        this.initiumViRepository = initiumViRepository;
        this.initiumViMapper = initiumViMapper;
    }

    /**
     * Save a initiumVi.
     *
     * @param initiumViDTO the entity to save.
     * @return the persisted entity.
     */
    public InitiumViDTO save(InitiumViDTO initiumViDTO) {
        LOG.debug("Request to save InitiumVi : {}", initiumViDTO);
        InitiumVi initiumVi = initiumViMapper.toEntity(initiumViDTO);
        initiumVi = initiumViRepository.save(initiumVi);
        return initiumViMapper.toDto(initiumVi);
    }

    /**
     * Update a initiumVi.
     *
     * @param initiumViDTO the entity to save.
     * @return the persisted entity.
     */
    public InitiumViDTO update(InitiumViDTO initiumViDTO) {
        LOG.debug("Request to update InitiumVi : {}", initiumViDTO);
        InitiumVi initiumVi = initiumViMapper.toEntity(initiumViDTO);
        initiumVi = initiumViRepository.save(initiumVi);
        return initiumViMapper.toDto(initiumVi);
    }

    /**
     * Partially update a initiumVi.
     *
     * @param initiumViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InitiumViDTO> partialUpdate(InitiumViDTO initiumViDTO) {
        LOG.debug("Request to partially update InitiumVi : {}", initiumViDTO);

        return initiumViRepository
            .findById(initiumViDTO.getId())
            .map(existingInitiumVi -> {
                initiumViMapper.partialUpdate(existingInitiumVi, initiumViDTO);

                return existingInitiumVi;
            })
            .map(initiumViRepository::save)
            .map(initiumViMapper::toDto);
    }

    /**
     * Get one initiumVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InitiumViDTO> findOne(Long id) {
        LOG.debug("Request to get InitiumVi : {}", id);
        return initiumViRepository.findById(id).map(initiumViMapper::toDto);
    }

    /**
     * Delete the initiumVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InitiumVi : {}", id);
        initiumViRepository.deleteById(id);
    }
}
