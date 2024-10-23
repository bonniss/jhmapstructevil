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
import xyz.jhmapstruct.domain.NextReview;
import xyz.jhmapstruct.repository.NextReviewRepository;
import xyz.jhmapstruct.service.NextReviewQueryService;
import xyz.jhmapstruct.service.NextReviewService;
import xyz.jhmapstruct.service.criteria.NextReviewCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReview}.
 */
@RestController
@RequestMapping("/api/next-reviews")
public class NextReviewResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewResource.class);

    private static final String ENTITY_NAME = "nextReview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewService nextReviewService;

    private final NextReviewRepository nextReviewRepository;

    private final NextReviewQueryService nextReviewQueryService;

    public NextReviewResource(
        NextReviewService nextReviewService,
        NextReviewRepository nextReviewRepository,
        NextReviewQueryService nextReviewQueryService
    ) {
        this.nextReviewService = nextReviewService;
        this.nextReviewRepository = nextReviewRepository;
        this.nextReviewQueryService = nextReviewQueryService;
    }

    /**
     * {@code POST  /next-reviews} : Create a new nextReview.
     *
     * @param nextReview the nextReview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReview, or with status {@code 400 (Bad Request)} if the nextReview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReview> createNextReview(@Valid @RequestBody NextReview nextReview) throws URISyntaxException {
        LOG.debug("REST request to save NextReview : {}", nextReview);
        if (nextReview.getId() != null) {
            throw new BadRequestAlertException("A new nextReview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReview = nextReviewService.save(nextReview);
        return ResponseEntity.created(new URI("/api/next-reviews/" + nextReview.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReview.getId().toString()))
            .body(nextReview);
    }

    /**
     * {@code PUT  /next-reviews/:id} : Updates an existing nextReview.
     *
     * @param id the id of the nextReview to save.
     * @param nextReview the nextReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReview,
     * or with status {@code 400 (Bad Request)} if the nextReview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReview> updateNextReview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReview nextReview
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReview : {}, {}", id, nextReview);
        if (nextReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReview = nextReviewService.update(nextReview);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReview.getId().toString()))
            .body(nextReview);
    }

    /**
     * {@code PATCH  /next-reviews/:id} : Partial updates given fields of an existing nextReview, field will ignore if it is null
     *
     * @param id the id of the nextReview to save.
     * @param nextReview the nextReview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReview,
     * or with status {@code 400 (Bad Request)} if the nextReview is not valid,
     * or with status {@code 404 (Not Found)} if the nextReview is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReview> partialUpdateNextReview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReview nextReview
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReview partially : {}, {}", id, nextReview);
        if (nextReview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReview> result = nextReviewService.partialUpdate(nextReview);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReview.getId().toString())
        );
    }

    /**
     * {@code GET  /next-reviews} : get all the nextReviews.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviews in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReview>> getAllNextReviews(
        NextReviewCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviews by criteria: {}", criteria);

        Page<NextReview> page = nextReviewQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-reviews/count} : count all the nextReviews.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviews(NextReviewCriteria criteria) {
        LOG.debug("REST request to count NextReviews by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-reviews/:id} : get the "id" nextReview.
     *
     * @param id the id of the nextReview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReview> getNextReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReview : {}", id);
        Optional<NextReview> nextReview = nextReviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReview);
    }

    /**
     * {@code DELETE  /next-reviews/:id} : delete the "id" nextReview.
     *
     * @param id the id of the nextReview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReview(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReview : {}", id);
        nextReviewService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
