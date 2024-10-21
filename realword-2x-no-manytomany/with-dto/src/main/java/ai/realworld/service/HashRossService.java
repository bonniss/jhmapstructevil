package ai.realworld.service;

import ai.realworld.domain.HashRoss;
import ai.realworld.repository.HashRossRepository;
import ai.realworld.service.dto.HashRossDTO;
import ai.realworld.service.mapper.HashRossMapper;
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

    private final HashRossMapper hashRossMapper;

    public HashRossService(HashRossRepository hashRossRepository, HashRossMapper hashRossMapper) {
        this.hashRossRepository = hashRossRepository;
        this.hashRossMapper = hashRossMapper;
    }

    /**
     * Save a hashRoss.
     *
     * @param hashRossDTO the entity to save.
     * @return the persisted entity.
     */
    public HashRossDTO save(HashRossDTO hashRossDTO) {
        LOG.debug("Request to save HashRoss : {}", hashRossDTO);
        HashRoss hashRoss = hashRossMapper.toEntity(hashRossDTO);
        hashRoss = hashRossRepository.save(hashRoss);
        return hashRossMapper.toDto(hashRoss);
    }

    /**
     * Update a hashRoss.
     *
     * @param hashRossDTO the entity to save.
     * @return the persisted entity.
     */
    public HashRossDTO update(HashRossDTO hashRossDTO) {
        LOG.debug("Request to update HashRoss : {}", hashRossDTO);
        HashRoss hashRoss = hashRossMapper.toEntity(hashRossDTO);
        hashRoss = hashRossRepository.save(hashRoss);
        return hashRossMapper.toDto(hashRoss);
    }

    /**
     * Partially update a hashRoss.
     *
     * @param hashRossDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HashRossDTO> partialUpdate(HashRossDTO hashRossDTO) {
        LOG.debug("Request to partially update HashRoss : {}", hashRossDTO);

        return hashRossRepository
            .findById(hashRossDTO.getId())
            .map(existingHashRoss -> {
                hashRossMapper.partialUpdate(existingHashRoss, hashRossDTO);

                return existingHashRoss;
            })
            .map(hashRossRepository::save)
            .map(hashRossMapper::toDto);
    }

    /**
     * Get one hashRoss by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HashRossDTO> findOne(Long id) {
        LOG.debug("Request to get HashRoss : {}", id);
        return hashRossRepository.findById(id).map(hashRossMapper::toDto);
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
