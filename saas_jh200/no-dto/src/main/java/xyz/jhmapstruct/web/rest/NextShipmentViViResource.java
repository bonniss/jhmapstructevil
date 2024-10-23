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
import xyz.jhmapstruct.domain.NextShipmentViVi;
import xyz.jhmapstruct.repository.NextShipmentViViRepository;
import xyz.jhmapstruct.service.NextShipmentViViQueryService;
import xyz.jhmapstruct.service.NextShipmentViViService;
import xyz.jhmapstruct.service.criteria.NextShipmentViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentViVi}.
 */
@RestController
@RequestMapping("/api/next-shipment-vi-vis")
public class NextShipmentViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViViResource.class);

    private static final String ENTITY_NAME = "nextShipmentViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentViViService nextShipmentViViService;

    private final NextShipmentViViRepository nextShipmentViViRepository;

    private final NextShipmentViViQueryService nextShipmentViViQueryService;

    public NextShipmentViViResource(
        NextShipmentViViService nextShipmentViViService,
        NextShipmentViViRepository nextShipmentViViRepository,
        NextShipmentViViQueryService nextShipmentViViQueryService
    ) {
        this.nextShipmentViViService = nextShipmentViViService;
        this.nextShipmentViViRepository = nextShipmentViViRepository;
        this.nextShipmentViViQueryService = nextShipmentViViQueryService;
    }

    /**
     * {@code POST  /next-shipment-vi-vis} : Create a new nextShipmentViVi.
     *
     * @param nextShipmentViVi the nextShipmentViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentViVi, or with status {@code 400 (Bad Request)} if the nextShipmentViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentViVi> createNextShipmentViVi(@Valid @RequestBody NextShipmentViVi nextShipmentViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentViVi : {}", nextShipmentViVi);
        if (nextShipmentViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentViVi = nextShipmentViViService.save(nextShipmentViVi);
        return ResponseEntity.created(new URI("/api/next-shipment-vi-vis/" + nextShipmentViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentViVi.getId().toString()))
            .body(nextShipmentViVi);
    }

    /**
     * {@code PUT  /next-shipment-vi-vis/:id} : Updates an existing nextShipmentViVi.
     *
     * @param id the id of the nextShipmentViVi to save.
     * @param nextShipmentViVi the nextShipmentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentViVi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentViVi> updateNextShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentViVi nextShipmentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentViVi : {}, {}", id, nextShipmentViVi);
        if (nextShipmentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentViVi = nextShipmentViViService.update(nextShipmentViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentViVi.getId().toString()))
            .body(nextShipmentViVi);
    }

    /**
     * {@code PATCH  /next-shipment-vi-vis/:id} : Partial updates given fields of an existing nextShipmentViVi, field will ignore if it is null
     *
     * @param id the id of the nextShipmentViVi to save.
     * @param nextShipmentViVi the nextShipmentViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentViVi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentViVi> partialUpdateNextShipmentViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentViVi nextShipmentViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentViVi partially : {}, {}", id, nextShipmentViVi);
        if (nextShipmentViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentViVi> result = nextShipmentViViService.partialUpdate(nextShipmentViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-vi-vis} : get all the nextShipmentViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentViVi>> getAllNextShipmentViVis(
        NextShipmentViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentViVis by criteria: {}", criteria);

        Page<NextShipmentViVi> page = nextShipmentViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-vi-vis/count} : count all the nextShipmentViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentViVis(NextShipmentViViCriteria criteria) {
        LOG.debug("REST request to count NextShipmentViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-vi-vis/:id} : get the "id" nextShipmentViVi.
     *
     * @param id the id of the nextShipmentViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentViVi> getNextShipmentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentViVi : {}", id);
        Optional<NextShipmentViVi> nextShipmentViVi = nextShipmentViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentViVi);
    }

    /**
     * {@code DELETE  /next-shipment-vi-vis/:id} : delete the "id" nextShipmentViVi.
     *
     * @param id the id of the nextShipmentViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentViVi : {}", id);
        nextShipmentViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
