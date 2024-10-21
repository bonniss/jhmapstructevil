package ai.realworld.service;

import ai.realworld.domain.AntonioBanderas;
import ai.realworld.repository.AntonioBanderasRepository;
import ai.realworld.service.dto.AntonioBanderasDTO;
import ai.realworld.service.mapper.AntonioBanderasMapper;
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
 * Service Implementation for managing {@link ai.realworld.domain.AntonioBanderas}.
 */
@Service
@Transactional
public class AntonioBanderasService {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasService.class);

    private final AntonioBanderasRepository antonioBanderasRepository;

    private final AntonioBanderasMapper antonioBanderasMapper;

    public AntonioBanderasService(AntonioBanderasRepository antonioBanderasRepository, AntonioBanderasMapper antonioBanderasMapper) {
        this.antonioBanderasRepository = antonioBanderasRepository;
        this.antonioBanderasMapper = antonioBanderasMapper;
    }

    /**
     * Save a antonioBanderas.
     *
     * @param antonioBanderasDTO the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasDTO save(AntonioBanderasDTO antonioBanderasDTO) {
        LOG.debug("Request to save AntonioBanderas : {}", antonioBanderasDTO);
        AntonioBanderas antonioBanderas = antonioBanderasMapper.toEntity(antonioBanderasDTO);
        antonioBanderas = antonioBanderasRepository.save(antonioBanderas);
        return antonioBanderasMapper.toDto(antonioBanderas);
    }

    /**
     * Update a antonioBanderas.
     *
     * @param antonioBanderasDTO the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasDTO update(AntonioBanderasDTO antonioBanderasDTO) {
        LOG.debug("Request to update AntonioBanderas : {}", antonioBanderasDTO);
        AntonioBanderas antonioBanderas = antonioBanderasMapper.toEntity(antonioBanderasDTO);
        antonioBanderas = antonioBanderasRepository.save(antonioBanderas);
        return antonioBanderasMapper.toDto(antonioBanderas);
    }

    /**
     * Partially update a antonioBanderas.
     *
     * @param antonioBanderasDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AntonioBanderasDTO> partialUpdate(AntonioBanderasDTO antonioBanderasDTO) {
        LOG.debug("Request to partially update AntonioBanderas : {}", antonioBanderasDTO);

        return antonioBanderasRepository
            .findById(antonioBanderasDTO.getId())
            .map(existingAntonioBanderas -> {
                antonioBanderasMapper.partialUpdate(existingAntonioBanderas, antonioBanderasDTO);

                return existingAntonioBanderas;
            })
            .map(antonioBanderasRepository::save)
            .map(antonioBanderasMapper::toDto);
    }

    /**
     * Get all the antonioBanderas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AntonioBanderasDTO> findAllWithEagerRelationships(Pageable pageable) {
        return antonioBanderasRepository.findAllWithEagerRelationships(pageable).map(antonioBanderasMapper::toDto);
    }

    /**
     *  Get all the antonioBanderas where AntonioBanderas is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AntonioBanderasDTO> findAllWhereAntonioBanderasIsNull() {
        LOG.debug("Request to get all antonioBanderas where AntonioBanderas is null");
        return StreamSupport.stream(antonioBanderasRepository.findAll().spliterator(), false)
            .filter(antonioBanderas -> antonioBanderas.getAntonioBanderas() == null)
            .map(antonioBanderasMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one antonioBanderas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AntonioBanderasDTO> findOne(Long id) {
        LOG.debug("Request to get AntonioBanderas : {}", id);
        return antonioBanderasRepository.findOneWithEagerRelationships(id).map(antonioBanderasMapper::toDto);
    }

    /**
     * Delete the antonioBanderas by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AntonioBanderas : {}", id);
        antonioBanderasRepository.deleteById(id);
    }
}
