package xyz.jhmapstruct.service.impl;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViService;

/**
 * Service Implementation for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
@Service
@Transactional
public class OrderViViServiceImpl implements OrderViViService {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViServiceImpl.class);

    private final OrderViViRepository orderViViRepository;

    public OrderViViServiceImpl(OrderViViRepository orderViViRepository) {
        this.orderViViRepository = orderViViRepository;
    }

    @Override
    public OrderViVi save(OrderViVi orderViVi) {
        LOG.debug("Request to save OrderViVi : {}", orderViVi);
        return orderViViRepository.save(orderViVi);
    }

    @Override
    public OrderViVi update(OrderViVi orderViVi) {
        LOG.debug("Request to update OrderViVi : {}", orderViVi);
        return orderViViRepository.save(orderViVi);
    }

    @Override
    public Optional<OrderViVi> partialUpdate(OrderViVi orderViVi) {
        LOG.debug("Request to partially update OrderViVi : {}", orderViVi);

        return orderViViRepository
            .findById(orderViVi.getId())
            .map(existingOrderViVi -> {
                if (orderViVi.getOrderDate() != null) {
                    existingOrderViVi.setOrderDate(orderViVi.getOrderDate());
                }
                if (orderViVi.getTotalPrice() != null) {
                    existingOrderViVi.setTotalPrice(orderViVi.getTotalPrice());
                }
                if (orderViVi.getStatus() != null) {
                    existingOrderViVi.setStatus(orderViVi.getStatus());
                }

                return existingOrderViVi;
            })
            .map(orderViViRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderViVi> findAll() {
        LOG.debug("Request to get all OrderViVis");
        return orderViViRepository.findAll();
    }

    public Page<OrderViVi> findAllWithEagerRelationships(Pageable pageable) {
        return orderViViRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrderViVi> findOne(Long id) {
        LOG.debug("Request to get OrderViVi : {}", id);
        return orderViViRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OrderViVi : {}", id);
        orderViViRepository.deleteById(id);
    }
}
