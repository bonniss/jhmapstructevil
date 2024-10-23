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
import xyz.jhmapstruct.repository.ShipmentThetaRepository;
import xyz.jhmapstruct.service.ShipmentThetaQueryService;
import xyz.jhmapstruct.service.ShipmentThetaService;
import xyz.jhmapstruct.service.criteria.ShipmentThetaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentTheta}.
 */
@RestController
@RequestMapping("/api/shipment-thetas")
public class ShipmentThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentThetaResource.class);

    private static final String ENTITY_NAME = "shipmentTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentThetaService shipmentThetaService;

    private final ShipmentThetaRepository shipmentThetaRepository;

    private final ShipmentThetaQueryService shipmentThetaQueryService;

    public ShipmentThetaResource(
        ShipmentThetaService shipmentThetaService,
        ShipmentThetaRepository shipmentThetaRepository,
        ShipmentThetaQueryService shipmentThetaQueryService
    ) {
        this.shipmentThetaService = shipmentThetaService;
        this.shipmentThetaRepository = shipmentThetaRepository;
        this.shipmentThetaQueryService = shipmentThetaQueryService;
    }

    /**
     * {@code POST  /shipment-thetas} : Create a new shipmentTheta.
     *
     * @param shipmentThetaDTO the shipmentThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentThetaDTO, or with status {@code 400 (Bad Request)} if the shipmentTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentThetaDTO> createShipmentTheta(@Valid @RequestBody ShipmentThetaDTO shipmentThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentTheta : {}", shipmentThetaDTO);
        if (shipmentThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentThetaDTO = shipmentThetaService.save(shipmentThetaDTO);
        return ResponseEntity.created(new URI("/api/shipment-thetas/" + shipmentThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentThetaDTO.getId().toString()))
            .body(shipmentThetaDTO);
    }

    /**
     * {@code PUT  /shipment-thetas/:id} : Updates an existing shipmentTheta.
     *
     * @param id the id of the shipmentThetaDTO to save.
     * @param shipmentThetaDTO the shipmentThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentThetaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentThetaDTO> updateShipmentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentThetaDTO shipmentThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentTheta : {}, {}", id, shipmentThetaDTO);
        if (shipmentThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentThetaDTO = shipmentThetaService.update(shipmentThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentThetaDTO.getId().toString()))
            .body(shipmentThetaDTO);
    }

    /**
     * {@code PATCH  /shipment-thetas/:id} : Partial updates given fields of an existing shipmentTheta, field will ignore if it is null
     *
     * @param id the id of the shipmentThetaDTO to save.
     * @param shipmentThetaDTO the shipmentThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentThetaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentThetaDTO> partialUpdateShipmentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentThetaDTO shipmentThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentTheta partially : {}, {}", id, shipmentThetaDTO);
        if (shipmentThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentThetaDTO> result = shipmentThetaService.partialUpdate(shipmentThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-thetas} : get all the shipmentThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentThetaDTO>> getAllShipmentThetas(
        ShipmentThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentThetas by criteria: {}", criteria);

        Page<ShipmentThetaDTO> page = shipmentThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-thetas/count} : count all the shipmentThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentThetas(ShipmentThetaCriteria criteria) {
        LOG.debug("REST request to count ShipmentThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-thetas/:id} : get the "id" shipmentTheta.
     *
     * @param id the id of the shipmentThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentThetaDTO> getShipmentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentTheta : {}", id);
        Optional<ShipmentThetaDTO> shipmentThetaDTO = shipmentThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentThetaDTO);
    }

    /**
     * {@code DELETE  /shipment-thetas/:id} : delete the "id" shipmentTheta.
     *
     * @param id the id of the shipmentThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentTheta : {}", id);
        shipmentThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
