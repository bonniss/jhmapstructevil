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
import xyz.jhmapstruct.repository.InvoiceThetaRepository;
import xyz.jhmapstruct.service.InvoiceThetaQueryService;
import xyz.jhmapstruct.service.InvoiceThetaService;
import xyz.jhmapstruct.service.criteria.InvoiceThetaCriteria;
import xyz.jhmapstruct.service.dto.InvoiceThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceTheta}.
 */
@RestController
@RequestMapping("/api/invoice-thetas")
public class InvoiceThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceThetaResource.class);

    private static final String ENTITY_NAME = "invoiceTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceThetaService invoiceThetaService;

    private final InvoiceThetaRepository invoiceThetaRepository;

    private final InvoiceThetaQueryService invoiceThetaQueryService;

    public InvoiceThetaResource(
        InvoiceThetaService invoiceThetaService,
        InvoiceThetaRepository invoiceThetaRepository,
        InvoiceThetaQueryService invoiceThetaQueryService
    ) {
        this.invoiceThetaService = invoiceThetaService;
        this.invoiceThetaRepository = invoiceThetaRepository;
        this.invoiceThetaQueryService = invoiceThetaQueryService;
    }

    /**
     * {@code POST  /invoice-thetas} : Create a new invoiceTheta.
     *
     * @param invoiceThetaDTO the invoiceThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceThetaDTO, or with status {@code 400 (Bad Request)} if the invoiceTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceThetaDTO> createInvoiceTheta(@Valid @RequestBody InvoiceThetaDTO invoiceThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceTheta : {}", invoiceThetaDTO);
        if (invoiceThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceThetaDTO = invoiceThetaService.save(invoiceThetaDTO);
        return ResponseEntity.created(new URI("/api/invoice-thetas/" + invoiceThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceThetaDTO.getId().toString()))
            .body(invoiceThetaDTO);
    }

    /**
     * {@code PUT  /invoice-thetas/:id} : Updates an existing invoiceTheta.
     *
     * @param id the id of the invoiceThetaDTO to save.
     * @param invoiceThetaDTO the invoiceThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceThetaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceThetaDTO> updateInvoiceTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceThetaDTO invoiceThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceTheta : {}, {}", id, invoiceThetaDTO);
        if (invoiceThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceThetaDTO = invoiceThetaService.update(invoiceThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceThetaDTO.getId().toString()))
            .body(invoiceThetaDTO);
    }

    /**
     * {@code PATCH  /invoice-thetas/:id} : Partial updates given fields of an existing invoiceTheta, field will ignore if it is null
     *
     * @param id the id of the invoiceThetaDTO to save.
     * @param invoiceThetaDTO the invoiceThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceThetaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceThetaDTO> partialUpdateInvoiceTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceThetaDTO invoiceThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceTheta partially : {}, {}", id, invoiceThetaDTO);
        if (invoiceThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceThetaDTO> result = invoiceThetaService.partialUpdate(invoiceThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-thetas} : get all the invoiceThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceThetaDTO>> getAllInvoiceThetas(
        InvoiceThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceThetas by criteria: {}", criteria);

        Page<InvoiceThetaDTO> page = invoiceThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-thetas/count} : count all the invoiceThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceThetas(InvoiceThetaCriteria criteria) {
        LOG.debug("REST request to count InvoiceThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-thetas/:id} : get the "id" invoiceTheta.
     *
     * @param id the id of the invoiceThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceThetaDTO> getInvoiceTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceTheta : {}", id);
        Optional<InvoiceThetaDTO> invoiceThetaDTO = invoiceThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceThetaDTO);
    }

    /**
     * {@code DELETE  /invoice-thetas/:id} : delete the "id" invoiceTheta.
     *
     * @param id the id of the invoiceThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceTheta : {}", id);
        invoiceThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
