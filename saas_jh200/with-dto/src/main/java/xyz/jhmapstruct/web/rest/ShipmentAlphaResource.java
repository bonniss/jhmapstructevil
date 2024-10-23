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
import xyz.jhmapstruct.repository.ShipmentAlphaRepository;
import xyz.jhmapstruct.service.ShipmentAlphaQueryService;
import xyz.jhmapstruct.service.ShipmentAlphaService;
import xyz.jhmapstruct.service.criteria.ShipmentAlphaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentAlpha}.
 */
@RestController
@RequestMapping("/api/shipment-alphas")
public class ShipmentAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentAlphaResource.class);

    private static final String ENTITY_NAME = "shipmentAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentAlphaService shipmentAlphaService;

    private final ShipmentAlphaRepository shipmentAlphaRepository;

    private final ShipmentAlphaQueryService shipmentAlphaQueryService;

    public ShipmentAlphaResource(
        ShipmentAlphaService shipmentAlphaService,
        ShipmentAlphaRepository shipmentAlphaRepository,
        ShipmentAlphaQueryService shipmentAlphaQueryService
    ) {
        this.shipmentAlphaService = shipmentAlphaService;
        this.shipmentAlphaRepository = shipmentAlphaRepository;
        this.shipmentAlphaQueryService = shipmentAlphaQueryService;
    }

    /**
     * {@code POST  /shipment-alphas} : Create a new shipmentAlpha.
     *
     * @param shipmentAlphaDTO the shipmentAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentAlphaDTO, or with status {@code 400 (Bad Request)} if the shipmentAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentAlphaDTO> createShipmentAlpha(@Valid @RequestBody ShipmentAlphaDTO shipmentAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentAlpha : {}", shipmentAlphaDTO);
        if (shipmentAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentAlphaDTO = shipmentAlphaService.save(shipmentAlphaDTO);
        return ResponseEntity.created(new URI("/api/shipment-alphas/" + shipmentAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentAlphaDTO.getId().toString()))
            .body(shipmentAlphaDTO);
    }

    /**
     * {@code PUT  /shipment-alphas/:id} : Updates an existing shipmentAlpha.
     *
     * @param id the id of the shipmentAlphaDTO to save.
     * @param shipmentAlphaDTO the shipmentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentAlphaDTO> updateShipmentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentAlphaDTO shipmentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentAlpha : {}, {}", id, shipmentAlphaDTO);
        if (shipmentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentAlphaDTO = shipmentAlphaService.update(shipmentAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentAlphaDTO.getId().toString()))
            .body(shipmentAlphaDTO);
    }

    /**
     * {@code PATCH  /shipment-alphas/:id} : Partial updates given fields of an existing shipmentAlpha, field will ignore if it is null
     *
     * @param id the id of the shipmentAlphaDTO to save.
     * @param shipmentAlphaDTO the shipmentAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentAlphaDTO> partialUpdateShipmentAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentAlphaDTO shipmentAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentAlpha partially : {}, {}", id, shipmentAlphaDTO);
        if (shipmentAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentAlphaDTO> result = shipmentAlphaService.partialUpdate(shipmentAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-alphas} : get all the shipmentAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentAlphaDTO>> getAllShipmentAlphas(
        ShipmentAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentAlphas by criteria: {}", criteria);

        Page<ShipmentAlphaDTO> page = shipmentAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-alphas/count} : count all the shipmentAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentAlphas(ShipmentAlphaCriteria criteria) {
        LOG.debug("REST request to count ShipmentAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-alphas/:id} : get the "id" shipmentAlpha.
     *
     * @param id the id of the shipmentAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentAlphaDTO> getShipmentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentAlpha : {}", id);
        Optional<ShipmentAlphaDTO> shipmentAlphaDTO = shipmentAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentAlphaDTO);
    }

    /**
     * {@code DELETE  /shipment-alphas/:id} : delete the "id" shipmentAlpha.
     *
     * @param id the id of the shipmentAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentAlpha : {}", id);
        shipmentAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
