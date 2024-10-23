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
import xyz.jhmapstruct.domain.NextCustomerViVi;
import xyz.jhmapstruct.repository.NextCustomerViViRepository;
import xyz.jhmapstruct.service.NextCustomerViViQueryService;
import xyz.jhmapstruct.service.NextCustomerViViService;
import xyz.jhmapstruct.service.criteria.NextCustomerViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerViVi}.
 */
@RestController
@RequestMapping("/api/next-customer-vi-vis")
public class NextCustomerViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerViViResource.class);

    private static final String ENTITY_NAME = "nextCustomerViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerViViService nextCustomerViViService;

    private final NextCustomerViViRepository nextCustomerViViRepository;

    private final NextCustomerViViQueryService nextCustomerViViQueryService;

    public NextCustomerViViResource(
        NextCustomerViViService nextCustomerViViService,
        NextCustomerViViRepository nextCustomerViViRepository,
        NextCustomerViViQueryService nextCustomerViViQueryService
    ) {
        this.nextCustomerViViService = nextCustomerViViService;
        this.nextCustomerViViRepository = nextCustomerViViRepository;
        this.nextCustomerViViQueryService = nextCustomerViViQueryService;
    }

    /**
     * {@code POST  /next-customer-vi-vis} : Create a new nextCustomerViVi.
     *
     * @param nextCustomerViVi the nextCustomerViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerViVi, or with status {@code 400 (Bad Request)} if the nextCustomerViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerViVi> createNextCustomerViVi(@Valid @RequestBody NextCustomerViVi nextCustomerViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerViVi : {}", nextCustomerViVi);
        if (nextCustomerViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerViVi = nextCustomerViViService.save(nextCustomerViVi);
        return ResponseEntity.created(new URI("/api/next-customer-vi-vis/" + nextCustomerViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerViVi.getId().toString()))
            .body(nextCustomerViVi);
    }

    /**
     * {@code PUT  /next-customer-vi-vis/:id} : Updates an existing nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViVi to save.
     * @param nextCustomerViVi the nextCustomerViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerViVi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerViVi> updateNextCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerViVi nextCustomerViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerViVi : {}, {}", id, nextCustomerViVi);
        if (nextCustomerViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerViVi = nextCustomerViViService.update(nextCustomerViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerViVi.getId().toString()))
            .body(nextCustomerViVi);
    }

    /**
     * {@code PATCH  /next-customer-vi-vis/:id} : Partial updates given fields of an existing nextCustomerViVi, field will ignore if it is null
     *
     * @param id the id of the nextCustomerViVi to save.
     * @param nextCustomerViVi the nextCustomerViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerViVi,
     * or with status {@code 400 (Bad Request)} if the nextCustomerViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerViVi> partialUpdateNextCustomerViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerViVi nextCustomerViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerViVi partially : {}, {}", id, nextCustomerViVi);
        if (nextCustomerViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerViVi> result = nextCustomerViViService.partialUpdate(nextCustomerViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-vi-vis} : get all the nextCustomerViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerViVi>> getAllNextCustomerViVis(
        NextCustomerViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerViVis by criteria: {}", criteria);

        Page<NextCustomerViVi> page = nextCustomerViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-vi-vis/count} : count all the nextCustomerViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerViVis(NextCustomerViViCriteria criteria) {
        LOG.debug("REST request to count NextCustomerViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-vi-vis/:id} : get the "id" nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerViVi> getNextCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerViVi : {}", id);
        Optional<NextCustomerViVi> nextCustomerViVi = nextCustomerViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerViVi);
    }

    /**
     * {@code DELETE  /next-customer-vi-vis/:id} : delete the "id" nextCustomerViVi.
     *
     * @param id the id of the nextCustomerViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerViVi : {}", id);
        nextCustomerViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
