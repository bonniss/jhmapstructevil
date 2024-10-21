package ai.realworld.service;

import ai.realworld.domain.HashRossVi;
import ai.realworld.repository.HashRossViRepository;
import ai.realworld.service.dto.HashRossViDTO;
import ai.realworld.service.mapper.HashRossViMapper;
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

    private final HashRossViMapper hashRossViMapper;

    public HashRossViService(HashRossViRepository hashRossViRepository, HashRossViMapper hashRossViMapper) {
        this.hashRossViRepository = hashRossViRepository;
        this.hashRossViMapper = hashRossViMapper;
    }

    /**
     * Save a hashRossVi.
     *
     * @param hashRossViDTO the entity to save.
     * @return the persisted entity.
     */
    public HashRossViDTO save(HashRossViDTO hashRossViDTO) {
        LOG.debug("Request to save HashRossVi : {}", hashRossViDTO);
        HashRossVi hashRossVi = hashRossViMapper.toEntity(hashRossViDTO);
        hashRossVi = hashRossViRepository.save(hashRossVi);
        return hashRossViMapper.toDto(hashRossVi);
    }

    /**
     * Update a hashRossVi.
     *
     * @param hashRossViDTO the entity to save.
     * @return the persisted entity.
     */
    public HashRossViDTO update(HashRossViDTO hashRossViDTO) {
        LOG.debug("Request to update HashRossVi : {}", hashRossViDTO);
        HashRossVi hashRossVi = hashRossViMapper.toEntity(hashRossViDTO);
        hashRossVi = hashRossViRepository.save(hashRossVi);
        return hashRossViMapper.toDto(hashRossVi);
    }

    /**
     * Partially update a hashRossVi.
     *
     * @param hashRossViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HashRossViDTO> partialUpdate(HashRossViDTO hashRossViDTO) {
        LOG.debug("Request to partially update HashRossVi : {}", hashRossViDTO);

        return hashRossViRepository
            .findById(hashRossViDTO.getId())
            .map(existingHashRossVi -> {
                hashRossViMapper.partialUpdate(existingHashRossVi, hashRossViDTO);

                return existingHashRossVi;
            })
            .map(hashRossViRepository::save)
            .map(hashRossViMapper::toDto);
    }

    /**
     * Get one hashRossVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HashRossViDTO> findOne(Long id) {
        LOG.debug("Request to get HashRossVi : {}", id);
        return hashRossViRepository.findById(id).map(hashRossViMapper::toDto);
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
