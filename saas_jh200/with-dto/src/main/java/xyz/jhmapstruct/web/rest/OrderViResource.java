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
import xyz.jhmapstruct.repository.OrderViRepository;
import xyz.jhmapstruct.service.OrderViQueryService;
import xyz.jhmapstruct.service.OrderViService;
import xyz.jhmapstruct.service.criteria.OrderViCriteria;
import xyz.jhmapstruct.service.dto.OrderViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderVi}.
 */
@RestController
@RequestMapping("/api/order-vis")
public class OrderViResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderViResource.class);

    private static final String ENTITY_NAME = "orderVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderViService orderViService;

    private final OrderViRepository orderViRepository;

    private final OrderViQueryService orderViQueryService;

    public OrderViResource(OrderViService orderViService, OrderViRepository orderViRepository, OrderViQueryService orderViQueryService) {
        this.orderViService = orderViService;
        this.orderViRepository = orderViRepository;
        this.orderViQueryService = orderViQueryService;
    }

    /**
     * {@code POST  /order-vis} : Create a new orderVi.
     *
     * @param orderViDTO the orderViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderViDTO, or with status {@code 400 (Bad Request)} if the orderVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderViDTO> createOrderVi(@Valid @RequestBody OrderViDTO orderViDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderVi : {}", orderViDTO);
        if (orderViDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderViDTO = orderViService.save(orderViDTO);
        return ResponseEntity.created(new URI("/api/order-vis/" + orderViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderViDTO.getId().toString()))
            .body(orderViDTO);
    }

    /**
     * {@code PUT  /order-vis/:id} : Updates an existing orderVi.
     *
     * @param id the id of the orderViDTO to save.
     * @param orderViDTO the orderViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViDTO,
     * or with status {@code 400 (Bad Request)} if the orderViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderViDTO> updateOrderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderViDTO orderViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderVi : {}, {}", id, orderViDTO);
        if (orderViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderViDTO = orderViService.update(orderViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViDTO.getId().toString()))
            .body(orderViDTO);
    }

    /**
     * {@code PATCH  /order-vis/:id} : Partial updates given fields of an existing orderVi, field will ignore if it is null
     *
     * @param id the id of the orderViDTO to save.
     * @param orderViDTO the orderViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderViDTO,
     * or with status {@code 400 (Bad Request)} if the orderViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderViDTO> partialUpdateOrderVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderViDTO orderViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderVi partially : {}, {}", id, orderViDTO);
        if (orderViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderViDTO> result = orderViService.partialUpdate(orderViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-vis} : get all the orderVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderViDTO>> getAllOrderVis(
        OrderViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderVis by criteria: {}", criteria);

        Page<OrderViDTO> page = orderViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-vis/count} : count all the orderVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderVis(OrderViCriteria criteria) {
        LOG.debug("REST request to count OrderVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-vis/:id} : get the "id" orderVi.
     *
     * @param id the id of the orderViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderViDTO> getOrderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderVi : {}", id);
        Optional<OrderViDTO> orderViDTO = orderViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderViDTO);
    }

    /**
     * {@code DELETE  /order-vis/:id} : delete the "id" orderVi.
     *
     * @param id the id of the orderViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderVi : {}", id);
        orderViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
