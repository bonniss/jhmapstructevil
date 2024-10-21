package ai.realworld.service;

import ai.realworld.domain.AllMassageThaiVi;
import ai.realworld.repository.AllMassageThaiViRepository;
import ai.realworld.service.dto.AllMassageThaiViDTO;
import ai.realworld.service.mapper.AllMassageThaiViMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AllMassageThaiVi}.
 */
@Service
@Transactional
public class AllMassageThaiViService {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiViService.class);

    private final AllMassageThaiViRepository allMassageThaiViRepository;

    private final AllMassageThaiViMapper allMassageThaiViMapper;

    public AllMassageThaiViService(AllMassageThaiViRepository allMassageThaiViRepository, AllMassageThaiViMapper allMassageThaiViMapper) {
        this.allMassageThaiViRepository = allMassageThaiViRepository;
        this.allMassageThaiViMapper = allMassageThaiViMapper;
    }

    /**
     * Save a allMassageThaiVi.
     *
     * @param allMassageThaiViDTO the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiViDTO save(AllMassageThaiViDTO allMassageThaiViDTO) {
        LOG.debug("Request to save AllMassageThaiVi : {}", allMassageThaiViDTO);
        AllMassageThaiVi allMassageThaiVi = allMassageThaiViMapper.toEntity(allMassageThaiViDTO);
        allMassageThaiVi = allMassageThaiViRepository.save(allMassageThaiVi);
        return allMassageThaiViMapper.toDto(allMassageThaiVi);
    }

    /**
     * Update a allMassageThaiVi.
     *
     * @param allMassageThaiViDTO the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiViDTO update(AllMassageThaiViDTO allMassageThaiViDTO) {
        LOG.debug("Request to update AllMassageThaiVi : {}", allMassageThaiViDTO);
        AllMassageThaiVi allMassageThaiVi = allMassageThaiViMapper.toEntity(allMassageThaiViDTO);
        allMassageThaiVi = allMassageThaiViRepository.save(allMassageThaiVi);
        return allMassageThaiViMapper.toDto(allMassageThaiVi);
    }

    /**
     * Partially update a allMassageThaiVi.
     *
     * @param allMassageThaiViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllMassageThaiViDTO> partialUpdate(AllMassageThaiViDTO allMassageThaiViDTO) {
        LOG.debug("Request to partially update AllMassageThaiVi : {}", allMassageThaiViDTO);

        return allMassageThaiViRepository
            .findById(allMassageThaiViDTO.getId())
            .map(existingAllMassageThaiVi -> {
                allMassageThaiViMapper.partialUpdate(existingAllMassageThaiVi, allMassageThaiViDTO);

                return existingAllMassageThaiVi;
            })
            .map(allMassageThaiViRepository::save)
            .map(allMassageThaiViMapper::toDto);
    }

    /**
     * Get one allMassageThaiVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllMassageThaiViDTO> findOne(Long id) {
        LOG.debug("Request to get AllMassageThaiVi : {}", id);
        return allMassageThaiViRepository.findById(id).map(allMassageThaiViMapper::toDto);
    }

    /**
     * Delete the allMassageThaiVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllMassageThaiVi : {}", id);
        allMassageThaiViRepository.deleteById(id);
    }
}
