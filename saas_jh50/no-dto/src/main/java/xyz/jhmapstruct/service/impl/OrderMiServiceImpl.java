package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.OrderMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
@Service
@Transactional
public class OrderMiServiceImpl implements OrderMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiServiceImpl.class);

    private final OrderMiRepository orderMiRepository;

    public OrderMiServiceImpl(OrderMiRepository orderMiRepository) {
        this.orderMiRepository = orderMiRepository;
    }

    @Override
    public OrderMi save(OrderMi orderMi) {
        LOG.debug("Request to save OrderMi : {}", orderMi);
        return orderMiRepository.save(orderMi);
    }

    @Override
    public OrderMi update(OrderMi orderMi) {
        LOG.debug("Request to update OrderMi : {}", orderMi);
        return orderMiRepository.save(orderMi);
    }

    @Override
    public Optional<OrderMi> partialUpdate(OrderMi orderMi) {
        LOG.debug("Request to partially update OrderMi : {}", orderMi);

        return orderMiRepository
            .findById(orderMi.getId())
            .map(existingOrderMi -> {
                if (orderMi.getOrderDate() != null) {
                    existingOrderMi.setOrderDate(orderMi.getOrderDate());
                }
                if (orderMi.getTotalPrice() != null) {
                    existingOrderMi.setTotalPrice(orderMi.getTotalPrice());
                }
                if (orderMi.getStatus() != null) {
                    existingOrderMi.setStatus(orderMi.getStatus());
                }

                return existingOrderMi;
            })
            .map(orderMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderMi> findAll() {
        LOG.debug("Request to get all OrderMis");
        return orderMiRepository.findAll();
    }

    public Page<OrderMi> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderMi> findOne(Long id) {
        LOG.debug("Request to get OrderMi : {}", id);
        return orderMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMi : {}", id);
        orderMiRepository.deleteById(id);
    }
}
