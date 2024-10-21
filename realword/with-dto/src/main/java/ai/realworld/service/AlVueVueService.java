package ai.realworld.service;

import ai.realworld.domain.AlVueVue;
import ai.realworld.repository.AlVueVueRepository;
import ai.realworld.service.dto.AlVueVueDTO;
import ai.realworld.service.mapper.AlVueVueMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlVueVue}.
 */
@Service
@Transactional
public class AlVueVueService {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueService.class);

    private final AlVueVueRepository alVueVueRepository;

    private final AlVueVueMapper alVueVueMapper;

    public AlVueVueService(AlVueVueRepository alVueVueRepository, AlVueVueMapper alVueVueMapper) {
        this.alVueVueRepository = alVueVueRepository;
        this.alVueVueMapper = alVueVueMapper;
    }

    /**
     * Save a alVueVue.
     *
     * @param alVueVueDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueDTO save(AlVueVueDTO alVueVueDTO) {
        LOG.debug("Request to save AlVueVue : {}", alVueVueDTO);
        AlVueVue alVueVue = alVueVueMapper.toEntity(alVueVueDTO);
        alVueVue = alVueVueRepository.save(alVueVue);
        return alVueVueMapper.toDto(alVueVue);
    }

    /**
     * Update a alVueVue.
     *
     * @param alVueVueDTO the entity to save.
     * @return the persisted entity.
     */
    public AlVueVueDTO update(AlVueVueDTO alVueVueDTO) {
        LOG.debug("Request to update AlVueVue : {}", alVueVueDTO);
        AlVueVue alVueVue = alVueVueMapper.toEntity(alVueVueDTO);
        alVueVue = alVueVueRepository.save(alVueVue);
        return alVueVueMapper.toDto(alVueVue);
    }

    /**
     * Partially update a alVueVue.
     *
     * @param alVueVueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlVueVueDTO> partialUpdate(AlVueVueDTO alVueVueDTO) {
        LOG.debug("Request to partially update AlVueVue : {}", alVueVueDTO);

        return alVueVueRepository
            .findById(alVueVueDTO.getId())
            .map(existingAlVueVue -> {
                alVueVueMapper.partialUpdate(existingAlVueVue, alVueVueDTO);

                return existingAlVueVue;
            })
            .map(alVueVueRepository::save)
            .map(alVueVueMapper::toDto);
    }

    /**
     * Get one alVueVue by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlVueVueDTO> findOne(UUID id) {
        LOG.debug("Request to get AlVueVue : {}", id);
        return alVueVueRepository.findById(id).map(alVueVueMapper::toDto);
    }

    /**
     * Delete the alVueVue by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlVueVue : {}", id);
        alVueVueRepository.deleteById(id);
    }
}
