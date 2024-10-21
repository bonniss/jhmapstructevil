package ai.realworld.service;

import ai.realworld.domain.AlGoogleMeet;
import ai.realworld.repository.AlGoogleMeetRepository;
import ai.realworld.service.dto.AlGoogleMeetDTO;
import ai.realworld.service.mapper.AlGoogleMeetMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlGoogleMeet}.
 */
@Service
@Transactional
public class AlGoogleMeetService {

    private static final Logger LOG = LoggerFactory.getLogger(AlGoogleMeetService.class);

    private final AlGoogleMeetRepository alGoogleMeetRepository;

    private final AlGoogleMeetMapper alGoogleMeetMapper;

    public AlGoogleMeetService(AlGoogleMeetRepository alGoogleMeetRepository, AlGoogleMeetMapper alGoogleMeetMapper) {
        this.alGoogleMeetRepository = alGoogleMeetRepository;
        this.alGoogleMeetMapper = alGoogleMeetMapper;
    }

    /**
     * Save a alGoogleMeet.
     *
     * @param alGoogleMeetDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetDTO save(AlGoogleMeetDTO alGoogleMeetDTO) {
        LOG.debug("Request to save AlGoogleMeet : {}", alGoogleMeetDTO);
        AlGoogleMeet alGoogleMeet = alGoogleMeetMapper.toEntity(alGoogleMeetDTO);
        alGoogleMeet = alGoogleMeetRepository.save(alGoogleMeet);
        return alGoogleMeetMapper.toDto(alGoogleMeet);
    }

    /**
     * Update a alGoogleMeet.
     *
     * @param alGoogleMeetDTO the entity to save.
     * @return the persisted entity.
     */
    public AlGoogleMeetDTO update(AlGoogleMeetDTO alGoogleMeetDTO) {
        LOG.debug("Request to update AlGoogleMeet : {}", alGoogleMeetDTO);
        AlGoogleMeet alGoogleMeet = alGoogleMeetMapper.toEntity(alGoogleMeetDTO);
        alGoogleMeet = alGoogleMeetRepository.save(alGoogleMeet);
        return alGoogleMeetMapper.toDto(alGoogleMeet);
    }

    /**
     * Partially update a alGoogleMeet.
     *
     * @param alGoogleMeetDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlGoogleMeetDTO> partialUpdate(AlGoogleMeetDTO alGoogleMeetDTO) {
        LOG.debug("Request to partially update AlGoogleMeet : {}", alGoogleMeetDTO);

        return alGoogleMeetRepository
            .findById(alGoogleMeetDTO.getId())
            .map(existingAlGoogleMeet -> {
                alGoogleMeetMapper.partialUpdate(existingAlGoogleMeet, alGoogleMeetDTO);

                return existingAlGoogleMeet;
            })
            .map(alGoogleMeetRepository::save)
            .map(alGoogleMeetMapper::toDto);
    }

    /**
     * Get one alGoogleMeet by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlGoogleMeetDTO> findOne(UUID id) {
        LOG.debug("Request to get AlGoogleMeet : {}", id);
        return alGoogleMeetRepository.findById(id).map(alGoogleMeetMapper::toDto);
    }

    /**
     * Delete the alGoogleMeet by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlGoogleMeet : {}", id);
        alGoogleMeetRepository.deleteById(id);
    }
}
