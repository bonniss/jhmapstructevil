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
import xyz.jhmapstruct.repository.OrderSigmaRepository;
import xyz.jhmapstruct.service.OrderSigmaQueryService;
import xyz.jhmapstruct.service.OrderSigmaService;
import xyz.jhmapstruct.service.criteria.OrderSigmaCriteria;
import xyz.jhmapstruct.service.dto.OrderSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderSigma}.
 */
@RestController
@RequestMapping("/api/order-sigmas")
public class OrderSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderSigmaResource.class);

    private static final String ENTITY_NAME = "orderSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderSigmaService orderSigmaService;

    private final OrderSigmaRepository orderSigmaRepository;

    private final OrderSigmaQueryService orderSigmaQueryService;

    public OrderSigmaResource(
        OrderSigmaService orderSigmaService,
        OrderSigmaRepository orderSigmaRepository,
        OrderSigmaQueryService orderSigmaQueryService
    ) {
        this.orderSigmaService = orderSigmaService;
        this.orderSigmaRepository = orderSigmaRepository;
        this.orderSigmaQueryService = orderSigmaQueryService;
    }

    /**
     * {@code POST  /order-sigmas} : Create a new orderSigma.
     *
     * @param orderSigmaDTO the orderSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderSigmaDTO, or with status {@code 400 (Bad Request)} if the orderSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderSigmaDTO> createOrderSigma(@Valid @RequestBody OrderSigmaDTO orderSigmaDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderSigma : {}", orderSigmaDTO);
        if (orderSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderSigmaDTO = orderSigmaService.save(orderSigmaDTO);
        return ResponseEntity.created(new URI("/api/order-sigmas/" + orderSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderSigmaDTO.getId().toString()))
            .body(orderSigmaDTO);
    }

    /**
     * {@code PUT  /order-sigmas/:id} : Updates an existing orderSigma.
     *
     * @param id the id of the orderSigmaDTO to save.
     * @param orderSigmaDTO the orderSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the orderSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderSigmaDTO> updateOrderSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderSigmaDTO orderSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderSigma : {}, {}", id, orderSigmaDTO);
        if (orderSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderSigmaDTO = orderSigmaService.update(orderSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderSigmaDTO.getId().toString()))
            .body(orderSigmaDTO);
    }

    /**
     * {@code PATCH  /order-sigmas/:id} : Partial updates given fields of an existing orderSigma, field will ignore if it is null
     *
     * @param id the id of the orderSigmaDTO to save.
     * @param orderSigmaDTO the orderSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the orderSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderSigmaDTO> partialUpdateOrderSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderSigmaDTO orderSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderSigma partially : {}, {}", id, orderSigmaDTO);
        if (orderSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderSigmaDTO> result = orderSigmaService.partialUpdate(orderSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-sigmas} : get all the orderSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderSigmaDTO>> getAllOrderSigmas(
        OrderSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderSigmas by criteria: {}", criteria);

        Page<OrderSigmaDTO> page = orderSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-sigmas/count} : count all the orderSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderSigmas(OrderSigmaCriteria criteria) {
        LOG.debug("REST request to count OrderSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-sigmas/:id} : get the "id" orderSigma.
     *
     * @param id the id of the orderSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderSigmaDTO> getOrderSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderSigma : {}", id);
        Optional<OrderSigmaDTO> orderSigmaDTO = orderSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderSigmaDTO);
    }

    /**
     * {@code DELETE  /order-sigmas/:id} : delete the "id" orderSigma.
     *
     * @param id the id of the orderSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderSigma : {}", id);
        orderSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
