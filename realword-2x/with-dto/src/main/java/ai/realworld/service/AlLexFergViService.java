package ai.realworld.service;

import ai.realworld.domain.AlLexFergVi;
import ai.realworld.repository.AlLexFergViRepository;
import ai.realworld.service.dto.AlLexFergViDTO;
import ai.realworld.service.mapper.AlLexFergViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlLexFergVi}.
 */
@Service
@Transactional
public class AlLexFergViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergViService.class);

    private final AlLexFergViRepository alLexFergViRepository;

    private final AlLexFergViMapper alLexFergViMapper;

    public AlLexFergViService(AlLexFergViRepository alLexFergViRepository, AlLexFergViMapper alLexFergViMapper) {
        this.alLexFergViRepository = alLexFergViRepository;
        this.alLexFergViMapper = alLexFergViMapper;
    }

    /**
     * Save a alLexFergVi.
     *
     * @param alLexFergViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergViDTO save(AlLexFergViDTO alLexFergViDTO) {
        LOG.debug("Request to save AlLexFergVi : {}", alLexFergViDTO);
        AlLexFergVi alLexFergVi = alLexFergViMapper.toEntity(alLexFergViDTO);
        alLexFergVi = alLexFergViRepository.save(alLexFergVi);
        return alLexFergViMapper.toDto(alLexFergVi);
    }

    /**
     * Update a alLexFergVi.
     *
     * @param alLexFergViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlLexFergViDTO update(AlLexFergViDTO alLexFergViDTO) {
        LOG.debug("Request to update AlLexFergVi : {}", alLexFergViDTO);
        AlLexFergVi alLexFergVi = alLexFergViMapper.toEntity(alLexFergViDTO);
        alLexFergVi = alLexFergViRepository.save(alLexFergVi);
        return alLexFergViMapper.toDto(alLexFergVi);
    }

    /**
     * Partially update a alLexFergVi.
     *
     * @param alLexFergViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlLexFergViDTO> partialUpdate(AlLexFergViDTO alLexFergViDTO) {
        LOG.debug("Request to partially update AlLexFergVi : {}", alLexFergViDTO);

        return alLexFergViRepository
            .findById(alLexFergViDTO.getId())
            .map(existingAlLexFergVi -> {
                alLexFergViMapper.partialUpdate(existingAlLexFergVi, alLexFergViDTO);

                return existingAlLexFergVi;
            })
            .map(alLexFergViRepository::save)
            .map(alLexFergViMapper::toDto);
    }

    /**
     * Get one alLexFergVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlLexFergViDTO> findOne(Long id) {
        LOG.debug("Request to get AlLexFergVi : {}", id);
        return alLexFergViRepository.findById(id).map(alLexFergViMapper::toDto);
    }

    /**
     * Delete the alLexFergVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlLexFergVi : {}", id);
        alLexFergViRepository.deleteById(id);
    }
}
