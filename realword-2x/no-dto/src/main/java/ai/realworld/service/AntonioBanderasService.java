package ai.realworld.service;

import ai.realworld.domain.AntonioBanderas;
import ai.realworld.repository.AntonioBanderasRepository;
import java.util.List;
import java.util.Optional;
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

    public AntonioBanderasService(AntonioBanderasRepository antonioBanderasRepository) {
        this.antonioBanderasRepository = antonioBanderasRepository;
    }

    /**
     * Save a antonioBanderas.
     *
     * @param antonioBanderas the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderas save(AntonioBanderas antonioBanderas) {
        LOG.debug("Request to save AntonioBanderas : {}", antonioBanderas);
        return antonioBanderasRepository.save(antonioBanderas);
    }

    /**
     * Update a antonioBanderas.
     *
     * @param antonioBanderas the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderas update(AntonioBanderas antonioBanderas) {
        LOG.debug("Request to update AntonioBanderas : {}", antonioBanderas);
        return antonioBanderasRepository.save(antonioBanderas);
    }

    /**
     * Partially update a antonioBanderas.
     *
     * @param antonioBanderas the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AntonioBanderas> partialUpdate(AntonioBanderas antonioBanderas) {
        LOG.debug("Request to partially update AntonioBanderas : {}", antonioBanderas);

        return antonioBanderasRepository
            .findById(antonioBanderas.getId())
            .map(existingAntonioBanderas -> {
                if (antonioBanderas.getLevel() != null) {
                    existingAntonioBanderas.setLevel(antonioBanderas.getLevel());
                }
                if (antonioBanderas.getCode() != null) {
                    existingAntonioBanderas.setCode(antonioBanderas.getCode());
                }
                if (antonioBanderas.getName() != null) {
                    existingAntonioBanderas.setName(antonioBanderas.getName());
                }
                if (antonioBanderas.getFullName() != null) {
                    existingAntonioBanderas.setFullName(antonioBanderas.getFullName());
                }
                if (antonioBanderas.getNativeName() != null) {
                    existingAntonioBanderas.setNativeName(antonioBanderas.getNativeName());
                }
                if (antonioBanderas.getOfficialCode() != null) {
                    existingAntonioBanderas.setOfficialCode(antonioBanderas.getOfficialCode());
                }
                if (antonioBanderas.getDivisionTerm() != null) {
                    existingAntonioBanderas.setDivisionTerm(antonioBanderas.getDivisionTerm());
                }
                if (antonioBanderas.getIsDeleted() != null) {
                    existingAntonioBanderas.setIsDeleted(antonioBanderas.getIsDeleted());
                }

                return existingAntonioBanderas;
            })
            .map(antonioBanderasRepository::save);
    }

    /**
     * Get all the antonioBanderas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AntonioBanderas> findAllWithEagerRelationships(Pageable pageable) {
        return antonioBanderasRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the antonioBanderas where AntonioBanderas is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AntonioBanderas> findAllWhereAntonioBanderasIsNull() {
        LOG.debug("Request to get all antonioBanderas where AntonioBanderas is null");
        return StreamSupport.stream(antonioBanderasRepository.findAll().spliterator(), false)
            .filter(antonioBanderas -> antonioBanderas.getAntonioBanderas() == null)
            .toList();
    }

    /**
     * Get one antonioBanderas by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AntonioBanderas> findOne(Long id) {
        LOG.debug("Request to get AntonioBanderas : {}", id);
        return antonioBanderasRepository.findOneWithEagerRelationships(id);
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
