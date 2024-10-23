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
import xyz.jhmapstruct.domain.ReviewSigma;
import xyz.jhmapstruct.repository.ReviewSigmaRepository;
import xyz.jhmapstruct.service.ReviewSigmaQueryService;
import xyz.jhmapstruct.service.ReviewSigmaService;
import xyz.jhmapstruct.service.criteria.ReviewSigmaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewSigma}.
 */
@RestController
@RequestMapping("/api/review-sigmas")
public class ReviewSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewSigmaResource.class);

    private static final String ENTITY_NAME = "reviewSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewSigmaService reviewSigmaService;

    private final ReviewSigmaRepository reviewSigmaRepository;

    private final ReviewSigmaQueryService reviewSigmaQueryService;

    public ReviewSigmaResource(
        ReviewSigmaService reviewSigmaService,
        ReviewSigmaRepository reviewSigmaRepository,
        ReviewSigmaQueryService reviewSigmaQueryService
    ) {
        this.reviewSigmaService = reviewSigmaService;
        this.reviewSigmaRepository = reviewSigmaRepository;
        this.reviewSigmaQueryService = reviewSigmaQueryService;
    }

    /**
     * {@code POST  /review-sigmas} : Create a new reviewSigma.
     *
     * @param reviewSigma the reviewSigma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewSigma, or with status {@code 400 (Bad Request)} if the reviewSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewSigma> createReviewSigma(@Valid @RequestBody ReviewSigma reviewSigma) throws URISyntaxException {
        LOG.debug("REST request to save ReviewSigma : {}", reviewSigma);
        if (reviewSigma.getId() != null) {
            throw new BadRequestAlertException("A new reviewSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewSigma = reviewSigmaService.save(reviewSigma);
        return ResponseEntity.created(new URI("/api/review-sigmas/" + reviewSigma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewSigma.getId().toString()))
            .body(reviewSigma);
    }

    /**
     * {@code PUT  /review-sigmas/:id} : Updates an existing reviewSigma.
     *
     * @param id the id of the reviewSigma to save.
     * @param reviewSigma the reviewSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewSigma,
     * or with status {@code 400 (Bad Request)} if the reviewSigma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewSigma> updateReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewSigma reviewSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewSigma : {}, {}", id, reviewSigma);
        if (reviewSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewSigma = reviewSigmaService.update(reviewSigma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewSigma.getId().toString()))
            .body(reviewSigma);
    }

    /**
     * {@code PATCH  /review-sigmas/:id} : Partial updates given fields of an existing reviewSigma, field will ignore if it is null
     *
     * @param id the id of the reviewSigma to save.
     * @param reviewSigma the reviewSigma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewSigma,
     * or with status {@code 400 (Bad Request)} if the reviewSigma is not valid,
     * or with status {@code 404 (Not Found)} if the reviewSigma is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewSigma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewSigma> partialUpdateReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewSigma reviewSigma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewSigma partially : {}, {}", id, reviewSigma);
        if (reviewSigma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewSigma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewSigma> result = reviewSigmaService.partialUpdate(reviewSigma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewSigma.getId().toString())
        );
    }

    /**
     * {@code GET  /review-sigmas} : get all the reviewSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewSigma>> getAllReviewSigmas(
        ReviewSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewSigmas by criteria: {}", criteria);

        Page<ReviewSigma> page = reviewSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-sigmas/count} : count all the reviewSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewSigmas(ReviewSigmaCriteria criteria) {
        LOG.debug("REST request to count ReviewSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-sigmas/:id} : get the "id" reviewSigma.
     *
     * @param id the id of the reviewSigma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewSigma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewSigma> getReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewSigma : {}", id);
        Optional<ReviewSigma> reviewSigma = reviewSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewSigma);
    }

    /**
     * {@code DELETE  /review-sigmas/:id} : delete the "id" reviewSigma.
     *
     * @param id the id of the reviewSigma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewSigma : {}", id);
        reviewSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
