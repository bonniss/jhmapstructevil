package ai.realworld.service;

import ai.realworld.domain.HashRossVi;
import ai.realworld.repository.HashRossViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HashRossVi}.
 */
@Service
@Transactional
public class HashRossViService {

    private static final Logger LOG = LoggerFactory.getLogger(HashRossViService.class);

    private final HashRossViRepository hashRossViRepository;

    public HashRossViService(HashRossViRepository hashRossViRepository) {
        this.hashRossViRepository = hashRossViRepository;
    }

    /**
     * Save a hashRossVi.
     *
     * @param hashRossVi the entity to save.
     * @return the persisted entity.
     */
    public HashRossVi save(HashRossVi hashRossVi) {
        LOG.debug("Request to save HashRossVi : {}", hashRossVi);
        return hashRossViRepository.save(hashRossVi);
    }

    /**
     * Update a hashRossVi.
     *
     * @param hashRossVi the entity to save.
     * @return the persisted entity.
     */
    public HashRossVi update(HashRossVi hashRossVi) {
        LOG.debug("Request to update HashRossVi : {}", hashRossVi);
        return hashRossViRepository.save(hashRossVi);
    }

    /**
     * Partially update a hashRossVi.
     *
     * @param hashRossVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HashRossVi> partialUpdate(HashRossVi hashRossVi) {
        LOG.debug("Request to partially update HashRossVi : {}", hashRossVi);

        return hashRossViRepository
            .findById(hashRossVi.getId())
            .map(existingHashRossVi -> {
                if (hashRossVi.getName() != null) {
                    existingHashRossVi.setName(hashRossVi.getName());
                }
                if (hashRossVi.getSlug() != null) {
                    existingHashRossVi.setSlug(hashRossVi.getSlug());
                }
                if (hashRossVi.getDescription() != null) {
                    existingHashRossVi.setDescription(hashRossVi.getDescription());
                }
                if (hashRossVi.getPermissionGridJason() != null) {
                    existingHashRossVi.setPermissionGridJason(hashRossVi.getPermissionGridJason());
                }

                return existingHashRossVi;
            })
            .map(hashRossViRepository::save);
    }

    /**
     * Get one hashRossVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HashRossVi> findOne(Long id) {
        LOG.debug("Request to get HashRossVi : {}", id);
        return hashRossViRepository.findById(id);
    }

    /**
     * Delete the hashRossVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HashRossVi : {}", id);
        hashRossViRepository.deleteById(id);
    }
}
