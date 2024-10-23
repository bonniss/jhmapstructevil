package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextPayment;
import xyz.jhmapstruct.repository.NextPaymentRepository;
import xyz.jhmapstruct.service.dto.NextPaymentDTO;
import xyz.jhmapstruct.service.mapper.NextPaymentMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextPayment}.
 */
@Service
@Transactional
public class NextPaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentService.class);

    private final NextPaymentRepository nextPaymentRepository;

    private final NextPaymentMapper nextPaymentMapper;

    public NextPaymentService(NextPaymentRepository nextPaymentRepository, NextPaymentMapper nextPaymentMapper) {
        this.nextPaymentRepository = nextPaymentRepository;
        this.nextPaymentMapper = nextPaymentMapper;
    }

    /**
     * Save a nextPayment.
     *
     * @param nextPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentDTO save(NextPaymentDTO nextPaymentDTO) {
        LOG.debug("Request to save NextPayment : {}", nextPaymentDTO);
        NextPayment nextPayment = nextPaymentMapper.toEntity(nextPaymentDTO);
        nextPayment = nextPaymentRepository.save(nextPayment);
        return nextPaymentMapper.toDto(nextPayment);
    }

    /**
     * Update a nextPayment.
     *
     * @param nextPaymentDTO the entity to save.
     * @return the persisted entity.
     */
    public NextPaymentDTO update(NextPaymentDTO nextPaymentDTO) {
        LOG.debug("Request to update NextPayment : {}", nextPaymentDTO);
        NextPayment nextPayment = nextPaymentMapper.toEntity(nextPaymentDTO);
        nextPayment = nextPaymentRepository.save(nextPayment);
        return nextPaymentMapper.toDto(nextPayment);
    }

    /**
     * Partially update a nextPayment.
     *
     * @param nextPaymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextPaymentDTO> partialUpdate(NextPaymentDTO nextPaymentDTO) {
        LOG.debug("Request to partially update NextPayment : {}", nextPaymentDTO);

        return nextPaymentRepository
            .findById(nextPaymentDTO.getId())
            .map(existingNextPayment -> {
                nextPaymentMapper.partialUpdate(existingNextPayment, nextPaymentDTO);

                return existingNextPayment;
            })
            .map(nextPaymentRepository::save)
            .map(nextPaymentMapper::toDto);
    }

    /**
     * Get one nextPayment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextPaymentDTO> findOne(Long id) {
        LOG.debug("Request to get NextPayment : {}", id);
        return nextPaymentRepository.findById(id).map(nextPaymentMapper::toDto);
    }

    /**
     * Delete the nextPayment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextPayment : {}", id);
        nextPaymentRepository.deleteById(id);
    }
}
