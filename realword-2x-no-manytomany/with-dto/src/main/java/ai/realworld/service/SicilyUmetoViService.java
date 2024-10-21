package ai.realworld.service;

import ai.realworld.domain.SicilyUmetoVi;
import ai.realworld.repository.SicilyUmetoViRepository;
import ai.realworld.service.dto.SicilyUmetoViDTO;
import ai.realworld.service.mapper.SicilyUmetoViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SicilyUmetoVi}.
 */
@Service
@Transactional
public class SicilyUmetoViService {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoViService.class);

    private final SicilyUmetoViRepository sicilyUmetoViRepository;

    private final SicilyUmetoViMapper sicilyUmetoViMapper;

    public SicilyUmetoViService(SicilyUmetoViRepository sicilyUmetoViRepository, SicilyUmetoViMapper sicilyUmetoViMapper) {
        this.sicilyUmetoViRepository = sicilyUmetoViRepository;
        this.sicilyUmetoViMapper = sicilyUmetoViMapper;
    }

    /**
     * Save a sicilyUmetoVi.
     *
     * @param sicilyUmetoViDTO the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoViDTO save(SicilyUmetoViDTO sicilyUmetoViDTO) {
        LOG.debug("Request to save SicilyUmetoVi : {}", sicilyUmetoViDTO);
        SicilyUmetoVi sicilyUmetoVi = sicilyUmetoViMapper.toEntity(sicilyUmetoViDTO);
        sicilyUmetoVi = sicilyUmetoViRepository.save(sicilyUmetoVi);
        return sicilyUmetoViMapper.toDto(sicilyUmetoVi);
    }

    /**
     * Update a sicilyUmetoVi.
     *
     * @param sicilyUmetoViDTO the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoViDTO update(SicilyUmetoViDTO sicilyUmetoViDTO) {
        LOG.debug("Request to update SicilyUmetoVi : {}", sicilyUmetoViDTO);
        SicilyUmetoVi sicilyUmetoVi = sicilyUmetoViMapper.toEntity(sicilyUmetoViDTO);
        sicilyUmetoVi = sicilyUmetoViRepository.save(sicilyUmetoVi);
        return sicilyUmetoViMapper.toDto(sicilyUmetoVi);
    }

    /**
     * Partially update a sicilyUmetoVi.
     *
     * @param sicilyUmetoViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SicilyUmetoViDTO> partialUpdate(SicilyUmetoViDTO sicilyUmetoViDTO) {
        LOG.debug("Request to partially update SicilyUmetoVi : {}", sicilyUmetoViDTO);

        return sicilyUmetoViRepository
            .findById(sicilyUmetoViDTO.getId())
            .map(existingSicilyUmetoVi -> {
                sicilyUmetoViMapper.partialUpdate(existingSicilyUmetoVi, sicilyUmetoViDTO);

                return existingSicilyUmetoVi;
            })
            .map(sicilyUmetoViRepository::save)
            .map(sicilyUmetoViMapper::toDto);
    }

    /**
     * Get one sicilyUmetoVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SicilyUmetoViDTO> findOne(Long id) {
        LOG.debug("Request to get SicilyUmetoVi : {}", id);
        return sicilyUmetoViRepository.findById(id).map(sicilyUmetoViMapper::toDto);
    }

    /**
     * Delete the sicilyUmetoVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SicilyUmetoVi : {}", id);
        sicilyUmetoViRepository.deleteById(id);
    }
}
