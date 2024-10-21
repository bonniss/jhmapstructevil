package ai.realworld.service;

import ai.realworld.domain.AlInquiryVi;
import ai.realworld.repository.AlInquiryViRepository;
import ai.realworld.service.dto.AlInquiryViDTO;
import ai.realworld.service.mapper.AlInquiryViMapper;
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

    private final AlInquiryViMapper alInquiryViMapper;

    public AlInquiryViService(AlInquiryViRepository alInquiryViRepository, AlInquiryViMapper alInquiryViMapper) {
        this.alInquiryViRepository = alInquiryViRepository;
        this.alInquiryViMapper = alInquiryViMapper;
    }

    /**
     * Save a alInquiryVi.
     *
     * @param alInquiryViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryViDTO save(AlInquiryViDTO alInquiryViDTO) {
        LOG.debug("Request to save AlInquiryVi : {}", alInquiryViDTO);
        AlInquiryVi alInquiryVi = alInquiryViMapper.toEntity(alInquiryViDTO);
        alInquiryVi = alInquiryViRepository.save(alInquiryVi);
        return alInquiryViMapper.toDto(alInquiryVi);
    }

    /**
     * Update a alInquiryVi.
     *
     * @param alInquiryViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlInquiryViDTO update(AlInquiryViDTO alInquiryViDTO) {
        LOG.debug("Request to update AlInquiryVi : {}", alInquiryViDTO);
        AlInquiryVi alInquiryVi = alInquiryViMapper.toEntity(alInquiryViDTO);
        alInquiryVi = alInquiryViRepository.save(alInquiryVi);
        return alInquiryViMapper.toDto(alInquiryVi);
    }

    /**
     * Partially update a alInquiryVi.
     *
     * @param alInquiryViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlInquiryViDTO> partialUpdate(AlInquiryViDTO alInquiryViDTO) {
        LOG.debug("Request to partially update AlInquiryVi : {}", alInquiryViDTO);

        return alInquiryViRepository
            .findById(alInquiryViDTO.getId())
            .map(existingAlInquiryVi -> {
                alInquiryViMapper.partialUpdate(existingAlInquiryVi, alInquiryViDTO);

                return existingAlInquiryVi;
            })
            .map(alInquiryViRepository::save)
            .map(alInquiryViMapper::toDto);
    }

    /**
     * Get one alInquiryVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlInquiryViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlInquiryVi : {}", id);
        return alInquiryViRepository.findById(id).map(alInquiryViMapper::toDto);
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
