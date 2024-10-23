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
import xyz.jhmapstruct.domain.NextCustomerSigma;
import xyz.jhmapstruct.repository.NextCustomerSigmaRepository;
import xyz.jhmapstruct.service.NextCustomerSigmaQueryService;
import xyz.jhmapstruct.service.NextCustomerSigmaService;
import xyz.jhmapstruct.service.criteria.NextCustomerSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerSigma}.
 */
@RestController
@RequestMapping("/api/next-customer-sigmas")
public class NextCustomerSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerSigmaResource.class);

    private static final String ENTITY_NAME = "nextCustomerSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerSigmaService nextCustomerSigmaService;

    private final NextCustomerSigmaRepository nextCustomerSigmaRepository;

    private final NextCustomerSigmaQueryService nextCustomerSigmaQueryService;

    public NextCustomerSigmaResource(
        NextCustomerSigmaService nextCustomerSigmaService,
        NextCustomerSigmaRepository nextCustomerSigmaRepository,
        NextCustomerSigmaQueryService nextCustomerSigmaQueryService
    ) {
        this.nextCustomerSigmaService = nextCustomerSigmaService;
        this.nextCustomerSigmaRepository = nextCustomerSigmaRepository;
        this.nextCustomerSigmaQueryService = nextCustomerSigmaQueryService;
    }

    /**
     * {@code POST  /next-customer-sigmas} : Create a new nextCustomerSigma.
     *
     * @param nextCustomerSigma the nextCustomerSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerSigma, or with status {@code 400 (Bad Request)} if the nextCustomerSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerSigma> createNextCustomerSigma(@Valid @RequestBody NextCustomerSigma nextCustomerSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerSigma : {}", nextCustomerSigma);
        if (nextCustomerSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerSigma = nextCustomerSigmaService.save(nextCustomerSigma);
        return ResponseEntity.created(new URI("/api/next-customer-sigmas/" + nextCustomerSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerSigma.getId().toString()))
            .body(nextCustomerSigma);
    }

    /**
     * {@code PUT  /next-customer-sigmas/:id} : Updates an existing nextCustomerSigma.
     *
     * @param id the id of the nextCustomerSigma to save.
     * @param nextCustomerSigma the nextCustomerSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerSigma,
     * or with status {@code 400 (Bad Request)} if the nextCustomerSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerSigma> updateNextCustomerSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerSigma nextCustomerSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerSigma : {}, {}", id, nextCustomerSigma);
        if (nextCustomerSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerSigma = nextCustomerSigmaService.update(nextCustomerSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerSigma.getId().toString()))
            .body(nextCustomerSigma);
    }

    /**
     * {@code PATCH  /next-customer-sigmas/:id} : Partial updates given fields of an existing nextCustomerSigma, field will ignore if it is null
     *
     * @param id the id of the nextCustomerSigma to save.
     * @param nextCustomerSigma the nextCustomerSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerSigma,
     * or with status {@code 400 (Bad Request)} if the nextCustomerSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerSigma> partialUpdateNextCustomerSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerSigma nextCustomerSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerSigma partially : {}, {}", id, nextCustomerSigma);
        if (nextCustomerSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerSigma> result = nextCustomerSigmaService.partialUpdate(nextCustomerSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-sigmas} : get all the nextCustomerSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerSigma>> getAllNextCustomerSigmas(
        NextCustomerSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerSigmas by criteria: {}", criteria);

        Page<NextCustomerSigma> page = nextCustomerSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-sigmas/count} : count all the nextCustomerSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerSigmas(NextCustomerSigmaCriteria criteria) {
        LOG.debug("REST request to count NextCustomerSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-sigmas/:id} : get the "id" nextCustomerSigma.
     *
     * @param id the id of the nextCustomerSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerSigma> getNextCustomerSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerSigma : {}", id);
        Optional<NextCustomerSigma> nextCustomerSigma = nextCustomerSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerSigma);
    }

    /**
     * {@code DELETE  /next-customer-sigmas/:id} : delete the "id" nextCustomerSigma.
     *
     * @param id the id of the nextCustomerSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerSigma : {}", id);
        nextCustomerSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
