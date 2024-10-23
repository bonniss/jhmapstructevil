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
import xyz.jhmapstruct.repository.OrderViViRepository;
import xyz.jhmapstruct.service.OrderViViQueryService;
import xyz.jhmapstruct.service.OrderViViService;
import xyz.jhmapstruct.service.criteria.OrderViViCriteria;
import xyz.jhmapstruct.service.dto.OrderViViDTO;
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

    private final OrderViViQueryService orderViViQueryService;

    public OrderViViResource(
        OrderViViService orderViViService,
        OrderViViRepository orderViViRepository,
        OrderViViQueryService orderViViQueryService
    ) {
        this.orderViViService = orderViViService;
        this.orderViViRepository = orderViViRepository;
        this.orderViViQueryService = orderViViQueryService;
    }

    /**
     * {@code POST  /order-vi-vis} : Create a new orderViVi.
     *
     * @param orderViViDTO the orderViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderViViDTO, or with status {@code 400 (Bad Request)} if the orderViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderViViDTO> createOrderViVi(@Valid @RequestBody OrderViViDTO orderViViDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderViVi : {}", orderViViDTO);
        if (orderViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderViViDTO = orderViViService.save(orderViViDTO);
        return ResponseEntity.created(new URI("/api/order-vi-vis/" + orderViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderViViDTO.getId().toString()))
            .body(orderViViDTO);
    }

    /**
     * {@code PUT  /order-vi-vis/:id} : Updates an existing orderViVi.
     *
     * @param id the id of the orderViViDTO to save.
     * @param orderViViDTO the orderViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViViDTO,
     * or with status {@code 400 (Bad Request)} if the orderViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderViViDTO> updateOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderViViDTO orderViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderViVi : {}, {}", id, orderViViDTO);
        if (orderViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderViViDTO = orderViViService.update(orderViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViViDTO.getId().toString()))
            .body(orderViViDTO);
    }

    /**
     * {@code PATCH  /order-vi-vis/:id} : Partial updates given fields of an existing orderViVi, field will ignore if it is null
     *
     * @param id the id of the orderViViDTO to save.
     * @param orderViViDTO the orderViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViViDTO,
     * or with status {@code 400 (Bad Request)} if the orderViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderViViDTO> partialUpdateOrderViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderViViDTO orderViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderViVi partially : {}, {}", id, orderViViDTO);
        if (orderViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderViViDTO> result = orderViViService.partialUpdate(orderViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-vi-vis} : get all the orderViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderViViDTO>> getAllOrderViVis(
        OrderViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderViVis by criteria: {}", criteria);

        Page<OrderViViDTO> page = orderViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-vi-vis/count} : count all the orderViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderViVis(OrderViViCriteria criteria) {
        LOG.debug("REST request to count OrderViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-vi-vis/:id} : get the "id" orderViVi.
     *
     * @param id the id of the orderViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderViViDTO> getOrderViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderViVi : {}", id);
        Optional<OrderViViDTO> orderViViDTO = orderViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderViViDTO);
    }

    /**
     * {@code DELETE  /order-vi-vis/:id} : delete the "id" orderViVi.
     *
     * @param id the id of the orderViViDTO to delete.
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
