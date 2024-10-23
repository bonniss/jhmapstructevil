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
import xyz.jhmapstruct.domain.NextOrder;
import xyz.jhmapstruct.repository.NextOrderRepository;
import xyz.jhmapstruct.service.NextOrderQueryService;
import xyz.jhmapstruct.service.NextOrderService;
import xyz.jhmapstruct.service.criteria.NextOrderCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrder}.
 */
@RestController
@RequestMapping("/api/next-orders")
public class NextOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderResource.class);

    private static final String ENTITY_NAME = "nextOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderService nextOrderService;

    private final NextOrderRepository nextOrderRepository;

    private final NextOrderQueryService nextOrderQueryService;

    public NextOrderResource(
        NextOrderService nextOrderService,
        NextOrderRepository nextOrderRepository,
        NextOrderQueryService nextOrderQueryService
    ) {
        this.nextOrderService = nextOrderService;
        this.nextOrderRepository = nextOrderRepository;
        this.nextOrderQueryService = nextOrderQueryService;
    }

    /**
     * {@code POST  /next-orders} : Create a new nextOrder.
     *
     * @param nextOrder the nextOrder to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrder, or with status {@code 400 (Bad Request)} if the nextOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrder> createNextOrder(@Valid @RequestBody NextOrder nextOrder) throws URISyntaxException {
        LOG.debug("REST request to save NextOrder : {}", nextOrder);
        if (nextOrder.getId() != null) {
            throw new BadRequestAlertException("A new nextOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrder = nextOrderService.save(nextOrder);
        return ResponseEntity.created(new URI("/api/next-orders/" + nextOrder.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrder.getId().toString()))
            .body(nextOrder);
    }

    /**
     * {@code PUT  /next-orders/:id} : Updates an existing nextOrder.
     *
     * @param id the id of the nextOrder to save.
     * @param nextOrder the nextOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrder,
     * or with status {@code 400 (Bad Request)} if the nextOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrder> updateNextOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrder nextOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrder : {}, {}", id, nextOrder);
        if (nextOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrder = nextOrderService.update(nextOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrder.getId().toString()))
            .body(nextOrder);
    }

    /**
     * {@code PATCH  /next-orders/:id} : Partial updates given fields of an existing nextOrder, field will ignore if it is null
     *
     * @param id the id of the nextOrder to save.
     * @param nextOrder the nextOrder to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrder,
     * or with status {@code 400 (Bad Request)} if the nextOrder is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrder is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrder> partialUpdateNextOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrder nextOrder
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrder partially : {}, {}", id, nextOrder);
        if (nextOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrder.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrder> result = nextOrderService.partialUpdate(nextOrder);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrder.getId().toString())
        );
    }

    /**
     * {@code GET  /next-orders} : get all the nextOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrder>> getAllNextOrders(
        NextOrderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrders by criteria: {}", criteria);

        Page<NextOrder> page = nextOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-orders/count} : count all the nextOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrders(NextOrderCriteria criteria) {
        LOG.debug("REST request to count NextOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-orders/:id} : get the "id" nextOrder.
     *
     * @param id the id of the nextOrder to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrder, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrder> getNextOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrder : {}", id);
        Optional<NextOrder> nextOrder = nextOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrder);
    }

    /**
     * {@code DELETE  /next-orders/:id} : delete the "id" nextOrder.
     *
     * @param id the id of the nextOrder to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrder : {}", id);
        nextOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
