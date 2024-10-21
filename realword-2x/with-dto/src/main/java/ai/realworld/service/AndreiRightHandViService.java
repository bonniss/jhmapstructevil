package ai.realworld.service;

import ai.realworld.domain.AndreiRightHandVi;
import ai.realworld.repository.AndreiRightHandViRepository;
import ai.realworld.service.dto.AndreiRightHandViDTO;
import ai.realworld.service.mapper.AndreiRightHandViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AndreiRightHandVi}.
 */
@Service
@Transactional
public class AndreiRightHandViService {

    private static final Logger LOG = LoggerFactory.getLogger(AndreiRightHandViService.class);

    private final AndreiRightHandViRepository andreiRightHandViRepository;

    private final AndreiRightHandViMapper andreiRightHandViMapper;

    public AndreiRightHandViService(
        AndreiRightHandViRepository andreiRightHandViRepository,
        AndreiRightHandViMapper andreiRightHandViMapper
    ) {
        this.andreiRightHandViRepository = andreiRightHandViRepository;
        this.andreiRightHandViMapper = andreiRightHandViMapper;
    }

    /**
     * Save a andreiRightHandVi.
     *
     * @param andreiRightHandViDTO the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandViDTO save(AndreiRightHandViDTO andreiRightHandViDTO) {
        LOG.debug("Request to save AndreiRightHandVi : {}", andreiRightHandViDTO);
        AndreiRightHandVi andreiRightHandVi = andreiRightHandViMapper.toEntity(andreiRightHandViDTO);
        andreiRightHandVi = andreiRightHandViRepository.save(andreiRightHandVi);
        return andreiRightHandViMapper.toDto(andreiRightHandVi);
    }

    /**
     * Update a andreiRightHandVi.
     *
     * @param andreiRightHandViDTO the entity to save.
     * @return the persisted entity.
     */
    public AndreiRightHandViDTO update(AndreiRightHandViDTO andreiRightHandViDTO) {
        LOG.debug("Request to update AndreiRightHandVi : {}", andreiRightHandViDTO);
        AndreiRightHandVi andreiRightHandVi = andreiRightHandViMapper.toEntity(andreiRightHandViDTO);
        andreiRightHandVi = andreiRightHandViRepository.save(andreiRightHandVi);
        return andreiRightHandViMapper.toDto(andreiRightHandVi);
    }

    /**
     * Partially update a andreiRightHandVi.
     *
     * @param andreiRightHandViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AndreiRightHandViDTO> partialUpdate(AndreiRightHandViDTO andreiRightHandViDTO) {
        LOG.debug("Request to partially update AndreiRightHandVi : {}", andreiRightHandViDTO);

        return andreiRightHandViRepository
            .findById(andreiRightHandViDTO.getId())
            .map(existingAndreiRightHandVi -> {
                andreiRightHandViMapper.partialUpdate(existingAndreiRightHandVi, andreiRightHandViDTO);

                return existingAndreiRightHandVi;
            })
            .map(andreiRightHandViRepository::save)
            .map(andreiRightHandViMapper::toDto);
    }

    /**
     * Get one andreiRightHandVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AndreiRightHandViDTO> findOne(Long id) {
        LOG.debug("Request to get AndreiRightHandVi : {}", id);
        return andreiRightHandViRepository.findById(id).map(andreiRightHandViMapper::toDto);
    }

    /**
     * Delete the andreiRightHandVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AndreiRightHandVi : {}", id);
        andreiRightHandViRepository.deleteById(id);
    }
}
