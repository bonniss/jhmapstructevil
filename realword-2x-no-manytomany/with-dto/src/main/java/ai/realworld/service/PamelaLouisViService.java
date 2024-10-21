package ai.realworld.service;

import ai.realworld.domain.PamelaLouisVi;
import ai.realworld.repository.PamelaLouisViRepository;
import ai.realworld.service.dto.PamelaLouisViDTO;
import ai.realworld.service.mapper.PamelaLouisViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.PamelaLouisVi}.
 */
@Service
@Transactional
public class PamelaLouisViService {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisViService.class);

    private final PamelaLouisViRepository pamelaLouisViRepository;

    private final PamelaLouisViMapper pamelaLouisViMapper;

    public PamelaLouisViService(PamelaLouisViRepository pamelaLouisViRepository, PamelaLouisViMapper pamelaLouisViMapper) {
        this.pamelaLouisViRepository = pamelaLouisViRepository;
        this.pamelaLouisViMapper = pamelaLouisViMapper;
    }

    /**
     * Save a pamelaLouisVi.
     *
     * @param pamelaLouisViDTO the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisViDTO save(PamelaLouisViDTO pamelaLouisViDTO) {
        LOG.debug("Request to save PamelaLouisVi : {}", pamelaLouisViDTO);
        PamelaLouisVi pamelaLouisVi = pamelaLouisViMapper.toEntity(pamelaLouisViDTO);
        pamelaLouisVi = pamelaLouisViRepository.save(pamelaLouisVi);
        return pamelaLouisViMapper.toDto(pamelaLouisVi);
    }

    /**
     * Update a pamelaLouisVi.
     *
     * @param pamelaLouisViDTO the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisViDTO update(PamelaLouisViDTO pamelaLouisViDTO) {
        LOG.debug("Request to update PamelaLouisVi : {}", pamelaLouisViDTO);
        PamelaLouisVi pamelaLouisVi = pamelaLouisViMapper.toEntity(pamelaLouisViDTO);
        pamelaLouisVi = pamelaLouisViRepository.save(pamelaLouisVi);
        return pamelaLouisViMapper.toDto(pamelaLouisVi);
    }

    /**
     * Partially update a pamelaLouisVi.
     *
     * @param pamelaLouisViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PamelaLouisViDTO> partialUpdate(PamelaLouisViDTO pamelaLouisViDTO) {
        LOG.debug("Request to partially update PamelaLouisVi : {}", pamelaLouisViDTO);

        return pamelaLouisViRepository
            .findById(pamelaLouisViDTO.getId())
            .map(existingPamelaLouisVi -> {
                pamelaLouisViMapper.partialUpdate(existingPamelaLouisVi, pamelaLouisViDTO);

                return existingPamelaLouisVi;
            })
            .map(pamelaLouisViRepository::save)
            .map(pamelaLouisViMapper::toDto);
    }

    /**
     * Get one pamelaLouisVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PamelaLouisViDTO> findOne(Long id) {
        LOG.debug("Request to get PamelaLouisVi : {}", id);
        return pamelaLouisViRepository.findById(id).map(pamelaLouisViMapper::toDto);
    }

    /**
     * Delete the pamelaLouisVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PamelaLouisVi : {}", id);
        pamelaLouisViRepository.deleteById(id);
    }
}
