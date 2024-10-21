package ai.realworld.service;

import ai.realworld.domain.AlSherMale;
import ai.realworld.repository.AlSherMaleRepository;
import ai.realworld.service.dto.AlSherMaleDTO;
import ai.realworld.service.mapper.AlSherMaleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlSherMale}.
 */
@Service
@Transactional
public class AlSherMaleService {

    private static final Logger LOG = LoggerFactory.getLogger(AlSherMaleService.class);

    private final AlSherMaleRepository alSherMaleRepository;

    private final AlSherMaleMapper alSherMaleMapper;

    public AlSherMaleService(AlSherMaleRepository alSherMaleRepository, AlSherMaleMapper alSherMaleMapper) {
        this.alSherMaleRepository = alSherMaleRepository;
        this.alSherMaleMapper = alSherMaleMapper;
    }

    /**
     * Save a alSherMale.
     *
     * @param alSherMaleDTO the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleDTO save(AlSherMaleDTO alSherMaleDTO) {
        LOG.debug("Request to save AlSherMale : {}", alSherMaleDTO);
        AlSherMale alSherMale = alSherMaleMapper.toEntity(alSherMaleDTO);
        alSherMale = alSherMaleRepository.save(alSherMale);
        return alSherMaleMapper.toDto(alSherMale);
    }

    /**
     * Update a alSherMale.
     *
     * @param alSherMaleDTO the entity to save.
     * @return the persisted entity.
     */
    public AlSherMaleDTO update(AlSherMaleDTO alSherMaleDTO) {
        LOG.debug("Request to update AlSherMale : {}", alSherMaleDTO);
        AlSherMale alSherMale = alSherMaleMapper.toEntity(alSherMaleDTO);
        alSherMale = alSherMaleRepository.save(alSherMale);
        return alSherMaleMapper.toDto(alSherMale);
    }

    /**
     * Partially update a alSherMale.
     *
     * @param alSherMaleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlSherMaleDTO> partialUpdate(AlSherMaleDTO alSherMaleDTO) {
        LOG.debug("Request to partially update AlSherMale : {}", alSherMaleDTO);

        return alSherMaleRepository
            .findById(alSherMaleDTO.getId())
            .map(existingAlSherMale -> {
                alSherMaleMapper.partialUpdate(existingAlSherMale, alSherMaleDTO);

                return existingAlSherMale;
            })
            .map(alSherMaleRepository::save)
            .map(alSherMaleMapper::toDto);
    }

    /**
     * Get one alSherMale by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlSherMaleDTO> findOne(Long id) {
        LOG.debug("Request to get AlSherMale : {}", id);
        return alSherMaleRepository.findById(id).map(alSherMaleMapper::toDto);
    }

    /**
     * Delete the alSherMale by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlSherMale : {}", id);
        alSherMaleRepository.deleteById(id);
    }
}
