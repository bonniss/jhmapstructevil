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
import xyz.jhmapstruct.domain.ShipmentVi;
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.ShipmentViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentVi}.
 */
@RestController
@RequestMapping("/api/shipment-vis")
public class ShipmentViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentViResource.class);

    private static final String ENTITY_NAME = "shipmentVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentViService shipmentViService;

    private final ShipmentViRepository shipmentViRepository;

    public ShipmentViResource(ShipmentViService shipmentViService, ShipmentViRepository shipmentViRepository) {
        this.shipmentViService = shipmentViService;
        this.shipmentViRepository = shipmentViRepository;
    }

    /**
     * {@code POST  /shipment-vis} : Create a new shipmentVi.
     *
     * @param shipmentVi the shipmentVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentVi, or with status {@code 400 (Bad Request)} if the shipmentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentVi> createShipmentVi(@Valid @RequestBody ShipmentVi shipmentVi) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentVi : {}", shipmentVi);
        if (shipmentVi.getId() != null) {
            throw new BadRequestAlertException("A new shipmentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentVi = shipmentViService.save(shipmentVi);
        return ResponseEntity.created(new URI("/api/shipment-vis/" + shipmentVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentVi.getId().toString()))
            .body(shipmentVi);
    }

    /**
     * {@code PUT  /shipment-vis/:id} : Updates an existing shipmentVi.
     *
     * @param id the id of the shipmentVi to save.
     * @param shipmentVi the shipmentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentVi,
     * or with status {@code 400 (Bad Request)} if the shipmentVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentVi> updateShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentVi shipmentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentVi : {}, {}", id, shipmentVi);
        if (shipmentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentVi = shipmentViService.update(shipmentVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentVi.getId().toString()))
            .body(shipmentVi);
    }

    /**
     * {@code PATCH  /shipment-vis/:id} : Partial updates given fields of an existing shipmentVi, field will ignore if it is null
     *
     * @param id the id of the shipmentVi to save.
     * @param shipmentVi the shipmentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentVi,
     * or with status {@code 400 (Bad Request)} if the shipmentVi is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentVi> partialUpdateShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentVi shipmentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentVi partially : {}, {}", id, shipmentVi);
        if (shipmentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentVi> result = shipmentViService.partialUpdate(shipmentVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentVi.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-vis} : get all the shipmentVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentVis in body.
     */
    @GetMapping("")
    public List<ShipmentVi> getAllShipmentVis() {
        LOG.debug("REST request to get all ShipmentVis");
        return shipmentViService.findAll();
    }

    /**
     * {@code GET  /shipment-vis/:id} : get the "id" shipmentVi.
     *
     * @param id the id of the shipmentVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentVi> getShipmentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentVi : {}", id);
        Optional<ShipmentVi> shipmentVi = shipmentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentVi);
    }

    /**
     * {@code DELETE  /shipment-vis/:id} : delete the "id" shipmentVi.
     *
     * @param id the id of the shipmentVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentVi : {}", id);
        shipmentViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
