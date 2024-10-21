package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderMiMi;
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.OrderMiMiService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
@Service
@Transactional
public class OrderMiMiServiceImpl implements OrderMiMiService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiServiceImpl.class);

    private final OrderMiMiRepository orderMiMiRepository;

    public OrderMiMiServiceImpl(OrderMiMiRepository orderMiMiRepository) {
        this.orderMiMiRepository = orderMiMiRepository;
    }

    @Override
    public OrderMiMi save(OrderMiMi orderMiMi) {
        LOG.debug("Request to save OrderMiMi : {}", orderMiMi);
        return orderMiMiRepository.save(orderMiMi);
    }

    @Override
    public OrderMiMi update(OrderMiMi orderMiMi) {
        LOG.debug("Request to update OrderMiMi : {}", orderMiMi);
        return orderMiMiRepository.save(orderMiMi);
    }

    @Override
    public Optional<OrderMiMi> partialUpdate(OrderMiMi orderMiMi) {
        LOG.debug("Request to partially update OrderMiMi : {}", orderMiMi);

        return orderMiMiRepository
            .findById(orderMiMi.getId())
            .map(existingOrderMiMi -> {
                if (orderMiMi.getOrderDate() != null) {
                    existingOrderMiMi.setOrderDate(orderMiMi.getOrderDate());
                }
                if (orderMiMi.getTotalPrice() != null) {
                    existingOrderMiMi.setTotalPrice(orderMiMi.getTotalPrice());
                }
                if (orderMiMi.getStatus() != null) {
                    existingOrderMiMi.setStatus(orderMiMi.getStatus());
                }

                return existingOrderMiMi;
            })
            .map(orderMiMiRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderMiMi> findAll() {
        LOG.debug("Request to get all OrderMiMis");
        return orderMiMiRepository.findAll();
    }

    public Page<OrderMiMi> findAllWithEagerRelationships(Pageable pageable) {
        return orderMiMiRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderMiMi> findOne(Long id) {
        LOG.debug("Request to get OrderMiMi : {}", id);
        return orderMiMiRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderMiMi : {}", id);
        orderMiMiRepository.deleteById(id);
    }
}
