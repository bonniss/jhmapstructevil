package ai.realworld.service;

import ai.realworld.domain.AlPyuThomasWayne;
import ai.realworld.repository.AlPyuThomasWayneRepository;
import ai.realworld.service.dto.AlPyuThomasWayneDTO;
import ai.realworld.service.mapper.AlPyuThomasWayneMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPyuThomasWayne}.
 */
@Service
@Transactional
public class AlPyuThomasWayneService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneService.class);

    private final AlPyuThomasWayneRepository alPyuThomasWayneRepository;

    private final AlPyuThomasWayneMapper alPyuThomasWayneMapper;

    public AlPyuThomasWayneService(AlPyuThomasWayneRepository alPyuThomasWayneRepository, AlPyuThomasWayneMapper alPyuThomasWayneMapper) {
        this.alPyuThomasWayneRepository = alPyuThomasWayneRepository;
        this.alPyuThomasWayneMapper = alPyuThomasWayneMapper;
    }

    /**
     * Save a alPyuThomasWayne.
     *
     * @param alPyuThomasWayneDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneDTO save(AlPyuThomasWayneDTO alPyuThomasWayneDTO) {
        LOG.debug("Request to save AlPyuThomasWayne : {}", alPyuThomasWayneDTO);
        AlPyuThomasWayne alPyuThomasWayne = alPyuThomasWayneMapper.toEntity(alPyuThomasWayneDTO);
        alPyuThomasWayne = alPyuThomasWayneRepository.save(alPyuThomasWayne);
        return alPyuThomasWayneMapper.toDto(alPyuThomasWayne);
    }

    /**
     * Update a alPyuThomasWayne.
     *
     * @param alPyuThomasWayneDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPyuThomasWayneDTO update(AlPyuThomasWayneDTO alPyuThomasWayneDTO) {
        LOG.debug("Request to update AlPyuThomasWayne : {}", alPyuThomasWayneDTO);
        AlPyuThomasWayne alPyuThomasWayne = alPyuThomasWayneMapper.toEntity(alPyuThomasWayneDTO);
        alPyuThomasWayne = alPyuThomasWayneRepository.save(alPyuThomasWayne);
        return alPyuThomasWayneMapper.toDto(alPyuThomasWayne);
    }

    /**
     * Partially update a alPyuThomasWayne.
     *
     * @param alPyuThomasWayneDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPyuThomasWayneDTO> partialUpdate(AlPyuThomasWayneDTO alPyuThomasWayneDTO) {
        LOG.debug("Request to partially update AlPyuThomasWayne : {}", alPyuThomasWayneDTO);

        return alPyuThomasWayneRepository
            .findById(alPyuThomasWayneDTO.getId())
            .map(existingAlPyuThomasWayne -> {
                alPyuThomasWayneMapper.partialUpdate(existingAlPyuThomasWayne, alPyuThomasWayneDTO);

                return existingAlPyuThomasWayne;
            })
            .map(alPyuThomasWayneRepository::save)
            .map(alPyuThomasWayneMapper::toDto);
    }

    /**
     * Get one alPyuThomasWayne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPyuThomasWayneDTO> findOne(Long id) {
        LOG.debug("Request to get AlPyuThomasWayne : {}", id);
        return alPyuThomasWayneRepository.findById(id).map(alPyuThomasWayneMapper::toDto);
    }

    /**
     * Delete the alPyuThomasWayne by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPyuThomasWayne : {}", id);
        alPyuThomasWayneRepository.deleteById(id);
    }
}
