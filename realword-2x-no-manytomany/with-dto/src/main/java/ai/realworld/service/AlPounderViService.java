package ai.realworld.service;

import ai.realworld.domain.AlPounderVi;
import ai.realworld.repository.AlPounderViRepository;
import ai.realworld.service.dto.AlPounderViDTO;
import ai.realworld.service.mapper.AlPounderViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPounderVi}.
 */
@Service
@Transactional
public class AlPounderViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderViService.class);

    private final AlPounderViRepository alPounderViRepository;

    private final AlPounderViMapper alPounderViMapper;

    public AlPounderViService(AlPounderViRepository alPounderViRepository, AlPounderViMapper alPounderViMapper) {
        this.alPounderViRepository = alPounderViRepository;
        this.alPounderViMapper = alPounderViMapper;
    }

    /**
     * Save a alPounderVi.
     *
     * @param alPounderViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPounderViDTO save(AlPounderViDTO alPounderViDTO) {
        LOG.debug("Request to save AlPounderVi : {}", alPounderViDTO);
        AlPounderVi alPounderVi = alPounderViMapper.toEntity(alPounderViDTO);
        alPounderVi = alPounderViRepository.save(alPounderVi);
        return alPounderViMapper.toDto(alPounderVi);
    }

    /**
     * Update a alPounderVi.
     *
     * @param alPounderViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPounderViDTO update(AlPounderViDTO alPounderViDTO) {
        LOG.debug("Request to update AlPounderVi : {}", alPounderViDTO);
        AlPounderVi alPounderVi = alPounderViMapper.toEntity(alPounderViDTO);
        alPounderVi = alPounderViRepository.save(alPounderVi);
        return alPounderViMapper.toDto(alPounderVi);
    }

    /**
     * Partially update a alPounderVi.
     *
     * @param alPounderViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPounderViDTO> partialUpdate(AlPounderViDTO alPounderViDTO) {
        LOG.debug("Request to partially update AlPounderVi : {}", alPounderViDTO);

        return alPounderViRepository
            .findById(alPounderViDTO.getId())
            .map(existingAlPounderVi -> {
                alPounderViMapper.partialUpdate(existingAlPounderVi, alPounderViDTO);

                return existingAlPounderVi;
            })
            .map(alPounderViRepository::save)
            .map(alPounderViMapper::toDto);
    }

    /**
     * Get one alPounderVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPounderViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPounderVi : {}", id);
        return alPounderViRepository.findById(id).map(alPounderViMapper::toDto);
    }

    /**
     * Delete the alPounderVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPounderVi : {}", id);
        alPounderViRepository.deleteById(id);
    }
}
