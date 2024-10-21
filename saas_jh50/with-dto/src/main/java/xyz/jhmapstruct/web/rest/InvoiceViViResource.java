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
import xyz.jhmapstruct.repository.InvoiceViViRepository;
import xyz.jhmapstruct.service.InvoiceViViService;
import xyz.jhmapstruct.service.dto.InvoiceViViDTO;
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

    public InvoiceViViResource(InvoiceViViService invoiceViViService, InvoiceViViRepository invoiceViViRepository) {
        this.invoiceViViService = invoiceViViService;
        this.invoiceViViRepository = invoiceViViRepository;
    }

    /**
     * {@code POST  /invoice-vi-vis} : Create a new invoiceViVi.
     *
     * @param invoiceViViDTO the invoiceViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceViViDTO, or with status {@code 400 (Bad Request)} if the invoiceViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceViViDTO> createInvoiceViVi(@Valid @RequestBody InvoiceViViDTO invoiceViViDTO) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceViVi : {}", invoiceViViDTO);
        if (invoiceViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceViViDTO = invoiceViViService.save(invoiceViViDTO);
        return ResponseEntity.created(new URI("/api/invoice-vi-vis/" + invoiceViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceViViDTO.getId().toString()))
            .body(invoiceViViDTO);
    }

    /**
     * {@code PUT  /invoice-vi-vis/:id} : Updates an existing invoiceViVi.
     *
     * @param id the id of the invoiceViViDTO to save.
     * @param invoiceViViDTO the invoiceViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViViDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceViViDTO> updateInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceViViDTO invoiceViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceViVi : {}, {}", id, invoiceViViDTO);
        if (invoiceViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceViViDTO = invoiceViViService.update(invoiceViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViViDTO.getId().toString()))
            .body(invoiceViViDTO);
    }

    /**
     * {@code PATCH  /invoice-vi-vis/:id} : Partial updates given fields of an existing invoiceViVi, field will ignore if it is null
     *
     * @param id the id of the invoiceViViDTO to save.
     * @param invoiceViViDTO the invoiceViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViViDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceViViDTO> partialUpdateInvoiceViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceViViDTO invoiceViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceViVi partially : {}, {}", id, invoiceViViDTO);
        if (invoiceViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceViViDTO> result = invoiceViViService.partialUpdate(invoiceViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-vi-vis} : get all the invoiceViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceViVis in body.
     */
    @GetMapping("")
    public List<InvoiceViViDTO> getAllInvoiceViVis() {
        LOG.debug("REST request to get all InvoiceViVis");
        return invoiceViViService.findAll();
    }

    /**
     * {@code GET  /invoice-vi-vis/:id} : get the "id" invoiceViVi.
     *
     * @param id the id of the invoiceViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceViViDTO> getInvoiceViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceViVi : {}", id);
        Optional<InvoiceViViDTO> invoiceViViDTO = invoiceViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceViViDTO);
    }

    /**
     * {@code DELETE  /invoice-vi-vis/:id} : delete the "id" invoiceViVi.
     *
     * @param id the id of the invoiceViViDTO to delete.
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
