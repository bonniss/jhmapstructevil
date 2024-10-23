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
import xyz.jhmapstruct.domain.NextInvoiceVi;
import xyz.jhmapstruct.repository.NextInvoiceViRepository;
import xyz.jhmapstruct.service.NextInvoiceViQueryService;
import xyz.jhmapstruct.service.NextInvoiceViService;
import xyz.jhmapstruct.service.criteria.NextInvoiceViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceVi}.
 */
@RestController
@RequestMapping("/api/next-invoice-vis")
public class NextInvoiceViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceViResource.class);

    private static final String ENTITY_NAME = "nextInvoiceVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceViService nextInvoiceViService;

    private final NextInvoiceViRepository nextInvoiceViRepository;

    private final NextInvoiceViQueryService nextInvoiceViQueryService;

    public NextInvoiceViResource(
        NextInvoiceViService nextInvoiceViService,
        NextInvoiceViRepository nextInvoiceViRepository,
        NextInvoiceViQueryService nextInvoiceViQueryService
    ) {
        this.nextInvoiceViService = nextInvoiceViService;
        this.nextInvoiceViRepository = nextInvoiceViRepository;
        this.nextInvoiceViQueryService = nextInvoiceViQueryService;
    }

    /**
     * {@code POST  /next-invoice-vis} : Create a new nextInvoiceVi.
     *
     * @param nextInvoiceVi the nextInvoiceVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceVi, or with status {@code 400 (Bad Request)} if the nextInvoiceVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceVi> createNextInvoiceVi(@Valid @RequestBody NextInvoiceVi nextInvoiceVi) throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceVi : {}", nextInvoiceVi);
        if (nextInvoiceVi.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceVi = nextInvoiceViService.save(nextInvoiceVi);
        return ResponseEntity.created(new URI("/api/next-invoice-vis/" + nextInvoiceVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceVi.getId().toString()))
            .body(nextInvoiceVi);
    }

    /**
     * {@code PUT  /next-invoice-vis/:id} : Updates an existing nextInvoiceVi.
     *
     * @param id the id of the nextInvoiceVi to save.
     * @param nextInvoiceVi the nextInvoiceVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceVi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceVi> updateNextInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceVi nextInvoiceVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceVi : {}, {}", id, nextInvoiceVi);
        if (nextInvoiceVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceVi = nextInvoiceViService.update(nextInvoiceVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceVi.getId().toString()))
            .body(nextInvoiceVi);
    }

    /**
     * {@code PATCH  /next-invoice-vis/:id} : Partial updates given fields of an existing nextInvoiceVi, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceVi to save.
     * @param nextInvoiceVi the nextInvoiceVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceVi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceVi> partialUpdateNextInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceVi nextInvoiceVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceVi partially : {}, {}", id, nextInvoiceVi);
        if (nextInvoiceVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceVi> result = nextInvoiceViService.partialUpdate(nextInvoiceVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-vis} : get all the nextInvoiceVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceVi>> getAllNextInvoiceVis(
        NextInvoiceViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceVis by criteria: {}", criteria);

        Page<NextInvoiceVi> page = nextInvoiceViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-vis/count} : count all the nextInvoiceVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceVis(NextInvoiceViCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-vis/:id} : get the "id" nextInvoiceVi.
     *
     * @param id the id of the nextInvoiceVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceVi> getNextInvoiceVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceVi : {}", id);
        Optional<NextInvoiceVi> nextInvoiceVi = nextInvoiceViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceVi);
    }

    /**
     * {@code DELETE  /next-invoice-vis/:id} : delete the "id" nextInvoiceVi.
     *
     * @param id the id of the nextInvoiceVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceVi : {}", id);
        nextInvoiceViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
