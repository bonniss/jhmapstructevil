package ai.realworld.service;

import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.repository.AlPedroTaxViRepository;
import ai.realworld.service.dto.AlPedroTaxViDTO;
import ai.realworld.service.mapper.AlPedroTaxViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPedroTaxVi}.
 */
@Service
@Transactional
public class AlPedroTaxViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxViService.class);

    private final AlPedroTaxViRepository alPedroTaxViRepository;

    private final AlPedroTaxViMapper alPedroTaxViMapper;

    public AlPedroTaxViService(AlPedroTaxViRepository alPedroTaxViRepository, AlPedroTaxViMapper alPedroTaxViMapper) {
        this.alPedroTaxViRepository = alPedroTaxViRepository;
        this.alPedroTaxViMapper = alPedroTaxViMapper;
    }

    /**
     * Save a alPedroTaxVi.
     *
     * @param alPedroTaxViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxViDTO save(AlPedroTaxViDTO alPedroTaxViDTO) {
        LOG.debug("Request to save AlPedroTaxVi : {}", alPedroTaxViDTO);
        AlPedroTaxVi alPedroTaxVi = alPedroTaxViMapper.toEntity(alPedroTaxViDTO);
        alPedroTaxVi = alPedroTaxViRepository.save(alPedroTaxVi);
        return alPedroTaxViMapper.toDto(alPedroTaxVi);
    }

    /**
     * Update a alPedroTaxVi.
     *
     * @param alPedroTaxViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxViDTO update(AlPedroTaxViDTO alPedroTaxViDTO) {
        LOG.debug("Request to update AlPedroTaxVi : {}", alPedroTaxViDTO);
        AlPedroTaxVi alPedroTaxVi = alPedroTaxViMapper.toEntity(alPedroTaxViDTO);
        alPedroTaxVi = alPedroTaxViRepository.save(alPedroTaxVi);
        return alPedroTaxViMapper.toDto(alPedroTaxVi);
    }

    /**
     * Partially update a alPedroTaxVi.
     *
     * @param alPedroTaxViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPedroTaxViDTO> partialUpdate(AlPedroTaxViDTO alPedroTaxViDTO) {
        LOG.debug("Request to partially update AlPedroTaxVi : {}", alPedroTaxViDTO);

        return alPedroTaxViRepository
            .findById(alPedroTaxViDTO.getId())
            .map(existingAlPedroTaxVi -> {
                alPedroTaxViMapper.partialUpdate(existingAlPedroTaxVi, alPedroTaxViDTO);

                return existingAlPedroTaxVi;
            })
            .map(alPedroTaxViRepository::save)
            .map(alPedroTaxViMapper::toDto);
    }

    /**
     * Get one alPedroTaxVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPedroTaxViDTO> findOne(Long id) {
        LOG.debug("Request to get AlPedroTaxVi : {}", id);
        return alPedroTaxViRepository.findById(id).map(alPedroTaxViMapper::toDto);
    }

    /**
     * Delete the alPedroTaxVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPedroTaxVi : {}", id);
        alPedroTaxViRepository.deleteById(id);
    }
}
