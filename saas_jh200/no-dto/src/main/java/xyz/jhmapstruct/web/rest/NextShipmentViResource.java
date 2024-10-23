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
import xyz.jhmapstruct.domain.NextShipmentVi;
import xyz.jhmapstruct.repository.NextShipmentViRepository;
import xyz.jhmapstruct.service.NextShipmentViQueryService;
import xyz.jhmapstruct.service.NextShipmentViService;
import xyz.jhmapstruct.service.criteria.NextShipmentViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextShipmentVi}.
 */
@RestController
@RequestMapping("/api/next-shipment-vis")
public class NextShipmentViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextShipmentViResource.class);

    private static final String ENTITY_NAME = "nextShipmentVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextShipmentViService nextShipmentViService;

    private final NextShipmentViRepository nextShipmentViRepository;

    private final NextShipmentViQueryService nextShipmentViQueryService;

    public NextShipmentViResource(
        NextShipmentViService nextShipmentViService,
        NextShipmentViRepository nextShipmentViRepository,
        NextShipmentViQueryService nextShipmentViQueryService
    ) {
        this.nextShipmentViService = nextShipmentViService;
        this.nextShipmentViRepository = nextShipmentViRepository;
        this.nextShipmentViQueryService = nextShipmentViQueryService;
    }

    /**
     * {@code POST  /next-shipment-vis} : Create a new nextShipmentVi.
     *
     * @param nextShipmentVi the nextShipmentVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextShipmentVi, or with status {@code 400 (Bad Request)} if the nextShipmentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextShipmentVi> createNextShipmentVi(@Valid @RequestBody NextShipmentVi nextShipmentVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextShipmentVi : {}", nextShipmentVi);
        if (nextShipmentVi.getId() != null) {
            throw new BadRequestAlertException("A new nextShipmentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextShipmentVi = nextShipmentViService.save(nextShipmentVi);
        return ResponseEntity.created(new URI("/api/next-shipment-vis/" + nextShipmentVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextShipmentVi.getId().toString()))
            .body(nextShipmentVi);
    }

    /**
     * {@code PUT  /next-shipment-vis/:id} : Updates an existing nextShipmentVi.
     *
     * @param id the id of the nextShipmentVi to save.
     * @param nextShipmentVi the nextShipmentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentVi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextShipmentVi> updateNextShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextShipmentVi nextShipmentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextShipmentVi : {}, {}", id, nextShipmentVi);
        if (nextShipmentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextShipmentVi = nextShipmentViService.update(nextShipmentVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentVi.getId().toString()))
            .body(nextShipmentVi);
    }

    /**
     * {@code PATCH  /next-shipment-vis/:id} : Partial updates given fields of an existing nextShipmentVi, field will ignore if it is null
     *
     * @param id the id of the nextShipmentVi to save.
     * @param nextShipmentVi the nextShipmentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextShipmentVi,
     * or with status {@code 400 (Bad Request)} if the nextShipmentVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextShipmentVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextShipmentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextShipmentVi> partialUpdateNextShipmentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextShipmentVi nextShipmentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextShipmentVi partially : {}, {}", id, nextShipmentVi);
        if (nextShipmentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextShipmentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextShipmentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextShipmentVi> result = nextShipmentViService.partialUpdate(nextShipmentVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextShipmentVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-shipment-vis} : get all the nextShipmentVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextShipmentVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextShipmentVi>> getAllNextShipmentVis(
        NextShipmentViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextShipmentVis by criteria: {}", criteria);

        Page<NextShipmentVi> page = nextShipmentViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-shipment-vis/count} : count all the nextShipmentVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextShipmentVis(NextShipmentViCriteria criteria) {
        LOG.debug("REST request to count NextShipmentVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextShipmentViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-shipment-vis/:id} : get the "id" nextShipmentVi.
     *
     * @param id the id of the nextShipmentVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextShipmentVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextShipmentVi> getNextShipmentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextShipmentVi : {}", id);
        Optional<NextShipmentVi> nextShipmentVi = nextShipmentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextShipmentVi);
    }

    /**
     * {@code DELETE  /next-shipment-vis/:id} : delete the "id" nextShipmentVi.
     *
     * @param id the id of the nextShipmentVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextShipmentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextShipmentVi : {}", id);
        nextShipmentViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
