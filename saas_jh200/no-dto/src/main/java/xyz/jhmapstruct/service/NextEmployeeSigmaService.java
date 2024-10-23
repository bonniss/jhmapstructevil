package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.repository.NextEmployeeSigmaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeSigma}.
 */
@Service
@Transactional
public class NextEmployeeSigmaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeSigmaService.class);

    private final NextEmployeeSigmaRepository nextEmployeeSigmaRepository;

    public NextEmployeeSigmaService(NextEmployeeSigmaRepository nextEmployeeSigmaRepository) {
        this.nextEmployeeSigmaRepository = nextEmployeeSigmaRepository;
    }

    /**
     * Save a nextEmployeeSigma.
     *
     * @param nextEmployeeSigma the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeSigma save(NextEmployeeSigma nextEmployeeSigma) {
        LOG.debug("Request to save NextEmployeeSigma : {}", nextEmployeeSigma);
        return nextEmployeeSigmaRepository.save(nextEmployeeSigma);
    }

    /**
     * Update a nextEmployeeSigma.
     *
     * @param nextEmployeeSigma the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeSigma update(NextEmployeeSigma nextEmployeeSigma) {
        LOG.debug("Request to update NextEmployeeSigma : {}", nextEmployeeSigma);
        return nextEmployeeSigmaRepository.save(nextEmployeeSigma);
    }

    /**
     * Partially update a nextEmployeeSigma.
     *
     * @param nextEmployeeSigma the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeSigma> partialUpdate(NextEmployeeSigma nextEmployeeSigma) {
        LOG.debug("Request to partially update NextEmployeeSigma : {}", nextEmployeeSigma);

        return nextEmployeeSigmaRepository
            .findById(nextEmployeeSigma.getId())
            .map(existingNextEmployeeSigma -> {
                if (nextEmployeeSigma.getFirstName() != null) {
                    existingNextEmployeeSigma.setFirstName(nextEmployeeSigma.getFirstName());
                }
                if (nextEmployeeSigma.getLastName() != null) {
                    existingNextEmployeeSigma.setLastName(nextEmployeeSigma.getLastName());
                }
                if (nextEmployeeSigma.getEmail() != null) {
                    existingNextEmployeeSigma.setEmail(nextEmployeeSigma.getEmail());
                }
                if (nextEmployeeSigma.getHireDate() != null) {
                    existingNextEmployeeSigma.setHireDate(nextEmployeeSigma.getHireDate());
                }
                if (nextEmployeeSigma.getPosition() != null) {
                    existingNextEmployeeSigma.setPosition(nextEmployeeSigma.getPosition());
                }

                return existingNextEmployeeSigma;
            })
            .map(nextEmployeeSigmaRepository::save);
    }

    /**
     * Get one nextEmployeeSigma by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeSigma> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeSigma : {}", id);
        return nextEmployeeSigmaRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeSigma by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeSigma : {}", id);
        nextEmployeeSigmaRepository.deleteById(id);
    }
}
