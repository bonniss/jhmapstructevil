package ai.realworld.service;

import ai.realworld.domain.HexChar;
import ai.realworld.repository.HexCharRepository;
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

    public HexCharService(HexCharRepository hexCharRepository) {
        this.hexCharRepository = hexCharRepository;
    }

    /**
     * Save a hexChar.
     *
     * @param hexChar the entity to save.
     * @return the persisted entity.
     */
    public HexChar save(HexChar hexChar) {
        LOG.debug("Request to save HexChar : {}", hexChar);
        return hexCharRepository.save(hexChar);
    }

    /**
     * Update a hexChar.
     *
     * @param hexChar the entity to save.
     * @return the persisted entity.
     */
    public HexChar update(HexChar hexChar) {
        LOG.debug("Request to update HexChar : {}", hexChar);
        return hexCharRepository.save(hexChar);
    }

    /**
     * Partially update a hexChar.
     *
     * @param hexChar the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HexChar> partialUpdate(HexChar hexChar) {
        LOG.debug("Request to partially update HexChar : {}", hexChar);

        return hexCharRepository
            .findById(hexChar.getId())
            .map(existingHexChar -> {
                if (hexChar.getDob() != null) {
                    existingHexChar.setDob(hexChar.getDob());
                }
                if (hexChar.getGender() != null) {
                    existingHexChar.setGender(hexChar.getGender());
                }
                if (hexChar.getPhone() != null) {
                    existingHexChar.setPhone(hexChar.getPhone());
                }
                if (hexChar.getBioHeitiga() != null) {
                    existingHexChar.setBioHeitiga(hexChar.getBioHeitiga());
                }
                if (hexChar.getIsEnabled() != null) {
                    existingHexChar.setIsEnabled(hexChar.getIsEnabled());
                }

                return existingHexChar;
            })
            .map(hexCharRepository::save);
    }

    /**
     * Get all the hexChars with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HexChar> findAllWithEagerRelationships(Pageable pageable) {
        return hexCharRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one hexChar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HexChar> findOne(Long id) {
        LOG.debug("Request to get HexChar : {}", id);
        return hexCharRepository.findOneWithEagerRelationships(id);
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
