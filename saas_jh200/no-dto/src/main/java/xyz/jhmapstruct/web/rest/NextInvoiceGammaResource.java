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
import xyz.jhmapstruct.domain.NextInvoiceGamma;
import xyz.jhmapstruct.repository.NextInvoiceGammaRepository;
import xyz.jhmapstruct.service.NextInvoiceGammaQueryService;
import xyz.jhmapstruct.service.NextInvoiceGammaService;
import xyz.jhmapstruct.service.criteria.NextInvoiceGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceGamma}.
 */
@RestController
@RequestMapping("/api/next-invoice-gammas")
public class NextInvoiceGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceGammaResource.class);

    private static final String ENTITY_NAME = "nextInvoiceGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceGammaService nextInvoiceGammaService;

    private final NextInvoiceGammaRepository nextInvoiceGammaRepository;

    private final NextInvoiceGammaQueryService nextInvoiceGammaQueryService;

    public NextInvoiceGammaResource(
        NextInvoiceGammaService nextInvoiceGammaService,
        NextInvoiceGammaRepository nextInvoiceGammaRepository,
        NextInvoiceGammaQueryService nextInvoiceGammaQueryService
    ) {
        this.nextInvoiceGammaService = nextInvoiceGammaService;
        this.nextInvoiceGammaRepository = nextInvoiceGammaRepository;
        this.nextInvoiceGammaQueryService = nextInvoiceGammaQueryService;
    }

    /**
     * {@code POST  /next-invoice-gammas} : Create a new nextInvoiceGamma.
     *
     * @param nextInvoiceGamma the nextInvoiceGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceGamma, or with status {@code 400 (Bad Request)} if the nextInvoiceGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceGamma> createNextInvoiceGamma(@Valid @RequestBody NextInvoiceGamma nextInvoiceGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceGamma : {}", nextInvoiceGamma);
        if (nextInvoiceGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceGamma = nextInvoiceGammaService.save(nextInvoiceGamma);
        return ResponseEntity.created(new URI("/api/next-invoice-gammas/" + nextInvoiceGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceGamma.getId().toString()))
            .body(nextInvoiceGamma);
    }

    /**
     * {@code PUT  /next-invoice-gammas/:id} : Updates an existing nextInvoiceGamma.
     *
     * @param id the id of the nextInvoiceGamma to save.
     * @param nextInvoiceGamma the nextInvoiceGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceGamma,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceGamma> updateNextInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceGamma nextInvoiceGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceGamma : {}, {}", id, nextInvoiceGamma);
        if (nextInvoiceGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceGamma = nextInvoiceGammaService.update(nextInvoiceGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceGamma.getId().toString()))
            .body(nextInvoiceGamma);
    }

    /**
     * {@code PATCH  /next-invoice-gammas/:id} : Partial updates given fields of an existing nextInvoiceGamma, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceGamma to save.
     * @param nextInvoiceGamma the nextInvoiceGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceGamma,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceGamma> partialUpdateNextInvoiceGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceGamma nextInvoiceGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceGamma partially : {}, {}", id, nextInvoiceGamma);
        if (nextInvoiceGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceGamma> result = nextInvoiceGammaService.partialUpdate(nextInvoiceGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-gammas} : get all the nextInvoiceGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceGamma>> getAllNextInvoiceGammas(
        NextInvoiceGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceGammas by criteria: {}", criteria);

        Page<NextInvoiceGamma> page = nextInvoiceGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-gammas/count} : count all the nextInvoiceGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceGammas(NextInvoiceGammaCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-gammas/:id} : get the "id" nextInvoiceGamma.
     *
     * @param id the id of the nextInvoiceGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceGamma> getNextInvoiceGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceGamma : {}", id);
        Optional<NextInvoiceGamma> nextInvoiceGamma = nextInvoiceGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceGamma);
    }

    /**
     * {@code DELETE  /next-invoice-gammas/:id} : delete the "id" nextInvoiceGamma.
     *
     * @param id the id of the nextInvoiceGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceGamma : {}", id);
        nextInvoiceGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
