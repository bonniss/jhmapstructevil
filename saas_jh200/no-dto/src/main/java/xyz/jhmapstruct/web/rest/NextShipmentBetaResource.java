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
import xyz.jhmapstruct.domain.NextShipmentBeta;
import xyz.jhmapstruct.repository.NextShipmentBetaRepository;
import xyz.jhmapstruct.service.NextShipmentBetaQueryService;
import xyz.jhmapstruct.service.NextShipmentBetaService;
import xyz.jhmapstruct.service.criteria.NextShipmentBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentBeta}.
 */
@RestController
@RequestMapping("/api/next-shipment-betas")
public class NextShipmentBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentBetaResource.class);

    private static final String ENTITY_NAME = "nextShipmentBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentBetaService nextShipmentBetaService;

    private final NextShipmentBetaRepository nextShipmentBetaRepository;

    private final NextShipmentBetaQueryService nextShipmentBetaQueryService;

    public NextShipmentBetaResource(
        NextShipmentBetaService nextShipmentBetaService,
        NextShipmentBetaRepository nextShipmentBetaRepository,
        NextShipmentBetaQueryService nextShipmentBetaQueryService
    ) {
        this.nextShipmentBetaService = nextShipmentBetaService;
        this.nextShipmentBetaRepository = nextShipmentBetaRepository;
        this.nextShipmentBetaQueryService = nextShipmentBetaQueryService;
    }

    /**
     * {@code POST  /next-shipment-betas} : Create a new nextShipmentBeta.
     *
     * @param nextShipmentBeta the nextShipmentBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentBeta, or with status {@code 400 (Bad Request)} if the nextShipmentBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentBeta> createNextShipmentBeta(@Valid @RequestBody NextShipmentBeta nextShipmentBeta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentBeta : {}", nextShipmentBeta);
        if (nextShipmentBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentBeta = nextShipmentBetaService.save(nextShipmentBeta);
        return ResponseEntity.created(new URI("/api/next-shipment-betas/" + nextShipmentBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentBeta.getId().toString()))
            .body(nextShipmentBeta);
    }

    /**
     * {@code PUT  /next-shipment-betas/:id} : Updates an existing nextShipmentBeta.
     *
     * @param id the id of the nextShipmentBeta to save.
     * @param nextShipmentBeta the nextShipmentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentBeta,
     * or with status {@code 400 (Bad Request)} if the nextShipmentBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentBeta> updateNextShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentBeta nextShipmentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentBeta : {}, {}", id, nextShipmentBeta);
        if (nextShipmentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentBeta = nextShipmentBetaService.update(nextShipmentBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentBeta.getId().toString()))
            .body(nextShipmentBeta);
    }

    /**
     * {@code PATCH  /next-shipment-betas/:id} : Partial updates given fields of an existing nextShipmentBeta, field will ignore if it is null
     *
     * @param id the id of the nextShipmentBeta to save.
     * @param nextShipmentBeta the nextShipmentBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentBeta,
     * or with status {@code 400 (Bad Request)} if the nextShipmentBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentBeta> partialUpdateNextShipmentBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentBeta nextShipmentBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentBeta partially : {}, {}", id, nextShipmentBeta);
        if (nextShipmentBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentBeta> result = nextShipmentBetaService.partialUpdate(nextShipmentBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-betas} : get all the nextShipmentBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentBeta>> getAllNextShipmentBetas(
        NextShipmentBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentBetas by criteria: {}", criteria);

        Page<NextShipmentBeta> page = nextShipmentBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-betas/count} : count all the nextShipmentBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentBetas(NextShipmentBetaCriteria criteria) {
        LOG.debug("REST request to count NextShipmentBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-betas/:id} : get the "id" nextShipmentBeta.
     *
     * @param id the id of the nextShipmentBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentBeta> getNextShipmentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentBeta : {}", id);
        Optional<NextShipmentBeta> nextShipmentBeta = nextShipmentBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentBeta);
    }

    /**
     * {@code DELETE  /next-shipment-betas/:id} : delete the "id" nextShipmentBeta.
     *
     * @param id the id of the nextShipmentBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentBeta : {}", id);
        nextShipmentBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
