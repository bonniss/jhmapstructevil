package ai.realworld.service;

import ai.realworld.domain.AlApple;
import ai.realworld.repository.AlAppleRepository;
import ai.realworld.service.dto.AlAppleDTO;
import ai.realworld.service.mapper.AlAppleMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlApple}.
 */
@Service
@Transactional
public class AlAppleService {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleService.class);

    private final AlAppleRepository alAppleRepository;

    private final AlAppleMapper alAppleMapper;

    public AlAppleService(AlAppleRepository alAppleRepository, AlAppleMapper alAppleMapper) {
        this.alAppleRepository = alAppleRepository;
        this.alAppleMapper = alAppleMapper;
    }

    /**
     * Save a alApple.
     *
     * @param alAppleDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAppleDTO save(AlAppleDTO alAppleDTO) {
        LOG.debug("Request to save AlApple : {}", alAppleDTO);
        AlApple alApple = alAppleMapper.toEntity(alAppleDTO);
        alApple = alAppleRepository.save(alApple);
        return alAppleMapper.toDto(alApple);
    }

    /**
     * Update a alApple.
     *
     * @param alAppleDTO the entity to save.
     * @return the persisted entity.
     */
    public AlAppleDTO update(AlAppleDTO alAppleDTO) {
        LOG.debug("Request to update AlApple : {}", alAppleDTO);
        AlApple alApple = alAppleMapper.toEntity(alAppleDTO);
        alApple = alAppleRepository.save(alApple);
        return alAppleMapper.toDto(alApple);
    }

    /**
     * Partially update a alApple.
     *
     * @param alAppleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlAppleDTO> partialUpdate(AlAppleDTO alAppleDTO) {
        LOG.debug("Request to partially update AlApple : {}", alAppleDTO);

        return alAppleRepository
            .findById(alAppleDTO.getId())
            .map(existingAlApple -> {
                alAppleMapper.partialUpdate(existingAlApple, alAppleDTO);

                return existingAlApple;
            })
            .map(alAppleRepository::save)
            .map(alAppleMapper::toDto);
    }

    /**
     * Get one alApple by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlAppleDTO> findOne(UUID id) {
        LOG.debug("Request to get AlApple : {}", id);
        return alAppleRepository.findById(id).map(alAppleMapper::toDto);
    }

    /**
     * Delete the alApple by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlApple : {}", id);
        alAppleRepository.deleteById(id);
    }
}
