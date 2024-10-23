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
import xyz.jhmapstruct.domain.NextShipmentTheta;
import xyz.jhmapstruct.repository.NextShipmentThetaRepository;
import xyz.jhmapstruct.service.NextShipmentThetaQueryService;
import xyz.jhmapstruct.service.NextShipmentThetaService;
import xyz.jhmapstruct.service.criteria.NextShipmentThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentTheta}.
 */
@RestController
@RequestMapping("/api/next-shipment-thetas")
public class NextShipmentThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentThetaResource.class);

    private static final String ENTITY_NAME = "nextShipmentTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentThetaService nextShipmentThetaService;

    private final NextShipmentThetaRepository nextShipmentThetaRepository;

    private final NextShipmentThetaQueryService nextShipmentThetaQueryService;

    public NextShipmentThetaResource(
        NextShipmentThetaService nextShipmentThetaService,
        NextShipmentThetaRepository nextShipmentThetaRepository,
        NextShipmentThetaQueryService nextShipmentThetaQueryService
    ) {
        this.nextShipmentThetaService = nextShipmentThetaService;
        this.nextShipmentThetaRepository = nextShipmentThetaRepository;
        this.nextShipmentThetaQueryService = nextShipmentThetaQueryService;
    }

    /**
     * {@code POST  /next-shipment-thetas} : Create a new nextShipmentTheta.
     *
     * @param nextShipmentTheta the nextShipmentTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentTheta, or with status {@code 400 (Bad Request)} if the nextShipmentTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentTheta> createNextShipmentTheta(@Valid @RequestBody NextShipmentTheta nextShipmentTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentTheta : {}", nextShipmentTheta);
        if (nextShipmentTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentTheta = nextShipmentThetaService.save(nextShipmentTheta);
        return ResponseEntity.created(new URI("/api/next-shipment-thetas/" + nextShipmentTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentTheta.getId().toString()))
            .body(nextShipmentTheta);
    }

    /**
     * {@code PUT  /next-shipment-thetas/:id} : Updates an existing nextShipmentTheta.
     *
     * @param id the id of the nextShipmentTheta to save.
     * @param nextShipmentTheta the nextShipmentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentTheta,
     * or with status {@code 400 (Bad Request)} if the nextShipmentTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentTheta> updateNextShipmentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentTheta nextShipmentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentTheta : {}, {}", id, nextShipmentTheta);
        if (nextShipmentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentTheta = nextShipmentThetaService.update(nextShipmentTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentTheta.getId().toString()))
            .body(nextShipmentTheta);
    }

    /**
     * {@code PATCH  /next-shipment-thetas/:id} : Partial updates given fields of an existing nextShipmentTheta, field will ignore if it is null
     *
     * @param id the id of the nextShipmentTheta to save.
     * @param nextShipmentTheta the nextShipmentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentTheta,
     * or with status {@code 400 (Bad Request)} if the nextShipmentTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentTheta> partialUpdateNextShipmentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentTheta nextShipmentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentTheta partially : {}, {}", id, nextShipmentTheta);
        if (nextShipmentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentTheta> result = nextShipmentThetaService.partialUpdate(nextShipmentTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-thetas} : get all the nextShipmentThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentTheta>> getAllNextShipmentThetas(
        NextShipmentThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentThetas by criteria: {}", criteria);

        Page<NextShipmentTheta> page = nextShipmentThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-thetas/count} : count all the nextShipmentThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentThetas(NextShipmentThetaCriteria criteria) {
        LOG.debug("REST request to count NextShipmentThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-thetas/:id} : get the "id" nextShipmentTheta.
     *
     * @param id the id of the nextShipmentTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentTheta> getNextShipmentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentTheta : {}", id);
        Optional<NextShipmentTheta> nextShipmentTheta = nextShipmentThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentTheta);
    }

    /**
     * {@code DELETE  /next-shipment-thetas/:id} : delete the "id" nextShipmentTheta.
     *
     * @param id the id of the nextShipmentTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentTheta : {}", id);
        nextShipmentThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
