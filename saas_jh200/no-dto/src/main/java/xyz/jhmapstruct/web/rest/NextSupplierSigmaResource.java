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
import xyz.jhmapstruct.domain.NextSupplierSigma;
import xyz.jhmapstruct.repository.NextSupplierSigmaRepository;
import xyz.jhmapstruct.service.NextSupplierSigmaQueryService;
import xyz.jhmapstruct.service.NextSupplierSigmaService;
import xyz.jhmapstruct.service.criteria.NextSupplierSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierSigma}.
 */
@RestController
@RequestMapping("/api/next-supplier-sigmas")
public class NextSupplierSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierSigmaResource.class);

    private static final String ENTITY_NAME = "nextSupplierSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierSigmaService nextSupplierSigmaService;

    private final NextSupplierSigmaRepository nextSupplierSigmaRepository;

    private final NextSupplierSigmaQueryService nextSupplierSigmaQueryService;

    public NextSupplierSigmaResource(
        NextSupplierSigmaService nextSupplierSigmaService,
        NextSupplierSigmaRepository nextSupplierSigmaRepository,
        NextSupplierSigmaQueryService nextSupplierSigmaQueryService
    ) {
        this.nextSupplierSigmaService = nextSupplierSigmaService;
        this.nextSupplierSigmaRepository = nextSupplierSigmaRepository;
        this.nextSupplierSigmaQueryService = nextSupplierSigmaQueryService;
    }

    /**
     * {@code POST  /next-supplier-sigmas} : Create a new nextSupplierSigma.
     *
     * @param nextSupplierSigma the nextSupplierSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierSigma, or with status {@code 400 (Bad Request)} if the nextSupplierSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierSigma> createNextSupplierSigma(@Valid @RequestBody NextSupplierSigma nextSupplierSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierSigma : {}", nextSupplierSigma);
        if (nextSupplierSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierSigma = nextSupplierSigmaService.save(nextSupplierSigma);
        return ResponseEntity.created(new URI("/api/next-supplier-sigmas/" + nextSupplierSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierSigma.getId().toString()))
            .body(nextSupplierSigma);
    }

    /**
     * {@code PUT  /next-supplier-sigmas/:id} : Updates an existing nextSupplierSigma.
     *
     * @param id the id of the nextSupplierSigma to save.
     * @param nextSupplierSigma the nextSupplierSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierSigma,
     * or with status {@code 400 (Bad Request)} if the nextSupplierSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierSigma> updateNextSupplierSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierSigma nextSupplierSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierSigma : {}, {}", id, nextSupplierSigma);
        if (nextSupplierSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierSigma = nextSupplierSigmaService.update(nextSupplierSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierSigma.getId().toString()))
            .body(nextSupplierSigma);
    }

    /**
     * {@code PATCH  /next-supplier-sigmas/:id} : Partial updates given fields of an existing nextSupplierSigma, field will ignore if it is null
     *
     * @param id the id of the nextSupplierSigma to save.
     * @param nextSupplierSigma the nextSupplierSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierSigma,
     * or with status {@code 400 (Bad Request)} if the nextSupplierSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierSigma> partialUpdateNextSupplierSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierSigma nextSupplierSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierSigma partially : {}, {}", id, nextSupplierSigma);
        if (nextSupplierSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierSigma> result = nextSupplierSigmaService.partialUpdate(nextSupplierSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-sigmas} : get all the nextSupplierSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierSigma>> getAllNextSupplierSigmas(
        NextSupplierSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierSigmas by criteria: {}", criteria);

        Page<NextSupplierSigma> page = nextSupplierSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-sigmas/count} : count all the nextSupplierSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierSigmas(NextSupplierSigmaCriteria criteria) {
        LOG.debug("REST request to count NextSupplierSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-sigmas/:id} : get the "id" nextSupplierSigma.
     *
     * @param id the id of the nextSupplierSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierSigma> getNextSupplierSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierSigma : {}", id);
        Optional<NextSupplierSigma> nextSupplierSigma = nextSupplierSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierSigma);
    }

    /**
     * {@code DELETE  /next-supplier-sigmas/:id} : delete the "id" nextSupplierSigma.
     *
     * @param id the id of the nextSupplierSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierSigma : {}", id);
        nextSupplierSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
