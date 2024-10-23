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
import xyz.jhmapstruct.repository.ShipmentSigmaRepository;
import xyz.jhmapstruct.service.ShipmentSigmaQueryService;
import xyz.jhmapstruct.service.ShipmentSigmaService;
import xyz.jhmapstruct.service.criteria.ShipmentSigmaCriteria;
import xyz.jhmapstruct.service.dto.ShipmentSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ShipmentSigma}.
 */
@RestController
@RequestMapping("/api/shipment-sigmas")
public class ShipmentSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShipmentSigmaResource.class);

    private static final String ENTITY_NAME = "shipmentSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentSigmaService shipmentSigmaService;

    private final ShipmentSigmaRepository shipmentSigmaRepository;

    private final ShipmentSigmaQueryService shipmentSigmaQueryService;

    public ShipmentSigmaResource(
        ShipmentSigmaService shipmentSigmaService,
        ShipmentSigmaRepository shipmentSigmaRepository,
        ShipmentSigmaQueryService shipmentSigmaQueryService
    ) {
        this.shipmentSigmaService = shipmentSigmaService;
        this.shipmentSigmaRepository = shipmentSigmaRepository;
        this.shipmentSigmaQueryService = shipmentSigmaQueryService;
    }

    /**
     * {@code POST  /shipment-sigmas} : Create a new shipmentSigma.
     *
     * @param shipmentSigmaDTO the shipmentSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shipmentSigmaDTO, or with status {@code 400 (Bad Request)} if the shipmentSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShipmentSigmaDTO> createShipmentSigma(@Valid @RequestBody ShipmentSigmaDTO shipmentSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ShipmentSigma : {}", shipmentSigmaDTO);
        if (shipmentSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new shipmentSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        shipmentSigmaDTO = shipmentSigmaService.save(shipmentSigmaDTO);
        return ResponseEntity.created(new URI("/api/shipment-sigmas/" + shipmentSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, shipmentSigmaDTO.getId().toString()))
            .body(shipmentSigmaDTO);
    }

    /**
     * {@code PUT  /shipment-sigmas/:id} : Updates an existing shipmentSigma.
     *
     * @param id the id of the shipmentSigmaDTO to save.
     * @param shipmentSigmaDTO the shipmentSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipmentSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShipmentSigmaDTO> updateShipmentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShipmentSigmaDTO shipmentSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ShipmentSigma : {}, {}", id, shipmentSigmaDTO);
        if (shipmentSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        shipmentSigmaDTO = shipmentSigmaService.update(shipmentSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentSigmaDTO.getId().toString()))
            .body(shipmentSigmaDTO);
    }

    /**
     * {@code PATCH  /shipment-sigmas/:id} : Partial updates given fields of an existing shipmentSigma, field will ignore if it is null
     *
     * @param id the id of the shipmentSigmaDTO to save.
     * @param shipmentSigmaDTO the shipmentSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shipmentSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the shipmentSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shipmentSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shipmentSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShipmentSigmaDTO> partialUpdateShipmentSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShipmentSigmaDTO shipmentSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ShipmentSigma partially : {}, {}", id, shipmentSigmaDTO);
        if (shipmentSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shipmentSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shipmentSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShipmentSigmaDTO> result = shipmentSigmaService.partialUpdate(shipmentSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shipmentSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shipment-sigmas} : get all the shipmentSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shipmentSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShipmentSigmaDTO>> getAllShipmentSigmas(
        ShipmentSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ShipmentSigmas by criteria: {}", criteria);

        Page<ShipmentSigmaDTO> page = shipmentSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shipment-sigmas/count} : count all the shipmentSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShipmentSigmas(ShipmentSigmaCriteria criteria) {
        LOG.debug("REST request to count ShipmentSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(shipmentSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shipment-sigmas/:id} : get the "id" shipmentSigma.
     *
     * @param id the id of the shipmentSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shipmentSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShipmentSigmaDTO> getShipmentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ShipmentSigma : {}", id);
        Optional<ShipmentSigmaDTO> shipmentSigmaDTO = shipmentSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shipmentSigmaDTO);
    }

    /**
     * {@code DELETE  /shipment-sigmas/:id} : delete the "id" shipmentSigma.
     *
     * @param id the id of the shipmentSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipmentSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ShipmentSigma : {}", id);
        shipmentSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
