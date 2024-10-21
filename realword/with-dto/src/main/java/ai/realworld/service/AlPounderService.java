package ai.realworld.service;

import ai.realworld.domain.AlPounder;
import ai.realworld.repository.AlPounderRepository;
import ai.realworld.service.dto.AlPounderDTO;
import ai.realworld.service.mapper.AlPounderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPounder}.
 */
@Service
@Transactional
public class AlPounderService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPounderService.class);

    private final AlPounderRepository alPounderRepository;

    private final AlPounderMapper alPounderMapper;

    public AlPounderService(AlPounderRepository alPounderRepository, AlPounderMapper alPounderMapper) {
        this.alPounderRepository = alPounderRepository;
        this.alPounderMapper = alPounderMapper;
    }

    /**
     * Save a alPounder.
     *
     * @param alPounderDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPounderDTO save(AlPounderDTO alPounderDTO) {
        LOG.debug("Request to save AlPounder : {}", alPounderDTO);
        AlPounder alPounder = alPounderMapper.toEntity(alPounderDTO);
        alPounder = alPounderRepository.save(alPounder);
        return alPounderMapper.toDto(alPounder);
    }

    /**
     * Update a alPounder.
     *
     * @param alPounderDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPounderDTO update(AlPounderDTO alPounderDTO) {
        LOG.debug("Request to update AlPounder : {}", alPounderDTO);
        AlPounder alPounder = alPounderMapper.toEntity(alPounderDTO);
        alPounder = alPounderRepository.save(alPounder);
        return alPounderMapper.toDto(alPounder);
    }

    /**
     * Partially update a alPounder.
     *
     * @param alPounderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPounderDTO> partialUpdate(AlPounderDTO alPounderDTO) {
        LOG.debug("Request to partially update AlPounder : {}", alPounderDTO);

        return alPounderRepository
            .findById(alPounderDTO.getId())
            .map(existingAlPounder -> {
                alPounderMapper.partialUpdate(existingAlPounder, alPounderDTO);

                return existingAlPounder;
            })
            .map(alPounderRepository::save)
            .map(alPounderMapper::toDto);
    }

    /**
     * Get one alPounder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPounderDTO> findOne(Long id) {
        LOG.debug("Request to get AlPounder : {}", id);
        return alPounderRepository.findById(id).map(alPounderMapper::toDto);
    }

    /**
     * Delete the alPounder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPounder : {}", id);
        alPounderRepository.deleteById(id);
    }
}
