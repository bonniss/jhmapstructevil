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
import xyz.jhmapstruct.domain.NextEmployeeViVi;
import xyz.jhmapstruct.repository.NextEmployeeViViRepository;
import xyz.jhmapstruct.service.NextEmployeeViViQueryService;
import xyz.jhmapstruct.service.NextEmployeeViViService;
import xyz.jhmapstruct.service.criteria.NextEmployeeViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeViVi}.
 */
@RestController
@RequestMapping("/api/next-employee-vi-vis")
public class NextEmployeeViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViViResource.class);

    private static final String ENTITY_NAME = "nextEmployeeViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeViViService nextEmployeeViViService;

    private final NextEmployeeViViRepository nextEmployeeViViRepository;

    private final NextEmployeeViViQueryService nextEmployeeViViQueryService;

    public NextEmployeeViViResource(
        NextEmployeeViViService nextEmployeeViViService,
        NextEmployeeViViRepository nextEmployeeViViRepository,
        NextEmployeeViViQueryService nextEmployeeViViQueryService
    ) {
        this.nextEmployeeViViService = nextEmployeeViViService;
        this.nextEmployeeViViRepository = nextEmployeeViViRepository;
        this.nextEmployeeViViQueryService = nextEmployeeViViQueryService;
    }

    /**
     * {@code POST  /next-employee-vi-vis} : Create a new nextEmployeeViVi.
     *
     * @param nextEmployeeViVi the nextEmployeeViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeViVi, or with status {@code 400 (Bad Request)} if the nextEmployeeViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeViVi> createNextEmployeeViVi(@Valid @RequestBody NextEmployeeViVi nextEmployeeViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeViVi : {}", nextEmployeeViVi);
        if (nextEmployeeViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeViVi = nextEmployeeViViService.save(nextEmployeeViVi);
        return ResponseEntity.created(new URI("/api/next-employee-vi-vis/" + nextEmployeeViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeViVi.getId().toString()))
            .body(nextEmployeeViVi);
    }

    /**
     * {@code PUT  /next-employee-vi-vis/:id} : Updates an existing nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViVi to save.
     * @param nextEmployeeViVi the nextEmployeeViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeViVi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeViVi> updateNextEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeViVi nextEmployeeViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeViVi : {}, {}", id, nextEmployeeViVi);
        if (nextEmployeeViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeViVi = nextEmployeeViViService.update(nextEmployeeViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeViVi.getId().toString()))
            .body(nextEmployeeViVi);
    }

    /**
     * {@code PATCH  /next-employee-vi-vis/:id} : Partial updates given fields of an existing nextEmployeeViVi, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeViVi to save.
     * @param nextEmployeeViVi the nextEmployeeViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeViVi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeViVi> partialUpdateNextEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeViVi nextEmployeeViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeViVi partially : {}, {}", id, nextEmployeeViVi);
        if (nextEmployeeViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeViVi> result = nextEmployeeViViService.partialUpdate(nextEmployeeViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-vi-vis} : get all the nextEmployeeViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeViVi>> getAllNextEmployeeViVis(
        NextEmployeeViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeViVis by criteria: {}", criteria);

        Page<NextEmployeeViVi> page = nextEmployeeViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-vi-vis/count} : count all the nextEmployeeViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeViVis(NextEmployeeViViCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-vi-vis/:id} : get the "id" nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeViVi> getNextEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeViVi : {}", id);
        Optional<NextEmployeeViVi> nextEmployeeViVi = nextEmployeeViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeViVi);
    }

    /**
     * {@code DELETE  /next-employee-vi-vis/:id} : delete the "id" nextEmployeeViVi.
     *
     * @param id the id of the nextEmployeeViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeViVi : {}", id);
        nextEmployeeViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
