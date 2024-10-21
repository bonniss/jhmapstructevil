package ai.realworld.service;

import ai.realworld.domain.AlCatalina;
import ai.realworld.repository.AlCatalinaRepository;
import ai.realworld.service.dto.AlCatalinaDTO;
import ai.realworld.service.mapper.AlCatalinaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlCatalina}.
 */
@Service
@Transactional
public class AlCatalinaService {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaService.class);

    private final AlCatalinaRepository alCatalinaRepository;

    private final AlCatalinaMapper alCatalinaMapper;

    public AlCatalinaService(AlCatalinaRepository alCatalinaRepository, AlCatalinaMapper alCatalinaMapper) {
        this.alCatalinaRepository = alCatalinaRepository;
        this.alCatalinaMapper = alCatalinaMapper;
    }

    /**
     * Save a alCatalina.
     *
     * @param alCatalinaDTO the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaDTO save(AlCatalinaDTO alCatalinaDTO) {
        LOG.debug("Request to save AlCatalina : {}", alCatalinaDTO);
        AlCatalina alCatalina = alCatalinaMapper.toEntity(alCatalinaDTO);
        alCatalina = alCatalinaRepository.save(alCatalina);
        return alCatalinaMapper.toDto(alCatalina);
    }

    /**
     * Update a alCatalina.
     *
     * @param alCatalinaDTO the entity to save.
     * @return the persisted entity.
     */
    public AlCatalinaDTO update(AlCatalinaDTO alCatalinaDTO) {
        LOG.debug("Request to update AlCatalina : {}", alCatalinaDTO);
        AlCatalina alCatalina = alCatalinaMapper.toEntity(alCatalinaDTO);
        alCatalina = alCatalinaRepository.save(alCatalina);
        return alCatalinaMapper.toDto(alCatalina);
    }

    /**
     * Partially update a alCatalina.
     *
     * @param alCatalinaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlCatalinaDTO> partialUpdate(AlCatalinaDTO alCatalinaDTO) {
        LOG.debug("Request to partially update AlCatalina : {}", alCatalinaDTO);

        return alCatalinaRepository
            .findById(alCatalinaDTO.getId())
            .map(existingAlCatalina -> {
                alCatalinaMapper.partialUpdate(existingAlCatalina, alCatalinaDTO);

                return existingAlCatalina;
            })
            .map(alCatalinaRepository::save)
            .map(alCatalinaMapper::toDto);
    }

    /**
     * Get one alCatalina by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlCatalinaDTO> findOne(Long id) {
        LOG.debug("Request to get AlCatalina : {}", id);
        return alCatalinaRepository.findById(id).map(alCatalinaMapper::toDto);
    }

    /**
     * Delete the alCatalina by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlCatalina : {}", id);
        alCatalinaRepository.deleteById(id);
    }
}
