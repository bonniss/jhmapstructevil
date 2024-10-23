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
import xyz.jhmapstruct.domain.OrderGamma;
import xyz.jhmapstruct.repository.OrderGammaRepository;
import xyz.jhmapstruct.service.OrderGammaQueryService;
import xyz.jhmapstruct.service.OrderGammaService;
import xyz.jhmapstruct.service.criteria.OrderGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderGamma}.
 */
@RestController
@RequestMapping("/api/order-gammas")
public class OrderGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderGammaResource.class);

    private static final String ENTITY_NAME = "orderGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderGammaService orderGammaService;

    private final OrderGammaRepository orderGammaRepository;

    private final OrderGammaQueryService orderGammaQueryService;

    public OrderGammaResource(
        OrderGammaService orderGammaService,
        OrderGammaRepository orderGammaRepository,
        OrderGammaQueryService orderGammaQueryService
    ) {
        this.orderGammaService = orderGammaService;
        this.orderGammaRepository = orderGammaRepository;
        this.orderGammaQueryService = orderGammaQueryService;
    }

    /**
     * {@code POST  /order-gammas} : Create a new orderGamma.
     *
     * @param orderGamma the orderGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderGamma, or with status {@code 400 (Bad Request)} if the orderGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderGamma> createOrderGamma(@Valid @RequestBody OrderGamma orderGamma) throws URISyntaxException {
        LOG.debug("REST request to save OrderGamma : {}", orderGamma);
        if (orderGamma.getId() != null) {
            throw new BadRequestAlertException("A new orderGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderGamma = orderGammaService.save(orderGamma);
        return ResponseEntity.created(new URI("/api/order-gammas/" + orderGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderGamma.getId().toString()))
            .body(orderGamma);
    }

    /**
     * {@code PUT  /order-gammas/:id} : Updates an existing orderGamma.
     *
     * @param id the id of the orderGamma to save.
     * @param orderGamma the orderGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderGamma,
     * or with status {@code 400 (Bad Request)} if the orderGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderGamma> updateOrderGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderGamma orderGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderGamma : {}, {}", id, orderGamma);
        if (orderGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderGamma = orderGammaService.update(orderGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderGamma.getId().toString()))
            .body(orderGamma);
    }

    /**
     * {@code PATCH  /order-gammas/:id} : Partial updates given fields of an existing orderGamma, field will ignore if it is null
     *
     * @param id the id of the orderGamma to save.
     * @param orderGamma the orderGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderGamma,
     * or with status {@code 400 (Bad Request)} if the orderGamma is not valid,
     * or with status {@code 404 (Not Found)} if the orderGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderGamma> partialUpdateOrderGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderGamma orderGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderGamma partially : {}, {}", id, orderGamma);
        if (orderGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderGamma> result = orderGammaService.partialUpdate(orderGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /order-gammas} : get all the orderGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderGamma>> getAllOrderGammas(
        OrderGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderGammas by criteria: {}", criteria);

        Page<OrderGamma> page = orderGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-gammas/count} : count all the orderGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderGammas(OrderGammaCriteria criteria) {
        LOG.debug("REST request to count OrderGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-gammas/:id} : get the "id" orderGamma.
     *
     * @param id the id of the orderGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderGamma> getOrderGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderGamma : {}", id);
        Optional<OrderGamma> orderGamma = orderGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderGamma);
    }

    /**
     * {@code DELETE  /order-gammas/:id} : delete the "id" orderGamma.
     *
     * @param id the id of the orderGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderGamma : {}", id);
        orderGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
