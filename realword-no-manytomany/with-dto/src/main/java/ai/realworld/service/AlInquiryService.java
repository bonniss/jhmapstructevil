package ai.realworld.service;

import ai.realworld.domain.AlInquiry;
import ai.realworld.repository.AlInquiryRepository;
import ai.realworld.service.dto.AlInquiryDTO;
import ai.realworld.service.mapper.AlInquiryMapper;
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

    private final AlInquiryMapper alInquiryMapper;

    public AlInquiryService(AlInquiryRepository alInquiryRepository, AlInquiryMapper alInquiryMapper) {
        this.alInquiryRepository = alInquiryRepository;
        this.alInquiryMapper = alInquiryMapper;
    }

    /**
     * Save a alInquiry.
     *
     * @param alInquiryDTO the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryDTO save(AlInquiryDTO alInquiryDTO) {
        LOG.debug("Request to save AlInquiry : {}", alInquiryDTO);
        AlInquiry alInquiry = alInquiryMapper.toEntity(alInquiryDTO);
        alInquiry = alInquiryRepository.save(alInquiry);
        return alInquiryMapper.toDto(alInquiry);
    }

    /**
     * Update a alInquiry.
     *
     * @param alInquiryDTO the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryDTO update(AlInquiryDTO alInquiryDTO) {
        LOG.debug("Request to update AlInquiry : {}", alInquiryDTO);
        AlInquiry alInquiry = alInquiryMapper.toEntity(alInquiryDTO);
        alInquiry = alInquiryRepository.save(alInquiry);
        return alInquiryMapper.toDto(alInquiry);
    }

    /**
     * Partially update a alInquiry.
     *
     * @param alInquiryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlInquiryDTO> partialUpdate(AlInquiryDTO alInquiryDTO) {
        LOG.debug("Request to partially update AlInquiry : {}", alInquiryDTO);

        return alInquiryRepository
            .findById(alInquiryDTO.getId())
            .map(existingAlInquiry -> {
                alInquiryMapper.partialUpdate(existingAlInquiry, alInquiryDTO);

                return existingAlInquiry;
            })
            .map(alInquiryRepository::save)
            .map(alInquiryMapper::toDto);
    }

    /**
     * Get one alInquiry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlInquiryDTO> findOne(UUID id) {
        LOG.debug("Request to get AlInquiry : {}", id);
        return alInquiryRepository.findById(id).map(alInquiryMapper::toDto);
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
