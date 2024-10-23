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
import xyz.jhmapstruct.domain.NextEmployeeSigma;
import xyz.jhmapstruct.repository.NextEmployeeSigmaRepository;
import xyz.jhmapstruct.service.NextEmployeeSigmaQueryService;
import xyz.jhmapstruct.service.NextEmployeeSigmaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeSigma}.
 */
@RestController
@RequestMapping("/api/next-employee-sigmas")
public class NextEmployeeSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeSigmaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeSigmaService nextEmployeeSigmaService;

    private final NextEmployeeSigmaRepository nextEmployeeSigmaRepository;

    private final NextEmployeeSigmaQueryService nextEmployeeSigmaQueryService;

    public NextEmployeeSigmaResource(
        NextEmployeeSigmaService nextEmployeeSigmaService,
        NextEmployeeSigmaRepository nextEmployeeSigmaRepository,
        NextEmployeeSigmaQueryService nextEmployeeSigmaQueryService
    ) {
        this.nextEmployeeSigmaService = nextEmployeeSigmaService;
        this.nextEmployeeSigmaRepository = nextEmployeeSigmaRepository;
        this.nextEmployeeSigmaQueryService = nextEmployeeSigmaQueryService;
    }

    /**
     * {@code POST  /next-employee-sigmas} : Create a new nextEmployeeSigma.
     *
     * @param nextEmployeeSigma the nextEmployeeSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeSigma, or with status {@code 400 (Bad Request)} if the nextEmployeeSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeSigma> createNextEmployeeSigma(@Valid @RequestBody NextEmployeeSigma nextEmployeeSigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeSigma : {}", nextEmployeeSigma);
        if (nextEmployeeSigma.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeSigma = nextEmployeeSigmaService.save(nextEmployeeSigma);
        return ResponseEntity.created(new URI("/api/next-employee-sigmas/" + nextEmployeeSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeSigma.getId().toString()))
            .body(nextEmployeeSigma);
    }

    /**
     * {@code PUT  /next-employee-sigmas/:id} : Updates an existing nextEmployeeSigma.
     *
     * @param id the id of the nextEmployeeSigma to save.
     * @param nextEmployeeSigma the nextEmployeeSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeSigma,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeSigma> updateNextEmployeeSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeSigma nextEmployeeSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeSigma : {}, {}", id, nextEmployeeSigma);
        if (nextEmployeeSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeSigma = nextEmployeeSigmaService.update(nextEmployeeSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeSigma.getId().toString()))
            .body(nextEmployeeSigma);
    }

    /**
     * {@code PATCH  /next-employee-sigmas/:id} : Partial updates given fields of an existing nextEmployeeSigma, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeSigma to save.
     * @param nextEmployeeSigma the nextEmployeeSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeSigma,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeSigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeSigma> partialUpdateNextEmployeeSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeSigma nextEmployeeSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeSigma partially : {}, {}", id, nextEmployeeSigma);
        if (nextEmployeeSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeSigma> result = nextEmployeeSigmaService.partialUpdate(nextEmployeeSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-sigmas} : get all the nextEmployeeSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeSigma>> getAllNextEmployeeSigmas(
        NextEmployeeSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeSigmas by criteria: {}", criteria);

        Page<NextEmployeeSigma> page = nextEmployeeSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-sigmas/count} : count all the nextEmployeeSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeSigmas(NextEmployeeSigmaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-sigmas/:id} : get the "id" nextEmployeeSigma.
     *
     * @param id the id of the nextEmployeeSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeSigma> getNextEmployeeSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeSigma : {}", id);
        Optional<NextEmployeeSigma> nextEmployeeSigma = nextEmployeeSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeSigma);
    }

    /**
     * {@code DELETE  /next-employee-sigmas/:id} : delete the "id" nextEmployeeSigma.
     *
     * @param id the id of the nextEmployeeSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeSigma : {}", id);
        nextEmployeeSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
