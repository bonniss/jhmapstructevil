package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextInvoice;
import xyz.jhmapstruct.repository.NextInvoiceRepository;
import xyz.jhmapstruct.service.dto.NextInvoiceDTO;
import xyz.jhmapstruct.service.mapper.NextInvoiceMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextInvoice}.
 */
@Service
@Transactional
public class NextInvoiceService {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceService.class);

    private final NextInvoiceRepository nextInvoiceRepository;

    private final NextInvoiceMapper nextInvoiceMapper;

    public NextInvoiceService(NextInvoiceRepository nextInvoiceRepository, NextInvoiceMapper nextInvoiceMapper) {
        this.nextInvoiceRepository = nextInvoiceRepository;
        this.nextInvoiceMapper = nextInvoiceMapper;
    }

    /**
     * Save a nextInvoice.
     *
     * @param nextInvoiceDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceDTO save(NextInvoiceDTO nextInvoiceDTO) {
        LOG.debug("Request to save NextInvoice : {}", nextInvoiceDTO);
        NextInvoice nextInvoice = nextInvoiceMapper.toEntity(nextInvoiceDTO);
        nextInvoice = nextInvoiceRepository.save(nextInvoice);
        return nextInvoiceMapper.toDto(nextInvoice);
    }

    /**
     * Update a nextInvoice.
     *
     * @param nextInvoiceDTO the entity to save.
     * @return the persisted entity.
     */
    public NextInvoiceDTO update(NextInvoiceDTO nextInvoiceDTO) {
        LOG.debug("Request to update NextInvoice : {}", nextInvoiceDTO);
        NextInvoice nextInvoice = nextInvoiceMapper.toEntity(nextInvoiceDTO);
        nextInvoice = nextInvoiceRepository.save(nextInvoice);
        return nextInvoiceMapper.toDto(nextInvoice);
    }

    /**
     * Partially update a nextInvoice.
     *
     * @param nextInvoiceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextInvoiceDTO> partialUpdate(NextInvoiceDTO nextInvoiceDTO) {
        LOG.debug("Request to partially update NextInvoice : {}", nextInvoiceDTO);

        return nextInvoiceRepository
            .findById(nextInvoiceDTO.getId())
            .map(existingNextInvoice -> {
                nextInvoiceMapper.partialUpdate(existingNextInvoice, nextInvoiceDTO);

                return existingNextInvoice;
            })
            .map(nextInvoiceRepository::save)
            .map(nextInvoiceMapper::toDto);
    }

    /**
     * Get one nextInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextInvoiceDTO> findOne(Long id) {
        LOG.debug("Request to get NextInvoice : {}", id);
        return nextInvoiceRepository.findById(id).map(nextInvoiceMapper::toDto);
    }

    /**
     * Delete the nextInvoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextInvoice : {}", id);
        nextInvoiceRepository.deleteById(id);
    }
}
