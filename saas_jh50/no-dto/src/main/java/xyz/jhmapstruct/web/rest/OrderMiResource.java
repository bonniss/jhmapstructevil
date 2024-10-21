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
import xyz.jhmapstruct.domain.OrderMi;
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.OrderMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderMi}.
 */
@RestController
@RequestMapping("/api/order-mis")
public class OrderMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiResource.class);

    private static final String ENTITY_NAME = "orderMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderMiService orderMiService;

    private final OrderMiRepository orderMiRepository;

    public OrderMiResource(OrderMiService orderMiService, OrderMiRepository orderMiRepository) {
        this.orderMiService = orderMiService;
        this.orderMiRepository = orderMiRepository;
    }

    /**
     * {@code POST  /order-mis} : Create a new orderMi.
     *
     * @param orderMi the orderMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderMi, or with status {@code 400 (Bad Request)} if the orderMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderMi> createOrderMi(@Valid @RequestBody OrderMi orderMi) throws URISyntaxException {
        LOG.debug("REST request to save OrderMi : {}", orderMi);
        if (orderMi.getId() != null) {
            throw new BadRequestAlertException("A new orderMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderMi = orderMiService.save(orderMi);
        return ResponseEntity.created(new URI("/api/order-mis/" + orderMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderMi.getId().toString()))
            .body(orderMi);
    }

    /**
     * {@code PUT  /order-mis/:id} : Updates an existing orderMi.
     *
     * @param id the id of the orderMi to save.
     * @param orderMi the orderMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMi,
     * or with status {@code 400 (Bad Request)} if the orderMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderMi> updateOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderMi orderMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderMi : {}, {}", id, orderMi);
        if (orderMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderMi = orderMiService.update(orderMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMi.getId().toString()))
            .body(orderMi);
    }

    /**
     * {@code PATCH  /order-mis/:id} : Partial updates given fields of an existing orderMi, field will ignore if it is null
     *
     * @param id the id of the orderMi to save.
     * @param orderMi the orderMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMi,
     * or with status {@code 400 (Bad Request)} if the orderMi is not valid,
     * or with status {@code 404 (Not Found)} if the orderMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderMi> partialUpdateOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderMi orderMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderMi partially : {}, {}", id, orderMi);
        if (orderMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderMi> result = orderMiService.partialUpdate(orderMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMi.getId().toString())
        );
    }

    /**
     * {@code GET  /order-mis} : get all the orderMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderMis in body.
     */
    @GetMapping("")
    public List<OrderMi> getAllOrderMis(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all OrderMis");
        return orderMiService.findAll();
    }

    /**
     * {@code GET  /order-mis/:id} : get the "id" orderMi.
     *
     * @param id the id of the orderMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderMi> getOrderMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderMi : {}", id);
        Optional<OrderMi> orderMi = orderMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderMi);
    }

    /**
     * {@code DELETE  /order-mis/:id} : delete the "id" orderMi.
     *
     * @param id the id of the orderMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderMi : {}", id);
        orderMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
