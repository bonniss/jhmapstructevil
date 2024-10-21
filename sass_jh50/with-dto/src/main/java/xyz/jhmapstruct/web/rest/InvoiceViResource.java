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
import xyz.jhmapstruct.repository.InvoiceViRepository;
import xyz.jhmapstruct.service.InvoiceViService;
import xyz.jhmapstruct.service.dto.InvoiceViDTO;
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

    public InvoiceViResource(InvoiceViService invoiceViService, InvoiceViRepository invoiceViRepository) {
        this.invoiceViService = invoiceViService;
        this.invoiceViRepository = invoiceViRepository;
    }

    /**
     * {@code POST  /invoice-vis} : Create a new invoiceVi.
     *
     * @param invoiceViDTO the invoiceViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceViDTO, or with status {@code 400 (Bad Request)} if the invoiceVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceViDTO> createInvoiceVi(@Valid @RequestBody InvoiceViDTO invoiceViDTO) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceVi : {}", invoiceViDTO);
        if (invoiceViDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceViDTO = invoiceViService.save(invoiceViDTO);
        return ResponseEntity.created(new URI("/api/invoice-vis/" + invoiceViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceViDTO.getId().toString()))
            .body(invoiceViDTO);
    }

    /**
     * {@code PUT  /invoice-vis/:id} : Updates an existing invoiceVi.
     *
     * @param id the id of the invoiceViDTO to save.
     * @param invoiceViDTO the invoiceViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceViDTO> updateInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceViDTO invoiceViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceVi : {}, {}", id, invoiceViDTO);
        if (invoiceViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceViDTO = invoiceViService.update(invoiceViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViDTO.getId().toString()))
            .body(invoiceViDTO);
    }

    /**
     * {@code PATCH  /invoice-vis/:id} : Partial updates given fields of an existing invoiceVi, field will ignore if it is null
     *
     * @param id the id of the invoiceViDTO to save.
     * @param invoiceViDTO the invoiceViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceViDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceViDTO> partialUpdateInvoiceVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceViDTO invoiceViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceVi partially : {}, {}", id, invoiceViDTO);
        if (invoiceViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceViDTO> result = invoiceViService.partialUpdate(invoiceViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-vis} : get all the invoiceVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceVis in body.
     */
    @GetMapping("")
    public List<InvoiceViDTO> getAllInvoiceVis() {
        LOG.debug("REST request to get all InvoiceVis");
        return invoiceViService.findAll();
    }

    /**
     * {@code GET  /invoice-vis/:id} : get the "id" invoiceVi.
     *
     * @param id the id of the invoiceViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceViDTO> getInvoiceVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceVi : {}", id);
        Optional<InvoiceViDTO> invoiceViDTO = invoiceViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceViDTO);
    }

    /**
     * {@code DELETE  /invoice-vis/:id} : delete the "id" invoiceVi.
     *
     * @param id the id of the invoiceViDTO to delete.
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
