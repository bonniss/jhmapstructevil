package ai.realworld.service;

import ai.realworld.domain.AlMenity;
import ai.realworld.repository.AlMenityRepository;
import ai.realworld.service.dto.AlMenityDTO;
import ai.realworld.service.mapper.AlMenityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlMenity}.
 */
@Service
@Transactional
public class AlMenityService {

    private static final Logger LOG = LoggerFactory.getLogger(AlMenityService.class);

    private final AlMenityRepository alMenityRepository;

    private final AlMenityMapper alMenityMapper;

    public AlMenityService(AlMenityRepository alMenityRepository, AlMenityMapper alMenityMapper) {
        this.alMenityRepository = alMenityRepository;
        this.alMenityMapper = alMenityMapper;
    }

    /**
     * Save a alMenity.
     *
     * @param alMenityDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMenityDTO save(AlMenityDTO alMenityDTO) {
        LOG.debug("Request to save AlMenity : {}", alMenityDTO);
        AlMenity alMenity = alMenityMapper.toEntity(alMenityDTO);
        alMenity = alMenityRepository.save(alMenity);
        return alMenityMapper.toDto(alMenity);
    }

    /**
     * Update a alMenity.
     *
     * @param alMenityDTO the entity to save.
     * @return the persisted entity.
     */
    public AlMenityDTO update(AlMenityDTO alMenityDTO) {
        LOG.debug("Request to update AlMenity : {}", alMenityDTO);
        AlMenity alMenity = alMenityMapper.toEntity(alMenityDTO);
        alMenity = alMenityRepository.save(alMenity);
        return alMenityMapper.toDto(alMenity);
    }

    /**
     * Partially update a alMenity.
     *
     * @param alMenityDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlMenityDTO> partialUpdate(AlMenityDTO alMenityDTO) {
        LOG.debug("Request to partially update AlMenity : {}", alMenityDTO);

        return alMenityRepository
            .findById(alMenityDTO.getId())
            .map(existingAlMenity -> {
                alMenityMapper.partialUpdate(existingAlMenity, alMenityDTO);

                return existingAlMenity;
            })
            .map(alMenityRepository::save)
            .map(alMenityMapper::toDto);
    }

    /**
     * Get one alMenity by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlMenityDTO> findOne(Long id) {
        LOG.debug("Request to get AlMenity : {}", id);
        return alMenityRepository.findById(id).map(alMenityMapper::toDto);
    }

    /**
     * Delete the alMenity by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlMenity : {}", id);
        alMenityRepository.deleteById(id);
    }
}
