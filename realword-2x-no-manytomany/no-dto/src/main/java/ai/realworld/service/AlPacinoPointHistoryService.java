package ai.realworld.service;

import ai.realworld.domain.AlPacinoPointHistory;
import ai.realworld.repository.AlPacinoPointHistoryRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoPointHistory}.
 */
@Service
@Transactional
public class AlPacinoPointHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoPointHistoryService.class);

    private final AlPacinoPointHistoryRepository alPacinoPointHistoryRepository;

    public AlPacinoPointHistoryService(AlPacinoPointHistoryRepository alPacinoPointHistoryRepository) {
        this.alPacinoPointHistoryRepository = alPacinoPointHistoryRepository;
    }

    /**
     * Save a alPacinoPointHistory.
     *
     * @param alPacinoPointHistory the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistory save(AlPacinoPointHistory alPacinoPointHistory) {
        LOG.debug("Request to save AlPacinoPointHistory : {}", alPacinoPointHistory);
        return alPacinoPointHistoryRepository.save(alPacinoPointHistory);
    }

    /**
     * Update a alPacinoPointHistory.
     *
     * @param alPacinoPointHistory the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoPointHistory update(AlPacinoPointHistory alPacinoPointHistory) {
        LOG.debug("Request to update AlPacinoPointHistory : {}", alPacinoPointHistory);
        return alPacinoPointHistoryRepository.save(alPacinoPointHistory);
    }

    /**
     * Partially update a alPacinoPointHistory.
     *
     * @param alPacinoPointHistory the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoPointHistory> partialUpdate(AlPacinoPointHistory alPacinoPointHistory) {
        LOG.debug("Request to partially update AlPacinoPointHistory : {}", alPacinoPointHistory);

        return alPacinoPointHistoryRepository
            .findById(alPacinoPointHistory.getId())
            .map(existingAlPacinoPointHistory -> {
                if (alPacinoPointHistory.getSource() != null) {
                    existingAlPacinoPointHistory.setSource(alPacinoPointHistory.getSource());
                }
                if (alPacinoPointHistory.getAssociatedId() != null) {
                    existingAlPacinoPointHistory.setAssociatedId(alPacinoPointHistory.getAssociatedId());
                }
                if (alPacinoPointHistory.getPointAmount() != null) {
                    existingAlPacinoPointHistory.setPointAmount(alPacinoPointHistory.getPointAmount());
                }

                return existingAlPacinoPointHistory;
            })
            .map(alPacinoPointHistoryRepository::save);
    }

    /**
     * Get one alPacinoPointHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoPointHistory> findOne(Long id) {
        LOG.debug("Request to get AlPacinoPointHistory : {}", id);
        return alPacinoPointHistoryRepository.findById(id);
    }

    /**
     * Delete the alPacinoPointHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AlPacinoPointHistory : {}", id);
        alPacinoPointHistoryRepository.deleteById(id);
    }
}
