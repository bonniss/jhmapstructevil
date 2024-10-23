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
import xyz.jhmapstruct.domain.NextReviewMi;
import xyz.jhmapstruct.repository.NextReviewMiRepository;
import xyz.jhmapstruct.service.NextReviewMiQueryService;
import xyz.jhmapstruct.service.NextReviewMiService;
import xyz.jhmapstruct.service.criteria.NextReviewMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewMi}.
 */
@RestController
@RequestMapping("/api/next-review-mis")
public class NextReviewMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewMiResource.class);

    private static final String ENTITY_NAME = "nextReviewMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewMiService nextReviewMiService;

    private final NextReviewMiRepository nextReviewMiRepository;

    private final NextReviewMiQueryService nextReviewMiQueryService;

    public NextReviewMiResource(
        NextReviewMiService nextReviewMiService,
        NextReviewMiRepository nextReviewMiRepository,
        NextReviewMiQueryService nextReviewMiQueryService
    ) {
        this.nextReviewMiService = nextReviewMiService;
        this.nextReviewMiRepository = nextReviewMiRepository;
        this.nextReviewMiQueryService = nextReviewMiQueryService;
    }

    /**
     * {@code POST  /next-review-mis} : Create a new nextReviewMi.
     *
     * @param nextReviewMi the nextReviewMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewMi, or with status {@code 400 (Bad Request)} if the nextReviewMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewMi> createNextReviewMi(@Valid @RequestBody NextReviewMi nextReviewMi) throws URISyntaxException {
        LOG.debug("REST request to save NextReviewMi : {}", nextReviewMi);
        if (nextReviewMi.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewMi = nextReviewMiService.save(nextReviewMi);
        return ResponseEntity.created(new URI("/api/next-review-mis/" + nextReviewMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewMi.getId().toString()))
            .body(nextReviewMi);
    }

    /**
     * {@code PUT  /next-review-mis/:id} : Updates an existing nextReviewMi.
     *
     * @param id the id of the nextReviewMi to save.
     * @param nextReviewMi the nextReviewMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewMi,
     * or with status {@code 400 (Bad Request)} if the nextReviewMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewMi> updateNextReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewMi nextReviewMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewMi : {}, {}", id, nextReviewMi);
        if (nextReviewMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewMi = nextReviewMiService.update(nextReviewMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewMi.getId().toString()))
            .body(nextReviewMi);
    }

    /**
     * {@code PATCH  /next-review-mis/:id} : Partial updates given fields of an existing nextReviewMi, field will ignore if it is null
     *
     * @param id the id of the nextReviewMi to save.
     * @param nextReviewMi the nextReviewMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewMi,
     * or with status {@code 400 (Bad Request)} if the nextReviewMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewMi> partialUpdateNextReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewMi nextReviewMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewMi partially : {}, {}", id, nextReviewMi);
        if (nextReviewMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewMi> result = nextReviewMiService.partialUpdate(nextReviewMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-mis} : get all the nextReviewMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewMi>> getAllNextReviewMis(
        NextReviewMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewMis by criteria: {}", criteria);

        Page<NextReviewMi> page = nextReviewMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-mis/count} : count all the nextReviewMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewMis(NextReviewMiCriteria criteria) {
        LOG.debug("REST request to count NextReviewMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-mis/:id} : get the "id" nextReviewMi.
     *
     * @param id the id of the nextReviewMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewMi> getNextReviewMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewMi : {}", id);
        Optional<NextReviewMi> nextReviewMi = nextReviewMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewMi);
    }

    /**
     * {@code DELETE  /next-review-mis/:id} : delete the "id" nextReviewMi.
     *
     * @param id the id of the nextReviewMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewMi : {}", id);
        nextReviewMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
