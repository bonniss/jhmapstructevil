package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderVi;
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.OrderViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
@Service
@Transactional
public class OrderViServiceImpl implements OrderViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViServiceImpl.class);

    private final OrderViRepository orderViRepository;

    public OrderViServiceImpl(OrderViRepository orderViRepository) {
        this.orderViRepository = orderViRepository;
    }

    @Override
    public OrderVi save(OrderVi orderVi) {
        LOG.debug("Request to save OrderVi : {}", orderVi);
        return orderViRepository.save(orderVi);
    }

    @Override
    public OrderVi update(OrderVi orderVi) {
        LOG.debug("Request to update OrderVi : {}", orderVi);
        return orderViRepository.save(orderVi);
    }

    @Override
    public Optional<OrderVi> partialUpdate(OrderVi orderVi) {
        LOG.debug("Request to partially update OrderVi : {}", orderVi);

        return orderViRepository
            .findById(orderVi.getId())
            .map(existingOrderVi -> {
                if (orderVi.getOrderDate() != null) {
                    existingOrderVi.setOrderDate(orderVi.getOrderDate());
                }
                if (orderVi.getTotalPrice() != null) {
                    existingOrderVi.setTotalPrice(orderVi.getTotalPrice());
                }
                if (orderVi.getStatus() != null) {
                    existingOrderVi.setStatus(orderVi.getStatus());
                }

                return existingOrderVi;
            })
            .map(orderViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderVi> findAll() {
        LOG.debug("Request to get all OrderVis");
        return orderViRepository.findAll();
    }

    public Page<OrderVi> findAllWithEagerRelationships(Pageable pageable) {
        return orderViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderVi> findOne(Long id) {
        LOG.debug("Request to get OrderVi : {}", id);
        return orderViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderVi : {}", id);
        orderViRepository.deleteById(id);
    }
}
