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
import xyz.jhmapstruct.repository.OrderMiMiRepository;
import xyz.jhmapstruct.service.OrderMiMiQueryService;
import xyz.jhmapstruct.service.OrderMiMiService;
import xyz.jhmapstruct.service.criteria.OrderMiMiCriteria;
import xyz.jhmapstruct.service.dto.OrderMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.OrderMiMi}.
 */
@RestController
@RequestMapping("/api/order-mi-mis")
public class OrderMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrderMiMiResource.class);

    private static final String ENTITY_NAME = "orderMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderMiMiService orderMiMiService;

    private final OrderMiMiRepository orderMiMiRepository;

    private final OrderMiMiQueryService orderMiMiQueryService;

    public OrderMiMiResource(
        OrderMiMiService orderMiMiService,
        OrderMiMiRepository orderMiMiRepository,
        OrderMiMiQueryService orderMiMiQueryService
    ) {
        this.orderMiMiService = orderMiMiService;
        this.orderMiMiRepository = orderMiMiRepository;
        this.orderMiMiQueryService = orderMiMiQueryService;
    }

    /**
     * {@code POST  /order-mi-mis} : Create a new orderMiMi.
     *
     * @param orderMiMiDTO the orderMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderMiMiDTO, or with status {@code 400 (Bad Request)} if the orderMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderMiMiDTO> createOrderMiMi(@Valid @RequestBody OrderMiMiDTO orderMiMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderMiMi : {}", orderMiMiDTO);
        if (orderMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderMiMiDTO = orderMiMiService.save(orderMiMiDTO);
        return ResponseEntity.created(new URI("/api/order-mi-mis/" + orderMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderMiMiDTO.getId().toString()))
            .body(orderMiMiDTO);
    }

    /**
     * {@code PUT  /order-mi-mis/:id} : Updates an existing orderMiMi.
     *
     * @param id the id of the orderMiMiDTO to save.
     * @param orderMiMiDTO the orderMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the orderMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderMiMiDTO> updateOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderMiMiDTO orderMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderMiMi : {}, {}", id, orderMiMiDTO);
        if (orderMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderMiMiDTO = orderMiMiService.update(orderMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMiMiDTO.getId().toString()))
            .body(orderMiMiDTO);
    }

    /**
     * {@code PATCH  /order-mi-mis/:id} : Partial updates given fields of an existing orderMiMi, field will ignore if it is null
     *
     * @param id the id of the orderMiMiDTO to save.
     * @param orderMiMiDTO the orderMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the orderMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderMiMiDTO> partialUpdateOrderMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderMiMiDTO orderMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderMiMi partially : {}, {}", id, orderMiMiDTO);
        if (orderMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderMiMiDTO> result = orderMiMiService.partialUpdate(orderMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /order-mi-mis} : get all the orderMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrderMiMiDTO>> getAllOrderMiMis(
        OrderMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderMiMis by criteria: {}", criteria);

        Page<OrderMiMiDTO> page = orderMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /order-mi-mis/count} : count all the orderMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrderMiMis(OrderMiMiCriteria criteria) {
        LOG.debug("REST request to count OrderMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(orderMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /order-mi-mis/:id} : get the "id" orderMiMi.
     *
     * @param id the id of the orderMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderMiMiDTO> getOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderMiMi : {}", id);
        Optional<OrderMiMiDTO> orderMiMiDTO = orderMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderMiMiDTO);
    }

    /**
     * {@code DELETE  /order-mi-mis/:id} : delete the "id" orderMiMi.
     *
     * @param id the id of the orderMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrderMiMi : {}", id);
        orderMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
