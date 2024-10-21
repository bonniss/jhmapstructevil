package ai.realworld.service;

import ai.realworld.domain.HandCraftVi;
import ai.realworld.repository.HandCraftViRepository;
import ai.realworld.service.dto.HandCraftViDTO;
import ai.realworld.service.mapper.HandCraftViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HandCraftVi}.
 */
@Service
@Transactional
public class HandCraftViService {

    private static final Logger LOG = LoggerFactory.getLogger(HandCraftViService.class);

    private final HandCraftViRepository handCraftViRepository;

    private final HandCraftViMapper handCraftViMapper;

    public HandCraftViService(HandCraftViRepository handCraftViRepository, HandCraftViMapper handCraftViMapper) {
        this.handCraftViRepository = handCraftViRepository;
        this.handCraftViMapper = handCraftViMapper;
    }

    /**
     * Save a handCraftVi.
     *
     * @param handCraftViDTO the entity to save.
     * @return the persisted entity.
     */
    public HandCraftViDTO save(HandCraftViDTO handCraftViDTO) {
        LOG.debug("Request to save HandCraftVi : {}", handCraftViDTO);
        HandCraftVi handCraftVi = handCraftViMapper.toEntity(handCraftViDTO);
        handCraftVi = handCraftViRepository.save(handCraftVi);
        return handCraftViMapper.toDto(handCraftVi);
    }

    /**
     * Update a handCraftVi.
     *
     * @param handCraftViDTO the entity to save.
     * @return the persisted entity.
     */
    public HandCraftViDTO update(HandCraftViDTO handCraftViDTO) {
        LOG.debug("Request to update HandCraftVi : {}", handCraftViDTO);
        HandCraftVi handCraftVi = handCraftViMapper.toEntity(handCraftViDTO);
        handCraftVi = handCraftViRepository.save(handCraftVi);
        return handCraftViMapper.toDto(handCraftVi);
    }

    /**
     * Partially update a handCraftVi.
     *
     * @param handCraftViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HandCraftViDTO> partialUpdate(HandCraftViDTO handCraftViDTO) {
        LOG.debug("Request to partially update HandCraftVi : {}", handCraftViDTO);

        return handCraftViRepository
            .findById(handCraftViDTO.getId())
            .map(existingHandCraftVi -> {
                handCraftViMapper.partialUpdate(existingHandCraftVi, handCraftViDTO);

                return existingHandCraftVi;
            })
            .map(handCraftViRepository::save)
            .map(handCraftViMapper::toDto);
    }

    /**
     * Get one handCraftVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HandCraftViDTO> findOne(Long id) {
        LOG.debug("Request to get HandCraftVi : {}", id);
        return handCraftViRepository.findById(id).map(handCraftViMapper::toDto);
    }

    /**
     * Delete the handCraftVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HandCraftVi : {}", id);
        handCraftViRepository.deleteById(id);
    }
}
