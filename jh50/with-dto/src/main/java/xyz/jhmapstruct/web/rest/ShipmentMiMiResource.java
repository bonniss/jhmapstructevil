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
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.ShipmentMiMiService;
import xyz.jhmapstruct.service.dto.ShipmentMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentMiMi}.
 */
@RestController
@RequestMapping("/api/shipment-mi-mis")
public class ShipmentMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentMiMiResource.class);

    private static final String ENTITY_NAME = "shipmentMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentMiMiService shipmentMiMiService;

    private final ShipmentMiMiRepository shipmentMiMiRepository;

    public ShipmentMiMiResource(ShipmentMiMiService shipmentMiMiService, ShipmentMiMiRepository shipmentMiMiRepository) {
        this.shipmentMiMiService = shipmentMiMiService;
        this.shipmentMiMiRepository = shipmentMiMiRepository;
    }

    /**
     * {@code POST  /shipment-mi-mis} : Create a new shipmentMiMi.
     *
     * @param shipmentMiMiDTO the shipmentMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentMiMiDTO, or with status {@code 400 (Bad Request)} if the shipmentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentMiMiDTO> createShipmentMiMi(@Valid @RequestBody ShipmentMiMiDTO shipmentMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentMiMi : {}", shipmentMiMiDTO);
        if (shipmentMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentMiMiDTO = shipmentMiMiService.save(shipmentMiMiDTO);
        return ResponseEntity.created(new URI("/api/shipment-mi-mis/" + shipmentMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentMiMiDTO.getId().toString()))
            .body(shipmentMiMiDTO);
    }

    /**
     * {@code PUT  /shipment-mi-mis/:id} : Updates an existing shipmentMiMi.
     *
     * @param id the id of the shipmentMiMiDTO to save.
     * @param shipmentMiMiDTO the shipmentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMiMiDTO> updateShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentMiMiDTO shipmentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentMiMi : {}, {}", id, shipmentMiMiDTO);
        if (shipmentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentMiMiDTO = shipmentMiMiService.update(shipmentMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiMiDTO.getId().toString()))
            .body(shipmentMiMiDTO);
    }

    /**
     * {@code PATCH  /shipment-mi-mis/:id} : Partial updates given fields of an existing shipmentMiMi, field will ignore if it is null
     *
     * @param id the id of the shipmentMiMiDTO to save.
     * @param shipmentMiMiDTO the shipmentMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentMiMiDTO> partialUpdateShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentMiMiDTO shipmentMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentMiMi partially : {}, {}", id, shipmentMiMiDTO);
        if (shipmentMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentMiMiDTO> result = shipmentMiMiService.partialUpdate(shipmentMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-mi-mis} : get all the shipmentMiMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentMiMis in body.
     */
    @GetMapping("")
    public List<ShipmentMiMiDTO> getAllShipmentMiMis() {
        LOG.debug("REST request to get all ShipmentMiMis");
        return shipmentMiMiService.findAll();
    }

    /**
     * {@code GET  /shipment-mi-mis/:id} : get the "id" shipmentMiMi.
     *
     * @param id the id of the shipmentMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMiMiDTO> getShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentMiMi : {}", id);
        Optional<ShipmentMiMiDTO> shipmentMiMiDTO = shipmentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentMiMiDTO);
    }

    /**
     * {@code DELETE  /shipment-mi-mis/:id} : delete the "id" shipmentMiMi.
     *
     * @param id the id of the shipmentMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentMiMi : {}", id);
        shipmentMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
