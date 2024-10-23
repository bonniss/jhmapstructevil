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
import xyz.jhmapstruct.domain.NextInvoiceSigma;
import xyz.jhmapstruct.repository.NextInvoiceSigmaRepository;
import xyz.jhmapstruct.service.NextInvoiceSigmaQueryService;
import xyz.jhmapstruct.service.NextInvoiceSigmaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceSigma}.
 */
@RestController
@RequestMapping("/api/next-invoice-sigmas")
public class NextInvoiceSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceSigmaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceSigmaService nextInvoiceSigmaService;

    private final NextInvoiceSigmaRepository nextInvoiceSigmaRepository;

    private final NextInvoiceSigmaQueryService nextInvoiceSigmaQueryService;

    public NextInvoiceSigmaResource(
        NextInvoiceSigmaService nextInvoiceSigmaService,
        NextInvoiceSigmaRepository nextInvoiceSigmaRepository,
        NextInvoiceSigmaQueryService nextInvoiceSigmaQueryService
    ) {
        this.nextInvoiceSigmaService = nextInvoiceSigmaService;
        this.nextInvoiceSigmaRepository = nextInvoiceSigmaRepository;
        this.nextInvoiceSigmaQueryService = nextInvoiceSigmaQueryService;
    }

    /**
     * {@code POST  /next-invoice-sigmas} : Create a new nextInvoiceSigma.
     *
     * @param nextInvoiceSigma the nextInvoiceSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceSigma, or with status {@code 400 (Bad Request)} if the nextInvoiceSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceSigma> createNextInvoiceSigma(@Valid @RequestBody NextInvoiceSigma nextInvoiceSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceSigma : {}", nextInvoiceSigma);
        if (nextInvoiceSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceSigma = nextInvoiceSigmaService.save(nextInvoiceSigma);
        return ResponseEntity.created(new URI("/api/next-invoice-sigmas/" + nextInvoiceSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceSigma.getId().toString()))
            .body(nextInvoiceSigma);
    }

    /**
     * {@code PUT  /next-invoice-sigmas/:id} : Updates an existing nextInvoiceSigma.
     *
     * @param id the id of the nextInvoiceSigma to save.
     * @param nextInvoiceSigma the nextInvoiceSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceSigma,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceSigma> updateNextInvoiceSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceSigma nextInvoiceSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceSigma : {}, {}", id, nextInvoiceSigma);
        if (nextInvoiceSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceSigma = nextInvoiceSigmaService.update(nextInvoiceSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceSigma.getId().toString()))
            .body(nextInvoiceSigma);
    }

    /**
     * {@code PATCH  /next-invoice-sigmas/:id} : Partial updates given fields of an existing nextInvoiceSigma, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceSigma to save.
     * @param nextInvoiceSigma the nextInvoiceSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceSigma,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceSigma> partialUpdateNextInvoiceSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceSigma nextInvoiceSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceSigma partially : {}, {}", id, nextInvoiceSigma);
        if (nextInvoiceSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceSigma> result = nextInvoiceSigmaService.partialUpdate(nextInvoiceSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-sigmas} : get all the nextInvoiceSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceSigma>> getAllNextInvoiceSigmas(
        NextInvoiceSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceSigmas by criteria: {}", criteria);

        Page<NextInvoiceSigma> page = nextInvoiceSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-sigmas/count} : count all the nextInvoiceSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceSigmas(NextInvoiceSigmaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-sigmas/:id} : get the "id" nextInvoiceSigma.
     *
     * @param id the id of the nextInvoiceSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceSigma> getNextInvoiceSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceSigma : {}", id);
        Optional<NextInvoiceSigma> nextInvoiceSigma = nextInvoiceSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceSigma);
    }

    /**
     * {@code DELETE  /next-invoice-sigmas/:id} : delete the "id" nextInvoiceSigma.
     *
     * @param id the id of the nextInvoiceSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceSigma : {}", id);
        nextInvoiceSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
