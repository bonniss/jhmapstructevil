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
import xyz.jhmapstruct.repository.OrderAlphaRepository;
import xyz.jhmapstruct.service.OrderAlphaQueryService;
import xyz.jhmapstruct.service.OrderAlphaService;
import xyz.jhmapstruct.service.criteria.OrderAlphaCriteria;
import xyz.jhmapstruct.service.dto.OrderAlphaDTO;
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
     * @param orderAlphaDTO the orderAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderAlphaDTO, or with status {@code 400 (Bad Request)} if the orderAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrderAlphaDTO> createOrderAlpha(@Valid @RequestBody OrderAlphaDTO orderAlphaDTO) throws URISyntaxException {
        LOG.debug("REST request to save OrderAlpha : {}", orderAlphaDTO);
        if (orderAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orderAlphaDTO = orderAlphaService.save(orderAlphaDTO);
        return ResponseEntity.created(new URI("/api/order-alphas/" + orderAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orderAlphaDTO.getId().toString()))
            .body(orderAlphaDTO);
    }

    /**
     * {@code PUT  /order-alphas/:id} : Updates an existing orderAlpha.
     *
     * @param id the id of the orderAlphaDTO to save.
     * @param orderAlphaDTO the orderAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the orderAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderAlphaDTO> updateOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrderAlphaDTO orderAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrderAlpha : {}, {}", id, orderAlphaDTO);
        if (orderAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orderAlphaDTO = orderAlphaService.update(orderAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAlphaDTO.getId().toString()))
            .body(orderAlphaDTO);
    }

    /**
     * {@code PATCH  /order-alphas/:id} : Partial updates given fields of an existing orderAlpha, field will ignore if it is null
     *
     * @param id the id of the orderAlphaDTO to save.
     * @param orderAlphaDTO the orderAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the orderAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orderAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orderAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrderAlphaDTO> partialUpdateOrderAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrderAlphaDTO orderAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrderAlpha partially : {}, {}", id, orderAlphaDTO);
        if (orderAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orderAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orderAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrderAlphaDTO> result = orderAlphaService.partialUpdate(orderAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderAlphaDTO.getId().toString())
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
    public ResponseEntity<List<OrderAlphaDTO>> getAllOrderAlphas(
        OrderAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrderAlphas by criteria: {}", criteria);

        Page<OrderAlphaDTO> page = orderAlphaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the orderAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderAlphaDTO> getOrderAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrderAlpha : {}", id);
        Optional<OrderAlphaDTO> orderAlphaDTO = orderAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderAlphaDTO);
    }

    /**
     * {@code DELETE  /order-alphas/:id} : delete the "id" orderAlpha.
     *
     * @param id the id of the orderAlphaDTO to delete.
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
