package ai.realworld.service;

import ai.realworld.domain.AllMassageThai;
import ai.realworld.repository.AllMassageThaiRepository;
import ai.realworld.service.dto.AllMassageThaiDTO;
import ai.realworld.service.mapper.AllMassageThaiMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AllMassageThai}.
 */
@Service
@Transactional
public class AllMassageThaiService {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiService.class);

    private final AllMassageThaiRepository allMassageThaiRepository;

    private final AllMassageThaiMapper allMassageThaiMapper;

    public AllMassageThaiService(AllMassageThaiRepository allMassageThaiRepository, AllMassageThaiMapper allMassageThaiMapper) {
        this.allMassageThaiRepository = allMassageThaiRepository;
        this.allMassageThaiMapper = allMassageThaiMapper;
    }

    /**
     * Save a allMassageThai.
     *
     * @param allMassageThaiDTO the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiDTO save(AllMassageThaiDTO allMassageThaiDTO) {
        LOG.debug("Request to save AllMassageThai : {}", allMassageThaiDTO);
        AllMassageThai allMassageThai = allMassageThaiMapper.toEntity(allMassageThaiDTO);
        allMassageThai = allMassageThaiRepository.save(allMassageThai);
        return allMassageThaiMapper.toDto(allMassageThai);
    }

    /**
     * Update a allMassageThai.
     *
     * @param allMassageThaiDTO the entity to save.
     * @return the persisted entity.
     */
    public AllMassageThaiDTO update(AllMassageThaiDTO allMassageThaiDTO) {
        LOG.debug("Request to update AllMassageThai : {}", allMassageThaiDTO);
        AllMassageThai allMassageThai = allMassageThaiMapper.toEntity(allMassageThaiDTO);
        allMassageThai = allMassageThaiRepository.save(allMassageThai);
        return allMassageThaiMapper.toDto(allMassageThai);
    }

    /**
     * Partially update a allMassageThai.
     *
     * @param allMassageThaiDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AllMassageThaiDTO> partialUpdate(AllMassageThaiDTO allMassageThaiDTO) {
        LOG.debug("Request to partially update AllMassageThai : {}", allMassageThaiDTO);

        return allMassageThaiRepository
            .findById(allMassageThaiDTO.getId())
            .map(existingAllMassageThai -> {
                allMassageThaiMapper.partialUpdate(existingAllMassageThai, allMassageThaiDTO);

                return existingAllMassageThai;
            })
            .map(allMassageThaiRepository::save)
            .map(allMassageThaiMapper::toDto);
    }

    /**
     * Get one allMassageThai by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AllMassageThaiDTO> findOne(Long id) {
        LOG.debug("Request to get AllMassageThai : {}", id);
        return allMassageThaiRepository.findById(id).map(allMassageThaiMapper::toDto);
    }

    /**
     * Delete the allMassageThai by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AllMassageThai : {}", id);
        allMassageThaiRepository.deleteById(id);
    }
}
