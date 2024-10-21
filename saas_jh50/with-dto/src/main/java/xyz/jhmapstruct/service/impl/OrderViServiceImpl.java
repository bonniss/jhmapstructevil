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
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.OrderViService;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.service.mapper.OrderViMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
@Service
@Transactional
public class OrderViServiceImpl implements OrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViServiceImpl.class);

    private final OrderViRepository orderViRepository;

    private final OrderViMapper orderViMapper;

    public OrderViServiceImpl(OrderViRepository orderViRepository, OrderViMapper orderViMapper) {
        this.orderViRepository = orderViRepository;
        this.orderViMapper = orderViMapper;
    }

    @Override
    public OrderViDTO save(OrderViDTO orderViDTO) {
        LOG.debug("Request to save OrderVi : {}", orderViDTO);
        OrderVi orderVi = orderViMapper.toEntity(orderViDTO);
        orderVi = orderViRepository.save(orderVi);
        return orderViMapper.toDto(orderVi);
    }

    @Override
    public OrderViDTO update(OrderViDTO orderViDTO) {
        LOG.debug("Request to update OrderVi : {}", orderViDTO);
        OrderVi orderVi = orderViMapper.toEntity(orderViDTO);
        orderVi = orderViRepository.save(orderVi);
        return orderViMapper.toDto(orderVi);
    }

    @Override
    public Optional<OrderViDTO> partialUpdate(OrderViDTO orderViDTO) {
        LOG.debug("Request to partially update OrderVi : {}", orderViDTO);

        return orderViRepository
            .findById(orderViDTO.getId())
            .map(existingOrderVi -> {
                orderViMapper.partialUpdate(existingOrderVi, orderViDTO);

                return existingOrderVi;
            })
            .map(orderViRepository::save)
            .map(orderViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderViDTO> findAll() {
        LOG.debug("Request to get all OrderVis");
        return orderViRepository.findAll().stream().map(orderViMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<OrderViDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderViRepository.findAllWithEagerRelationships(pageable).map(orderViMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderViDTO> findOne(Long id) {
        LOG.debug("Request to get OrderVi : {}", id);
        return orderViRepository.findOneWithEagerRelationships(id).map(orderViMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderVi : {}", id);
        orderViRepository.deleteById(id);
    }
}
