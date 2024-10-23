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
import xyz.jhmapstruct.domain.NextCategorySigma;
import xyz.jhmapstruct.repository.NextCategorySigmaRepository;
import xyz.jhmapstruct.service.NextCategorySigmaQueryService;
import xyz.jhmapstruct.service.NextCategorySigmaService;
import xyz.jhmapstruct.service.criteria.NextCategorySigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategorySigma}.
 */
@RestController
@RequestMapping("/api/next-category-sigmas")
public class NextCategorySigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategorySigmaResource.class);

    private static final String ENTITY_NAME = "nextCategorySigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategorySigmaService nextCategorySigmaService;

    private final NextCategorySigmaRepository nextCategorySigmaRepository;

    private final NextCategorySigmaQueryService nextCategorySigmaQueryService;

    public NextCategorySigmaResource(
        NextCategorySigmaService nextCategorySigmaService,
        NextCategorySigmaRepository nextCategorySigmaRepository,
        NextCategorySigmaQueryService nextCategorySigmaQueryService
    ) {
        this.nextCategorySigmaService = nextCategorySigmaService;
        this.nextCategorySigmaRepository = nextCategorySigmaRepository;
        this.nextCategorySigmaQueryService = nextCategorySigmaQueryService;
    }

    /**
     * {@code POST  /next-category-sigmas} : Create a new nextCategorySigma.
     *
     * @param nextCategorySigma the nextCategorySigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategorySigma, or with status {@code 400 (Bad Request)} if the nextCategorySigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategorySigma> createNextCategorySigma(@Valid @RequestBody NextCategorySigma nextCategorySigma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategorySigma : {}", nextCategorySigma);
        if (nextCategorySigma.getId() != null) {
            throw new BadRequestAlertException("A new nextCategorySigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategorySigma = nextCategorySigmaService.save(nextCategorySigma);
        return ResponseEntity.created(new URI("/api/next-category-sigmas/" + nextCategorySigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategorySigma.getId().toString()))
            .body(nextCategorySigma);
    }

    /**
     * {@code PUT  /next-category-sigmas/:id} : Updates an existing nextCategorySigma.
     *
     * @param id the id of the nextCategorySigma to save.
     * @param nextCategorySigma the nextCategorySigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategorySigma,
     * or with status {@code 400 (Bad Request)} if the nextCategorySigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategorySigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategorySigma> updateNextCategorySigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategorySigma nextCategorySigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategorySigma : {}, {}", id, nextCategorySigma);
        if (nextCategorySigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategorySigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategorySigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategorySigma = nextCategorySigmaService.update(nextCategorySigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategorySigma.getId().toString()))
            .body(nextCategorySigma);
    }

    /**
     * {@code PATCH  /next-category-sigmas/:id} : Partial updates given fields of an existing nextCategorySigma, field will ignore if it is null
     *
     * @param id the id of the nextCategorySigma to save.
     * @param nextCategorySigma the nextCategorySigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategorySigma,
     * or with status {@code 400 (Bad Request)} if the nextCategorySigma is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategorySigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategorySigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategorySigma> partialUpdateNextCategorySigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategorySigma nextCategorySigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategorySigma partially : {}, {}", id, nextCategorySigma);
        if (nextCategorySigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategorySigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategorySigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategorySigma> result = nextCategorySigmaService.partialUpdate(nextCategorySigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategorySigma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-sigmas} : get all the nextCategorySigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategorySigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategorySigma>> getAllNextCategorySigmas(
        NextCategorySigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategorySigmas by criteria: {}", criteria);

        Page<NextCategorySigma> page = nextCategorySigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-sigmas/count} : count all the nextCategorySigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategorySigmas(NextCategorySigmaCriteria criteria) {
        LOG.debug("REST request to count NextCategorySigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategorySigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-sigmas/:id} : get the "id" nextCategorySigma.
     *
     * @param id the id of the nextCategorySigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategorySigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategorySigma> getNextCategorySigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategorySigma : {}", id);
        Optional<NextCategorySigma> nextCategorySigma = nextCategorySigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategorySigma);
    }

    /**
     * {@code DELETE  /next-category-sigmas/:id} : delete the "id" nextCategorySigma.
     *
     * @param id the id of the nextCategorySigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategorySigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategorySigma : {}", id);
        nextCategorySigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
