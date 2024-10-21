package ai.realworld.service;

import ai.realworld.domain.AlPacinoPointHistoryVi;
import ai.realworld.repository.AlPacinoPointHistoryViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoPointHistoryVi}.
 */
@Service
@Transactional
public class AlPacinoPointHistoryViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryViService.class);

    private final AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository;

    public AlPacinoPointHistoryViService(AlPacinoPointHistoryViRepository alPacinoPointHistoryViRepository) {
        this.alPacinoPointHistoryViRepository = alPacinoPointHistoryViRepository;
    }

    /**
     * Save a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryVi save(AlPacinoPointHistoryVi alPacinoPointHistoryVi) {
        LOG.debug("Request to save AlPacinoPointHistoryVi : {}", alPacinoPointHistoryVi);
        return alPacinoPointHistoryViRepository.save(alPacinoPointHistoryVi);
    }

    /**
     * Update a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryVi the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistoryVi update(AlPacinoPointHistoryVi alPacinoPointHistoryVi) {
        LOG.debug("Request to update AlPacinoPointHistoryVi : {}", alPacinoPointHistoryVi);
        return alPacinoPointHistoryViRepository.save(alPacinoPointHistoryVi);
    }

    /**
     * Partially update a alPacinoPointHistoryVi.
     *
     * @param alPacinoPointHistoryVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoPointHistoryVi> partialUpdate(AlPacinoPointHistoryVi alPacinoPointHistoryVi) {
        LOG.debug("Request to partially update AlPacinoPointHistoryVi : {}", alPacinoPointHistoryVi);

        return alPacinoPointHistoryViRepository
            .findById(alPacinoPointHistoryVi.getId())
            .map(existingAlPacinoPointHistoryVi -> {
                if (alPacinoPointHistoryVi.getSource() != null) {
                    existingAlPacinoPointHistoryVi.setSource(alPacinoPointHistoryVi.getSource());
                }
                if (alPacinoPointHistoryVi.getAssociatedId() != null) {
                    existingAlPacinoPointHistoryVi.setAssociatedId(alPacinoPointHistoryVi.getAssociatedId());
                }
                if (alPacinoPointHistoryVi.getPointAmount() != null) {
                    existingAlPacinoPointHistoryVi.setPointAmount(alPacinoPointHistoryVi.getPointAmount());
                }

                return existingAlPacinoPointHistoryVi;
            })
            .map(alPacinoPointHistoryViRepository::save);
    }

    /**
     * Get one alPacinoPointHistoryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoPointHistoryVi> findOne(Long id) {
        LOG.debug("Request to get AlPacinoPointHistoryVi : {}", id);
        return alPacinoPointHistoryViRepository.findById(id);
    }

    /**
     * Delete the alPacinoPointHistoryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoPointHistoryVi : {}", id);
        alPacinoPointHistoryViRepository.deleteById(id);
    }
}
