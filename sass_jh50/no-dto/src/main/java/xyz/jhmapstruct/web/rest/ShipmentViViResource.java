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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.ShipmentViVi;
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.ShipmentViViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentViVi}.
 */
@RestController
@RequestMapping("/api/shipment-vi-vis")
public class ShipmentViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViViResource.class);

    private static final String ENTITY_NAME = "shipmentViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentViViService shipmentViViService;

    private final ShipmentViViRepository shipmentViViRepository;

    public ShipmentViViResource(ShipmentViViService shipmentViViService, ShipmentViViRepository shipmentViViRepository) {
        this.shipmentViViService = shipmentViViService;
        this.shipmentViViRepository = shipmentViViRepository;
    }

    /**
     * {@code POST  /shipment-vi-vis} : Create a new shipmentViVi.
     *
     * @param shipmentViVi the shipmentViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentViVi, or with status {@code 400 (Bad Request)} if the shipmentViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentViVi> createShipmentViVi(@Valid @RequestBody ShipmentViVi shipmentViVi) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentViVi : {}", shipmentViVi);
        if (shipmentViVi.getId() != null) {
            throw new BadRequestAlertException("A new shipmentViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentViVi = shipmentViViService.save(shipmentViVi);
        return ResponseEntity.created(new URI("/api/shipment-vi-vis/" + shipmentViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentViVi.getId().toString()))
            .body(shipmentViVi);
    }

    /**
     * {@code PUT  /shipment-vi-vis/:id} : Updates an existing shipmentViVi.
     *
     * @param id the id of the shipmentViVi to save.
     * @param shipmentViVi the shipmentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViVi,
     * or with status {@code 400 (Bad Request)} if the shipmentViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentViVi> updateShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentViVi shipmentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentViVi : {}, {}", id, shipmentViVi);
        if (shipmentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentViVi = shipmentViViService.update(shipmentViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViVi.getId().toString()))
            .body(shipmentViVi);
    }

    /**
     * {@code PATCH  /shipment-vi-vis/:id} : Partial updates given fields of an existing shipmentViVi, field will ignore if it is null
     *
     * @param id the id of the shipmentViVi to save.
     * @param shipmentViVi the shipmentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViVi,
     * or with status {@code 400 (Bad Request)} if the shipmentViVi is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentViVi> partialUpdateShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentViVi shipmentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentViVi partially : {}, {}", id, shipmentViVi);
        if (shipmentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentViVi> result = shipmentViViService.partialUpdate(shipmentViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-vi-vis} : get all the shipmentViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentViVis in body.
     */
    @GetMapping("")
    public List<ShipmentViVi> getAllShipmentViVis() {
        LOG.debug("REST request to get all ShipmentViVis");
        return shipmentViViService.findAll();
    }

    /**
     * {@code GET  /shipment-vi-vis/:id} : get the "id" shipmentViVi.
     *
     * @param id the id of the shipmentViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentViVi> getShipmentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentViVi : {}", id);
        Optional<ShipmentViVi> shipmentViVi = shipmentViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentViVi);
    }

    /**
     * {@code DELETE  /shipment-vi-vis/:id} : delete the "id" shipmentViVi.
     *
     * @param id the id of the shipmentViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentViVi : {}", id);
        shipmentViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
