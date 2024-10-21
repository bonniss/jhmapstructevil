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
import xyz.jhmapstruct.repository.ShipmentViRepository;
import xyz.jhmapstruct.service.ShipmentViService;
import xyz.jhmapstruct.service.dto.ShipmentViDTO;
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
     * @param shipmentViDTO the shipmentViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentViDTO, or with status {@code 400 (Bad Request)} if the shipmentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentViDTO> createShipmentVi(@Valid @RequestBody ShipmentViDTO shipmentViDTO) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentVi : {}", shipmentViDTO);
        if (shipmentViDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentViDTO = shipmentViService.save(shipmentViDTO);
        return ResponseEntity.created(new URI("/api/shipment-vis/" + shipmentViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentViDTO.getId().toString()))
            .body(shipmentViDTO);
    }

    /**
     * {@code PUT  /shipment-vis/:id} : Updates an existing shipmentVi.
     *
     * @param id the id of the shipmentViDTO to save.
     * @param shipmentViDTO the shipmentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentViDTO> updateShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentViDTO shipmentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentVi : {}, {}", id, shipmentViDTO);
        if (shipmentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentViDTO = shipmentViService.update(shipmentViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViDTO.getId().toString()))
            .body(shipmentViDTO);
    }

    /**
     * {@code PATCH  /shipment-vis/:id} : Partial updates given fields of an existing shipmentVi, field will ignore if it is null
     *
     * @param id the id of the shipmentViDTO to save.
     * @param shipmentViDTO the shipmentViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentViDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentViDTO> partialUpdateShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentViDTO shipmentViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentVi partially : {}, {}", id, shipmentViDTO);
        if (shipmentViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentViDTO> result = shipmentViService.partialUpdate(shipmentViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-vis} : get all the shipmentVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentVis in body.
     */
    @GetMapping("")
    public List<ShipmentViDTO> getAllShipmentVis() {
        LOG.debug("REST request to get all ShipmentVis");
        return shipmentViService.findAll();
    }

    /**
     * {@code GET  /shipment-vis/:id} : get the "id" shipmentVi.
     *
     * @param id the id of the shipmentViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentViDTO> getShipmentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentVi : {}", id);
        Optional<ShipmentViDTO> shipmentViDTO = shipmentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentViDTO);
    }

    /**
     * {@code DELETE  /shipment-vis/:id} : delete the "id" shipmentVi.
     *
     * @param id the id of the shipmentViDTO to delete.
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
