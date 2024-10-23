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
import xyz.jhmapstruct.domain.InvoiceViVi;
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.InvoiceViViQueryService;
import xyz.jhmapstruct.service.InvoiceViViService;
import xyz.jhmapstruct.service.criteria.InvoiceViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceViVi}.
 */
@RestController
@RequestMapping("/api/invoice-vi-vis")
public class InvoiceViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViViResource.class);

    private static final String ENTITY_NAME = "invoiceViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceViViService invoiceViViService;

    private final InvoiceViViRepository invoiceViViRepository;

    private final InvoiceViViQueryService invoiceViViQueryService;

    public InvoiceViViResource(
        InvoiceViViService invoiceViViService,
        InvoiceViViRepository invoiceViViRepository,
        InvoiceViViQueryService invoiceViViQueryService
    ) {
        this.invoiceViViService = invoiceViViService;
        this.invoiceViViRepository = invoiceViViRepository;
        this.invoiceViViQueryService = invoiceViViQueryService;
    }

    /**
     * {@code POST  /invoice-vi-vis} : Create a new invoiceViVi.
     *
     * @param invoiceViVi the invoiceViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceViVi, or with status {@code 400 (Bad Request)} if the invoiceViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceViVi> createInvoiceViVi(@Valid @RequestBody InvoiceViVi invoiceViVi) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceViVi : {}", invoiceViVi);
        if (invoiceViVi.getId() != null) {
            throw new BadRequestAlertException("A new invoiceViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceViVi = invoiceViViService.save(invoiceViVi);
        return ResponseEntity.created(new URI("/api/invoice-vi-vis/" + invoiceViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceViVi.getId().toString()))
            .body(invoiceViVi);
    }

    /**
     * {@code PUT  /invoice-vi-vis/:id} : Updates an existing invoiceViVi.
     *
     * @param id the id of the invoiceViVi to save.
     * @param invoiceViVi the invoiceViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViVi,
     * or with status {@code 400 (Bad Request)} if the invoiceViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceViVi> updateInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceViVi invoiceViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceViVi : {}, {}", id, invoiceViVi);
        if (invoiceViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceViVi = invoiceViViService.update(invoiceViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViVi.getId().toString()))
            .body(invoiceViVi);
    }

    /**
     * {@code PATCH  /invoice-vi-vis/:id} : Partial updates given fields of an existing invoiceViVi, field will ignore if it is null
     *
     * @param id the id of the invoiceViVi to save.
     * @param invoiceViVi the invoiceViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViVi,
     * or with status {@code 400 (Bad Request)} if the invoiceViVi is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceViVi> partialUpdateInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceViVi invoiceViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceViVi partially : {}, {}", id, invoiceViVi);
        if (invoiceViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceViVi> result = invoiceViViService.partialUpdate(invoiceViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-vi-vis} : get all the invoiceViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceViVi>> getAllInvoiceViVis(
        InvoiceViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceViVis by criteria: {}", criteria);

        Page<InvoiceViVi> page = invoiceViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-vi-vis/count} : count all the invoiceViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceViVis(InvoiceViViCriteria criteria) {
        LOG.debug("REST request to count InvoiceViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-vi-vis/:id} : get the "id" invoiceViVi.
     *
     * @param id the id of the invoiceViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceViVi> getInvoiceViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceViVi : {}", id);
        Optional<InvoiceViVi> invoiceViVi = invoiceViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceViVi);
    }

    /**
     * {@code DELETE  /invoice-vi-vis/:id} : delete the "id" invoiceViVi.
     *
     * @param id the id of the invoiceViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceViVi : {}", id);
        invoiceViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
