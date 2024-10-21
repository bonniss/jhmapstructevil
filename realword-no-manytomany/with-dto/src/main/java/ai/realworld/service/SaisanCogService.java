package ai.realworld.service;

import ai.realworld.domain.SaisanCog;
import ai.realworld.repository.SaisanCogRepository;
import ai.realworld.service.dto.SaisanCogDTO;
import ai.realworld.service.mapper.SaisanCogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SaisanCog}.
 */
@Service
@Transactional
public class SaisanCogService {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogService.class);

    private final SaisanCogRepository saisanCogRepository;

    private final SaisanCogMapper saisanCogMapper;

    public SaisanCogService(SaisanCogRepository saisanCogRepository, SaisanCogMapper saisanCogMapper) {
        this.saisanCogRepository = saisanCogRepository;
        this.saisanCogMapper = saisanCogMapper;
    }

    /**
     * Save a saisanCog.
     *
     * @param saisanCogDTO the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogDTO save(SaisanCogDTO saisanCogDTO) {
        LOG.debug("Request to save SaisanCog : {}", saisanCogDTO);
        SaisanCog saisanCog = saisanCogMapper.toEntity(saisanCogDTO);
        saisanCog = saisanCogRepository.save(saisanCog);
        return saisanCogMapper.toDto(saisanCog);
    }

    /**
     * Update a saisanCog.
     *
     * @param saisanCogDTO the entity to save.
     * @return the persisted entity.
     */
    public SaisanCogDTO update(SaisanCogDTO saisanCogDTO) {
        LOG.debug("Request to update SaisanCog : {}", saisanCogDTO);
        SaisanCog saisanCog = saisanCogMapper.toEntity(saisanCogDTO);
        saisanCog = saisanCogRepository.save(saisanCog);
        return saisanCogMapper.toDto(saisanCog);
    }

    /**
     * Partially update a saisanCog.
     *
     * @param saisanCogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaisanCogDTO> partialUpdate(SaisanCogDTO saisanCogDTO) {
        LOG.debug("Request to partially update SaisanCog : {}", saisanCogDTO);

        return saisanCogRepository
            .findById(saisanCogDTO.getId())
            .map(existingSaisanCog -> {
                saisanCogMapper.partialUpdate(existingSaisanCog, saisanCogDTO);

                return existingSaisanCog;
            })
            .map(saisanCogRepository::save)
            .map(saisanCogMapper::toDto);
    }

    /**
     * Get one saisanCog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaisanCogDTO> findOne(Long id) {
        LOG.debug("Request to get SaisanCog : {}", id);
        return saisanCogRepository.findById(id).map(saisanCogMapper::toDto);
    }

    /**
     * Delete the saisanCog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaisanCog : {}", id);
        saisanCogRepository.deleteById(id);
    }
}
