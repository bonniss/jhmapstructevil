package ai.realworld.service;

import ai.realworld.domain.AlActisoVi;
import ai.realworld.repository.AlActisoViRepository;
import ai.realworld.service.dto.AlActisoViDTO;
import ai.realworld.service.mapper.AlActisoViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlActisoVi}.
 */
@Service
@Transactional
public class AlActisoViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoViService.class);

    private final AlActisoViRepository alActisoViRepository;

    private final AlActisoViMapper alActisoViMapper;

    public AlActisoViService(AlActisoViRepository alActisoViRepository, AlActisoViMapper alActisoViMapper) {
        this.alActisoViRepository = alActisoViRepository;
        this.alActisoViMapper = alActisoViMapper;
    }

    /**
     * Save a alActisoVi.
     *
     * @param alActisoViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlActisoViDTO save(AlActisoViDTO alActisoViDTO) {
        LOG.debug("Request to save AlActisoVi : {}", alActisoViDTO);
        AlActisoVi alActisoVi = alActisoViMapper.toEntity(alActisoViDTO);
        alActisoVi = alActisoViRepository.save(alActisoVi);
        return alActisoViMapper.toDto(alActisoVi);
    }

    /**
     * Update a alActisoVi.
     *
     * @param alActisoViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlActisoViDTO update(AlActisoViDTO alActisoViDTO) {
        LOG.debug("Request to update AlActisoVi : {}", alActisoViDTO);
        AlActisoVi alActisoVi = alActisoViMapper.toEntity(alActisoViDTO);
        alActisoVi = alActisoViRepository.save(alActisoVi);
        return alActisoViMapper.toDto(alActisoVi);
    }

    /**
     * Partially update a alActisoVi.
     *
     * @param alActisoViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlActisoViDTO> partialUpdate(AlActisoViDTO alActisoViDTO) {
        LOG.debug("Request to partially update AlActisoVi : {}", alActisoViDTO);

        return alActisoViRepository
            .findById(alActisoViDTO.getId())
            .map(existingAlActisoVi -> {
                alActisoViMapper.partialUpdate(existingAlActisoVi, alActisoViDTO);

                return existingAlActisoVi;
            })
            .map(alActisoViRepository::save)
            .map(alActisoViMapper::toDto);
    }

    /**
     * Get one alActisoVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlActisoViDTO> findOne(Long id) {
        LOG.debug("Request to get AlActisoVi : {}", id);
        return alActisoViRepository.findById(id).map(alActisoViMapper::toDto);
    }

    /**
     * Delete the alActisoVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlActisoVi : {}", id);
        alActisoViRepository.deleteById(id);
    }
}
