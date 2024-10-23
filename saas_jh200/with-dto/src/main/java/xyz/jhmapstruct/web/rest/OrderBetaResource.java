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
import xyz.jhmapstruct.repository.OrderBetaRepository;
import xyz.jhmapstruct.service.OrderBetaQueryService;
import xyz.jhmapstruct.service.OrderBetaService;
import xyz.jhmapstruct.service.criteria.OrderBetaCriteria;
import xyz.jhmapstruct.service.dto.OrderBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderBeta}.
 */
@RestController
@RequestMapping("/api/order-betas")
public class OrderBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderBetaResource.class);

    private static final String ENTITY_NAME = "orderBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderBetaService orderBetaService;

    private final OrderBetaRepository orderBetaRepository;

    private final OrderBetaQueryService orderBetaQueryService;

    public OrderBetaResource(
        OrderBetaService orderBetaService,
        OrderBetaRepository orderBetaRepository,
        OrderBetaQueryService orderBetaQueryService
    ) {
        this.orderBetaService = orderBetaService;
        this.orderBetaRepository = orderBetaRepository;
        this.orderBetaQueryService = orderBetaQueryService;
    }

    /**
     * {@code POST  /order-betas} : Create a new orderBeta.
     *
     * @param orderBetaDTO the orderBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderBetaDTO, or with status {@code 400 (Bad Request)} if the orderBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderBetaDTO> createOrderBeta(@Valid @RequestBody OrderBetaDTO orderBetaDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderBeta : {}", orderBetaDTO);
        if (orderBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderBetaDTO = orderBetaService.save(orderBetaDTO);
        return ResponseEntity.created(new URI("/api/order-betas/" + orderBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderBetaDTO.getId().toString()))
            .body(orderBetaDTO);
    }

    /**
     * {@code PUT  /order-betas/:id} : Updates an existing orderBeta.
     *
     * @param id the id of the orderBetaDTO to save.
     * @param orderBetaDTO the orderBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderBetaDTO,
     * or with status {@code 400 (Bad Request)} if the orderBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderBetaDTO> updateOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderBetaDTO orderBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderBeta : {}, {}", id, orderBetaDTO);
        if (orderBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderBetaDTO = orderBetaService.update(orderBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderBetaDTO.getId().toString()))
            .body(orderBetaDTO);
    }

    /**
     * {@code PATCH  /order-betas/:id} : Partial updates given fields of an existing orderBeta, field will ignore if it is null
     *
     * @param id the id of the orderBetaDTO to save.
     * @param orderBetaDTO the orderBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderBetaDTO,
     * or with status {@code 400 (Bad Request)} if the orderBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderBetaDTO> partialUpdateOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderBetaDTO orderBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderBeta partially : {}, {}", id, orderBetaDTO);
        if (orderBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderBetaDTO> result = orderBetaService.partialUpdate(orderBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-betas} : get all the orderBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderBetaDTO>> getAllOrderBetas(
        OrderBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderBetas by criteria: {}", criteria);

        Page<OrderBetaDTO> page = orderBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-betas/count} : count all the orderBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderBetas(OrderBetaCriteria criteria) {
        LOG.debug("REST request to count OrderBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-betas/:id} : get the "id" orderBeta.
     *
     * @param id the id of the orderBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderBetaDTO> getOrderBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderBeta : {}", id);
        Optional<OrderBetaDTO> orderBetaDTO = orderBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderBetaDTO);
    }

    /**
     * {@code DELETE  /order-betas/:id} : delete the "id" orderBeta.
     *
     * @param id the id of the orderBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderBeta : {}", id);
        orderBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
