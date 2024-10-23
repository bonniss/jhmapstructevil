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
import xyz.jhmapstruct.domain.NextInvoiceBeta;
import xyz.jhmapstruct.repository.NextInvoiceBetaRepository;
import xyz.jhmapstruct.service.NextInvoiceBetaQueryService;
import xyz.jhmapstruct.service.NextInvoiceBetaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceBeta}.
 */
@RestController
@RequestMapping("/api/next-invoice-betas")
public class NextInvoiceBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceBetaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceBetaService nextInvoiceBetaService;

    private final NextInvoiceBetaRepository nextInvoiceBetaRepository;

    private final NextInvoiceBetaQueryService nextInvoiceBetaQueryService;

    public NextInvoiceBetaResource(
        NextInvoiceBetaService nextInvoiceBetaService,
        NextInvoiceBetaRepository nextInvoiceBetaRepository,
        NextInvoiceBetaQueryService nextInvoiceBetaQueryService
    ) {
        this.nextInvoiceBetaService = nextInvoiceBetaService;
        this.nextInvoiceBetaRepository = nextInvoiceBetaRepository;
        this.nextInvoiceBetaQueryService = nextInvoiceBetaQueryService;
    }

    /**
     * {@code POST  /next-invoice-betas} : Create a new nextInvoiceBeta.
     *
     * @param nextInvoiceBeta the nextInvoiceBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceBeta, or with status {@code 400 (Bad Request)} if the nextInvoiceBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceBeta> createNextInvoiceBeta(@Valid @RequestBody NextInvoiceBeta nextInvoiceBeta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceBeta : {}", nextInvoiceBeta);
        if (nextInvoiceBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceBeta = nextInvoiceBetaService.save(nextInvoiceBeta);
        return ResponseEntity.created(new URI("/api/next-invoice-betas/" + nextInvoiceBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceBeta.getId().toString()))
            .body(nextInvoiceBeta);
    }

    /**
     * {@code PUT  /next-invoice-betas/:id} : Updates an existing nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBeta to save.
     * @param nextInvoiceBeta the nextInvoiceBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceBeta,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceBeta> updateNextInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceBeta nextInvoiceBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceBeta : {}, {}", id, nextInvoiceBeta);
        if (nextInvoiceBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceBeta = nextInvoiceBetaService.update(nextInvoiceBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceBeta.getId().toString()))
            .body(nextInvoiceBeta);
    }

    /**
     * {@code PATCH  /next-invoice-betas/:id} : Partial updates given fields of an existing nextInvoiceBeta, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceBeta to save.
     * @param nextInvoiceBeta the nextInvoiceBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceBeta,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceBeta> partialUpdateNextInvoiceBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceBeta nextInvoiceBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceBeta partially : {}, {}", id, nextInvoiceBeta);
        if (nextInvoiceBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceBeta> result = nextInvoiceBetaService.partialUpdate(nextInvoiceBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-betas} : get all the nextInvoiceBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceBeta>> getAllNextInvoiceBetas(
        NextInvoiceBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceBetas by criteria: {}", criteria);

        Page<NextInvoiceBeta> page = nextInvoiceBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-betas/count} : count all the nextInvoiceBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceBetas(NextInvoiceBetaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-betas/:id} : get the "id" nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceBeta> getNextInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceBeta : {}", id);
        Optional<NextInvoiceBeta> nextInvoiceBeta = nextInvoiceBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceBeta);
    }

    /**
     * {@code DELETE  /next-invoice-betas/:id} : delete the "id" nextInvoiceBeta.
     *
     * @param id the id of the nextInvoiceBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceBeta : {}", id);
        nextInvoiceBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
