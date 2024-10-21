package ai.realworld.service;

import ai.realworld.domain.AlInquiryVi;
import ai.realworld.repository.AlInquiryViRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlInquiryVi}.
 */
@Service
@Transactional
public class AlInquiryViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryViService.class);

    private final AlInquiryViRepository alInquiryViRepository;

    public AlInquiryViService(AlInquiryViRepository alInquiryViRepository) {
        this.alInquiryViRepository = alInquiryViRepository;
    }

    /**
     * Save a alInquiryVi.
     *
     * @param alInquiryVi the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryVi save(AlInquiryVi alInquiryVi) {
        LOG.debug("Request to save AlInquiryVi : {}", alInquiryVi);
        return alInquiryViRepository.save(alInquiryVi);
    }

    /**
     * Update a alInquiryVi.
     *
     * @param alInquiryVi the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryVi update(AlInquiryVi alInquiryVi) {
        LOG.debug("Request to update AlInquiryVi : {}", alInquiryVi);
        return alInquiryViRepository.save(alInquiryVi);
    }

    /**
     * Partially update a alInquiryVi.
     *
     * @param alInquiryVi the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlInquiryVi> partialUpdate(AlInquiryVi alInquiryVi) {
        LOG.debug("Request to partially update AlInquiryVi : {}", alInquiryVi);

        return alInquiryViRepository
            .findById(alInquiryVi.getId())
            .map(existingAlInquiryVi -> {
                if (alInquiryVi.getTitle() != null) {
                    existingAlInquiryVi.setTitle(alInquiryVi.getTitle());
                }
                if (alInquiryVi.getBody() != null) {
                    existingAlInquiryVi.setBody(alInquiryVi.getBody());
                }
                if (alInquiryVi.getSender() != null) {
                    existingAlInquiryVi.setSender(alInquiryVi.getSender());
                }
                if (alInquiryVi.getEmail() != null) {
                    existingAlInquiryVi.setEmail(alInquiryVi.getEmail());
                }
                if (alInquiryVi.getPhone() != null) {
                    existingAlInquiryVi.setPhone(alInquiryVi.getPhone());
                }
                if (alInquiryVi.getContentJason() != null) {
                    existingAlInquiryVi.setContentJason(alInquiryVi.getContentJason());
                }

                return existingAlInquiryVi;
            })
            .map(alInquiryViRepository::save);
    }

    /**
     * Get one alInquiryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlInquiryVi> findOne(UUID id) {
        LOG.debug("Request to get AlInquiryVi : {}", id);
        return alInquiryViRepository.findById(id);
    }

    /**
     * Delete the alInquiryVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlInquiryVi : {}", id);
        alInquiryViRepository.deleteById(id);
    }
}
