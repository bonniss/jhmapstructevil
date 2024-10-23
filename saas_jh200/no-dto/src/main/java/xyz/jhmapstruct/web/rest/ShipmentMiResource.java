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
import xyz.jhmapstruct.domain.ShipmentMi;
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.ShipmentMiQueryService;
import xyz.jhmapstruct.service.ShipmentMiService;
import xyz.jhmapstruct.service.criteria.ShipmentMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentMi}.
 */
@RestController
@RequestMapping("/api/shipment-mis")
public class ShipmentMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiResource.class);

    private static final String ENTITY_NAME = "shipmentMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentMiService shipmentMiService;

    private final ShipmentMiRepository shipmentMiRepository;

    private final ShipmentMiQueryService shipmentMiQueryService;

    public ShipmentMiResource(
        ShipmentMiService shipmentMiService,
        ShipmentMiRepository shipmentMiRepository,
        ShipmentMiQueryService shipmentMiQueryService
    ) {
        this.shipmentMiService = shipmentMiService;
        this.shipmentMiRepository = shipmentMiRepository;
        this.shipmentMiQueryService = shipmentMiQueryService;
    }

    /**
     * {@code POST  /shipment-mis} : Create a new shipmentMi.
     *
     * @param shipmentMi the shipmentMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentMi, or with status {@code 400 (Bad Request)} if the shipmentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentMi> createShipmentMi(@Valid @RequestBody ShipmentMi shipmentMi) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentMi : {}", shipmentMi);
        if (shipmentMi.getId() != null) {
            throw new BadRequestAlertException("A new shipmentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentMi = shipmentMiService.save(shipmentMi);
        return ResponseEntity.created(new URI("/api/shipment-mis/" + shipmentMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentMi.getId().toString()))
            .body(shipmentMi);
    }

    /**
     * {@code PUT  /shipment-mis/:id} : Updates an existing shipmentMi.
     *
     * @param id the id of the shipmentMi to save.
     * @param shipmentMi the shipmentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMi,
     * or with status {@code 400 (Bad Request)} if the shipmentMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMi> updateShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentMi shipmentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentMi : {}, {}", id, shipmentMi);
        if (shipmentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentMi = shipmentMiService.update(shipmentMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMi.getId().toString()))
            .body(shipmentMi);
    }

    /**
     * {@code PATCH  /shipment-mis/:id} : Partial updates given fields of an existing shipmentMi, field will ignore if it is null
     *
     * @param id the id of the shipmentMi to save.
     * @param shipmentMi the shipmentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMi,
     * or with status {@code 400 (Bad Request)} if the shipmentMi is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentMi> partialUpdateShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentMi shipmentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentMi partially : {}, {}", id, shipmentMi);
        if (shipmentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentMi> result = shipmentMiService.partialUpdate(shipmentMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMi.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-mis} : get all the shipmentMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentMi>> getAllShipmentMis(
        ShipmentMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentMis by criteria: {}", criteria);

        Page<ShipmentMi> page = shipmentMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-mis/count} : count all the shipmentMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentMis(ShipmentMiCriteria criteria) {
        LOG.debug("REST request to count ShipmentMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-mis/:id} : get the "id" shipmentMi.
     *
     * @param id the id of the shipmentMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMi> getShipmentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentMi : {}", id);
        Optional<ShipmentMi> shipmentMi = shipmentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentMi);
    }

    /**
     * {@code DELETE  /shipment-mis/:id} : delete the "id" shipmentMi.
     *
     * @param id the id of the shipmentMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentMi : {}", id);
        shipmentMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
