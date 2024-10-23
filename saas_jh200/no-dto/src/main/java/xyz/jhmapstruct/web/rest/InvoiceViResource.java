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
import xyz.jhmapstruct.domain.InvoiceVi;
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.InvoiceViQueryService;
import xyz.jhmapstruct.service.InvoiceViService;
import xyz.jhmapstruct.service.criteria.InvoiceViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceVi}.
 */
@RestController
@RequestMapping("/api/invoice-vis")
public class InvoiceViResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceViResource.class);

    private static final String ENTITY_NAME = "invoiceVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceViService invoiceViService;

    private final InvoiceViRepository invoiceViRepository;

    private final InvoiceViQueryService invoiceViQueryService;

    public InvoiceViResource(
        InvoiceViService invoiceViService,
        InvoiceViRepository invoiceViRepository,
        InvoiceViQueryService invoiceViQueryService
    ) {
        this.invoiceViService = invoiceViService;
        this.invoiceViRepository = invoiceViRepository;
        this.invoiceViQueryService = invoiceViQueryService;
    }

    /**
     * {@code POST  /invoice-vis} : Create a new invoiceVi.
     *
     * @param invoiceVi the invoiceVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceVi, or with status {@code 400 (Bad Request)} if the invoiceVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceVi> createInvoiceVi(@Valid @RequestBody InvoiceVi invoiceVi) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceVi : {}", invoiceVi);
        if (invoiceVi.getId() != null) {
            throw new BadRequestAlertException("A new invoiceVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceVi = invoiceViService.save(invoiceVi);
        return ResponseEntity.created(new URI("/api/invoice-vis/" + invoiceVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceVi.getId().toString()))
            .body(invoiceVi);
    }

    /**
     * {@code PUT  /invoice-vis/:id} : Updates an existing invoiceVi.
     *
     * @param id the id of the invoiceVi to save.
     * @param invoiceVi the invoiceVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceVi,
     * or with status {@code 400 (Bad Request)} if the invoiceVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceVi> updateInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceVi invoiceVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceVi : {}, {}", id, invoiceVi);
        if (invoiceVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceVi = invoiceViService.update(invoiceVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceVi.getId().toString()))
            .body(invoiceVi);
    }

    /**
     * {@code PATCH  /invoice-vis/:id} : Partial updates given fields of an existing invoiceVi, field will ignore if it is null
     *
     * @param id the id of the invoiceVi to save.
     * @param invoiceVi the invoiceVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceVi,
     * or with status {@code 400 (Bad Request)} if the invoiceVi is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceVi> partialUpdateInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceVi invoiceVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceVi partially : {}, {}", id, invoiceVi);
        if (invoiceVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceVi> result = invoiceViService.partialUpdate(invoiceVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceVi.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-vis} : get all the invoiceVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceVi>> getAllInvoiceVis(
        InvoiceViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceVis by criteria: {}", criteria);

        Page<InvoiceVi> page = invoiceViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-vis/count} : count all the invoiceVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceVis(InvoiceViCriteria criteria) {
        LOG.debug("REST request to count InvoiceVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-vis/:id} : get the "id" invoiceVi.
     *
     * @param id the id of the invoiceVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceVi> getInvoiceVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceVi : {}", id);
        Optional<InvoiceVi> invoiceVi = invoiceViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceVi);
    }

    /**
     * {@code DELETE  /invoice-vis/:id} : delete the "id" invoiceVi.
     *
     * @param id the id of the invoiceVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceVi : {}", id);
        invoiceViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
