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
import xyz.jhmapstruct.domain.ShipmentMiMi;
import xyz.jhmapstruct.repository.ShipmentMiMiRepository;
import xyz.jhmapstruct.service.ShipmentMiMiQueryService;
import xyz.jhmapstruct.service.ShipmentMiMiService;
import xyz.jhmapstruct.service.criteria.ShipmentMiMiCriteria;
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

    private final ShipmentMiMiQueryService shipmentMiMiQueryService;

    public ShipmentMiMiResource(
        ShipmentMiMiService shipmentMiMiService,
        ShipmentMiMiRepository shipmentMiMiRepository,
        ShipmentMiMiQueryService shipmentMiMiQueryService
    ) {
        this.shipmentMiMiService = shipmentMiMiService;
        this.shipmentMiMiRepository = shipmentMiMiRepository;
        this.shipmentMiMiQueryService = shipmentMiMiQueryService;
    }

    /**
     * {@code POST  /shipment-mi-mis} : Create a new shipmentMiMi.
     *
     * @param shipmentMiMi the shipmentMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentMiMi, or with status {@code 400 (Bad Request)} if the shipmentMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentMiMi> createShipmentMiMi(@Valid @RequestBody ShipmentMiMi shipmentMiMi) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentMiMi : {}", shipmentMiMi);
        if (shipmentMiMi.getId() != null) {
            throw new BadRequestAlertException("A new shipmentMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentMiMi = shipmentMiMiService.save(shipmentMiMi);
        return ResponseEntity.created(new URI("/api/shipment-mi-mis/" + shipmentMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentMiMi.getId().toString()))
            .body(shipmentMiMi);
    }

    /**
     * {@code PUT  /shipment-mi-mis/:id} : Updates an existing shipmentMiMi.
     *
     * @param id the id of the shipmentMiMi to save.
     * @param shipmentMiMi the shipmentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiMi,
     * or with status {@code 400 (Bad Request)} if the shipmentMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMiMi> updateShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentMiMi shipmentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentMiMi : {}, {}", id, shipmentMiMi);
        if (shipmentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentMiMi = shipmentMiMiService.update(shipmentMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiMi.getId().toString()))
            .body(shipmentMiMi);
    }

    /**
     * {@code PATCH  /shipment-mi-mis/:id} : Partial updates given fields of an existing shipmentMiMi, field will ignore if it is null
     *
     * @param id the id of the shipmentMiMi to save.
     * @param shipmentMiMi the shipmentMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiMi,
     * or with status {@code 400 (Bad Request)} if the shipmentMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentMiMi> partialUpdateShipmentMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentMiMi shipmentMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentMiMi partially : {}, {}", id, shipmentMiMi);
        if (shipmentMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentMiMi> result = shipmentMiMiService.partialUpdate(shipmentMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-mi-mis} : get all the shipmentMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentMiMi>> getAllShipmentMiMis(
        ShipmentMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentMiMis by criteria: {}", criteria);

        Page<ShipmentMiMi> page = shipmentMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-mi-mis/count} : count all the shipmentMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentMiMis(ShipmentMiMiCriteria criteria) {
        LOG.debug("REST request to count ShipmentMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-mi-mis/:id} : get the "id" shipmentMiMi.
     *
     * @param id the id of the shipmentMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMiMi> getShipmentMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentMiMi : {}", id);
        Optional<ShipmentMiMi> shipmentMiMi = shipmentMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentMiMi);
    }

    /**
     * {@code DELETE  /shipment-mi-mis/:id} : delete the "id" shipmentMiMi.
     *
     * @param id the id of the shipmentMiMi to delete.
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
