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
import xyz.jhmapstruct.domain.InvoiceSigma;
import xyz.jhmapstruct.repository.InvoiceSigmaRepository;
import xyz.jhmapstruct.service.InvoiceSigmaQueryService;
import xyz.jhmapstruct.service.InvoiceSigmaService;
import xyz.jhmapstruct.service.criteria.InvoiceSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceSigma}.
 */
@RestController
@RequestMapping("/api/invoice-sigmas")
public class InvoiceSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceSigmaResource.class);

    private static final String ENTITY_NAME = "invoiceSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceSigmaService invoiceSigmaService;

    private final InvoiceSigmaRepository invoiceSigmaRepository;

    private final InvoiceSigmaQueryService invoiceSigmaQueryService;

    public InvoiceSigmaResource(
        InvoiceSigmaService invoiceSigmaService,
        InvoiceSigmaRepository invoiceSigmaRepository,
        InvoiceSigmaQueryService invoiceSigmaQueryService
    ) {
        this.invoiceSigmaService = invoiceSigmaService;
        this.invoiceSigmaRepository = invoiceSigmaRepository;
        this.invoiceSigmaQueryService = invoiceSigmaQueryService;
    }

    /**
     * {@code POST  /invoice-sigmas} : Create a new invoiceSigma.
     *
     * @param invoiceSigma the invoiceSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceSigma, or with status {@code 400 (Bad Request)} if the invoiceSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceSigma> createInvoiceSigma(@Valid @RequestBody InvoiceSigma invoiceSigma) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceSigma : {}", invoiceSigma);
        if (invoiceSigma.getId() != null) {
            throw new BadRequestAlertException("A new invoiceSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceSigma = invoiceSigmaService.save(invoiceSigma);
        return ResponseEntity.created(new URI("/api/invoice-sigmas/" + invoiceSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceSigma.getId().toString()))
            .body(invoiceSigma);
    }

    /**
     * {@code PUT  /invoice-sigmas/:id} : Updates an existing invoiceSigma.
     *
     * @param id the id of the invoiceSigma to save.
     * @param invoiceSigma the invoiceSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSigma,
     * or with status {@code 400 (Bad Request)} if the invoiceSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceSigma> updateInvoiceSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceSigma invoiceSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceSigma : {}, {}", id, invoiceSigma);
        if (invoiceSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceSigma = invoiceSigmaService.update(invoiceSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceSigma.getId().toString()))
            .body(invoiceSigma);
    }

    /**
     * {@code PATCH  /invoice-sigmas/:id} : Partial updates given fields of an existing invoiceSigma, field will ignore if it is null
     *
     * @param id the id of the invoiceSigma to save.
     * @param invoiceSigma the invoiceSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceSigma,
     * or with status {@code 400 (Bad Request)} if the invoiceSigma is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceSigma> partialUpdateInvoiceSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceSigma invoiceSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceSigma partially : {}, {}", id, invoiceSigma);
        if (invoiceSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceSigma> result = invoiceSigmaService.partialUpdate(invoiceSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-sigmas} : get all the invoiceSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceSigma>> getAllInvoiceSigmas(
        InvoiceSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceSigmas by criteria: {}", criteria);

        Page<InvoiceSigma> page = invoiceSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-sigmas/count} : count all the invoiceSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceSigmas(InvoiceSigmaCriteria criteria) {
        LOG.debug("REST request to count InvoiceSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-sigmas/:id} : get the "id" invoiceSigma.
     *
     * @param id the id of the invoiceSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceSigma> getInvoiceSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceSigma : {}", id);
        Optional<InvoiceSigma> invoiceSigma = invoiceSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceSigma);
    }

    /**
     * {@code DELETE  /invoice-sigmas/:id} : delete the "id" invoiceSigma.
     *
     * @param id the id of the invoiceSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceSigma : {}", id);
        invoiceSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
