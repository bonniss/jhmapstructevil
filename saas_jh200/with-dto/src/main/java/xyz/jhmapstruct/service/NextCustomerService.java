package xyz.jhmapstruct.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.NextCustomer;
import xyz.jhmapstruct.repository.NextCustomerRepository;
import xyz.jhmapstruct.service.dto.NextCustomerDTO;
import xyz.jhmapstruct.service.mapper.NextCustomerMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.NextCustomer}.
 */
@Service
@Transactional
public class NextCustomerService {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerService.class);

    private final NextCustomerRepository nextCustomerRepository;

    private final NextCustomerMapper nextCustomerMapper;

    public NextCustomerService(NextCustomerRepository nextCustomerRepository, NextCustomerMapper nextCustomerMapper) {
        this.nextCustomerRepository = nextCustomerRepository;
        this.nextCustomerMapper = nextCustomerMapper;
    }

    /**
     * Save a nextCustomer.
     *
     * @param nextCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerDTO save(NextCustomerDTO nextCustomerDTO) {
        LOG.debug("Request to save NextCustomer : {}", nextCustomerDTO);
        NextCustomer nextCustomer = nextCustomerMapper.toEntity(nextCustomerDTO);
        nextCustomer = nextCustomerRepository.save(nextCustomer);
        return nextCustomerMapper.toDto(nextCustomer);
    }

    /**
     * Update a nextCustomer.
     *
     * @param nextCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    public NextCustomerDTO update(NextCustomerDTO nextCustomerDTO) {
        LOG.debug("Request to update NextCustomer : {}", nextCustomerDTO);
        NextCustomer nextCustomer = nextCustomerMapper.toEntity(nextCustomerDTO);
        nextCustomer = nextCustomerRepository.save(nextCustomer);
        return nextCustomerMapper.toDto(nextCustomer);
    }

    /**
     * Partially update a nextCustomer.
     *
     * @param nextCustomerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextCustomerDTO> partialUpdate(NextCustomerDTO nextCustomerDTO) {
        LOG.debug("Request to partially update NextCustomer : {}", nextCustomerDTO);

        return nextCustomerRepository
            .findById(nextCustomerDTO.getId())
            .map(existingNextCustomer -> {
                nextCustomerMapper.partialUpdate(existingNextCustomer, nextCustomerDTO);

                return existingNextCustomer;
            })
            .map(nextCustomerRepository::save)
            .map(nextCustomerMapper::toDto);
    }

    /**
     * Get one nextCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextCustomerDTO> findOne(Long id) {
        LOG.debug("Request to get NextCustomer : {}", id);
        return nextCustomerRepository.findById(id).map(nextCustomerMapper::toDto);
    }

    /**
     * Delete the nextCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete NextCustomer : {}", id);
        nextCustomerRepository.deleteById(id);
    }
}
