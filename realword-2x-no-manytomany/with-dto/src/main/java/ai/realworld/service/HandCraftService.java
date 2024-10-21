package ai.realworld.service;

import ai.realworld.domain.HandCraft;
import ai.realworld.repository.HandCraftRepository;
import ai.realworld.service.dto.HandCraftDTO;
import ai.realworld.service.mapper.HandCraftMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HandCraft}.
 */
@Service
@Transactional
public class HandCraftService {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftService.class);

    private final HandCraftRepository handCraftRepository;

    private final HandCraftMapper handCraftMapper;

    public HandCraftService(HandCraftRepository handCraftRepository, HandCraftMapper handCraftMapper) {
        this.handCraftRepository = handCraftRepository;
        this.handCraftMapper = handCraftMapper;
    }

    /**
     * Save a handCraft.
     *
     * @param handCraftDTO the entity to save.
     * @return the persisted entity.
     */
    public HandCraftDTO save(HandCraftDTO handCraftDTO) {
        LOG.debug("Request to save HandCraft : {}", handCraftDTO);
        HandCraft handCraft = handCraftMapper.toEntity(handCraftDTO);
        handCraft = handCraftRepository.save(handCraft);
        return handCraftMapper.toDto(handCraft);
    }

    /**
     * Update a handCraft.
     *
     * @param handCraftDTO the entity to save.
     * @return the persisted entity.
     */
    public HandCraftDTO update(HandCraftDTO handCraftDTO) {
        LOG.debug("Request to update HandCraft : {}", handCraftDTO);
        HandCraft handCraft = handCraftMapper.toEntity(handCraftDTO);
        handCraft = handCraftRepository.save(handCraft);
        return handCraftMapper.toDto(handCraft);
    }

    /**
     * Partially update a handCraft.
     *
     * @param handCraftDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HandCraftDTO> partialUpdate(HandCraftDTO handCraftDTO) {
        LOG.debug("Request to partially update HandCraft : {}", handCraftDTO);

        return handCraftRepository
            .findById(handCraftDTO.getId())
            .map(existingHandCraft -> {
                handCraftMapper.partialUpdate(existingHandCraft, handCraftDTO);

                return existingHandCraft;
            })
            .map(handCraftRepository::save)
            .map(handCraftMapper::toDto);
    }

    /**
     * Get one handCraft by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HandCraftDTO> findOne(Long id) {
        LOG.debug("Request to get HandCraft : {}", id);
        return handCraftRepository.findById(id).map(handCraftMapper::toDto);
    }

    /**
     * Delete the handCraft by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HandCraft : {}", id);
        handCraftRepository.deleteById(id);
    }
}
