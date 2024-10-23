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
import xyz.jhmapstruct.repository.InvoiceGammaRepository;
import xyz.jhmapstruct.service.InvoiceGammaQueryService;
import xyz.jhmapstruct.service.InvoiceGammaService;
import xyz.jhmapstruct.service.criteria.InvoiceGammaCriteria;
import xyz.jhmapstruct.service.dto.InvoiceGammaDTO;
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
     * @param invoiceGammaDTO the invoiceGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceGammaDTO, or with status {@code 400 (Bad Request)} if the invoiceGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceGammaDTO> createInvoiceGamma(@Valid @RequestBody InvoiceGammaDTO invoiceGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InvoiceGamma : {}", invoiceGammaDTO);
        if (invoiceGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceGammaDTO = invoiceGammaService.save(invoiceGammaDTO);
        return ResponseEntity.created(new URI("/api/invoice-gammas/" + invoiceGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceGammaDTO.getId().toString()))
            .body(invoiceGammaDTO);
    }

    /**
     * {@code PUT  /invoice-gammas/:id} : Updates an existing invoiceGamma.
     *
     * @param id the id of the invoiceGammaDTO to save.
     * @param invoiceGammaDTO the invoiceGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGammaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceGammaDTO> updateInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceGammaDTO invoiceGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceGamma : {}, {}", id, invoiceGammaDTO);
        if (invoiceGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceGammaDTO = invoiceGammaService.update(invoiceGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceGammaDTO.getId().toString()))
            .body(invoiceGammaDTO);
    }

    /**
     * {@code PATCH  /invoice-gammas/:id} : Partial updates given fields of an existing invoiceGamma, field will ignore if it is null
     *
     * @param id the id of the invoiceGammaDTO to save.
     * @param invoiceGammaDTO the invoiceGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceGammaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceGammaDTO> partialUpdateInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceGammaDTO invoiceGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceGamma partially : {}, {}", id, invoiceGammaDTO);
        if (invoiceGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceGammaDTO> result = invoiceGammaService.partialUpdate(invoiceGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceGammaDTO.getId().toString())
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
    public ResponseEntity<List<InvoiceGammaDTO>> getAllInvoiceGammas(
        InvoiceGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceGammas by criteria: {}", criteria);

        Page<InvoiceGammaDTO> page = invoiceGammaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the invoiceGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceGammaDTO> getInvoiceGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceGamma : {}", id);
        Optional<InvoiceGammaDTO> invoiceGammaDTO = invoiceGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceGammaDTO);
    }

    /**
     * {@code DELETE  /invoice-gammas/:id} : delete the "id" invoiceGamma.
     *
     * @param id the id of the invoiceGammaDTO to delete.
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
