package ai.realworld.service;

import ai.realworld.domain.JohnLennon;
import ai.realworld.repository.JohnLennonRepository;
import ai.realworld.service.dto.JohnLennonDTO;
import ai.realworld.service.mapper.JohnLennonMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.JohnLennon}.
 */
@Service
@Transactional
public class JohnLennonService {

    private static final Logger LOG = LoggerFactory.getLogger(JohnLennonService.class);

    private final JohnLennonRepository johnLennonRepository;

    private final JohnLennonMapper johnLennonMapper;

    public JohnLennonService(JohnLennonRepository johnLennonRepository, JohnLennonMapper johnLennonMapper) {
        this.johnLennonRepository = johnLennonRepository;
        this.johnLennonMapper = johnLennonMapper;
    }

    /**
     * Save a johnLennon.
     *
     * @param johnLennonDTO the entity to save.
     * @return the persisted entity.
     */
    public JohnLennonDTO save(JohnLennonDTO johnLennonDTO) {
        LOG.debug("Request to save JohnLennon : {}", johnLennonDTO);
        JohnLennon johnLennon = johnLennonMapper.toEntity(johnLennonDTO);
        johnLennon = johnLennonRepository.save(johnLennon);
        return johnLennonMapper.toDto(johnLennon);
    }

    /**
     * Update a johnLennon.
     *
     * @param johnLennonDTO the entity to save.
     * @return the persisted entity.
     */
    public JohnLennonDTO update(JohnLennonDTO johnLennonDTO) {
        LOG.debug("Request to update JohnLennon : {}", johnLennonDTO);
        JohnLennon johnLennon = johnLennonMapper.toEntity(johnLennonDTO);
        johnLennon = johnLennonRepository.save(johnLennon);
        return johnLennonMapper.toDto(johnLennon);
    }

    /**
     * Partially update a johnLennon.
     *
     * @param johnLennonDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<JohnLennonDTO> partialUpdate(JohnLennonDTO johnLennonDTO) {
        LOG.debug("Request to partially update JohnLennon : {}", johnLennonDTO);

        return johnLennonRepository
            .findById(johnLennonDTO.getId())
            .map(existingJohnLennon -> {
                johnLennonMapper.partialUpdate(existingJohnLennon, johnLennonDTO);

                return existingJohnLennon;
            })
            .map(johnLennonRepository::save)
            .map(johnLennonMapper::toDto);
    }

    /**
     * Get one johnLennon by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<JohnLennonDTO> findOne(UUID id) {
        LOG.debug("Request to get JohnLennon : {}", id);
        return johnLennonRepository.findById(id).map(johnLennonMapper::toDto);
    }

    /**
     * Delete the johnLennon by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete JohnLennon : {}", id);
        johnLennonRepository.deleteById(id);
    }
}
