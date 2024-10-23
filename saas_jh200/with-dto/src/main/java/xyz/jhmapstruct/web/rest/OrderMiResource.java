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
import xyz.jhmapstruct.repository.OrderMiRepository;
import xyz.jhmapstruct.service.OrderMiQueryService;
import xyz.jhmapstruct.service.OrderMiService;
import xyz.jhmapstruct.service.criteria.OrderMiCriteria;
import xyz.jhmapstruct.service.dto.OrderMiDTO;
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

    private final OrderMiQueryService orderMiQueryService;

    public OrderMiResource(OrderMiService orderMiService, OrderMiRepository orderMiRepository, OrderMiQueryService orderMiQueryService) {
        this.orderMiService = orderMiService;
        this.orderMiRepository = orderMiRepository;
        this.orderMiQueryService = orderMiQueryService;
    }

    /**
     * {@code POST  /order-mis} : Create a new orderMi.
     *
     * @param orderMiDTO the orderMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderMiDTO, or with status {@code 400 (Bad Request)} if the orderMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderMiDTO> createOrderMi(@Valid @RequestBody OrderMiDTO orderMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderMi : {}", orderMiDTO);
        if (orderMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderMiDTO = orderMiService.save(orderMiDTO);
        return ResponseEntity.created(new URI("/api/order-mis/" + orderMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderMiDTO.getId().toString()))
            .body(orderMiDTO);
    }

    /**
     * {@code PUT  /order-mis/:id} : Updates an existing orderMi.
     *
     * @param id the id of the orderMiDTO to save.
     * @param orderMiDTO the orderMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMiDTO,
     * or with status {@code 400 (Bad Request)} if the orderMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderMiDTO> updateOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderMiDTO orderMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderMi : {}, {}", id, orderMiDTO);
        if (orderMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderMiDTO = orderMiService.update(orderMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMiDTO.getId().toString()))
            .body(orderMiDTO);
    }

    /**
     * {@code PATCH  /order-mis/:id} : Partial updates given fields of an existing orderMi, field will ignore if it is null
     *
     * @param id the id of the orderMiDTO to save.
     * @param orderMiDTO the orderMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMiDTO,
     * or with status {@code 400 (Bad Request)} if the orderMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderMiDTO> partialUpdateOrderMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderMiDTO orderMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderMi partially : {}, {}", id, orderMiDTO);
        if (orderMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderMiDTO> result = orderMiService.partialUpdate(orderMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-mis} : get all the orderMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderMiDTO>> getAllOrderMis(
        OrderMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderMis by criteria: {}", criteria);

        Page<OrderMiDTO> page = orderMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-mis/count} : count all the orderMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderMis(OrderMiCriteria criteria) {
        LOG.debug("REST request to count OrderMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-mis/:id} : get the "id" orderMi.
     *
     * @param id the id of the orderMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderMiDTO> getOrderMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderMi : {}", id);
        Optional<OrderMiDTO> orderMiDTO = orderMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderMiDTO);
    }

    /**
     * {@code DELETE  /order-mis/:id} : delete the "id" orderMi.
     *
     * @param id the id of the orderMiDTO to delete.
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
