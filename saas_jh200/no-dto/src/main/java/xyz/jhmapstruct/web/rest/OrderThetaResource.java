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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.OrderTheta;
import xyz.jhmapstruct.repository.OrderThetaRepository;
import xyz.jhmapstruct.service.OrderThetaQueryService;
import xyz.jhmapstruct.service.OrderThetaService;
import xyz.jhmapstruct.service.criteria.OrderThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderTheta}.
 */
@RestController
@RequestMapping("/api/order-thetas")
public class OrderThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderThetaResource.class);

    private static final String ENTITY_NAME = "orderTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderThetaService orderThetaService;

    private final OrderThetaRepository orderThetaRepository;

    private final OrderThetaQueryService orderThetaQueryService;

    public OrderThetaResource(
        OrderThetaService orderThetaService,
        OrderThetaRepository orderThetaRepository,
        OrderThetaQueryService orderThetaQueryService
    ) {
        this.orderThetaService = orderThetaService;
        this.orderThetaRepository = orderThetaRepository;
        this.orderThetaQueryService = orderThetaQueryService;
    }

    /**
     * {@code POST  /order-thetas} : Create a new orderTheta.
     *
     * @param orderTheta the orderTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderTheta, or with status {@code 400 (Bad Request)} if the orderTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderTheta> createOrderTheta(@Valid @RequestBody OrderTheta orderTheta) throws URISyntaxException {
        LOG.debug("REST request to save OrderTheta : {}", orderTheta);
        if (orderTheta.getId() != null) {
            throw new BadRequestAlertException("A new orderTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderTheta = orderThetaService.save(orderTheta);
        return ResponseEntity.created(new URI("/api/order-thetas/" + orderTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderTheta.getId().toString()))
            .body(orderTheta);
    }

    /**
     * {@code PUT  /order-thetas/:id} : Updates an existing orderTheta.
     *
     * @param id the id of the orderTheta to save.
     * @param orderTheta the orderTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderTheta,
     * or with status {@code 400 (Bad Request)} if the orderTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderTheta> updateOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderTheta orderTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderTheta : {}, {}", id, orderTheta);
        if (orderTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderTheta = orderThetaService.update(orderTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderTheta.getId().toString()))
            .body(orderTheta);
    }

    /**
     * {@code PATCH  /order-thetas/:id} : Partial updates given fields of an existing orderTheta, field will ignore if it is null
     *
     * @param id the id of the orderTheta to save.
     * @param orderTheta the orderTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderTheta,
     * or with status {@code 400 (Bad Request)} if the orderTheta is not valid,
     * or with status {@code 404 (Not Found)} if the orderTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderTheta> partialUpdateOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderTheta orderTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderTheta partially : {}, {}", id, orderTheta);
        if (orderTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderTheta> result = orderThetaService.partialUpdate(orderTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /order-thetas} : get all the orderThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderTheta>> getAllOrderThetas(
        OrderThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderThetas by criteria: {}", criteria);

        Page<OrderTheta> page = orderThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-thetas/count} : count all the orderThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderThetas(OrderThetaCriteria criteria) {
        LOG.debug("REST request to count OrderThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-thetas/:id} : get the "id" orderTheta.
     *
     * @param id the id of the orderTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderTheta> getOrderTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderTheta : {}", id);
        Optional<OrderTheta> orderTheta = orderThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderTheta);
    }

    /**
     * {@code DELETE  /order-thetas/:id} : delete the "id" orderTheta.
     *
     * @param id the id of the orderTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderTheta : {}", id);
        orderThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
