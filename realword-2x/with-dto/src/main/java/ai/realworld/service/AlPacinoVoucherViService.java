package ai.realworld.service;

import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.repository.AlPacinoVoucherViRepository;
import ai.realworld.service.dto.AlPacinoVoucherViDTO;
import ai.realworld.service.mapper.AlPacinoVoucherViMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoVoucherVi}.
 */
@Service
@Transactional
public class AlPacinoVoucherViService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherViService.class);

    private final AlPacinoVoucherViRepository alPacinoVoucherViRepository;

    private final AlPacinoVoucherViMapper alPacinoVoucherViMapper;

    public AlPacinoVoucherViService(
        AlPacinoVoucherViRepository alPacinoVoucherViRepository,
        AlPacinoVoucherViMapper alPacinoVoucherViMapper
    ) {
        this.alPacinoVoucherViRepository = alPacinoVoucherViRepository;
        this.alPacinoVoucherViMapper = alPacinoVoucherViMapper;
    }

    /**
     * Save a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherViDTO save(AlPacinoVoucherViDTO alPacinoVoucherViDTO) {
        LOG.debug("Request to save AlPacinoVoucherVi : {}", alPacinoVoucherViDTO);
        AlPacinoVoucherVi alPacinoVoucherVi = alPacinoVoucherViMapper.toEntity(alPacinoVoucherViDTO);
        alPacinoVoucherVi = alPacinoVoucherViRepository.save(alPacinoVoucherVi);
        return alPacinoVoucherViMapper.toDto(alPacinoVoucherVi);
    }

    /**
     * Update a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherViDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherViDTO update(AlPacinoVoucherViDTO alPacinoVoucherViDTO) {
        LOG.debug("Request to update AlPacinoVoucherVi : {}", alPacinoVoucherViDTO);
        AlPacinoVoucherVi alPacinoVoucherVi = alPacinoVoucherViMapper.toEntity(alPacinoVoucherViDTO);
        alPacinoVoucherVi = alPacinoVoucherViRepository.save(alPacinoVoucherVi);
        return alPacinoVoucherViMapper.toDto(alPacinoVoucherVi);
    }

    /**
     * Partially update a alPacinoVoucherVi.
     *
     * @param alPacinoVoucherViDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoVoucherViDTO> partialUpdate(AlPacinoVoucherViDTO alPacinoVoucherViDTO) {
        LOG.debug("Request to partially update AlPacinoVoucherVi : {}", alPacinoVoucherViDTO);

        return alPacinoVoucherViRepository
            .findById(alPacinoVoucherViDTO.getId())
            .map(existingAlPacinoVoucherVi -> {
                alPacinoVoucherViMapper.partialUpdate(existingAlPacinoVoucherVi, alPacinoVoucherViDTO);

                return existingAlPacinoVoucherVi;
            })
            .map(alPacinoVoucherViRepository::save)
            .map(alPacinoVoucherViMapper::toDto);
    }

    /**
     * Get one alPacinoVoucherVi by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoVoucherViDTO> findOne(UUID id) {
        LOG.debug("Request to get AlPacinoVoucherVi : {}", id);
        return alPacinoVoucherViRepository.findById(id).map(alPacinoVoucherViMapper::toDto);
    }

    /**
     * Delete the alPacinoVoucherVi by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacinoVoucherVi : {}", id);
        alPacinoVoucherViRepository.deleteById(id);
    }
}
