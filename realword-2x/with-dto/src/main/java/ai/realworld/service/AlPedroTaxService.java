package ai.realworld.service;

import ai.realworld.domain.AlPedroTax;
import ai.realworld.repository.AlPedroTaxRepository;
import ai.realworld.service.dto.AlPedroTaxDTO;
import ai.realworld.service.mapper.AlPedroTaxMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPedroTax}.
 */
@Service
@Transactional
public class AlPedroTaxService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxService.class);

    private final AlPedroTaxRepository alPedroTaxRepository;

    private final AlPedroTaxMapper alPedroTaxMapper;

    public AlPedroTaxService(AlPedroTaxRepository alPedroTaxRepository, AlPedroTaxMapper alPedroTaxMapper) {
        this.alPedroTaxRepository = alPedroTaxRepository;
        this.alPedroTaxMapper = alPedroTaxMapper;
    }

    /**
     * Save a alPedroTax.
     *
     * @param alPedroTaxDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxDTO save(AlPedroTaxDTO alPedroTaxDTO) {
        LOG.debug("Request to save AlPedroTax : {}", alPedroTaxDTO);
        AlPedroTax alPedroTax = alPedroTaxMapper.toEntity(alPedroTaxDTO);
        alPedroTax = alPedroTaxRepository.save(alPedroTax);
        return alPedroTaxMapper.toDto(alPedroTax);
    }

    /**
     * Update a alPedroTax.
     *
     * @param alPedroTaxDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPedroTaxDTO update(AlPedroTaxDTO alPedroTaxDTO) {
        LOG.debug("Request to update AlPedroTax : {}", alPedroTaxDTO);
        AlPedroTax alPedroTax = alPedroTaxMapper.toEntity(alPedroTaxDTO);
        alPedroTax = alPedroTaxRepository.save(alPedroTax);
        return alPedroTaxMapper.toDto(alPedroTax);
    }

    /**
     * Partially update a alPedroTax.
     *
     * @param alPedroTaxDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPedroTaxDTO> partialUpdate(AlPedroTaxDTO alPedroTaxDTO) {
        LOG.debug("Request to partially update AlPedroTax : {}", alPedroTaxDTO);

        return alPedroTaxRepository
            .findById(alPedroTaxDTO.getId())
            .map(existingAlPedroTax -> {
                alPedroTaxMapper.partialUpdate(existingAlPedroTax, alPedroTaxDTO);

                return existingAlPedroTax;
            })
            .map(alPedroTaxRepository::save)
            .map(alPedroTaxMapper::toDto);
    }

    /**
     * Get one alPedroTax by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPedroTaxDTO> findOne(Long id) {
        LOG.debug("Request to get AlPedroTax : {}", id);
        return alPedroTaxRepository.findById(id).map(alPedroTaxMapper::toDto);
    }

    /**
     * Delete the alPedroTax by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPedroTax : {}", id);
        alPedroTaxRepository.deleteById(id);
    }
}
