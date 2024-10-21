package ai.realworld.service;

import ai.realworld.domain.SicilyUmeto;
import ai.realworld.repository.SicilyUmetoRepository;
import ai.realworld.service.dto.SicilyUmetoDTO;
import ai.realworld.service.mapper.SicilyUmetoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.SicilyUmeto}.
 */
@Service
@Transactional
public class SicilyUmetoService {

    private static final Logger LOG = LoggerFactory.getLogger(SicilyUmetoService.class);

    private final SicilyUmetoRepository sicilyUmetoRepository;

    private final SicilyUmetoMapper sicilyUmetoMapper;

    public SicilyUmetoService(SicilyUmetoRepository sicilyUmetoRepository, SicilyUmetoMapper sicilyUmetoMapper) {
        this.sicilyUmetoRepository = sicilyUmetoRepository;
        this.sicilyUmetoMapper = sicilyUmetoMapper;
    }

    /**
     * Save a sicilyUmeto.
     *
     * @param sicilyUmetoDTO the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoDTO save(SicilyUmetoDTO sicilyUmetoDTO) {
        LOG.debug("Request to save SicilyUmeto : {}", sicilyUmetoDTO);
        SicilyUmeto sicilyUmeto = sicilyUmetoMapper.toEntity(sicilyUmetoDTO);
        sicilyUmeto = sicilyUmetoRepository.save(sicilyUmeto);
        return sicilyUmetoMapper.toDto(sicilyUmeto);
    }

    /**
     * Update a sicilyUmeto.
     *
     * @param sicilyUmetoDTO the entity to save.
     * @return the persisted entity.
     */
    public SicilyUmetoDTO update(SicilyUmetoDTO sicilyUmetoDTO) {
        LOG.debug("Request to update SicilyUmeto : {}", sicilyUmetoDTO);
        SicilyUmeto sicilyUmeto = sicilyUmetoMapper.toEntity(sicilyUmetoDTO);
        sicilyUmeto = sicilyUmetoRepository.save(sicilyUmeto);
        return sicilyUmetoMapper.toDto(sicilyUmeto);
    }

    /**
     * Partially update a sicilyUmeto.
     *
     * @param sicilyUmetoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SicilyUmetoDTO> partialUpdate(SicilyUmetoDTO sicilyUmetoDTO) {
        LOG.debug("Request to partially update SicilyUmeto : {}", sicilyUmetoDTO);

        return sicilyUmetoRepository
            .findById(sicilyUmetoDTO.getId())
            .map(existingSicilyUmeto -> {
                sicilyUmetoMapper.partialUpdate(existingSicilyUmeto, sicilyUmetoDTO);

                return existingSicilyUmeto;
            })
            .map(sicilyUmetoRepository::save)
            .map(sicilyUmetoMapper::toDto);
    }

    /**
     * Get one sicilyUmeto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SicilyUmetoDTO> findOne(Long id) {
        LOG.debug("Request to get SicilyUmeto : {}", id);
        return sicilyUmetoRepository.findById(id).map(sicilyUmetoMapper::toDto);
    }

    /**
     * Delete the sicilyUmeto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SicilyUmeto : {}", id);
        sicilyUmetoRepository.deleteById(id);
    }
}
