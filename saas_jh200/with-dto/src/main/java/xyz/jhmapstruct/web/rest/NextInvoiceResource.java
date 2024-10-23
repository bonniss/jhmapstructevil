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
import xyz.jhmapstruct.repository.NextInvoiceRepository;
import xyz.jhmapstruct.service.NextInvoiceQueryService;
import xyz.jhmapstruct.service.NextInvoiceService;
import xyz.jhmapstruct.service.criteria.NextInvoiceCriteria;
import xyz.jhmapstruct.service.dto.NextInvoiceDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoice}.
 */
@RestController
@RequestMapping("/api/next-invoices")
public class NextInvoiceResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceResource.class);

    private static final String ENTITY_NAME = "nextInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceService nextInvoiceService;

    private final NextInvoiceRepository nextInvoiceRepository;

    private final NextInvoiceQueryService nextInvoiceQueryService;

    public NextInvoiceResource(
        NextInvoiceService nextInvoiceService,
        NextInvoiceRepository nextInvoiceRepository,
        NextInvoiceQueryService nextInvoiceQueryService
    ) {
        this.nextInvoiceService = nextInvoiceService;
        this.nextInvoiceRepository = nextInvoiceRepository;
        this.nextInvoiceQueryService = nextInvoiceQueryService;
    }

    /**
     * {@code POST  /next-invoices} : Create a new nextInvoice.
     *
     * @param nextInvoiceDTO the nextInvoiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceDTO, or with status {@code 400 (Bad Request)} if the nextInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceDTO> createNextInvoice(@Valid @RequestBody NextInvoiceDTO nextInvoiceDTO) throws URISyntaxException {
        LOG.debug("REST request to save NextInvoice : {}", nextInvoiceDTO);
        if (nextInvoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceDTO = nextInvoiceService.save(nextInvoiceDTO);
        return ResponseEntity.created(new URI("/api/next-invoices/" + nextInvoiceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceDTO.getId().toString()))
            .body(nextInvoiceDTO);
    }

    /**
     * {@code PUT  /next-invoices/:id} : Updates an existing nextInvoice.
     *
     * @param id the id of the nextInvoiceDTO to save.
     * @param nextInvoiceDTO the nextInvoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceDTO> updateNextInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceDTO nextInvoiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoice : {}, {}", id, nextInvoiceDTO);
        if (nextInvoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceDTO = nextInvoiceService.update(nextInvoiceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceDTO.getId().toString()))
            .body(nextInvoiceDTO);
    }

    /**
     * {@code PATCH  /next-invoices/:id} : Partial updates given fields of an existing nextInvoice, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceDTO to save.
     * @param nextInvoiceDTO the nextInvoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceDTO,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceDTO> partialUpdateNextInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceDTO nextInvoiceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoice partially : {}, {}", id, nextInvoiceDTO);
        if (nextInvoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceDTO> result = nextInvoiceService.partialUpdate(nextInvoiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoices} : get all the nextInvoices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoices in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceDTO>> getAllNextInvoices(
        NextInvoiceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoices by criteria: {}", criteria);

        Page<NextInvoiceDTO> page = nextInvoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoices/count} : count all the nextInvoices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoices(NextInvoiceCriteria criteria) {
        LOG.debug("REST request to count NextInvoices by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoices/:id} : get the "id" nextInvoice.
     *
     * @param id the id of the nextInvoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceDTO> getNextInvoice(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoice : {}", id);
        Optional<NextInvoiceDTO> nextInvoiceDTO = nextInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceDTO);
    }

    /**
     * {@code DELETE  /next-invoices/:id} : delete the "id" nextInvoice.
     *
     * @param id the id of the nextInvoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoice(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoice : {}", id);
        nextInvoiceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
