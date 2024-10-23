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
import xyz.jhmapstruct.domain.InvoiceBeta;
import xyz.jhmapstruct.repository.InvoiceBetaRepository;
import xyz.jhmapstruct.service.InvoiceBetaQueryService;
import xyz.jhmapstruct.service.InvoiceBetaService;
import xyz.jhmapstruct.service.criteria.InvoiceBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceBeta}.
 */
@RestController
@RequestMapping("/api/invoice-betas")
public class InvoiceBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBetaResource.class);

    private static final String ENTITY_NAME = "invoiceBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceBetaService invoiceBetaService;

    private final InvoiceBetaRepository invoiceBetaRepository;

    private final InvoiceBetaQueryService invoiceBetaQueryService;

    public InvoiceBetaResource(
        InvoiceBetaService invoiceBetaService,
        InvoiceBetaRepository invoiceBetaRepository,
        InvoiceBetaQueryService invoiceBetaQueryService
    ) {
        this.invoiceBetaService = invoiceBetaService;
        this.invoiceBetaRepository = invoiceBetaRepository;
        this.invoiceBetaQueryService = invoiceBetaQueryService;
    }

    /**
     * {@code POST  /invoice-betas} : Create a new invoiceBeta.
     *
     * @param invoiceBeta the invoiceBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceBeta, or with status {@code 400 (Bad Request)} if the invoiceBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceBeta> createInvoiceBeta(@Valid @RequestBody InvoiceBeta invoiceBeta) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceBeta : {}", invoiceBeta);
        if (invoiceBeta.getId() != null) {
            throw new BadRequestAlertException("A new invoiceBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceBeta = invoiceBetaService.save(invoiceBeta);
        return ResponseEntity.created(new URI("/api/invoice-betas/" + invoiceBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceBeta.getId().toString()))
            .body(invoiceBeta);
    }

    /**
     * {@code PUT  /invoice-betas/:id} : Updates an existing invoiceBeta.
     *
     * @param id the id of the invoiceBeta to save.
     * @param invoiceBeta the invoiceBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceBeta,
     * or with status {@code 400 (Bad Request)} if the invoiceBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceBeta> updateInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceBeta invoiceBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceBeta : {}, {}", id, invoiceBeta);
        if (invoiceBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceBeta = invoiceBetaService.update(invoiceBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceBeta.getId().toString()))
            .body(invoiceBeta);
    }

    /**
     * {@code PATCH  /invoice-betas/:id} : Partial updates given fields of an existing invoiceBeta, field will ignore if it is null
     *
     * @param id the id of the invoiceBeta to save.
     * @param invoiceBeta the invoiceBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceBeta,
     * or with status {@code 400 (Bad Request)} if the invoiceBeta is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceBeta> partialUpdateInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceBeta invoiceBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceBeta partially : {}, {}", id, invoiceBeta);
        if (invoiceBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceBeta> result = invoiceBetaService.partialUpdate(invoiceBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-betas} : get all the invoiceBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceBeta>> getAllInvoiceBetas(
        InvoiceBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceBetas by criteria: {}", criteria);

        Page<InvoiceBeta> page = invoiceBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-betas/count} : count all the invoiceBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceBetas(InvoiceBetaCriteria criteria) {
        LOG.debug("REST request to count InvoiceBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-betas/:id} : get the "id" invoiceBeta.
     *
     * @param id the id of the invoiceBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceBeta> getInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceBeta : {}", id);
        Optional<InvoiceBeta> invoiceBeta = invoiceBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceBeta);
    }

    /**
     * {@code DELETE  /invoice-betas/:id} : delete the "id" invoiceBeta.
     *
     * @param id the id of the invoiceBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceBeta : {}", id);
        invoiceBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
