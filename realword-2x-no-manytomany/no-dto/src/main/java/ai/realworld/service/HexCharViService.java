package ai.realworld.service;

import ai.realworld.domain.HexCharVi;
import ai.realworld.repository.HexCharViRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.HexCharVi}.
 */
@Service
@Transactional
public class HexCharViService {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharViService.class);

    private final HexCharViRepository hexCharViRepository;

    public HexCharViService(HexCharViRepository hexCharViRepository) {
        this.hexCharViRepository = hexCharViRepository;
    }

    /**
     * Save a hexCharVi.
     *
     * @param hexCharVi the entity to save.
     * @return the persisted entity.
     */
    public HexCharVi save(HexCharVi hexCharVi) {
        LOG.debug("Request to save HexCharVi : {}", hexCharVi);
        return hexCharViRepository.save(hexCharVi);
    }

    /**
     * Update a hexCharVi.
     *
     * @param hexCharVi the entity to save.
     * @return the persisted entity.
     */
    public HexCharVi update(HexCharVi hexCharVi) {
        LOG.debug("Request to update HexCharVi : {}", hexCharVi);
        return hexCharViRepository.save(hexCharVi);
    }

    /**
     * Partially update a hexCharVi.
     *
     * @param hexCharVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HexCharVi> partialUpdate(HexCharVi hexCharVi) {
        LOG.debug("Request to partially update HexCharVi : {}", hexCharVi);

        return hexCharViRepository
            .findById(hexCharVi.getId())
            .map(existingHexCharVi -> {
                if (hexCharVi.getDob() != null) {
                    existingHexCharVi.setDob(hexCharVi.getDob());
                }
                if (hexCharVi.getGender() != null) {
                    existingHexCharVi.setGender(hexCharVi.getGender());
                }
                if (hexCharVi.getPhone() != null) {
                    existingHexCharVi.setPhone(hexCharVi.getPhone());
                }
                if (hexCharVi.getBioHeitiga() != null) {
                    existingHexCharVi.setBioHeitiga(hexCharVi.getBioHeitiga());
                }
                if (hexCharVi.getIsEnabled() != null) {
                    existingHexCharVi.setIsEnabled(hexCharVi.getIsEnabled());
                }

                return existingHexCharVi;
            })
            .map(hexCharViRepository::save);
    }

    /**
     * Get all the hexCharVis with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HexCharVi> findAllWithEagerRelationships(Pageable pageable) {
        return hexCharViRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one hexCharVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HexCharVi> findOne(Long id) {
        LOG.debug("Request to get HexCharVi : {}", id);
        return hexCharViRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the hexCharVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HexCharVi : {}", id);
        hexCharViRepository.deleteById(id);
    }
}
