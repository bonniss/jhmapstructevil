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
import xyz.jhmapstruct.domain.NextInvoiceViVi;
import xyz.jhmapstruct.repository.NextInvoiceViViRepository;
import xyz.jhmapstruct.service.NextInvoiceViViQueryService;
import xyz.jhmapstruct.service.NextInvoiceViViService;
import xyz.jhmapstruct.service.criteria.NextInvoiceViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceViVi}.
 */
@RestController
@RequestMapping("/api/next-invoice-vi-vis")
public class NextInvoiceViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViViResource.class);

    private static final String ENTITY_NAME = "nextInvoiceViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceViViService nextInvoiceViViService;

    private final NextInvoiceViViRepository nextInvoiceViViRepository;

    private final NextInvoiceViViQueryService nextInvoiceViViQueryService;

    public NextInvoiceViViResource(
        NextInvoiceViViService nextInvoiceViViService,
        NextInvoiceViViRepository nextInvoiceViViRepository,
        NextInvoiceViViQueryService nextInvoiceViViQueryService
    ) {
        this.nextInvoiceViViService = nextInvoiceViViService;
        this.nextInvoiceViViRepository = nextInvoiceViViRepository;
        this.nextInvoiceViViQueryService = nextInvoiceViViQueryService;
    }

    /**
     * {@code POST  /next-invoice-vi-vis} : Create a new nextInvoiceViVi.
     *
     * @param nextInvoiceViVi the nextInvoiceViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceViVi, or with status {@code 400 (Bad Request)} if the nextInvoiceViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceViVi> createNextInvoiceViVi(@Valid @RequestBody NextInvoiceViVi nextInvoiceViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceViVi : {}", nextInvoiceViVi);
        if (nextInvoiceViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceViVi = nextInvoiceViViService.save(nextInvoiceViVi);
        return ResponseEntity.created(new URI("/api/next-invoice-vi-vis/" + nextInvoiceViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceViVi.getId().toString()))
            .body(nextInvoiceViVi);
    }

    /**
     * {@code PUT  /next-invoice-vi-vis/:id} : Updates an existing nextInvoiceViVi.
     *
     * @param id the id of the nextInvoiceViVi to save.
     * @param nextInvoiceViVi the nextInvoiceViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceViVi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceViVi> updateNextInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceViVi nextInvoiceViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceViVi : {}, {}", id, nextInvoiceViVi);
        if (nextInvoiceViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceViVi = nextInvoiceViViService.update(nextInvoiceViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceViVi.getId().toString()))
            .body(nextInvoiceViVi);
    }

    /**
     * {@code PATCH  /next-invoice-vi-vis/:id} : Partial updates given fields of an existing nextInvoiceViVi, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceViVi to save.
     * @param nextInvoiceViVi the nextInvoiceViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceViVi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceViVi> partialUpdateNextInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceViVi nextInvoiceViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceViVi partially : {}, {}", id, nextInvoiceViVi);
        if (nextInvoiceViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceViVi> result = nextInvoiceViViService.partialUpdate(nextInvoiceViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-vi-vis} : get all the nextInvoiceViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceViVi>> getAllNextInvoiceViVis(
        NextInvoiceViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceViVis by criteria: {}", criteria);

        Page<NextInvoiceViVi> page = nextInvoiceViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-vi-vis/count} : count all the nextInvoiceViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceViVis(NextInvoiceViViCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-vi-vis/:id} : get the "id" nextInvoiceViVi.
     *
     * @param id the id of the nextInvoiceViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceViVi> getNextInvoiceViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceViVi : {}", id);
        Optional<NextInvoiceViVi> nextInvoiceViVi = nextInvoiceViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceViVi);
    }

    /**
     * {@code DELETE  /next-invoice-vi-vis/:id} : delete the "id" nextInvoiceViVi.
     *
     * @param id the id of the nextInvoiceViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceViVi : {}", id);
        nextInvoiceViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
