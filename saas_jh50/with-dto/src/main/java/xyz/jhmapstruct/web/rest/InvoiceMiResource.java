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
import xyz.jhmapstruct.repository.InvoiceMiRepository;
import xyz.jhmapstruct.service.InvoiceMiService;
import xyz.jhmapstruct.service.dto.InvoiceMiDTO;
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
     * @param invoiceMiDTO the invoiceMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceMiDTO, or with status {@code 400 (Bad Request)} if the invoiceMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceMiDTO> createInvoiceMi(@Valid @RequestBody InvoiceMiDTO invoiceMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceMi : {}", invoiceMiDTO);
        if (invoiceMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceMiDTO = invoiceMiService.save(invoiceMiDTO);
        return ResponseEntity.created(new URI("/api/invoice-mis/" + invoiceMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceMiDTO.getId().toString()))
            .body(invoiceMiDTO);
    }

    /**
     * {@code PUT  /invoice-mis/:id} : Updates an existing invoiceMi.
     *
     * @param id the id of the invoiceMiDTO to save.
     * @param invoiceMiDTO the invoiceMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMiDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceMiDTO> updateInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceMiDTO invoiceMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceMi : {}, {}", id, invoiceMiDTO);
        if (invoiceMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceMiDTO = invoiceMiService.update(invoiceMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMiDTO.getId().toString()))
            .body(invoiceMiDTO);
    }

    /**
     * {@code PATCH  /invoice-mis/:id} : Partial updates given fields of an existing invoiceMi, field will ignore if it is null
     *
     * @param id the id of the invoiceMiDTO to save.
     * @param invoiceMiDTO the invoiceMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceMiDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceMiDTO> partialUpdateInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceMiDTO invoiceMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceMi partially : {}, {}", id, invoiceMiDTO);
        if (invoiceMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceMiDTO> result = invoiceMiService.partialUpdate(invoiceMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-mis} : get all the invoiceMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceMis in body.
     */
    @GetMapping("")
    public List<InvoiceMiDTO> getAllInvoiceMis() {
        LOG.debug("REST request to get all InvoiceMis");
        return invoiceMiService.findAll();
    }

    /**
     * {@code GET  /invoice-mis/:id} : get the "id" invoiceMi.
     *
     * @param id the id of the invoiceMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceMiDTO> getInvoiceMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceMi : {}", id);
        Optional<InvoiceMiDTO> invoiceMiDTO = invoiceMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceMiDTO);
    }

    /**
     * {@code DELETE  /invoice-mis/:id} : delete the "id" invoiceMi.
     *
     * @param id the id of the invoiceMiDTO to delete.
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
