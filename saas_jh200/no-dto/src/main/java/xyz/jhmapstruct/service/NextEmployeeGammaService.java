package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeGamma;
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeGamma}.
 */
@Service
@Transactional
public class NextEmployeeGammaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeGammaService.class);

    private final NextEmployeeGammaRepository nextEmployeeGammaRepository;

    public NextEmployeeGammaService(NextEmployeeGammaRepository nextEmployeeGammaRepository) {
        this.nextEmployeeGammaRepository = nextEmployeeGammaRepository;
    }

    /**
     * Save a nextEmployeeGamma.
     *
     * @param nextEmployeeGamma the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeGamma save(NextEmployeeGamma nextEmployeeGamma) {
        LOG.debug("Request to save NextEmployeeGamma : {}", nextEmployeeGamma);
        return nextEmployeeGammaRepository.save(nextEmployeeGamma);
    }

    /**
     * Update a nextEmployeeGamma.
     *
     * @param nextEmployeeGamma the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeGamma update(NextEmployeeGamma nextEmployeeGamma) {
        LOG.debug("Request to update NextEmployeeGamma : {}", nextEmployeeGamma);
        return nextEmployeeGammaRepository.save(nextEmployeeGamma);
    }

    /**
     * Partially update a nextEmployeeGamma.
     *
     * @param nextEmployeeGamma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeGamma> partialUpdate(NextEmployeeGamma nextEmployeeGamma) {
        LOG.debug("Request to partially update NextEmployeeGamma : {}", nextEmployeeGamma);

        return nextEmployeeGammaRepository
            .findById(nextEmployeeGamma.getId())
            .map(existingNextEmployeeGamma -> {
                if (nextEmployeeGamma.getFirstName() != null) {
                    existingNextEmployeeGamma.setFirstName(nextEmployeeGamma.getFirstName());
                }
                if (nextEmployeeGamma.getLastName() != null) {
                    existingNextEmployeeGamma.setLastName(nextEmployeeGamma.getLastName());
                }
                if (nextEmployeeGamma.getEmail() != null) {
                    existingNextEmployeeGamma.setEmail(nextEmployeeGamma.getEmail());
                }
                if (nextEmployeeGamma.getHireDate() != null) {
                    existingNextEmployeeGamma.setHireDate(nextEmployeeGamma.getHireDate());
                }
                if (nextEmployeeGamma.getPosition() != null) {
                    existingNextEmployeeGamma.setPosition(nextEmployeeGamma.getPosition());
                }

                return existingNextEmployeeGamma;
            })
            .map(nextEmployeeGammaRepository::save);
    }

    /**
     * Get one nextEmployeeGamma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeGamma> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeGamma : {}", id);
        return nextEmployeeGammaRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeGamma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeGamma : {}", id);
        nextEmployeeGammaRepository.deleteById(id);
    }
}
