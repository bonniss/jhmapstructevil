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
import xyz.jhmapstruct.domain.OrderAlpha;
import xyz.jhmapstruct.repository.OrderAlphaRepository;
import xyz.jhmapstruct.service.OrderAlphaQueryService;
import xyz.jhmapstruct.service.OrderAlphaService;
import xyz.jhmapstruct.service.criteria.OrderAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderAlpha}.
 */
@RestController
@RequestMapping("/api/order-alphas")
public class OrderAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderAlphaResource.class);

    private static final String ENTITY_NAME = "orderAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderAlphaService orderAlphaService;

    private final OrderAlphaRepository orderAlphaRepository;

    private final OrderAlphaQueryService orderAlphaQueryService;

    public OrderAlphaResource(
        OrderAlphaService orderAlphaService,
        OrderAlphaRepository orderAlphaRepository,
        OrderAlphaQueryService orderAlphaQueryService
    ) {
        this.orderAlphaService = orderAlphaService;
        this.orderAlphaRepository = orderAlphaRepository;
        this.orderAlphaQueryService = orderAlphaQueryService;
    }

    /**
     * {@code POST  /order-alphas} : Create a new orderAlpha.
     *
     * @param orderAlpha the orderAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderAlpha, or with status {@code 400 (Bad Request)} if the orderAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderAlpha> createOrderAlpha(@Valid @RequestBody OrderAlpha orderAlpha) throws URISyntaxException {
        LOG.debug("REST request to save OrderAlpha : {}", orderAlpha);
        if (orderAlpha.getId() != null) {
            throw new BadRequestAlertException("A new orderAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderAlpha = orderAlphaService.save(orderAlpha);
        return ResponseEntity.created(new URI("/api/order-alphas/" + orderAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderAlpha.getId().toString()))
            .body(orderAlpha);
    }

    /**
     * {@code PUT  /order-alphas/:id} : Updates an existing orderAlpha.
     *
     * @param id the id of the orderAlpha to save.
     * @param orderAlpha the orderAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAlpha,
     * or with status {@code 400 (Bad Request)} if the orderAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderAlpha> updateOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderAlpha orderAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderAlpha : {}, {}", id, orderAlpha);
        if (orderAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderAlpha = orderAlphaService.update(orderAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAlpha.getId().toString()))
            .body(orderAlpha);
    }

    /**
     * {@code PATCH  /order-alphas/:id} : Partial updates given fields of an existing orderAlpha, field will ignore if it is null
     *
     * @param id the id of the orderAlpha to save.
     * @param orderAlpha the orderAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAlpha,
     * or with status {@code 400 (Bad Request)} if the orderAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the orderAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderAlpha> partialUpdateOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderAlpha orderAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderAlpha partially : {}, {}", id, orderAlpha);
        if (orderAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderAlpha> result = orderAlphaService.partialUpdate(orderAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /order-alphas} : get all the orderAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderAlpha>> getAllOrderAlphas(
        OrderAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderAlphas by criteria: {}", criteria);

        Page<OrderAlpha> page = orderAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-alphas/count} : count all the orderAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderAlphas(OrderAlphaCriteria criteria) {
        LOG.debug("REST request to count OrderAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-alphas/:id} : get the "id" orderAlpha.
     *
     * @param id the id of the orderAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAlpha> getOrderAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderAlpha : {}", id);
        Optional<OrderAlpha> orderAlpha = orderAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderAlpha);
    }

    /**
     * {@code DELETE  /order-alphas/:id} : delete the "id" orderAlpha.
     *
     * @param id the id of the orderAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderAlpha : {}", id);
        orderAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
