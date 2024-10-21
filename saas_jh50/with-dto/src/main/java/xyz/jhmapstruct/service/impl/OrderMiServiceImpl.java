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
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.OrderMiService;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
import xyz.jhmapstruct.service.mapper.OrderMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
@Service
@Transactional
public class OrderMiServiceImpl implements OrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiServiceImpl.class);

    private final OrderMiRepository orderMiRepository;

    private final OrderMiMapper orderMiMapper;

    public OrderMiServiceImpl(OrderMiRepository orderMiRepository, OrderMiMapper orderMiMapper) {
        this.orderMiRepository = orderMiRepository;
        this.orderMiMapper = orderMiMapper;
    }

    @Override
    public OrderMiDTO save(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to save OrderMi : {}", orderMiDTO);
        OrderMi orderMi = orderMiMapper.toEntity(orderMiDTO);
        orderMi = orderMiRepository.save(orderMi);
        return orderMiMapper.toDto(orderMi);
    }

    @Override
    public OrderMiDTO update(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to update OrderMi : {}", orderMiDTO);
        OrderMi orderMi = orderMiMapper.toEntity(orderMiDTO);
        orderMi = orderMiRepository.save(orderMi);
        return orderMiMapper.toDto(orderMi);
    }

    @Override
    public Optional<OrderMiDTO> partialUpdate(OrderMiDTO orderMiDTO) {
        LOG.debug("Request to partially update OrderMi : {}", orderMiDTO);

        return orderMiRepository
            .findById(orderMiDTO.getId())
            .map(existingOrderMi -> {
                orderMiMapper.partialUpdate(existingOrderMi, orderMiDTO);

                return existingOrderMi;
            })
            .map(orderMiRepository::save)
            .map(orderMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderMiDTO> findAll() {
        LOG.debug("Request to get all OrderMis");
        return orderMiRepository.findAll().stream().map(orderMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<OrderMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiRepository.findAllWithEagerRelationships(pageable).map(orderMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderMiDTO> findOne(Long id) {
        LOG.debug("Request to get OrderMi : {}", id);
        return orderMiRepository.findOneWithEagerRelationships(id).map(orderMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMi : {}", id);
        orderMiRepository.deleteById(id);
    }
}
