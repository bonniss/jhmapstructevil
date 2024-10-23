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
import xyz.jhmapstruct.domain.NextReviewBeta;
import xyz.jhmapstruct.repository.NextReviewBetaRepository;
import xyz.jhmapstruct.service.NextReviewBetaQueryService;
import xyz.jhmapstruct.service.NextReviewBetaService;
import xyz.jhmapstruct.service.criteria.NextReviewBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewBeta}.
 */
@RestController
@RequestMapping("/api/next-review-betas")
public class NextReviewBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewBetaResource.class);

    private static final String ENTITY_NAME = "nextReviewBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewBetaService nextReviewBetaService;

    private final NextReviewBetaRepository nextReviewBetaRepository;

    private final NextReviewBetaQueryService nextReviewBetaQueryService;

    public NextReviewBetaResource(
        NextReviewBetaService nextReviewBetaService,
        NextReviewBetaRepository nextReviewBetaRepository,
        NextReviewBetaQueryService nextReviewBetaQueryService
    ) {
        this.nextReviewBetaService = nextReviewBetaService;
        this.nextReviewBetaRepository = nextReviewBetaRepository;
        this.nextReviewBetaQueryService = nextReviewBetaQueryService;
    }

    /**
     * {@code POST  /next-review-betas} : Create a new nextReviewBeta.
     *
     * @param nextReviewBeta the nextReviewBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewBeta, or with status {@code 400 (Bad Request)} if the nextReviewBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewBeta> createNextReviewBeta(@Valid @RequestBody NextReviewBeta nextReviewBeta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewBeta : {}", nextReviewBeta);
        if (nextReviewBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewBeta = nextReviewBetaService.save(nextReviewBeta);
        return ResponseEntity.created(new URI("/api/next-review-betas/" + nextReviewBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewBeta.getId().toString()))
            .body(nextReviewBeta);
    }

    /**
     * {@code PUT  /next-review-betas/:id} : Updates an existing nextReviewBeta.
     *
     * @param id the id of the nextReviewBeta to save.
     * @param nextReviewBeta the nextReviewBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewBeta,
     * or with status {@code 400 (Bad Request)} if the nextReviewBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewBeta> updateNextReviewBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewBeta nextReviewBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewBeta : {}, {}", id, nextReviewBeta);
        if (nextReviewBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewBeta = nextReviewBetaService.update(nextReviewBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewBeta.getId().toString()))
            .body(nextReviewBeta);
    }

    /**
     * {@code PATCH  /next-review-betas/:id} : Partial updates given fields of an existing nextReviewBeta, field will ignore if it is null
     *
     * @param id the id of the nextReviewBeta to save.
     * @param nextReviewBeta the nextReviewBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewBeta,
     * or with status {@code 400 (Bad Request)} if the nextReviewBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewBeta> partialUpdateNextReviewBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewBeta nextReviewBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewBeta partially : {}, {}", id, nextReviewBeta);
        if (nextReviewBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewBeta> result = nextReviewBetaService.partialUpdate(nextReviewBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-betas} : get all the nextReviewBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewBeta>> getAllNextReviewBetas(
        NextReviewBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewBetas by criteria: {}", criteria);

        Page<NextReviewBeta> page = nextReviewBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-betas/count} : count all the nextReviewBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewBetas(NextReviewBetaCriteria criteria) {
        LOG.debug("REST request to count NextReviewBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-betas/:id} : get the "id" nextReviewBeta.
     *
     * @param id the id of the nextReviewBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewBeta> getNextReviewBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewBeta : {}", id);
        Optional<NextReviewBeta> nextReviewBeta = nextReviewBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewBeta);
    }

    /**
     * {@code DELETE  /next-review-betas/:id} : delete the "id" nextReviewBeta.
     *
     * @param id the id of the nextReviewBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewBeta : {}", id);
        nextReviewBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
