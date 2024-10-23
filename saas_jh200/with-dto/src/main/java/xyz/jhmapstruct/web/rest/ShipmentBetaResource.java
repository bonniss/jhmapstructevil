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
import xyz.jhmapstruct.repository.ShipmentBetaRepository;
import xyz.jhmapstruct.service.ShipmentBetaQueryService;
import xyz.jhmapstruct.service.ShipmentBetaService;
import xyz.jhmapstruct.service.criteria.ShipmentBetaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentBetaDTO;
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
     * @param shipmentBetaDTO the shipmentBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentBetaDTO, or with status {@code 400 (Bad Request)} if the shipmentBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentBetaDTO> createShipmentBeta(@Valid @RequestBody ShipmentBetaDTO shipmentBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentBeta : {}", shipmentBetaDTO);
        if (shipmentBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentBetaDTO = shipmentBetaService.save(shipmentBetaDTO);
        return ResponseEntity.created(new URI("/api/shipment-betas/" + shipmentBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentBetaDTO.getId().toString()))
            .body(shipmentBetaDTO);
    }

    /**
     * {@code PUT  /shipment-betas/:id} : Updates an existing shipmentBeta.
     *
     * @param id the id of the shipmentBetaDTO to save.
     * @param shipmentBetaDTO the shipmentBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentBetaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentBetaDTO> updateShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentBetaDTO shipmentBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentBeta : {}, {}", id, shipmentBetaDTO);
        if (shipmentBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentBetaDTO = shipmentBetaService.update(shipmentBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentBetaDTO.getId().toString()))
            .body(shipmentBetaDTO);
    }

    /**
     * {@code PATCH  /shipment-betas/:id} : Partial updates given fields of an existing shipmentBeta, field will ignore if it is null
     *
     * @param id the id of the shipmentBetaDTO to save.
     * @param shipmentBetaDTO the shipmentBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentBetaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentBetaDTO> partialUpdateShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentBetaDTO shipmentBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentBeta partially : {}, {}", id, shipmentBetaDTO);
        if (shipmentBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentBetaDTO> result = shipmentBetaService.partialUpdate(shipmentBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentBetaDTO.getId().toString())
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
    public ResponseEntity<List<ShipmentBetaDTO>> getAllShipmentBetas(
        ShipmentBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentBetas by criteria: {}", criteria);

        Page<ShipmentBetaDTO> page = shipmentBetaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the shipmentBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentBetaDTO> getShipmentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentBeta : {}", id);
        Optional<ShipmentBetaDTO> shipmentBetaDTO = shipmentBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentBetaDTO);
    }

    /**
     * {@code DELETE  /shipment-betas/:id} : delete the "id" shipmentBeta.
     *
     * @param id the id of the shipmentBetaDTO to delete.
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
