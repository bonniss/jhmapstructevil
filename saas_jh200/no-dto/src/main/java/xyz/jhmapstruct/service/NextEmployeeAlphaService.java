package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextEmployeeAlpha;
import xyz.jhmapstruct.repository.NextEmployeeAlphaRepository;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextEmployeeAlpha}.
 */
@Service
@Transactional
public class NextEmployeeAlphaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeAlphaService.class);

    private final NextEmployeeAlphaRepository nextEmployeeAlphaRepository;

    public NextEmployeeAlphaService(NextEmployeeAlphaRepository nextEmployeeAlphaRepository) {
        this.nextEmployeeAlphaRepository = nextEmployeeAlphaRepository;
    }

    /**
     * Save a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeAlpha save(NextEmployeeAlpha nextEmployeeAlpha) {
        LOG.debug("Request to save NextEmployeeAlpha : {}", nextEmployeeAlpha);
        return nextEmployeeAlphaRepository.save(nextEmployeeAlpha);
    }

    /**
     * Update a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlpha the entity to save.
     * @return the persisted entity.
     */
    public NextEmployeeAlpha update(NextEmployeeAlpha nextEmployeeAlpha) {
        LOG.debug("Request to update NextEmployeeAlpha : {}", nextEmployeeAlpha);
        return nextEmployeeAlphaRepository.save(nextEmployeeAlpha);
    }

    /**
     * Partially update a nextEmployeeAlpha.
     *
     * @param nextEmployeeAlpha the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextEmployeeAlpha> partialUpdate(NextEmployeeAlpha nextEmployeeAlpha) {
        LOG.debug("Request to partially update NextEmployeeAlpha : {}", nextEmployeeAlpha);

        return nextEmployeeAlphaRepository
            .findById(nextEmployeeAlpha.getId())
            .map(existingNextEmployeeAlpha -> {
                if (nextEmployeeAlpha.getFirstName() != null) {
                    existingNextEmployeeAlpha.setFirstName(nextEmployeeAlpha.getFirstName());
                }
                if (nextEmployeeAlpha.getLastName() != null) {
                    existingNextEmployeeAlpha.setLastName(nextEmployeeAlpha.getLastName());
                }
                if (nextEmployeeAlpha.getEmail() != null) {
                    existingNextEmployeeAlpha.setEmail(nextEmployeeAlpha.getEmail());
                }
                if (nextEmployeeAlpha.getHireDate() != null) {
                    existingNextEmployeeAlpha.setHireDate(nextEmployeeAlpha.getHireDate());
                }
                if (nextEmployeeAlpha.getPosition() != null) {
                    existingNextEmployeeAlpha.setPosition(nextEmployeeAlpha.getPosition());
                }

                return existingNextEmployeeAlpha;
            })
            .map(nextEmployeeAlphaRepository::save);
    }

    /**
     * Get one nextEmployeeAlpha by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextEmployeeAlpha> findOne(Long id) {
        LOG.debug("Request to get NextEmployeeAlpha : {}", id);
        return nextEmployeeAlphaRepository.findById(id);
    }

    /**
     * Delete the nextEmployeeAlpha by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextEmployeeAlpha : {}", id);
        nextEmployeeAlphaRepository.deleteById(id);
    }
}
