package ai.realworld.service;

import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.repository.AlPacinoVoucherViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoVoucherVi}.
 */
@Service
@Transactional
public class AlPacinoVoucherViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherViService.class);

    private final AlPacinoVoucherViRepository alPacinoVoucherViRepository;

    public AlPacinoVoucherViService(AlPacinoVoucherViRepository alPacinoVoucherViRepository) {
        this.alPacinoVoucherViRepository = alPacinoVoucherViRepository;
    }

    /**
     * Save a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherVi save(AlPacinoVoucherVi alPacinoVoucherVi) {
        LOG.debug("Request to save AlPacinoVoucherVi : {}", alPacinoVoucherVi);
        return alPacinoVoucherViRepository.save(alPacinoVoucherVi);
    }

    /**
     * Update a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherVi update(AlPacinoVoucherVi alPacinoVoucherVi) {
        LOG.debug("Request to update AlPacinoVoucherVi : {}", alPacinoVoucherVi);
        return alPacinoVoucherViRepository.save(alPacinoVoucherVi);
    }

    /**
     * Partially update a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoVoucherVi> partialUpdate(AlPacinoVoucherVi alPacinoVoucherVi) {
        LOG.debug("Request to partially update AlPacinoVoucherVi : {}", alPacinoVoucherVi);

        return alPacinoVoucherViRepository
            .findById(alPacinoVoucherVi.getId())
            .map(existingAlPacinoVoucherVi -> {
                if (alPacinoVoucherVi.getSourceTitle() != null) {
                    existingAlPacinoVoucherVi.setSourceTitle(alPacinoVoucherVi.getSourceTitle());
                }
                if (alPacinoVoucherVi.getSourceUrl() != null) {
                    existingAlPacinoVoucherVi.setSourceUrl(alPacinoVoucherVi.getSourceUrl());
                }
                if (alPacinoVoucherVi.getCollectedDate() != null) {
                    existingAlPacinoVoucherVi.setCollectedDate(alPacinoVoucherVi.getCollectedDate());
                }

                return existingAlPacinoVoucherVi;
            })
            .map(alPacinoVoucherViRepository::save);
    }

    /**
     * Get one alPacinoVoucherVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoVoucherVi> findOne(UUID id) {
        LOG.debug("Request to get AlPacinoVoucherVi : {}", id);
        return alPacinoVoucherViRepository.findById(id);
    }

    /**
     * Delete the alPacinoVoucherVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacinoVoucherVi : {}", id);
        alPacinoVoucherViRepository.deleteById(id);
    }
}
