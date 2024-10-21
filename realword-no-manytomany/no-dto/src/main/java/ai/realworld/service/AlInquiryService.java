package ai.realworld.service;

import ai.realworld.domain.AlInquiry;
import ai.realworld.repository.AlInquiryRepository;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlInquiry}.
 */
@Service
@Transactional
public class AlInquiryService {

    private static final Logger LOG = LoggerFactory.getLogger(AlInquiryService.class);

    private final AlInquiryRepository alInquiryRepository;

    public AlInquiryService(AlInquiryRepository alInquiryRepository) {
        this.alInquiryRepository = alInquiryRepository;
    }

    /**
     * Save a alInquiry.
     *
     * @param alInquiry the entity to save.
     * @return the persisted entity.
     */
    public AlInquiry save(AlInquiry alInquiry) {
        LOG.debug("Request to save AlInquiry : {}", alInquiry);
        return alInquiryRepository.save(alInquiry);
    }

    /**
     * Update a alInquiry.
     *
     * @param alInquiry the entity to save.
     * @return the persisted entity.
     */
    public AlInquiry update(AlInquiry alInquiry) {
        LOG.debug("Request to update AlInquiry : {}", alInquiry);
        return alInquiryRepository.save(alInquiry);
    }

    /**
     * Partially update a alInquiry.
     *
     * @param alInquiry the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlInquiry> partialUpdate(AlInquiry alInquiry) {
        LOG.debug("Request to partially update AlInquiry : {}", alInquiry);

        return alInquiryRepository
            .findById(alInquiry.getId())
            .map(existingAlInquiry -> {
                if (alInquiry.getTitle() != null) {
                    existingAlInquiry.setTitle(alInquiry.getTitle());
                }
                if (alInquiry.getBody() != null) {
                    existingAlInquiry.setBody(alInquiry.getBody());
                }
                if (alInquiry.getSender() != null) {
                    existingAlInquiry.setSender(alInquiry.getSender());
                }
                if (alInquiry.getEmail() != null) {
                    existingAlInquiry.setEmail(alInquiry.getEmail());
                }
                if (alInquiry.getPhone() != null) {
                    existingAlInquiry.setPhone(alInquiry.getPhone());
                }
                if (alInquiry.getContentJason() != null) {
                    existingAlInquiry.setContentJason(alInquiry.getContentJason());
                }

                return existingAlInquiry;
            })
            .map(alInquiryRepository::save);
    }

    /**
     * Get one alInquiry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlInquiry> findOne(UUID id) {
        LOG.debug("Request to get AlInquiry : {}", id);
        return alInquiryRepository.findById(id);
    }

    /**
     * Delete the alInquiry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlInquiry : {}", id);
        alInquiryRepository.deleteById(id);
    }
}
