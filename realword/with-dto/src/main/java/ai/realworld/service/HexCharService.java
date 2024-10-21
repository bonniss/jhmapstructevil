package ai.realworld.service;

import ai.realworld.domain.HexChar;
import ai.realworld.repository.HexCharRepository;
import ai.realworld.service.dto.HexCharDTO;
import ai.realworld.service.mapper.HexCharMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HexChar}.
 */
@Service
@Transactional
public class HexCharService {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharService.class);

    private final HexCharRepository hexCharRepository;

    private final HexCharMapper hexCharMapper;

    public HexCharService(HexCharRepository hexCharRepository, HexCharMapper hexCharMapper) {
        this.hexCharRepository = hexCharRepository;
        this.hexCharMapper = hexCharMapper;
    }

    /**
     * Save a hexChar.
     *
     * @param hexCharDTO the entity to save.
     * @return the persisted entity.
     */
    public HexCharDTO save(HexCharDTO hexCharDTO) {
        LOG.debug("Request to save HexChar : {}", hexCharDTO);
        HexChar hexChar = hexCharMapper.toEntity(hexCharDTO);
        hexChar = hexCharRepository.save(hexChar);
        return hexCharMapper.toDto(hexChar);
    }

    /**
     * Update a hexChar.
     *
     * @param hexCharDTO the entity to save.
     * @return the persisted entity.
     */
    public HexCharDTO update(HexCharDTO hexCharDTO) {
        LOG.debug("Request to update HexChar : {}", hexCharDTO);
        HexChar hexChar = hexCharMapper.toEntity(hexCharDTO);
        hexChar = hexCharRepository.save(hexChar);
        return hexCharMapper.toDto(hexChar);
    }

    /**
     * Partially update a hexChar.
     *
     * @param hexCharDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HexCharDTO> partialUpdate(HexCharDTO hexCharDTO) {
        LOG.debug("Request to partially update HexChar : {}", hexCharDTO);

        return hexCharRepository
            .findById(hexCharDTO.getId())
            .map(existingHexChar -> {
                hexCharMapper.partialUpdate(existingHexChar, hexCharDTO);

                return existingHexChar;
            })
            .map(hexCharRepository::save)
            .map(hexCharMapper::toDto);
    }

    /**
     * Get all the hexChars with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HexCharDTO> findAllWithEagerRelationships(Pageable pageable) {
        return hexCharRepository.findAllWithEagerRelationships(pageable).map(hexCharMapper::toDto);
    }

    /**
     * Get one hexChar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HexCharDTO> findOne(Long id) {
        LOG.debug("Request to get HexChar : {}", id);
        return hexCharRepository.findOneWithEagerRelationships(id).map(hexCharMapper::toDto);
    }

    /**
     * Delete the hexChar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HexChar : {}", id);
        hexCharRepository.deleteById(id);
    }
}
