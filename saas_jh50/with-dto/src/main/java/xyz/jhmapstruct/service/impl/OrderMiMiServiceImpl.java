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
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.OrderMiMiService;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.service.mapper.OrderMiMiMapper;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
@Service
@Transactional
public class OrderMiMiServiceImpl implements OrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiServiceImpl.class);

    private final OrderMiMiRepository orderMiMiRepository;

    private final OrderMiMiMapper orderMiMiMapper;

    public OrderMiMiServiceImpl(OrderMiMiRepository orderMiMiRepository, OrderMiMiMapper orderMiMiMapper) {
        this.orderMiMiRepository = orderMiMiRepository;
        this.orderMiMiMapper = orderMiMiMapper;
    }

    @Override
    public OrderMiMiDTO save(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to save OrderMiMi : {}", orderMiMiDTO);
        OrderMiMi orderMiMi = orderMiMiMapper.toEntity(orderMiMiDTO);
        orderMiMi = orderMiMiRepository.save(orderMiMi);
        return orderMiMiMapper.toDto(orderMiMi);
    }

    @Override
    public OrderMiMiDTO update(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to update OrderMiMi : {}", orderMiMiDTO);
        OrderMiMi orderMiMi = orderMiMiMapper.toEntity(orderMiMiDTO);
        orderMiMi = orderMiMiRepository.save(orderMiMi);
        return orderMiMiMapper.toDto(orderMiMi);
    }

    @Override
    public Optional<OrderMiMiDTO> partialUpdate(OrderMiMiDTO orderMiMiDTO) {
        LOG.debug("Request to partially update OrderMiMi : {}", orderMiMiDTO);

        return orderMiMiRepository
            .findById(orderMiMiDTO.getId())
            .map(existingOrderMiMi -> {
                orderMiMiMapper.partialUpdate(existingOrderMiMi, orderMiMiDTO);

                return existingOrderMiMi;
            })
            .map(orderMiMiRepository::save)
            .map(orderMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderMiMiDTO> findAll() {
        LOG.debug("Request to get all OrderMiMis");
        return orderMiMiRepository.findAll().stream().map(orderMiMiMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<OrderMiMiDTO> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiMiRepository.findAllWithEagerRelationships(pageable).map(orderMiMiMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderMiMiDTO> findOne(Long id) {
        LOG.debug("Request to get OrderMiMi : {}", id);
        return orderMiMiRepository.findOneWithEagerRelationships(id).map(orderMiMiMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMiMi : {}", id);
        orderMiMiRepository.deleteById(id);
    }
}
