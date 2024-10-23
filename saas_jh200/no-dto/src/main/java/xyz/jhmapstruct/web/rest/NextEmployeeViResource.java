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
import xyz.jhmapstruct.domain.NextEmployeeVi;
import xyz.jhmapstruct.repository.NextEmployeeViRepository;
import xyz.jhmapstruct.service.NextEmployeeViQueryService;
import xyz.jhmapstruct.service.NextEmployeeViService;
import xyz.jhmapstruct.service.criteria.NextEmployeeViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeVi}.
 */
@RestController
@RequestMapping("/api/next-employee-vis")
public class NextEmployeeViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeViResource.class);

    private static final String ENTITY_NAME = "nextEmployeeVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeViService nextEmployeeViService;

    private final NextEmployeeViRepository nextEmployeeViRepository;

    private final NextEmployeeViQueryService nextEmployeeViQueryService;

    public NextEmployeeViResource(
        NextEmployeeViService nextEmployeeViService,
        NextEmployeeViRepository nextEmployeeViRepository,
        NextEmployeeViQueryService nextEmployeeViQueryService
    ) {
        this.nextEmployeeViService = nextEmployeeViService;
        this.nextEmployeeViRepository = nextEmployeeViRepository;
        this.nextEmployeeViQueryService = nextEmployeeViQueryService;
    }

    /**
     * {@code POST  /next-employee-vis} : Create a new nextEmployeeVi.
     *
     * @param nextEmployeeVi the nextEmployeeVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeVi, or with status {@code 400 (Bad Request)} if the nextEmployeeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeVi> createNextEmployeeVi(@Valid @RequestBody NextEmployeeVi nextEmployeeVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeVi : {}", nextEmployeeVi);
        if (nextEmployeeVi.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeVi = nextEmployeeViService.save(nextEmployeeVi);
        return ResponseEntity.created(new URI("/api/next-employee-vis/" + nextEmployeeVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeVi.getId().toString()))
            .body(nextEmployeeVi);
    }

    /**
     * {@code PUT  /next-employee-vis/:id} : Updates an existing nextEmployeeVi.
     *
     * @param id the id of the nextEmployeeVi to save.
     * @param nextEmployeeVi the nextEmployeeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeVi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeVi> updateNextEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeVi nextEmployeeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeVi : {}, {}", id, nextEmployeeVi);
        if (nextEmployeeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeVi = nextEmployeeViService.update(nextEmployeeVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeVi.getId().toString()))
            .body(nextEmployeeVi);
    }

    /**
     * {@code PATCH  /next-employee-vis/:id} : Partial updates given fields of an existing nextEmployeeVi, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeVi to save.
     * @param nextEmployeeVi the nextEmployeeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeVi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeVi> partialUpdateNextEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeVi nextEmployeeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeVi partially : {}, {}", id, nextEmployeeVi);
        if (nextEmployeeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeVi> result = nextEmployeeViService.partialUpdate(nextEmployeeVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-vis} : get all the nextEmployeeVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeVi>> getAllNextEmployeeVis(
        NextEmployeeViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeVis by criteria: {}", criteria);

        Page<NextEmployeeVi> page = nextEmployeeViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-vis/count} : count all the nextEmployeeVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeVis(NextEmployeeViCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-vis/:id} : get the "id" nextEmployeeVi.
     *
     * @param id the id of the nextEmployeeVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeVi> getNextEmployeeVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeVi : {}", id);
        Optional<NextEmployeeVi> nextEmployeeVi = nextEmployeeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeVi);
    }

    /**
     * {@code DELETE  /next-employee-vis/:id} : delete the "id" nextEmployeeVi.
     *
     * @param id the id of the nextEmployeeVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeVi : {}", id);
        nextEmployeeViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
