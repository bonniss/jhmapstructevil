package ai.realworld.service;

import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.repository.AlPacinoVoucherRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoVoucher}.
 */
@Service
@Transactional
public class AlPacinoVoucherService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherService.class);

    private final AlPacinoVoucherRepository alPacinoVoucherRepository;

    public AlPacinoVoucherService(AlPacinoVoucherRepository alPacinoVoucherRepository) {
        this.alPacinoVoucherRepository = alPacinoVoucherRepository;
    }

    /**
     * Save a alPacinoVoucher.
     *
     * @param alPacinoVoucher the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucher save(AlPacinoVoucher alPacinoVoucher) {
        LOG.debug("Request to save AlPacinoVoucher : {}", alPacinoVoucher);
        return alPacinoVoucherRepository.save(alPacinoVoucher);
    }

    /**
     * Update a alPacinoVoucher.
     *
     * @param alPacinoVoucher the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucher update(AlPacinoVoucher alPacinoVoucher) {
        LOG.debug("Request to update AlPacinoVoucher : {}", alPacinoVoucher);
        return alPacinoVoucherRepository.save(alPacinoVoucher);
    }

    /**
     * Partially update a alPacinoVoucher.
     *
     * @param alPacinoVoucher the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoVoucher> partialUpdate(AlPacinoVoucher alPacinoVoucher) {
        LOG.debug("Request to partially update AlPacinoVoucher : {}", alPacinoVoucher);

        return alPacinoVoucherRepository
            .findById(alPacinoVoucher.getId())
            .map(existingAlPacinoVoucher -> {
                if (alPacinoVoucher.getSourceTitle() != null) {
                    existingAlPacinoVoucher.setSourceTitle(alPacinoVoucher.getSourceTitle());
                }
                if (alPacinoVoucher.getSourceUrl() != null) {
                    existingAlPacinoVoucher.setSourceUrl(alPacinoVoucher.getSourceUrl());
                }
                if (alPacinoVoucher.getCollectedDate() != null) {
                    existingAlPacinoVoucher.setCollectedDate(alPacinoVoucher.getCollectedDate());
                }

                return existingAlPacinoVoucher;
            })
            .map(alPacinoVoucherRepository::save);
    }

    /**
     * Get one alPacinoVoucher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoVoucher> findOne(UUID id) {
        LOG.debug("Request to get AlPacinoVoucher : {}", id);
        return alPacinoVoucherRepository.findById(id);
    }

    /**
     * Delete the alPacinoVoucher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacinoVoucher : {}", id);
        alPacinoVoucherRepository.deleteById(id);
    }
}
