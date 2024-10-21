package ai.realworld.service;

import ai.realworld.domain.AlGoogleMeetVi;
import ai.realworld.repository.AlGoogleMeetViRepository;
import ai.realworld.service.dto.AlGoogleMeetViDTO;
import ai.realworld.service.mapper.AlGoogleMeetViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoogleMeetVi}.
 */
@Service
@Transactional
public class AlGoogleMeetViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetViService.class);

    private final AlGoogleMeetViRepository alGoogleMeetViRepository;

    private final AlGoogleMeetViMapper alGoogleMeetViMapper;

    public AlGoogleMeetViService(AlGoogleMeetViRepository alGoogleMeetViRepository, AlGoogleMeetViMapper alGoogleMeetViMapper) {
        this.alGoogleMeetViRepository = alGoogleMeetViRepository;
        this.alGoogleMeetViMapper = alGoogleMeetViMapper;
    }

    /**
     * Save a alGoogleMeetVi.
     *
     * @param alGoogleMeetViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetViDTO save(AlGoogleMeetViDTO alGoogleMeetViDTO) {
        LOG.debug("Request to save AlGoogleMeetVi : {}", alGoogleMeetViDTO);
        AlGoogleMeetVi alGoogleMeetVi = alGoogleMeetViMapper.toEntity(alGoogleMeetViDTO);
        alGoogleMeetVi = alGoogleMeetViRepository.save(alGoogleMeetVi);
        return alGoogleMeetViMapper.toDto(alGoogleMeetVi);
    }

    /**
     * Update a alGoogleMeetVi.
     *
     * @param alGoogleMeetViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetViDTO update(AlGoogleMeetViDTO alGoogleMeetViDTO) {
        LOG.debug("Request to update AlGoogleMeetVi : {}", alGoogleMeetViDTO);
        AlGoogleMeetVi alGoogleMeetVi = alGoogleMeetViMapper.toEntity(alGoogleMeetViDTO);
        alGoogleMeetVi = alGoogleMeetViRepository.save(alGoogleMeetVi);
        return alGoogleMeetViMapper.toDto(alGoogleMeetVi);
    }

    /**
     * Partially update a alGoogleMeetVi.
     *
     * @param alGoogleMeetViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoogleMeetViDTO> partialUpdate(AlGoogleMeetViDTO alGoogleMeetViDTO) {
        LOG.debug("Request to partially update AlGoogleMeetVi : {}", alGoogleMeetViDTO);

        return alGoogleMeetViRepository
            .findById(alGoogleMeetViDTO.getId())
            .map(existingAlGoogleMeetVi -> {
                alGoogleMeetViMapper.partialUpdate(existingAlGoogleMeetVi, alGoogleMeetViDTO);

                return existingAlGoogleMeetVi;
            })
            .map(alGoogleMeetViRepository::save)
            .map(alGoogleMeetViMapper::toDto);
    }

    /**
     * Get one alGoogleMeetVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoogleMeetViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlGoogleMeetVi : {}", id);
        return alGoogleMeetViRepository.findById(id).map(alGoogleMeetViMapper::toDto);
    }

    /**
     * Delete the alGoogleMeetVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlGoogleMeetVi : {}", id);
        alGoogleMeetViRepository.deleteById(id);
    }
}
