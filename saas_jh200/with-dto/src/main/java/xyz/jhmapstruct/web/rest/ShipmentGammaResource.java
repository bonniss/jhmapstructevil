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
import xyz.jhmapstruct.repository.ShipmentGammaRepository;
import xyz.jhmapstruct.service.ShipmentGammaQueryService;
import xyz.jhmapstruct.service.ShipmentGammaService;
import xyz.jhmapstruct.service.criteria.ShipmentGammaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentGamma}.
 */
@RestController
@RequestMapping("/api/shipment-gammas")
public class ShipmentGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentGammaResource.class);

    private static final String ENTITY_NAME = "shipmentGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentGammaService shipmentGammaService;

    private final ShipmentGammaRepository shipmentGammaRepository;

    private final ShipmentGammaQueryService shipmentGammaQueryService;

    public ShipmentGammaResource(
        ShipmentGammaService shipmentGammaService,
        ShipmentGammaRepository shipmentGammaRepository,
        ShipmentGammaQueryService shipmentGammaQueryService
    ) {
        this.shipmentGammaService = shipmentGammaService;
        this.shipmentGammaRepository = shipmentGammaRepository;
        this.shipmentGammaQueryService = shipmentGammaQueryService;
    }

    /**
     * {@code POST  /shipment-gammas} : Create a new shipmentGamma.
     *
     * @param shipmentGammaDTO the shipmentGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentGammaDTO, or with status {@code 400 (Bad Request)} if the shipmentGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentGammaDTO> createShipmentGamma(@Valid @RequestBody ShipmentGammaDTO shipmentGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentGamma : {}", shipmentGammaDTO);
        if (shipmentGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentGammaDTO = shipmentGammaService.save(shipmentGammaDTO);
        return ResponseEntity.created(new URI("/api/shipment-gammas/" + shipmentGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentGammaDTO.getId().toString()))
            .body(shipmentGammaDTO);
    }

    /**
     * {@code PUT  /shipment-gammas/:id} : Updates an existing shipmentGamma.
     *
     * @param id the id of the shipmentGammaDTO to save.
     * @param shipmentGammaDTO the shipmentGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentGammaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentGammaDTO> updateShipmentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentGammaDTO shipmentGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentGamma : {}, {}", id, shipmentGammaDTO);
        if (shipmentGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentGammaDTO = shipmentGammaService.update(shipmentGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentGammaDTO.getId().toString()))
            .body(shipmentGammaDTO);
    }

    /**
     * {@code PATCH  /shipment-gammas/:id} : Partial updates given fields of an existing shipmentGamma, field will ignore if it is null
     *
     * @param id the id of the shipmentGammaDTO to save.
     * @param shipmentGammaDTO the shipmentGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentGammaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentGammaDTO> partialUpdateShipmentGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentGammaDTO shipmentGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentGamma partially : {}, {}", id, shipmentGammaDTO);
        if (shipmentGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentGammaDTO> result = shipmentGammaService.partialUpdate(shipmentGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-gammas} : get all the shipmentGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentGammaDTO>> getAllShipmentGammas(
        ShipmentGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentGammas by criteria: {}", criteria);

        Page<ShipmentGammaDTO> page = shipmentGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-gammas/count} : count all the shipmentGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentGammas(ShipmentGammaCriteria criteria) {
        LOG.debug("REST request to count ShipmentGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-gammas/:id} : get the "id" shipmentGamma.
     *
     * @param id the id of the shipmentGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentGammaDTO> getShipmentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentGamma : {}", id);
        Optional<ShipmentGammaDTO> shipmentGammaDTO = shipmentGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentGammaDTO);
    }

    /**
     * {@code DELETE  /shipment-gammas/:id} : delete the "id" shipmentGamma.
     *
     * @param id the id of the shipmentGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentGamma : {}", id);
        shipmentGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
