package ai.realworld.service;

import ai.realworld.domain.AntonioBanderasVi;
import ai.realworld.repository.AntonioBanderasViRepository;
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
 * Service Implementation for managing {@link ai.realworld.domain.AntonioBanderasVi}.
 */
@Service
@Transactional
public class AntonioBanderasViService {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasViService.class);

    private final AntonioBanderasViRepository antonioBanderasViRepository;

    public AntonioBanderasViService(AntonioBanderasViRepository antonioBanderasViRepository) {
        this.antonioBanderasViRepository = antonioBanderasViRepository;
    }

    /**
     * Save a antonioBanderasVi.
     *
     * @param antonioBanderasVi the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasVi save(AntonioBanderasVi antonioBanderasVi) {
        LOG.debug("Request to save AntonioBanderasVi : {}", antonioBanderasVi);
        return antonioBanderasViRepository.save(antonioBanderasVi);
    }

    /**
     * Update a antonioBanderasVi.
     *
     * @param antonioBanderasVi the entity to save.
     * @return the persisted entity.
     */
    public AntonioBanderasVi update(AntonioBanderasVi antonioBanderasVi) {
        LOG.debug("Request to update AntonioBanderasVi : {}", antonioBanderasVi);
        return antonioBanderasViRepository.save(antonioBanderasVi);
    }

    /**
     * Partially update a antonioBanderasVi.
     *
     * @param antonioBanderasVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AntonioBanderasVi> partialUpdate(AntonioBanderasVi antonioBanderasVi) {
        LOG.debug("Request to partially update AntonioBanderasVi : {}", antonioBanderasVi);

        return antonioBanderasViRepository
            .findById(antonioBanderasVi.getId())
            .map(existingAntonioBanderasVi -> {
                if (antonioBanderasVi.getLevel() != null) {
                    existingAntonioBanderasVi.setLevel(antonioBanderasVi.getLevel());
                }
                if (antonioBanderasVi.getCode() != null) {
                    existingAntonioBanderasVi.setCode(antonioBanderasVi.getCode());
                }
                if (antonioBanderasVi.getName() != null) {
                    existingAntonioBanderasVi.setName(antonioBanderasVi.getName());
                }
                if (antonioBanderasVi.getFullName() != null) {
                    existingAntonioBanderasVi.setFullName(antonioBanderasVi.getFullName());
                }
                if (antonioBanderasVi.getNativeName() != null) {
                    existingAntonioBanderasVi.setNativeName(antonioBanderasVi.getNativeName());
                }
                if (antonioBanderasVi.getOfficialCode() != null) {
                    existingAntonioBanderasVi.setOfficialCode(antonioBanderasVi.getOfficialCode());
                }
                if (antonioBanderasVi.getDivisionTerm() != null) {
                    existingAntonioBanderasVi.setDivisionTerm(antonioBanderasVi.getDivisionTerm());
                }
                if (antonioBanderasVi.getIsDeleted() != null) {
                    existingAntonioBanderasVi.setIsDeleted(antonioBanderasVi.getIsDeleted());
                }

                return existingAntonioBanderasVi;
            })
            .map(antonioBanderasViRepository::save);
    }

    /**
     * Get all the antonioBanderasVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AntonioBanderasVi> findAllWithEagerRelationships(Pageable pageable) {
        return antonioBanderasViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the antonioBanderasVis where AntonioBanderasVi is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AntonioBanderasVi> findAllWhereAntonioBanderasViIsNull() {
        LOG.debug("Request to get all antonioBanderasVis where AntonioBanderasVi is null");
        return StreamSupport.stream(antonioBanderasViRepository.findAll().spliterator(), false)
            .filter(antonioBanderasVi -> antonioBanderasVi.getAntonioBanderasVi() == null)
            .toList();
    }

    /**
     * Get one antonioBanderasVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AntonioBanderasVi> findOne(Long id) {
        LOG.debug("Request to get AntonioBanderasVi : {}", id);
        return antonioBanderasViRepository.findOneWithEagerRelationships(id);
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
