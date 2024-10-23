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
import xyz.jhmapstruct.repository.NextOrderRepository;
import xyz.jhmapstruct.service.NextOrderQueryService;
import xyz.jhmapstruct.service.NextOrderService;
import xyz.jhmapstruct.service.criteria.NextOrderCriteria;
import xyz.jhmapstruct.service.dto.NextOrderDTO;
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
     * @param nextOrderDTO the nextOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderDTO, or with status {@code 400 (Bad Request)} if the nextOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderDTO> createNextOrder(@Valid @RequestBody NextOrderDTO nextOrderDTO) throws URISyntaxException {
        LOG.debug("REST request to save NextOrder : {}", nextOrderDTO);
        if (nextOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderDTO = nextOrderService.save(nextOrderDTO);
        return ResponseEntity.created(new URI("/api/next-orders/" + nextOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderDTO.getId().toString()))
            .body(nextOrderDTO);
    }

    /**
     * {@code PUT  /next-orders/:id} : Updates an existing nextOrder.
     *
     * @param id the id of the nextOrderDTO to save.
     * @param nextOrderDTO the nextOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderDTO> updateNextOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderDTO nextOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrder : {}, {}", id, nextOrderDTO);
        if (nextOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderDTO = nextOrderService.update(nextOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderDTO.getId().toString()))
            .body(nextOrderDTO);
    }

    /**
     * {@code PATCH  /next-orders/:id} : Partial updates given fields of an existing nextOrder, field will ignore if it is null
     *
     * @param id the id of the nextOrderDTO to save.
     * @param nextOrderDTO the nextOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderDTO> partialUpdateNextOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderDTO nextOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrder partially : {}, {}", id, nextOrderDTO);
        if (nextOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderDTO> result = nextOrderService.partialUpdate(nextOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderDTO.getId().toString())
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
    public ResponseEntity<List<NextOrderDTO>> getAllNextOrders(
        NextOrderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrders by criteria: {}", criteria);

        Page<NextOrderDTO> page = nextOrderQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the nextOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderDTO> getNextOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrder : {}", id);
        Optional<NextOrderDTO> nextOrderDTO = nextOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderDTO);
    }

    /**
     * {@code DELETE  /next-orders/:id} : delete the "id" nextOrder.
     *
     * @param id the id of the nextOrderDTO to delete.
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
