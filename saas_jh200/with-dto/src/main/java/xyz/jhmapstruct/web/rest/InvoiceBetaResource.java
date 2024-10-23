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
import xyz.jhmapstruct.repository.InvoiceBetaRepository;
import xyz.jhmapstruct.service.InvoiceBetaQueryService;
import xyz.jhmapstruct.service.InvoiceBetaService;
import xyz.jhmapstruct.service.criteria.InvoiceBetaCriteria;
import xyz.jhmapstruct.service.dto.InvoiceBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.InvoiceBeta}.
 */
@RestController
@RequestMapping("/api/invoice-betas")
public class InvoiceBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvoiceBetaResource.class);

    private static final String ENTITY_NAME = "invoiceBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvoiceBetaService invoiceBetaService;

    private final InvoiceBetaRepository invoiceBetaRepository;

    private final InvoiceBetaQueryService invoiceBetaQueryService;

    public InvoiceBetaResource(
        InvoiceBetaService invoiceBetaService,
        InvoiceBetaRepository invoiceBetaRepository,
        InvoiceBetaQueryService invoiceBetaQueryService
    ) {
        this.invoiceBetaService = invoiceBetaService;
        this.invoiceBetaRepository = invoiceBetaRepository;
        this.invoiceBetaQueryService = invoiceBetaQueryService;
    }

    /**
     * {@code POST  /invoice-betas} : Create a new invoiceBeta.
     *
     * @param invoiceBetaDTO the invoiceBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new invoiceBetaDTO, or with status {@code 400 (Bad Request)} if the invoiceBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvoiceBetaDTO> createInvoiceBeta(@Valid @RequestBody InvoiceBetaDTO invoiceBetaDTO) throws URISyntaxException {
        LOG.debug("REST request to save InvoiceBeta : {}", invoiceBetaDTO);
        if (invoiceBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new invoiceBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        invoiceBetaDTO = invoiceBetaService.save(invoiceBetaDTO);
        return ResponseEntity.created(new URI("/api/invoice-betas/" + invoiceBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, invoiceBetaDTO.getId().toString()))
            .body(invoiceBetaDTO);
    }

    /**
     * {@code PUT  /invoice-betas/:id} : Updates an existing invoiceBeta.
     *
     * @param id the id of the invoiceBetaDTO to save.
     * @param invoiceBetaDTO the invoiceBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceBetaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the invoiceBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceBetaDTO> updateInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvoiceBetaDTO invoiceBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvoiceBeta : {}, {}", id, invoiceBetaDTO);
        if (invoiceBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        invoiceBetaDTO = invoiceBetaService.update(invoiceBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceBetaDTO.getId().toString()))
            .body(invoiceBetaDTO);
    }

    /**
     * {@code PATCH  /invoice-betas/:id} : Partial updates given fields of an existing invoiceBeta, field will ignore if it is null
     *
     * @param id the id of the invoiceBetaDTO to save.
     * @param invoiceBetaDTO the invoiceBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated invoiceBetaDTO,
     * or with status {@code 400 (Bad Request)} if the invoiceBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the invoiceBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the invoiceBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvoiceBetaDTO> partialUpdateInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvoiceBetaDTO invoiceBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvoiceBeta partially : {}, {}", id, invoiceBetaDTO);
        if (invoiceBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, invoiceBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!invoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvoiceBetaDTO> result = invoiceBetaService.partialUpdate(invoiceBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, invoiceBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /invoice-betas} : get all the invoiceBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of invoiceBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvoiceBetaDTO>> getAllInvoiceBetas(
        InvoiceBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InvoiceBetas by criteria: {}", criteria);

        Page<InvoiceBetaDTO> page = invoiceBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /invoice-betas/count} : count all the invoiceBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInvoiceBetas(InvoiceBetaCriteria criteria) {
        LOG.debug("REST request to count InvoiceBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(invoiceBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /invoice-betas/:id} : get the "id" invoiceBeta.
     *
     * @param id the id of the invoiceBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the invoiceBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceBetaDTO> getInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvoiceBeta : {}", id);
        Optional<InvoiceBetaDTO> invoiceBetaDTO = invoiceBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(invoiceBetaDTO);
    }

    /**
     * {@code DELETE  /invoice-betas/:id} : delete the "id" invoiceBeta.
     *
     * @param id the id of the invoiceBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvoiceBeta : {}", id);
        invoiceBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
