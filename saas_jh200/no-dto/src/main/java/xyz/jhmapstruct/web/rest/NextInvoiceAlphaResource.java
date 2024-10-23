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
import xyz.jhmapstruct.domain.NextInvoiceAlpha;
import xyz.jhmapstruct.repository.NextInvoiceAlphaRepository;
import xyz.jhmapstruct.service.NextInvoiceAlphaQueryService;
import xyz.jhmapstruct.service.NextInvoiceAlphaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceAlpha}.
 */
@RestController
@RequestMapping("/api/next-invoice-alphas")
public class NextInvoiceAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceAlphaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceAlphaService nextInvoiceAlphaService;

    private final NextInvoiceAlphaRepository nextInvoiceAlphaRepository;

    private final NextInvoiceAlphaQueryService nextInvoiceAlphaQueryService;

    public NextInvoiceAlphaResource(
        NextInvoiceAlphaService nextInvoiceAlphaService,
        NextInvoiceAlphaRepository nextInvoiceAlphaRepository,
        NextInvoiceAlphaQueryService nextInvoiceAlphaQueryService
    ) {
        this.nextInvoiceAlphaService = nextInvoiceAlphaService;
        this.nextInvoiceAlphaRepository = nextInvoiceAlphaRepository;
        this.nextInvoiceAlphaQueryService = nextInvoiceAlphaQueryService;
    }

    /**
     * {@code POST  /next-invoice-alphas} : Create a new nextInvoiceAlpha.
     *
     * @param nextInvoiceAlpha the nextInvoiceAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceAlpha, or with status {@code 400 (Bad Request)} if the nextInvoiceAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceAlpha> createNextInvoiceAlpha(@Valid @RequestBody NextInvoiceAlpha nextInvoiceAlpha)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceAlpha : {}", nextInvoiceAlpha);
        if (nextInvoiceAlpha.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceAlpha = nextInvoiceAlphaService.save(nextInvoiceAlpha);
        return ResponseEntity.created(new URI("/api/next-invoice-alphas/" + nextInvoiceAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceAlpha.getId().toString()))
            .body(nextInvoiceAlpha);
    }

    /**
     * {@code PUT  /next-invoice-alphas/:id} : Updates an existing nextInvoiceAlpha.
     *
     * @param id the id of the nextInvoiceAlpha to save.
     * @param nextInvoiceAlpha the nextInvoiceAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceAlpha,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceAlpha> updateNextInvoiceAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceAlpha nextInvoiceAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceAlpha : {}, {}", id, nextInvoiceAlpha);
        if (nextInvoiceAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceAlpha = nextInvoiceAlphaService.update(nextInvoiceAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceAlpha.getId().toString()))
            .body(nextInvoiceAlpha);
    }

    /**
     * {@code PATCH  /next-invoice-alphas/:id} : Partial updates given fields of an existing nextInvoiceAlpha, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceAlpha to save.
     * @param nextInvoiceAlpha the nextInvoiceAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceAlpha,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceAlpha> partialUpdateNextInvoiceAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceAlpha nextInvoiceAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceAlpha partially : {}, {}", id, nextInvoiceAlpha);
        if (nextInvoiceAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceAlpha> result = nextInvoiceAlphaService.partialUpdate(nextInvoiceAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-alphas} : get all the nextInvoiceAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceAlpha>> getAllNextInvoiceAlphas(
        NextInvoiceAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceAlphas by criteria: {}", criteria);

        Page<NextInvoiceAlpha> page = nextInvoiceAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-alphas/count} : count all the nextInvoiceAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceAlphas(NextInvoiceAlphaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-alphas/:id} : get the "id" nextInvoiceAlpha.
     *
     * @param id the id of the nextInvoiceAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceAlpha> getNextInvoiceAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceAlpha : {}", id);
        Optional<NextInvoiceAlpha> nextInvoiceAlpha = nextInvoiceAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceAlpha);
    }

    /**
     * {@code DELETE  /next-invoice-alphas/:id} : delete the "id" nextInvoiceAlpha.
     *
     * @param id the id of the nextInvoiceAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceAlpha : {}", id);
        nextInvoiceAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
