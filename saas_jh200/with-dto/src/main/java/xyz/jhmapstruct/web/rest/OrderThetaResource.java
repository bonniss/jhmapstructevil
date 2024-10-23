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
import xyz.jhmapstruct.repository.OrderThetaRepository;
import xyz.jhmapstruct.service.OrderThetaQueryService;
import xyz.jhmapstruct.service.OrderThetaService;
import xyz.jhmapstruct.service.criteria.OrderThetaCriteria;
import xyz.jhmapstruct.service.dto.OrderThetaDTO;
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
     * @param orderThetaDTO the orderThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderThetaDTO, or with status {@code 400 (Bad Request)} if the orderTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderThetaDTO> createOrderTheta(@Valid @RequestBody OrderThetaDTO orderThetaDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderTheta : {}", orderThetaDTO);
        if (orderThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderThetaDTO = orderThetaService.save(orderThetaDTO);
        return ResponseEntity.created(new URI("/api/order-thetas/" + orderThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderThetaDTO.getId().toString()))
            .body(orderThetaDTO);
    }

    /**
     * {@code PUT  /order-thetas/:id} : Updates an existing orderTheta.
     *
     * @param id the id of the orderThetaDTO to save.
     * @param orderThetaDTO the orderThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderThetaDTO,
     * or with status {@code 400 (Bad Request)} if the orderThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderThetaDTO> updateOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderThetaDTO orderThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderTheta : {}, {}", id, orderThetaDTO);
        if (orderThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderThetaDTO = orderThetaService.update(orderThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderThetaDTO.getId().toString()))
            .body(orderThetaDTO);
    }

    /**
     * {@code PATCH  /order-thetas/:id} : Partial updates given fields of an existing orderTheta, field will ignore if it is null
     *
     * @param id the id of the orderThetaDTO to save.
     * @param orderThetaDTO the orderThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderThetaDTO,
     * or with status {@code 400 (Bad Request)} if the orderThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderThetaDTO> partialUpdateOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderThetaDTO orderThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderTheta partially : {}, {}", id, orderThetaDTO);
        if (orderThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderThetaDTO> result = orderThetaService.partialUpdate(orderThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderThetaDTO.getId().toString())
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
    public ResponseEntity<List<OrderThetaDTO>> getAllOrderThetas(
        OrderThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderThetas by criteria: {}", criteria);

        Page<OrderThetaDTO> page = orderThetaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the orderThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderThetaDTO> getOrderTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderTheta : {}", id);
        Optional<OrderThetaDTO> orderThetaDTO = orderThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderThetaDTO);
    }

    /**
     * {@code DELETE  /order-thetas/:id} : delete the "id" orderTheta.
     *
     * @param id the id of the orderThetaDTO to delete.
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
