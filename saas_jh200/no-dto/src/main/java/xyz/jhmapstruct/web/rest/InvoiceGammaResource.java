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
import xyz.jhmapstruct.domain.InvoiceGamma;
import xyz.jhmapstruct.repository.InvoiceGammaRepository;
import xyz.jhmapstruct.service.InvoiceGammaQueryService;
import xyz.jhmapstruct.service.InvoiceGammaService;
import xyz.jhmapstruct.service.criteria.InvoiceGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceGamma}.
 */
@RestController
@RequestMapping("/api/invoice-gammas")
public class InvoiceGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceGammaResource.class);

    private static final String ENTITY_NAME = "invoiceGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceGammaService invoiceGammaService;

    private final InvoiceGammaRepository invoiceGammaRepository;

    private final InvoiceGammaQueryService invoiceGammaQueryService;

    public InvoiceGammaResource(
        InvoiceGammaService invoiceGammaService,
        InvoiceGammaRepository invoiceGammaRepository,
        InvoiceGammaQueryService invoiceGammaQueryService
    ) {
        this.invoiceGammaService = invoiceGammaService;
        this.invoiceGammaRepository = invoiceGammaRepository;
        this.invoiceGammaQueryService = invoiceGammaQueryService;
    }

    /**
     * {@code POST  /invoice-gammas} : Create a new invoiceGamma.
     *
     * @param invoiceGamma the invoiceGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceGamma, or with status {@code 400 (Bad Request)} if the invoiceGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceGamma> createInvoiceGamma(@Valid @RequestBody InvoiceGamma invoiceGamma) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceGamma : {}", invoiceGamma);
        if (invoiceGamma.getId() != null) {
            throw new BadRequestAlertException("A new invoiceGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceGamma = invoiceGammaService.save(invoiceGamma);
        return ResponseEntity.created(new URI("/api/invoice-gammas/" + invoiceGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceGamma.getId().toString()))
            .body(invoiceGamma);
    }

    /**
     * {@code PUT  /invoice-gammas/:id} : Updates an existing invoiceGamma.
     *
     * @param id the id of the invoiceGamma to save.
     * @param invoiceGamma the invoiceGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGamma,
     * or with status {@code 400 (Bad Request)} if the invoiceGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceGamma> updateInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceGamma invoiceGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceGamma : {}, {}", id, invoiceGamma);
        if (invoiceGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceGamma = invoiceGammaService.update(invoiceGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceGamma.getId().toString()))
            .body(invoiceGamma);
    }

    /**
     * {@code PATCH  /invoice-gammas/:id} : Partial updates given fields of an existing invoiceGamma, field will ignore if it is null
     *
     * @param id the id of the invoiceGamma to save.
     * @param invoiceGamma the invoiceGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGamma,
     * or with status {@code 400 (Bad Request)} if the invoiceGamma is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceGamma> partialUpdateInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceGamma invoiceGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceGamma partially : {}, {}", id, invoiceGamma);
        if (invoiceGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceGamma> result = invoiceGammaService.partialUpdate(invoiceGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-gammas} : get all the invoiceGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceGamma>> getAllInvoiceGammas(
        InvoiceGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceGammas by criteria: {}", criteria);

        Page<InvoiceGamma> page = invoiceGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-gammas/count} : count all the invoiceGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceGammas(InvoiceGammaCriteria criteria) {
        LOG.debug("REST request to count InvoiceGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-gammas/:id} : get the "id" invoiceGamma.
     *
     * @param id the id of the invoiceGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceGamma> getInvoiceGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceGamma : {}", id);
        Optional<InvoiceGamma> invoiceGamma = invoiceGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceGamma);
    }

    /**
     * {@code DELETE  /invoice-gammas/:id} : delete the "id" invoiceGamma.
     *
     * @param id the id of the invoiceGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceGamma : {}", id);
        invoiceGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
