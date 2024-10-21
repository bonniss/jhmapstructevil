package ai.realworld.service;

import ai.realworld.domain.AlMenityVi;
import ai.realworld.repository.AlMenityViRepository;
import ai.realworld.service.dto.AlMenityViDTO;
import ai.realworld.service.mapper.AlMenityViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMenityVi}.
 */
@Service
@Transactional
public class AlMenityViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityViService.class);

    private final AlMenityViRepository alMenityViRepository;

    private final AlMenityViMapper alMenityViMapper;

    public AlMenityViService(AlMenityViRepository alMenityViRepository, AlMenityViMapper alMenityViMapper) {
        this.alMenityViRepository = alMenityViRepository;
        this.alMenityViMapper = alMenityViMapper;
    }

    /**
     * Save a alMenityVi.
     *
     * @param alMenityViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMenityViDTO save(AlMenityViDTO alMenityViDTO) {
        LOG.debug("Request to save AlMenityVi : {}", alMenityViDTO);
        AlMenityVi alMenityVi = alMenityViMapper.toEntity(alMenityViDTO);
        alMenityVi = alMenityViRepository.save(alMenityVi);
        return alMenityViMapper.toDto(alMenityVi);
    }

    /**
     * Update a alMenityVi.
     *
     * @param alMenityViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMenityViDTO update(AlMenityViDTO alMenityViDTO) {
        LOG.debug("Request to update AlMenityVi : {}", alMenityViDTO);
        AlMenityVi alMenityVi = alMenityViMapper.toEntity(alMenityViDTO);
        alMenityVi = alMenityViRepository.save(alMenityVi);
        return alMenityViMapper.toDto(alMenityVi);
    }

    /**
     * Partially update a alMenityVi.
     *
     * @param alMenityViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMenityViDTO> partialUpdate(AlMenityViDTO alMenityViDTO) {
        LOG.debug("Request to partially update AlMenityVi : {}", alMenityViDTO);

        return alMenityViRepository
            .findById(alMenityViDTO.getId())
            .map(existingAlMenityVi -> {
                alMenityViMapper.partialUpdate(existingAlMenityVi, alMenityViDTO);

                return existingAlMenityVi;
            })
            .map(alMenityViRepository::save)
            .map(alMenityViMapper::toDto);
    }

    /**
     * Get one alMenityVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMenityViDTO> findOne(Long id) {
        LOG.debug("Request to get AlMenityVi : {}", id);
        return alMenityViRepository.findById(id).map(alMenityViMapper::toDto);
    }

    /**
     * Delete the alMenityVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMenityVi : {}", id);
        alMenityViRepository.deleteById(id);
    }
}
