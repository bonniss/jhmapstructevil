package xyz.jhmapstruct.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.Order;
import xyz.jhmapstruct.repository.OrderRepository;
import xyz.jhmapstruct.service.OrderService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.Order}.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(Order order) {
        LOG.debug("Request to save Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        LOG.debug("Request to update Order : {}", order);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> partialUpdate(Order order) {
        LOG.debug("Request to partially update Order : {}", order);

        return orderRepository
            .findById(order.getId())
            .map(existingOrder -> {
                if (order.getOrderDate() != null) {
                    existingOrder.setOrderDate(order.getOrderDate());
                }
                if (order.getTotalPrice() != null) {
                    existingOrder.setTotalPrice(order.getTotalPrice());
                }
                if (order.getStatus() != null) {
                    existingOrder.setStatus(order.getStatus());
                }

                return existingOrder;
            })
            .map(orderRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        LOG.debug("Request to get all Orders");
        return orderRepository.findAll(pageable);
    }

    public Page<Order> findAllWithEagerRelationships(Pageable pageable) {
        return orderRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findOne(Long id) {
        LOG.debug("Request to get Order : {}", id);
        return orderRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }
}
