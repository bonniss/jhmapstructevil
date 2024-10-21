package ai.realworld.service;

import ai.realworld.domain.HashRoss;
import ai.realworld.repository.HashRossRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HashRoss}.
 */
@Service
@Transactional
public class HashRossService {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossService.class);

    private final HashRossRepository hashRossRepository;

    public HashRossService(HashRossRepository hashRossRepository) {
        this.hashRossRepository = hashRossRepository;
    }

    /**
     * Save a hashRoss.
     *
     * @param hashRoss the entity to save.
     * @return the persisted entity.
     */
    public HashRoss save(HashRoss hashRoss) {
        LOG.debug("Request to save HashRoss : {}", hashRoss);
        return hashRossRepository.save(hashRoss);
    }

    /**
     * Update a hashRoss.
     *
     * @param hashRoss the entity to save.
     * @return the persisted entity.
     */
    public HashRoss update(HashRoss hashRoss) {
        LOG.debug("Request to update HashRoss : {}", hashRoss);
        return hashRossRepository.save(hashRoss);
    }

    /**
     * Partially update a hashRoss.
     *
     * @param hashRoss the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HashRoss> partialUpdate(HashRoss hashRoss) {
        LOG.debug("Request to partially update HashRoss : {}", hashRoss);

        return hashRossRepository
            .findById(hashRoss.getId())
            .map(existingHashRoss -> {
                if (hashRoss.getName() != null) {
                    existingHashRoss.setName(hashRoss.getName());
                }
                if (hashRoss.getSlug() != null) {
                    existingHashRoss.setSlug(hashRoss.getSlug());
                }
                if (hashRoss.getDescription() != null) {
                    existingHashRoss.setDescription(hashRoss.getDescription());
                }
                if (hashRoss.getPermissionGridJason() != null) {
                    existingHashRoss.setPermissionGridJason(hashRoss.getPermissionGridJason());
                }

                return existingHashRoss;
            })
            .map(hashRossRepository::save);
    }

    /**
     * Get one hashRoss by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HashRoss> findOne(Long id) {
        LOG.debug("Request to get HashRoss : {}", id);
        return hashRossRepository.findById(id);
    }

    /**
     * Delete the hashRoss by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HashRoss : {}", id);
        hashRossRepository.deleteById(id);
    }
}
