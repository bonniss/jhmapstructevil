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
import xyz.jhmapstruct.repository.ShipmentViViRepository;
import xyz.jhmapstruct.service.ShipmentViViService;
import xyz.jhmapstruct.service.dto.ShipmentViViDTO;
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
     * @param shipmentViViDTO the shipmentViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentViViDTO, or with status {@code 400 (Bad Request)} if the shipmentViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentViViDTO> createShipmentViVi(@Valid @RequestBody ShipmentViViDTO shipmentViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentViVi : {}", shipmentViViDTO);
        if (shipmentViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentViViDTO = shipmentViViService.save(shipmentViViDTO);
        return ResponseEntity.created(new URI("/api/shipment-vi-vis/" + shipmentViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentViViDTO.getId().toString()))
            .body(shipmentViViDTO);
    }

    /**
     * {@code PUT  /shipment-vi-vis/:id} : Updates an existing shipmentViVi.
     *
     * @param id the id of the shipmentViViDTO to save.
     * @param shipmentViViDTO the shipmentViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViViDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentViViDTO> updateShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentViViDTO shipmentViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentViVi : {}, {}", id, shipmentViViDTO);
        if (shipmentViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentViViDTO = shipmentViViService.update(shipmentViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViViDTO.getId().toString()))
            .body(shipmentViViDTO);
    }

    /**
     * {@code PATCH  /shipment-vi-vis/:id} : Partial updates given fields of an existing shipmentViVi, field will ignore if it is null
     *
     * @param id the id of the shipmentViViDTO to save.
     * @param shipmentViViDTO the shipmentViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViViDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentViViDTO> partialUpdateShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentViViDTO shipmentViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentViVi partially : {}, {}", id, shipmentViViDTO);
        if (shipmentViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentViViDTO> result = shipmentViViService.partialUpdate(shipmentViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-vi-vis} : get all the shipmentViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentViVis in body.
     */
    @GetMapping("")
    public List<ShipmentViViDTO> getAllShipmentViVis() {
        LOG.debug("REST request to get all ShipmentViVis");
        return shipmentViViService.findAll();
    }

    /**
     * {@code GET  /shipment-vi-vis/:id} : get the "id" shipmentViVi.
     *
     * @param id the id of the shipmentViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentViViDTO> getShipmentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentViVi : {}", id);
        Optional<ShipmentViViDTO> shipmentViViDTO = shipmentViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentViViDTO);
    }

    /**
     * {@code DELETE  /shipment-vi-vis/:id} : delete the "id" shipmentViVi.
     *
     * @param id the id of the shipmentViViDTO to delete.
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
