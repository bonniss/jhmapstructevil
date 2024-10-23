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
import xyz.jhmapstruct.domain.NextReviewTheta;
import xyz.jhmapstruct.repository.NextReviewThetaRepository;
import xyz.jhmapstruct.service.NextReviewThetaQueryService;
import xyz.jhmapstruct.service.NextReviewThetaService;
import xyz.jhmapstruct.service.criteria.NextReviewThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewTheta}.
 */
@RestController
@RequestMapping("/api/next-review-thetas")
public class NextReviewThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewThetaResource.class);

    private static final String ENTITY_NAME = "nextReviewTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewThetaService nextReviewThetaService;

    private final NextReviewThetaRepository nextReviewThetaRepository;

    private final NextReviewThetaQueryService nextReviewThetaQueryService;

    public NextReviewThetaResource(
        NextReviewThetaService nextReviewThetaService,
        NextReviewThetaRepository nextReviewThetaRepository,
        NextReviewThetaQueryService nextReviewThetaQueryService
    ) {
        this.nextReviewThetaService = nextReviewThetaService;
        this.nextReviewThetaRepository = nextReviewThetaRepository;
        this.nextReviewThetaQueryService = nextReviewThetaQueryService;
    }

    /**
     * {@code POST  /next-review-thetas} : Create a new nextReviewTheta.
     *
     * @param nextReviewTheta the nextReviewTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewTheta, or with status {@code 400 (Bad Request)} if the nextReviewTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewTheta> createNextReviewTheta(@Valid @RequestBody NextReviewTheta nextReviewTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewTheta : {}", nextReviewTheta);
        if (nextReviewTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewTheta = nextReviewThetaService.save(nextReviewTheta);
        return ResponseEntity.created(new URI("/api/next-review-thetas/" + nextReviewTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewTheta.getId().toString()))
            .body(nextReviewTheta);
    }

    /**
     * {@code PUT  /next-review-thetas/:id} : Updates an existing nextReviewTheta.
     *
     * @param id the id of the nextReviewTheta to save.
     * @param nextReviewTheta the nextReviewTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewTheta,
     * or with status {@code 400 (Bad Request)} if the nextReviewTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewTheta> updateNextReviewTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewTheta nextReviewTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewTheta : {}, {}", id, nextReviewTheta);
        if (nextReviewTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewTheta = nextReviewThetaService.update(nextReviewTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewTheta.getId().toString()))
            .body(nextReviewTheta);
    }

    /**
     * {@code PATCH  /next-review-thetas/:id} : Partial updates given fields of an existing nextReviewTheta, field will ignore if it is null
     *
     * @param id the id of the nextReviewTheta to save.
     * @param nextReviewTheta the nextReviewTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewTheta,
     * or with status {@code 400 (Bad Request)} if the nextReviewTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewTheta> partialUpdateNextReviewTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewTheta nextReviewTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewTheta partially : {}, {}", id, nextReviewTheta);
        if (nextReviewTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewTheta> result = nextReviewThetaService.partialUpdate(nextReviewTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-thetas} : get all the nextReviewThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewTheta>> getAllNextReviewThetas(
        NextReviewThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewThetas by criteria: {}", criteria);

        Page<NextReviewTheta> page = nextReviewThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-thetas/count} : count all the nextReviewThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewThetas(NextReviewThetaCriteria criteria) {
        LOG.debug("REST request to count NextReviewThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-thetas/:id} : get the "id" nextReviewTheta.
     *
     * @param id the id of the nextReviewTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewTheta> getNextReviewTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewTheta : {}", id);
        Optional<NextReviewTheta> nextReviewTheta = nextReviewThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewTheta);
    }

    /**
     * {@code DELETE  /next-review-thetas/:id} : delete the "id" nextReviewTheta.
     *
     * @param id the id of the nextReviewTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewTheta : {}", id);
        nextReviewThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
