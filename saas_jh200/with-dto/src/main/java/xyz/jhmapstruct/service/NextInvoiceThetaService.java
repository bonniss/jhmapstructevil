package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.repository.NextInvoiceThetaRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceThetaDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceThetaMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoiceTheta}.
 */
@Service
@Transactional
public class NextInvoiceThetaService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceThetaService.class);

    private final NextInvoiceThetaRepository nextInvoiceThetaRepository;

    private final NextInvoiceThetaMapper nextInvoiceThetaMapper;

    public NextInvoiceThetaService(NextInvoiceThetaRepository nextInvoiceThetaRepository, NextInvoiceThetaMapper nextInvoiceThetaMapper) {
        this.nextInvoiceThetaRepository = nextInvoiceThetaRepository;
        this.nextInvoiceThetaMapper = nextInvoiceThetaMapper;
    }

    /**
     * Save a nextInvoiceTheta.
     *
     * @param nextInvoiceThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceThetaDTO save(NextInvoiceThetaDTO nextInvoiceThetaDTO) {
        LOG.debug("Request to save NextInvoiceTheta : {}", nextInvoiceThetaDTO);
        NextInvoiceTheta nextInvoiceTheta = nextInvoiceThetaMapper.toEntity(nextInvoiceThetaDTO);
        nextInvoiceTheta = nextInvoiceThetaRepository.save(nextInvoiceTheta);
        return nextInvoiceThetaMapper.toDto(nextInvoiceTheta);
    }

    /**
     * Update a nextInvoiceTheta.
     *
     * @param nextInvoiceThetaDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceThetaDTO update(NextInvoiceThetaDTO nextInvoiceThetaDTO) {
        LOG.debug("Request to update NextInvoiceTheta : {}", nextInvoiceThetaDTO);
        NextInvoiceTheta nextInvoiceTheta = nextInvoiceThetaMapper.toEntity(nextInvoiceThetaDTO);
        nextInvoiceTheta = nextInvoiceThetaRepository.save(nextInvoiceTheta);
        return nextInvoiceThetaMapper.toDto(nextInvoiceTheta);
    }

    /**
     * Partially update a nextInvoiceTheta.
     *
     * @param nextInvoiceThetaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceThetaDTO> partialUpdate(NextInvoiceThetaDTO nextInvoiceThetaDTO) {
        LOG.debug("Request to partially update NextInvoiceTheta : {}", nextInvoiceThetaDTO);

        return nextInvoiceThetaRepository
            .findById(nextInvoiceThetaDTO.getId())
            .map(existingNextInvoiceTheta -> {
                nextInvoiceThetaMapper.partialUpdate(existingNextInvoiceTheta, nextInvoiceThetaDTO);

                return existingNextInvoiceTheta;
            })
            .map(nextInvoiceThetaRepository::save)
            .map(nextInvoiceThetaMapper::toDto);
    }

    /**
     * Get one nextInvoiceTheta by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceThetaDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoiceTheta : {}", id);
        return nextInvoiceThetaRepository.findById(id).map(nextInvoiceThetaMapper::toDto);
    }

    /**
     * Delete the nextInvoiceTheta by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoiceTheta : {}", id);
        nextInvoiceThetaRepository.deleteById(id);
    }
}
