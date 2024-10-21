package ai.realworld.service;

import ai.realworld.domain.AlPacino;
import ai.realworld.repository.AlPacinoRepository;
import ai.realworld.service.dto.AlPacinoDTO;
import ai.realworld.service.mapper.AlPacinoMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacino}.
 */
@Service
@Transactional
public class AlPacinoService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoService.class);

    private final AlPacinoRepository alPacinoRepository;

    private final AlPacinoMapper alPacinoMapper;

    public AlPacinoService(AlPacinoRepository alPacinoRepository, AlPacinoMapper alPacinoMapper) {
        this.alPacinoRepository = alPacinoRepository;
        this.alPacinoMapper = alPacinoMapper;
    }

    /**
     * Save a alPacino.
     *
     * @param alPacinoDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoDTO save(AlPacinoDTO alPacinoDTO) {
        LOG.debug("Request to save AlPacino : {}", alPacinoDTO);
        AlPacino alPacino = alPacinoMapper.toEntity(alPacinoDTO);
        alPacino = alPacinoRepository.save(alPacino);
        return alPacinoMapper.toDto(alPacino);
    }

    /**
     * Update a alPacino.
     *
     * @param alPacinoDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoDTO update(AlPacinoDTO alPacinoDTO) {
        LOG.debug("Request to update AlPacino : {}", alPacinoDTO);
        AlPacino alPacino = alPacinoMapper.toEntity(alPacinoDTO);
        alPacino = alPacinoRepository.save(alPacino);
        return alPacinoMapper.toDto(alPacino);
    }

    /**
     * Partially update a alPacino.
     *
     * @param alPacinoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoDTO> partialUpdate(AlPacinoDTO alPacinoDTO) {
        LOG.debug("Request to partially update AlPacino : {}", alPacinoDTO);

        return alPacinoRepository
            .findById(alPacinoDTO.getId())
            .map(existingAlPacino -> {
                alPacinoMapper.partialUpdate(existingAlPacino, alPacinoDTO);

                return existingAlPacino;
            })
            .map(alPacinoRepository::save)
            .map(alPacinoMapper::toDto);
    }

    /**
     * Get one alPacino by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoDTO> findOne(UUID id) {
        LOG.debug("Request to get AlPacino : {}", id);
        return alPacinoRepository.findById(id).map(alPacinoMapper::toDto);
    }

    /**
     * Delete the alPacino by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacino : {}", id);
        alPacinoRepository.deleteById(id);
    }
}
