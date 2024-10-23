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
import xyz.jhmapstruct.repository.ShipmentMiRepository;
import xyz.jhmapstruct.service.ShipmentMiQueryService;
import xyz.jhmapstruct.service.ShipmentMiService;
import xyz.jhmapstruct.service.criteria.ShipmentMiCriteria;
import xyz.jhmapstruct.service.dto.ShipmentMiDTO;
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
     * @param shipmentMiDTO the shipmentMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentMiDTO, or with status {@code 400 (Bad Request)} if the shipmentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentMiDTO> createShipmentMi(@Valid @RequestBody ShipmentMiDTO shipmentMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ShipmentMi : {}", shipmentMiDTO);
        if (shipmentMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentMiDTO = shipmentMiService.save(shipmentMiDTO);
        return ResponseEntity.created(new URI("/api/shipment-mis/" + shipmentMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentMiDTO.getId().toString()))
            .body(shipmentMiDTO);
    }

    /**
     * {@code PUT  /shipment-mis/:id} : Updates an existing shipmentMi.
     *
     * @param id the id of the shipmentMiDTO to save.
     * @param shipmentMiDTO the shipmentMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentMiDTO> updateShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentMiDTO shipmentMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentMi : {}, {}", id, shipmentMiDTO);
        if (shipmentMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentMiDTO = shipmentMiService.update(shipmentMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiDTO.getId().toString()))
            .body(shipmentMiDTO);
    }

    /**
     * {@code PATCH  /shipment-mis/:id} : Partial updates given fields of an existing shipmentMi, field will ignore if it is null
     *
     * @param id the id of the shipmentMiDTO to save.
     * @param shipmentMiDTO the shipmentMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentMiDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentMiDTO> partialUpdateShipmentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentMiDTO shipmentMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentMi partially : {}, {}", id, shipmentMiDTO);
        if (shipmentMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentMiDTO> result = shipmentMiService.partialUpdate(shipmentMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentMiDTO.getId().toString())
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
    public ResponseEntity<List<ShipmentMiDTO>> getAllShipmentMis(
        ShipmentMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentMis by criteria: {}", criteria);

        Page<ShipmentMiDTO> page = shipmentMiQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the shipmentMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentMiDTO> getShipmentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentMi : {}", id);
        Optional<ShipmentMiDTO> shipmentMiDTO = shipmentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentMiDTO);
    }

    /**
     * {@code DELETE  /shipment-mis/:id} : delete the "id" shipmentMi.
     *
     * @param id the id of the shipmentMiDTO to delete.
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
