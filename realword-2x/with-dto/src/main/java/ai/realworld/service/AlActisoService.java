package ai.realworld.service;

import ai.realworld.domain.AlActiso;
import ai.realworld.repository.AlActisoRepository;
import ai.realworld.service.dto.AlActisoDTO;
import ai.realworld.service.mapper.AlActisoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlActiso}.
 */
@Service
@Transactional
public class AlActisoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlActisoService.class);

    private final AlActisoRepository alActisoRepository;

    private final AlActisoMapper alActisoMapper;

    public AlActisoService(AlActisoRepository alActisoRepository, AlActisoMapper alActisoMapper) {
        this.alActisoRepository = alActisoRepository;
        this.alActisoMapper = alActisoMapper;
    }

    /**
     * Save a alActiso.
     *
     * @param alActisoDTO the entity to save.
     * @return the persisted entity.
     */
    public AlActisoDTO save(AlActisoDTO alActisoDTO) {
        LOG.debug("Request to save AlActiso : {}", alActisoDTO);
        AlActiso alActiso = alActisoMapper.toEntity(alActisoDTO);
        alActiso = alActisoRepository.save(alActiso);
        return alActisoMapper.toDto(alActiso);
    }

    /**
     * Update a alActiso.
     *
     * @param alActisoDTO the entity to save.
     * @return the persisted entity.
     */
    public AlActisoDTO update(AlActisoDTO alActisoDTO) {
        LOG.debug("Request to update AlActiso : {}", alActisoDTO);
        AlActiso alActiso = alActisoMapper.toEntity(alActisoDTO);
        alActiso = alActisoRepository.save(alActiso);
        return alActisoMapper.toDto(alActiso);
    }

    /**
     * Partially update a alActiso.
     *
     * @param alActisoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlActisoDTO> partialUpdate(AlActisoDTO alActisoDTO) {
        LOG.debug("Request to partially update AlActiso : {}", alActisoDTO);

        return alActisoRepository
            .findById(alActisoDTO.getId())
            .map(existingAlActiso -> {
                alActisoMapper.partialUpdate(existingAlActiso, alActisoDTO);

                return existingAlActiso;
            })
            .map(alActisoRepository::save)
            .map(alActisoMapper::toDto);
    }

    /**
     * Get one alActiso by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlActisoDTO> findOne(Long id) {
        LOG.debug("Request to get AlActiso : {}", id);
        return alActisoRepository.findById(id).map(alActisoMapper::toDto);
    }

    /**
     * Delete the alActiso by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlActiso : {}", id);
        alActisoRepository.deleteById(id);
    }
}
