package xyz.jhmapstruct.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViService;
import xyz.jhmapstruct.service.dto.OrderViViDTO;
import xyz.jhmapstruct.service.mapper.OrderViViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
@Service
@Transactional
public class OrderViViServiceImpl implements OrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViServiceImpl.class);

    private final OrderViViRepository orderViViRepository;

    private final OrderViViMapper orderViViMapper;

    public OrderViViServiceImpl(OrderViViRepository orderViViRepository, OrderViViMapper orderViViMapper) {
        this.orderViViRepository = orderViViRepository;
        this.orderViViMapper = orderViViMapper;
    }

    @Override
    public OrderViViDTO save(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to save OrderViVi : {}", orderViViDTO);
        OrderViVi orderViVi = orderViViMapper.toEntity(orderViViDTO);
        orderViVi = orderViViRepository.save(orderViVi);
        return orderViViMapper.toDto(orderViVi);
    }

    @Override
    public OrderViViDTO update(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to update OrderViVi : {}", orderViViDTO);
        OrderViVi orderViVi = orderViViMapper.toEntity(orderViViDTO);
        orderViVi = orderViViRepository.save(orderViVi);
        return orderViViMapper.toDto(orderViVi);
    }

    @Override
    public Optional<OrderViViDTO> partialUpdate(OrderViViDTO orderViViDTO) {
        LOG.debug("Request to partially update OrderViVi : {}", orderViViDTO);

        return orderViViRepository
            .findById(orderViViDTO.getId())
            .map(existingOrderViVi -> {
                orderViViMapper.partialUpdate(existingOrderViVi, orderViViDTO);

                return existingOrderViVi;
            })
            .map(orderViViRepository::save)
            .map(orderViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderViViDTO> findAll() {
        LOG.debug("Request to get all OrderViVis");
        return orderViViRepository.findAll().stream().map(orderViViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<OrderViViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderViViRepository.findAllWithEagerRelationships(pageable).map(orderViViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderViViDTO> findOne(Long id) {
        LOG.debug("Request to get OrderViVi : {}", id);
        return orderViViRepository.findOneWithEagerRelationships(id).map(orderViViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderViVi : {}", id);
        orderViViRepository.deleteById(id);
    }
}
