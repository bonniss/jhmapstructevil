package ai.realworld.service;

import ai.realworld.domain.PamelaLouis;
import ai.realworld.repository.PamelaLouisRepository;
import ai.realworld.service.dto.PamelaLouisDTO;
import ai.realworld.service.mapper.PamelaLouisMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.PamelaLouis}.
 */
@Service
@Transactional
public class PamelaLouisService {

    private static final Logger LOG = LoggerFactory.getLogger(PamelaLouisService.class);

    private final PamelaLouisRepository pamelaLouisRepository;

    private final PamelaLouisMapper pamelaLouisMapper;

    public PamelaLouisService(PamelaLouisRepository pamelaLouisRepository, PamelaLouisMapper pamelaLouisMapper) {
        this.pamelaLouisRepository = pamelaLouisRepository;
        this.pamelaLouisMapper = pamelaLouisMapper;
    }

    /**
     * Save a pamelaLouis.
     *
     * @param pamelaLouisDTO the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisDTO save(PamelaLouisDTO pamelaLouisDTO) {
        LOG.debug("Request to save PamelaLouis : {}", pamelaLouisDTO);
        PamelaLouis pamelaLouis = pamelaLouisMapper.toEntity(pamelaLouisDTO);
        pamelaLouis = pamelaLouisRepository.save(pamelaLouis);
        return pamelaLouisMapper.toDto(pamelaLouis);
    }

    /**
     * Update a pamelaLouis.
     *
     * @param pamelaLouisDTO the entity to save.
     * @return the persisted entity.
     */
    public PamelaLouisDTO update(PamelaLouisDTO pamelaLouisDTO) {
        LOG.debug("Request to update PamelaLouis : {}", pamelaLouisDTO);
        PamelaLouis pamelaLouis = pamelaLouisMapper.toEntity(pamelaLouisDTO);
        pamelaLouis = pamelaLouisRepository.save(pamelaLouis);
        return pamelaLouisMapper.toDto(pamelaLouis);
    }

    /**
     * Partially update a pamelaLouis.
     *
     * @param pamelaLouisDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PamelaLouisDTO> partialUpdate(PamelaLouisDTO pamelaLouisDTO) {
        LOG.debug("Request to partially update PamelaLouis : {}", pamelaLouisDTO);

        return pamelaLouisRepository
            .findById(pamelaLouisDTO.getId())
            .map(existingPamelaLouis -> {
                pamelaLouisMapper.partialUpdate(existingPamelaLouis, pamelaLouisDTO);

                return existingPamelaLouis;
            })
            .map(pamelaLouisRepository::save)
            .map(pamelaLouisMapper::toDto);
    }

    /**
     * Get one pamelaLouis by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PamelaLouisDTO> findOne(Long id) {
        LOG.debug("Request to get PamelaLouis : {}", id);
        return pamelaLouisRepository.findById(id).map(pamelaLouisMapper::toDto);
    }

    /**
     * Delete the pamelaLouis by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PamelaLouis : {}", id);
        pamelaLouisRepository.deleteById(id);
    }
}
