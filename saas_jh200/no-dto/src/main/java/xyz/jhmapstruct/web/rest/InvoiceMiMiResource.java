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
import xyz.jhmapstruct.domain.InvoiceMiMi;
import xyz.jhmapstruct.repository.InvoiceMiMiRepository;
import xyz.jhmapstruct.service.InvoiceMiMiQueryService;
import xyz.jhmapstruct.service.InvoiceMiMiService;
import xyz.jhmapstruct.service.criteria.InvoiceMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceMiMi}.
 */
@RestController
@RequestMapping("/api/invoice-mi-mis")
public class InvoiceMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiMiResource.class);

    private static final String ENTITY_NAME = "invoiceMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceMiMiService invoiceMiMiService;

    private final InvoiceMiMiRepository invoiceMiMiRepository;

    private final InvoiceMiMiQueryService invoiceMiMiQueryService;

    public InvoiceMiMiResource(
        InvoiceMiMiService invoiceMiMiService,
        InvoiceMiMiRepository invoiceMiMiRepository,
        InvoiceMiMiQueryService invoiceMiMiQueryService
    ) {
        this.invoiceMiMiService = invoiceMiMiService;
        this.invoiceMiMiRepository = invoiceMiMiRepository;
        this.invoiceMiMiQueryService = invoiceMiMiQueryService;
    }

    /**
     * {@code POST  /invoice-mi-mis} : Create a new invoiceMiMi.
     *
     * @param invoiceMiMi the invoiceMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceMiMi, or with status {@code 400 (Bad Request)} if the invoiceMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceMiMi> createInvoiceMiMi(@Valid @RequestBody InvoiceMiMi invoiceMiMi) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceMiMi : {}", invoiceMiMi);
        if (invoiceMiMi.getId() != null) {
            throw new BadRequestAlertException("A new invoiceMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceMiMi = invoiceMiMiService.save(invoiceMiMi);
        return ResponseEntity.created(new URI("/api/invoice-mi-mis/" + invoiceMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceMiMi.getId().toString()))
            .body(invoiceMiMi);
    }

    /**
     * {@code PUT  /invoice-mi-mis/:id} : Updates an existing invoiceMiMi.
     *
     * @param id the id of the invoiceMiMi to save.
     * @param invoiceMiMi the invoiceMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMiMi,
     * or with status {@code 400 (Bad Request)} if the invoiceMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceMiMi> updateInvoiceMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceMiMi invoiceMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceMiMi : {}, {}", id, invoiceMiMi);
        if (invoiceMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceMiMi = invoiceMiMiService.update(invoiceMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMiMi.getId().toString()))
            .body(invoiceMiMi);
    }

    /**
     * {@code PATCH  /invoice-mi-mis/:id} : Partial updates given fields of an existing invoiceMiMi, field will ignore if it is null
     *
     * @param id the id of the invoiceMiMi to save.
     * @param invoiceMiMi the invoiceMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMiMi,
     * or with status {@code 400 (Bad Request)} if the invoiceMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceMiMi> partialUpdateInvoiceMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceMiMi invoiceMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceMiMi partially : {}, {}", id, invoiceMiMi);
        if (invoiceMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceMiMi> result = invoiceMiMiService.partialUpdate(invoiceMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-mi-mis} : get all the invoiceMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceMiMi>> getAllInvoiceMiMis(
        InvoiceMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceMiMis by criteria: {}", criteria);

        Page<InvoiceMiMi> page = invoiceMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-mi-mis/count} : count all the invoiceMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceMiMis(InvoiceMiMiCriteria criteria) {
        LOG.debug("REST request to count InvoiceMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-mi-mis/:id} : get the "id" invoiceMiMi.
     *
     * @param id the id of the invoiceMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceMiMi> getInvoiceMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceMiMi : {}", id);
        Optional<InvoiceMiMi> invoiceMiMi = invoiceMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceMiMi);
    }

    /**
     * {@code DELETE  /invoice-mi-mis/:id} : delete the "id" invoiceMiMi.
     *
     * @param id the id of the invoiceMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceMiMi : {}", id);
        invoiceMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
