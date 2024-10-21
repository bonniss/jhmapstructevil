package xyz.jhmapstruct.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.OrderViVi;
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderViVi}.
 */
@RestController
@RequestMapping("/api/order-vi-vis")
public class OrderViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViViResource.class);

    private static final String ENTITY_NAME = "orderViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderViViService orderViViService;

    private final OrderViViRepository orderViViRepository;

    public OrderViViResource(OrderViViService orderViViService, OrderViViRepository orderViViRepository) {
        this.orderViViService = orderViViService;
        this.orderViViRepository = orderViViRepository;
    }

    /**
     * {@code POST  /order-vi-vis} : Create a new orderViVi.
     *
     * @param orderViVi the orderViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderViVi, or with status {@code 400 (Bad Request)} if the orderViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderViVi> createOrderViVi(@Valid @RequestBody OrderViVi orderViVi) throws URISyntaxException {
        LOG.debug("REST request to save OrderViVi : {}", orderViVi);
        if (orderViVi.getId() != null) {
            throw new BadRequestAlertException("A new orderViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderViVi = orderViViService.save(orderViVi);
        return ResponseEntity.created(new URI("/api/order-vi-vis/" + orderViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderViVi.getId().toString()))
            .body(orderViVi);
    }

    /**
     * {@code PUT  /order-vi-vis/:id} : Updates an existing orderViVi.
     *
     * @param id the id of the orderViVi to save.
     * @param orderViVi the orderViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViVi,
     * or with status {@code 400 (Bad Request)} if the orderViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderViVi> updateOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderViVi orderViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderViVi : {}, {}", id, orderViVi);
        if (orderViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderViVi = orderViViService.update(orderViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViVi.getId().toString()))
            .body(orderViVi);
    }

    /**
     * {@code PATCH  /order-vi-vis/:id} : Partial updates given fields of an existing orderViVi, field will ignore if it is null
     *
     * @param id the id of the orderViVi to save.
     * @param orderViVi the orderViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViVi,
     * or with status {@code 400 (Bad Request)} if the orderViVi is not valid,
     * or with status {@code 404 (Not Found)} if the orderViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderViVi> partialUpdateOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderViVi orderViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderViVi partially : {}, {}", id, orderViVi);
        if (orderViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderViVi> result = orderViViService.partialUpdate(orderViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /order-vi-vis} : get all the orderViVis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderViVis in body.
     */
    @GetMapping("")
    public List<OrderViVi> getAllOrderViVis(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all OrderViVis");
        return orderViViService.findAll();
    }

    /**
     * {@code GET  /order-vi-vis/:id} : get the "id" orderViVi.
     *
     * @param id the id of the orderViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderViVi> getOrderViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderViVi : {}", id);
        Optional<OrderViVi> orderViVi = orderViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderViVi);
    }

    /**
     * {@code DELETE  /order-vi-vis/:id} : delete the "id" orderViVi.
     *
     * @param id the id of the orderViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderViVi : {}", id);
        orderViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
