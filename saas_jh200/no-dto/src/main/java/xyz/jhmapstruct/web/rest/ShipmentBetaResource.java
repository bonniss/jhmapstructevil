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
import xyz.jhmapstruct.domain.ShipmentBeta;
import xyz.jhmapstruct.repository.ShipmentBetaRepository;
import xyz.jhmapstruct.service.ShipmentBetaQueryService;
import xyz.jhmapstruct.service.ShipmentBetaService;
import xyz.jhmapstruct.service.criteria.ShipmentBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentBeta}.
 */
@RestController
@RequestMapping("/api/shipment-betas")
public class ShipmentBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentBetaResource.class);

    private static final String ENTITY_NAME = "shipmentBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentBetaService shipmentBetaService;

    private final ShipmentBetaRepository shipmentBetaRepository;

    private final ShipmentBetaQueryService shipmentBetaQueryService;

    public ShipmentBetaResource(
        ShipmentBetaService shipmentBetaService,
        ShipmentBetaRepository shipmentBetaRepository,
        ShipmentBetaQueryService shipmentBetaQueryService
    ) {
        this.shipmentBetaService = shipmentBetaService;
        this.shipmentBetaRepository = shipmentBetaRepository;
        this.shipmentBetaQueryService = shipmentBetaQueryService;
    }

    /**
     * {@code POST  /shipment-betas} : Create a new shipmentBeta.
     *
     * @param shipmentBeta the shipmentBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentBeta, or with status {@code 400 (Bad Request)} if the shipmentBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentBeta> createShipmentBeta(@Valid @RequestBody ShipmentBeta shipmentBeta) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentBeta : {}", shipmentBeta);
        if (shipmentBeta.getId() != null) {
            throw new BadRequestAlertException("A new shipmentBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentBeta = shipmentBetaService.save(shipmentBeta);
        return ResponseEntity.created(new URI("/api/shipment-betas/" + shipmentBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentBeta.getId().toString()))
            .body(shipmentBeta);
    }

    /**
     * {@code PUT  /shipment-betas/:id} : Updates an existing shipmentBeta.
     *
     * @param id the id of the shipmentBeta to save.
     * @param shipmentBeta the shipmentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentBeta,
     * or with status {@code 400 (Bad Request)} if the shipmentBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentBeta> updateShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentBeta shipmentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentBeta : {}, {}", id, shipmentBeta);
        if (shipmentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentBeta = shipmentBetaService.update(shipmentBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentBeta.getId().toString()))
            .body(shipmentBeta);
    }

    /**
     * {@code PATCH  /shipment-betas/:id} : Partial updates given fields of an existing shipmentBeta, field will ignore if it is null
     *
     * @param id the id of the shipmentBeta to save.
     * @param shipmentBeta the shipmentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentBeta,
     * or with status {@code 400 (Bad Request)} if the shipmentBeta is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentBeta> partialUpdateShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentBeta shipmentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentBeta partially : {}, {}", id, shipmentBeta);
        if (shipmentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentBeta> result = shipmentBetaService.partialUpdate(shipmentBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-betas} : get all the shipmentBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentBeta>> getAllShipmentBetas(
        ShipmentBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentBetas by criteria: {}", criteria);

        Page<ShipmentBeta> page = shipmentBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-betas/count} : count all the shipmentBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentBetas(ShipmentBetaCriteria criteria) {
        LOG.debug("REST request to count ShipmentBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-betas/:id} : get the "id" shipmentBeta.
     *
     * @param id the id of the shipmentBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentBeta> getShipmentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentBeta : {}", id);
        Optional<ShipmentBeta> shipmentBeta = shipmentBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentBeta);
    }

    /**
     * {@code DELETE  /shipment-betas/:id} : delete the "id" shipmentBeta.
     *
     * @param id the id of the shipmentBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentBeta : {}", id);
        shipmentBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
