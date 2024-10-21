package ai.realworld.service;

import ai.realworld.domain.SaisanCogVi;
import ai.realworld.repository.SaisanCogViRepository;
import ai.realworld.service.dto.SaisanCogViDTO;
import ai.realworld.service.mapper.SaisanCogViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SaisanCogVi}.
 */
@Service
@Transactional
public class SaisanCogViService {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogViService.class);

    private final SaisanCogViRepository saisanCogViRepository;

    private final SaisanCogViMapper saisanCogViMapper;

    public SaisanCogViService(SaisanCogViRepository saisanCogViRepository, SaisanCogViMapper saisanCogViMapper) {
        this.saisanCogViRepository = saisanCogViRepository;
        this.saisanCogViMapper = saisanCogViMapper;
    }

    /**
     * Save a saisanCogVi.
     *
     * @param saisanCogViDTO the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogViDTO save(SaisanCogViDTO saisanCogViDTO) {
        LOG.debug("Request to save SaisanCogVi : {}", saisanCogViDTO);
        SaisanCogVi saisanCogVi = saisanCogViMapper.toEntity(saisanCogViDTO);
        saisanCogVi = saisanCogViRepository.save(saisanCogVi);
        return saisanCogViMapper.toDto(saisanCogVi);
    }

    /**
     * Update a saisanCogVi.
     *
     * @param saisanCogViDTO the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogViDTO update(SaisanCogViDTO saisanCogViDTO) {
        LOG.debug("Request to update SaisanCogVi : {}", saisanCogViDTO);
        SaisanCogVi saisanCogVi = saisanCogViMapper.toEntity(saisanCogViDTO);
        saisanCogVi = saisanCogViRepository.save(saisanCogVi);
        return saisanCogViMapper.toDto(saisanCogVi);
    }

    /**
     * Partially update a saisanCogVi.
     *
     * @param saisanCogViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaisanCogViDTO> partialUpdate(SaisanCogViDTO saisanCogViDTO) {
        LOG.debug("Request to partially update SaisanCogVi : {}", saisanCogViDTO);

        return saisanCogViRepository
            .findById(saisanCogViDTO.getId())
            .map(existingSaisanCogVi -> {
                saisanCogViMapper.partialUpdate(existingSaisanCogVi, saisanCogViDTO);

                return existingSaisanCogVi;
            })
            .map(saisanCogViRepository::save)
            .map(saisanCogViMapper::toDto);
    }

    /**
     * Get one saisanCogVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaisanCogViDTO> findOne(Long id) {
        LOG.debug("Request to get SaisanCogVi : {}", id);
        return saisanCogViRepository.findById(id).map(saisanCogViMapper::toDto);
    }

    /**
     * Delete the saisanCogVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaisanCogVi : {}", id);
        saisanCogViRepository.deleteById(id);
    }
}
