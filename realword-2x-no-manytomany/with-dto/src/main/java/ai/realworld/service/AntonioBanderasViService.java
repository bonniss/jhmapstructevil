package ai.realworld.service;

import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.repository.AntonioBanderasViRepository;
import ai.realworld.service.dto.AntonioBanderasViDTO;
import ai.realworld.service.mapper.AntonioBanderasViMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AntonioBanderasVi}.
 */
@Service
@Transactional
public class AntonioBanderasViService {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasViService.class);

    private final AntonioBanderasViRepository antonioBanderasViRepository;

    private final AntonioBanderasViMapper antonioBanderasViMapper;

    public AntonioBanderasViService(
        AntonioBanderasViRepository antonioBanderasViRepository,
        AntonioBanderasViMapper antonioBanderasViMapper
    ) {
        this.antonioBanderasViRepository = antonioBanderasViRepository;
        this.antonioBanderasViMapper = antonioBanderasViMapper;
    }

    /**
     * Save a antonioBanderasVi.
     *
     * @param antonioBanderasViDTO the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasViDTO save(AntonioBanderasViDTO antonioBanderasViDTO) {
        LOG.debug("Request to save AntonioBanderasVi : {}", antonioBanderasViDTO);
        AntonioBanderasVi antonioBanderasVi = antonioBanderasViMapper.toEntity(antonioBanderasViDTO);
        antonioBanderasVi = antonioBanderasViRepository.save(antonioBanderasVi);
        return antonioBanderasViMapper.toDto(antonioBanderasVi);
    }

    /**
     * Update a antonioBanderasVi.
     *
     * @param antonioBanderasViDTO the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasViDTO update(AntonioBanderasViDTO antonioBanderasViDTO) {
        LOG.debug("Request to update AntonioBanderasVi : {}", antonioBanderasViDTO);
        AntonioBanderasVi antonioBanderasVi = antonioBanderasViMapper.toEntity(antonioBanderasViDTO);
        antonioBanderasVi = antonioBanderasViRepository.save(antonioBanderasVi);
        return antonioBanderasViMapper.toDto(antonioBanderasVi);
    }

    /**
     * Partially update a antonioBanderasVi.
     *
     * @param antonioBanderasViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AntonioBanderasViDTO> partialUpdate(AntonioBanderasViDTO antonioBanderasViDTO) {
        LOG.debug("Request to partially update AntonioBanderasVi : {}", antonioBanderasViDTO);

        return antonioBanderasViRepository
            .findById(antonioBanderasViDTO.getId())
            .map(existingAntonioBanderasVi -> {
                antonioBanderasViMapper.partialUpdate(existingAntonioBanderasVi, antonioBanderasViDTO);

                return existingAntonioBanderasVi;
            })
            .map(antonioBanderasViRepository::save)
            .map(antonioBanderasViMapper::toDto);
    }

    /**
     * Get all the antonioBanderasVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AntonioBanderasViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return antonioBanderasViRepository.findAllWithEagerRelationships(pageable).map(antonioBanderasViMapper::toDto);
    }

    /**
     *  Get all the antonioBanderasVis where AntonioBanderasVi is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AntonioBanderasViDTO> findAllWhereAntonioBanderasViIsNull() {
        LOG.debug("Request to get all antonioBanderasVis where AntonioBanderasVi is null");
        return StreamSupport.stream(antonioBanderasViRepository.findAll().spliterator(), false)
            .filter(antonioBanderasVi -> antonioBanderasVi.getAntonioBanderasVi() == null)
            .map(antonioBanderasViMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one antonioBanderasVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AntonioBanderasViDTO> findOne(Long id) {
        LOG.debug("Request to get AntonioBanderasVi : {}", id);
        return antonioBanderasViRepository.findOneWithEagerRelationships(id).map(antonioBanderasViMapper::toDto);
    }

    /**
     * Delete the antonioBanderasVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AntonioBanderasVi : {}", id);
        antonioBanderasViRepository.deleteById(id);
    }
}
