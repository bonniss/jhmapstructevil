package ai.realworld.service;

import ai.realworld.domain.AndreiRightHand;
import ai.realworld.repository.AndreiRightHandRepository;
import ai.realworld.service.dto.AndreiRightHandDTO;
import ai.realworld.service.mapper.AndreiRightHandMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AndreiRightHand}.
 */
@Service
@Transactional
public class AndreiRightHandService {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandService.class);

    private final AndreiRightHandRepository andreiRightHandRepository;

    private final AndreiRightHandMapper andreiRightHandMapper;

    public AndreiRightHandService(AndreiRightHandRepository andreiRightHandRepository, AndreiRightHandMapper andreiRightHandMapper) {
        this.andreiRightHandRepository = andreiRightHandRepository;
        this.andreiRightHandMapper = andreiRightHandMapper;
    }

    /**
     * Save a andreiRightHand.
     *
     * @param andreiRightHandDTO the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandDTO save(AndreiRightHandDTO andreiRightHandDTO) {
        LOG.debug("Request to save AndreiRightHand : {}", andreiRightHandDTO);
        AndreiRightHand andreiRightHand = andreiRightHandMapper.toEntity(andreiRightHandDTO);
        andreiRightHand = andreiRightHandRepository.save(andreiRightHand);
        return andreiRightHandMapper.toDto(andreiRightHand);
    }

    /**
     * Update a andreiRightHand.
     *
     * @param andreiRightHandDTO the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandDTO update(AndreiRightHandDTO andreiRightHandDTO) {
        LOG.debug("Request to update AndreiRightHand : {}", andreiRightHandDTO);
        AndreiRightHand andreiRightHand = andreiRightHandMapper.toEntity(andreiRightHandDTO);
        andreiRightHand = andreiRightHandRepository.save(andreiRightHand);
        return andreiRightHandMapper.toDto(andreiRightHand);
    }

    /**
     * Partially update a andreiRightHand.
     *
     * @param andreiRightHandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AndreiRightHandDTO> partialUpdate(AndreiRightHandDTO andreiRightHandDTO) {
        LOG.debug("Request to partially update AndreiRightHand : {}", andreiRightHandDTO);

        return andreiRightHandRepository
            .findById(andreiRightHandDTO.getId())
            .map(existingAndreiRightHand -> {
                andreiRightHandMapper.partialUpdate(existingAndreiRightHand, andreiRightHandDTO);

                return existingAndreiRightHand;
            })
            .map(andreiRightHandRepository::save)
            .map(andreiRightHandMapper::toDto);
    }

    /**
     * Get one andreiRightHand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AndreiRightHandDTO> findOne(Long id) {
        LOG.debug("Request to get AndreiRightHand : {}", id);
        return andreiRightHandRepository.findById(id).map(andreiRightHandMapper::toDto);
    }

    /**
     * Delete the andreiRightHand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AndreiRightHand : {}", id);
        andreiRightHandRepository.deleteById(id);
    }
}
