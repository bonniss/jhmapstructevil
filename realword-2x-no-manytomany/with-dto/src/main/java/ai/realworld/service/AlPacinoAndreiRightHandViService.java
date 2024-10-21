package ai.realworld.service;

import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import ai.realworld.service.dto.AlPacinoAndreiRightHandViDTO;
import ai.realworld.service.mapper.AlPacinoAndreiRightHandViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoAndreiRightHandVi}.
 */
@Service
@Transactional
public class AlPacinoAndreiRightHandViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandViService.class);

    private final AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    private final AlPacinoAndreiRightHandViMapper alPacinoAndreiRightHandViMapper;

    public AlPacinoAndreiRightHandViService(
        AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository,
        AlPacinoAndreiRightHandViMapper alPacinoAndreiRightHandViMapper
    ) {
        this.alPacinoAndreiRightHandViRepository = alPacinoAndreiRightHandViRepository;
        this.alPacinoAndreiRightHandViMapper = alPacinoAndreiRightHandViMapper;
    }

    /**
     * Save a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandViDTO save(AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO) {
        LOG.debug("Request to save AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandViDTO);
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViMapper.toEntity(alPacinoAndreiRightHandViDTO);
        alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.save(alPacinoAndreiRightHandVi);
        return alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);
    }

    /**
     * Update a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoAndreiRightHandViDTO update(AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO) {
        LOG.debug("Request to update AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandViDTO);
        AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViMapper.toEntity(alPacinoAndreiRightHandViDTO);
        alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViRepository.save(alPacinoAndreiRightHandVi);
        return alPacinoAndreiRightHandViMapper.toDto(alPacinoAndreiRightHandVi);
    }

    /**
     * Partially update a alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoAndreiRightHandViDTO> partialUpdate(AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO) {
        LOG.debug("Request to partially update AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandViDTO);

        return alPacinoAndreiRightHandViRepository
            .findById(alPacinoAndreiRightHandViDTO.getId())
            .map(existingAlPacinoAndreiRightHandVi -> {
                alPacinoAndreiRightHandViMapper.partialUpdate(existingAlPacinoAndreiRightHandVi, alPacinoAndreiRightHandViDTO);

                return existingAlPacinoAndreiRightHandVi;
            })
            .map(alPacinoAndreiRightHandViRepository::save)
            .map(alPacinoAndreiRightHandViMapper::toDto);
    }

    /**
     * Get one alPacinoAndreiRightHandVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoAndreiRightHandViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPacinoAndreiRightHandVi : {}", id);
        return alPacinoAndreiRightHandViRepository.findById(id).map(alPacinoAndreiRightHandViMapper::toDto);
    }

    /**
     * Delete the alPacinoAndreiRightHandVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoAndreiRightHandVi : {}", id);
        alPacinoAndreiRightHandViRepository.deleteById(id);
    }
}
