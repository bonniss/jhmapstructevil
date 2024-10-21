package ai.realworld.service;

import ai.realworld.domain.AlPacinoVoucher;
import ai.realworld.repository.AlPacinoVoucherRepository;
import ai.realworld.service.dto.AlPacinoVoucherDTO;
import ai.realworld.service.mapper.AlPacinoVoucherMapper;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ai.realworld.domain.AlPacinoVoucher}.
 */
@Service
@Transactional
public class AlPacinoVoucherService {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherService.class);

    private final AlPacinoVoucherRepository alPacinoVoucherRepository;

    private final AlPacinoVoucherMapper alPacinoVoucherMapper;

    public AlPacinoVoucherService(AlPacinoVoucherRepository alPacinoVoucherRepository, AlPacinoVoucherMapper alPacinoVoucherMapper) {
        this.alPacinoVoucherRepository = alPacinoVoucherRepository;
        this.alPacinoVoucherMapper = alPacinoVoucherMapper;
    }

    /**
     * Save a alPacinoVoucher.
     *
     * @param alPacinoVoucherDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherDTO save(AlPacinoVoucherDTO alPacinoVoucherDTO) {
        LOG.debug("Request to save AlPacinoVoucher : {}", alPacinoVoucherDTO);
        AlPacinoVoucher alPacinoVoucher = alPacinoVoucherMapper.toEntity(alPacinoVoucherDTO);
        alPacinoVoucher = alPacinoVoucherRepository.save(alPacinoVoucher);
        return alPacinoVoucherMapper.toDto(alPacinoVoucher);
    }

    /**
     * Update a alPacinoVoucher.
     *
     * @param alPacinoVoucherDTO the entity to save.
     * @return the persisted entity.
     */
    public AlPacinoVoucherDTO update(AlPacinoVoucherDTO alPacinoVoucherDTO) {
        LOG.debug("Request to update AlPacinoVoucher : {}", alPacinoVoucherDTO);
        AlPacinoVoucher alPacinoVoucher = alPacinoVoucherMapper.toEntity(alPacinoVoucherDTO);
        alPacinoVoucher = alPacinoVoucherRepository.save(alPacinoVoucher);
        return alPacinoVoucherMapper.toDto(alPacinoVoucher);
    }

    /**
     * Partially update a alPacinoVoucher.
     *
     * @param alPacinoVoucherDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AlPacinoVoucherDTO> partialUpdate(AlPacinoVoucherDTO alPacinoVoucherDTO) {
        LOG.debug("Request to partially update AlPacinoVoucher : {}", alPacinoVoucherDTO);

        return alPacinoVoucherRepository
            .findById(alPacinoVoucherDTO.getId())
            .map(existingAlPacinoVoucher -> {
                alPacinoVoucherMapper.partialUpdate(existingAlPacinoVoucher, alPacinoVoucherDTO);

                return existingAlPacinoVoucher;
            })
            .map(alPacinoVoucherRepository::save)
            .map(alPacinoVoucherMapper::toDto);
    }

    /**
     * Get one alPacinoVoucher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AlPacinoVoucherDTO> findOne(UUID id) {
        LOG.debug("Request to get AlPacinoVoucher : {}", id);
        return alPacinoVoucherRepository.findById(id).map(alPacinoVoucherMapper::toDto);
    }

    /**
     * Delete the alPacinoVoucher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(UUID id) {
        LOG.debug("Request to delete AlPacinoVoucher : {}", id);
        alPacinoVoucherRepository.deleteById(id);
    }
}
