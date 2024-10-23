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
import xyz.jhmapstruct.repository.InvoiceAlphaRepository;
import xyz.jhmapstruct.service.InvoiceAlphaQueryService;
import xyz.jhmapstruct.service.InvoiceAlphaService;
import xyz.jhmapstruct.service.criteria.InvoiceAlphaCriteria;
import xyz.jhmapstruct.service.dto.InvoiceAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceAlpha}.
 */
@RestController
@RequestMapping("/api/invoice-alphas")
public class InvoiceAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceAlphaResource.class);

    private static final String ENTITY_NAME = "invoiceAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceAlphaService invoiceAlphaService;

    private final InvoiceAlphaRepository invoiceAlphaRepository;

    private final InvoiceAlphaQueryService invoiceAlphaQueryService;

    public InvoiceAlphaResource(
        InvoiceAlphaService invoiceAlphaService,
        InvoiceAlphaRepository invoiceAlphaRepository,
        InvoiceAlphaQueryService invoiceAlphaQueryService
    ) {
        this.invoiceAlphaService = invoiceAlphaService;
        this.invoiceAlphaRepository = invoiceAlphaRepository;
        this.invoiceAlphaQueryService = invoiceAlphaQueryService;
    }

    /**
     * {@code POST  /invoice-alphas} : Create a new invoiceAlpha.
     *
     * @param invoiceAlphaDTO the invoiceAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceAlphaDTO, or with status {@code 400 (Bad Request)} if the invoiceAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceAlphaDTO> createInvoiceAlpha(@Valid @RequestBody InvoiceAlphaDTO invoiceAlphaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceAlpha : {}", invoiceAlphaDTO);
        if (invoiceAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceAlphaDTO = invoiceAlphaService.save(invoiceAlphaDTO);
        return ResponseEntity.created(new URI("/api/invoice-alphas/" + invoiceAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceAlphaDTO.getId().toString()))
            .body(invoiceAlphaDTO);
    }

    /**
     * {@code PUT  /invoice-alphas/:id} : Updates an existing invoiceAlpha.
     *
     * @param id the id of the invoiceAlphaDTO to save.
     * @param invoiceAlphaDTO the invoiceAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceAlphaDTO> updateInvoiceAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceAlphaDTO invoiceAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceAlpha : {}, {}", id, invoiceAlphaDTO);
        if (invoiceAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceAlphaDTO = invoiceAlphaService.update(invoiceAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceAlphaDTO.getId().toString()))
            .body(invoiceAlphaDTO);
    }

    /**
     * {@code PATCH  /invoice-alphas/:id} : Partial updates given fields of an existing invoiceAlpha, field will ignore if it is null
     *
     * @param id the id of the invoiceAlphaDTO to save.
     * @param invoiceAlphaDTO the invoiceAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceAlphaDTO> partialUpdateInvoiceAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceAlphaDTO invoiceAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceAlpha partially : {}, {}", id, invoiceAlphaDTO);
        if (invoiceAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceAlphaDTO> result = invoiceAlphaService.partialUpdate(invoiceAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-alphas} : get all the invoiceAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceAlphaDTO>> getAllInvoiceAlphas(
        InvoiceAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceAlphas by criteria: {}", criteria);

        Page<InvoiceAlphaDTO> page = invoiceAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-alphas/count} : count all the invoiceAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceAlphas(InvoiceAlphaCriteria criteria) {
        LOG.debug("REST request to count InvoiceAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-alphas/:id} : get the "id" invoiceAlpha.
     *
     * @param id the id of the invoiceAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceAlphaDTO> getInvoiceAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceAlpha : {}", id);
        Optional<InvoiceAlphaDTO> invoiceAlphaDTO = invoiceAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceAlphaDTO);
    }

    /**
     * {@code DELETE  /invoice-alphas/:id} : delete the "id" invoiceAlpha.
     *
     * @param id the id of the invoiceAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceAlpha : {}", id);
        invoiceAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
