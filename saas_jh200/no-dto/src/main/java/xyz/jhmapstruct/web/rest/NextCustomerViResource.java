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
import xyz.jhmapstruct.domain.NextCustomerVi;
import xyz.jhmapstruct.repository.NextCustomerViRepository;
import xyz.jhmapstruct.service.NextCustomerViQueryService;
import xyz.jhmapstruct.service.NextCustomerViService;
import xyz.jhmapstruct.service.criteria.NextCustomerViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerVi}.
 */
@RestController
@RequestMapping("/api/next-customer-vis")
public class NextCustomerViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViResource.class);

    private static final String ENTITY_NAME = "nextCustomerVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerViService nextCustomerViService;

    private final NextCustomerViRepository nextCustomerViRepository;

    private final NextCustomerViQueryService nextCustomerViQueryService;

    public NextCustomerViResource(
        NextCustomerViService nextCustomerViService,
        NextCustomerViRepository nextCustomerViRepository,
        NextCustomerViQueryService nextCustomerViQueryService
    ) {
        this.nextCustomerViService = nextCustomerViService;
        this.nextCustomerViRepository = nextCustomerViRepository;
        this.nextCustomerViQueryService = nextCustomerViQueryService;
    }

    /**
     * {@code POST  /next-customer-vis} : Create a new nextCustomerVi.
     *
     * @param nextCustomerVi the nextCustomerVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerVi, or with status {@code 400 (Bad Request)} if the nextCustomerVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerVi> createNextCustomerVi(@Valid @RequestBody NextCustomerVi nextCustomerVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerVi : {}", nextCustomerVi);
        if (nextCustomerVi.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerVi = nextCustomerViService.save(nextCustomerVi);
        return ResponseEntity.created(new URI("/api/next-customer-vis/" + nextCustomerVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerVi.getId().toString()))
            .body(nextCustomerVi);
    }

    /**
     * {@code PUT  /next-customer-vis/:id} : Updates an existing nextCustomerVi.
     *
     * @param id the id of the nextCustomerVi to save.
     * @param nextCustomerVi the nextCustomerVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerVi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerVi> updateNextCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerVi nextCustomerVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerVi : {}, {}", id, nextCustomerVi);
        if (nextCustomerVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerVi = nextCustomerViService.update(nextCustomerVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerVi.getId().toString()))
            .body(nextCustomerVi);
    }

    /**
     * {@code PATCH  /next-customer-vis/:id} : Partial updates given fields of an existing nextCustomerVi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerVi to save.
     * @param nextCustomerVi the nextCustomerVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerVi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerVi> partialUpdateNextCustomerVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerVi nextCustomerVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerVi partially : {}, {}", id, nextCustomerVi);
        if (nextCustomerVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerVi> result = nextCustomerViService.partialUpdate(nextCustomerVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-vis} : get all the nextCustomerVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerVi>> getAllNextCustomerVis(
        NextCustomerViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerVis by criteria: {}", criteria);

        Page<NextCustomerVi> page = nextCustomerViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-vis/count} : count all the nextCustomerVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerVis(NextCustomerViCriteria criteria) {
        LOG.debug("REST request to count NextCustomerVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-vis/:id} : get the "id" nextCustomerVi.
     *
     * @param id the id of the nextCustomerVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerVi> getNextCustomerVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerVi : {}", id);
        Optional<NextCustomerVi> nextCustomerVi = nextCustomerViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerVi);
    }

    /**
     * {@code DELETE  /next-customer-vis/:id} : delete the "id" nextCustomerVi.
     *
     * @param id the id of the nextCustomerVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerVi : {}", id);
        nextCustomerViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
