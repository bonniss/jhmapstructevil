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
import xyz.jhmapstruct.domain.NextInvoiceTheta;
import xyz.jhmapstruct.repository.NextInvoiceThetaRepository;
import xyz.jhmapstruct.service.NextInvoiceThetaQueryService;
import xyz.jhmapstruct.service.NextInvoiceThetaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceTheta}.
 */
@RestController
@RequestMapping("/api/next-invoice-thetas")
public class NextInvoiceThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceThetaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceThetaService nextInvoiceThetaService;

    private final NextInvoiceThetaRepository nextInvoiceThetaRepository;

    private final NextInvoiceThetaQueryService nextInvoiceThetaQueryService;

    public NextInvoiceThetaResource(
        NextInvoiceThetaService nextInvoiceThetaService,
        NextInvoiceThetaRepository nextInvoiceThetaRepository,
        NextInvoiceThetaQueryService nextInvoiceThetaQueryService
    ) {
        this.nextInvoiceThetaService = nextInvoiceThetaService;
        this.nextInvoiceThetaRepository = nextInvoiceThetaRepository;
        this.nextInvoiceThetaQueryService = nextInvoiceThetaQueryService;
    }

    /**
     * {@code POST  /next-invoice-thetas} : Create a new nextInvoiceTheta.
     *
     * @param nextInvoiceTheta the nextInvoiceTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceTheta, or with status {@code 400 (Bad Request)} if the nextInvoiceTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceTheta> createNextInvoiceTheta(@Valid @RequestBody NextInvoiceTheta nextInvoiceTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceTheta : {}", nextInvoiceTheta);
        if (nextInvoiceTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceTheta = nextInvoiceThetaService.save(nextInvoiceTheta);
        return ResponseEntity.created(new URI("/api/next-invoice-thetas/" + nextInvoiceTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceTheta.getId().toString()))
            .body(nextInvoiceTheta);
    }

    /**
     * {@code PUT  /next-invoice-thetas/:id} : Updates an existing nextInvoiceTheta.
     *
     * @param id the id of the nextInvoiceTheta to save.
     * @param nextInvoiceTheta the nextInvoiceTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceTheta,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceTheta> updateNextInvoiceTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceTheta nextInvoiceTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceTheta : {}, {}", id, nextInvoiceTheta);
        if (nextInvoiceTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceTheta = nextInvoiceThetaService.update(nextInvoiceTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceTheta.getId().toString()))
            .body(nextInvoiceTheta);
    }

    /**
     * {@code PATCH  /next-invoice-thetas/:id} : Partial updates given fields of an existing nextInvoiceTheta, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceTheta to save.
     * @param nextInvoiceTheta the nextInvoiceTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceTheta,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceTheta> partialUpdateNextInvoiceTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceTheta nextInvoiceTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceTheta partially : {}, {}", id, nextInvoiceTheta);
        if (nextInvoiceTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceTheta> result = nextInvoiceThetaService.partialUpdate(nextInvoiceTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-thetas} : get all the nextInvoiceThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceTheta>> getAllNextInvoiceThetas(
        NextInvoiceThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceThetas by criteria: {}", criteria);

        Page<NextInvoiceTheta> page = nextInvoiceThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-thetas/count} : count all the nextInvoiceThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceThetas(NextInvoiceThetaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-thetas/:id} : get the "id" nextInvoiceTheta.
     *
     * @param id the id of the nextInvoiceTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceTheta> getNextInvoiceTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceTheta : {}", id);
        Optional<NextInvoiceTheta> nextInvoiceTheta = nextInvoiceThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceTheta);
    }

    /**
     * {@code DELETE  /next-invoice-thetas/:id} : delete the "id" nextInvoiceTheta.
     *
     * @param id the id of the nextInvoiceTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceTheta : {}", id);
        nextInvoiceThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
