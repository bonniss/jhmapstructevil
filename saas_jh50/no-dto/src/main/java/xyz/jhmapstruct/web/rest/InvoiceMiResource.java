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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.InvoiceMi;
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.InvoiceMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceMi}.
 */
@RestController
@RequestMapping("/api/invoice-mis")
public class InvoiceMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceMiResource.class);

    private static final String ENTITY_NAME = "invoiceMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceMiService invoiceMiService;

    private final InvoiceMiRepository invoiceMiRepository;

    public InvoiceMiResource(InvoiceMiService invoiceMiService, InvoiceMiRepository invoiceMiRepository) {
        this.invoiceMiService = invoiceMiService;
        this.invoiceMiRepository = invoiceMiRepository;
    }

    /**
     * {@code POST  /invoice-mis} : Create a new invoiceMi.
     *
     * @param invoiceMi the invoiceMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceMi, or with status {@code 400 (Bad Request)} if the invoiceMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceMi> createInvoiceMi(@Valid @RequestBody InvoiceMi invoiceMi) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceMi : {}", invoiceMi);
        if (invoiceMi.getId() != null) {
            throw new BadRequestAlertException("A new invoiceMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceMi = invoiceMiService.save(invoiceMi);
        return ResponseEntity.created(new URI("/api/invoice-mis/" + invoiceMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceMi.getId().toString()))
            .body(invoiceMi);
    }

    /**
     * {@code PUT  /invoice-mis/:id} : Updates an existing invoiceMi.
     *
     * @param id the id of the invoiceMi to save.
     * @param invoiceMi the invoiceMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMi,
     * or with status {@code 400 (Bad Request)} if the invoiceMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceMi> updateInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceMi invoiceMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceMi : {}, {}", id, invoiceMi);
        if (invoiceMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceMi = invoiceMiService.update(invoiceMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMi.getId().toString()))
            .body(invoiceMi);
    }

    /**
     * {@code PATCH  /invoice-mis/:id} : Partial updates given fields of an existing invoiceMi, field will ignore if it is null
     *
     * @param id the id of the invoiceMi to save.
     * @param invoiceMi the invoiceMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMi,
     * or with status {@code 400 (Bad Request)} if the invoiceMi is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceMi> partialUpdateInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceMi invoiceMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceMi partially : {}, {}", id, invoiceMi);
        if (invoiceMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceMi> result = invoiceMiService.partialUpdate(invoiceMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMi.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-mis} : get all the invoiceMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceMis in body.
     */
    @GetMapping("")
    public List<InvoiceMi> getAllInvoiceMis() {
        LOG.debug("REST request to get all InvoiceMis");
        return invoiceMiService.findAll();
    }

    /**
     * {@code GET  /invoice-mis/:id} : get the "id" invoiceMi.
     *
     * @param id the id of the invoiceMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceMi> getInvoiceMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceMi : {}", id);
        Optional<InvoiceMi> invoiceMi = invoiceMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceMi);
    }

    /**
     * {@code DELETE  /invoice-mis/:id} : delete the "id" invoiceMi.
     *
     * @param id the id of the invoiceMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceMi : {}", id);
        invoiceMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
