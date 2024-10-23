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
import xyz.jhmapstruct.domain.ReviewGamma;
import xyz.jhmapstruct.repository.ReviewGammaRepository;
import xyz.jhmapstruct.service.ReviewGammaQueryService;
import xyz.jhmapstruct.service.ReviewGammaService;
import xyz.jhmapstruct.service.criteria.ReviewGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewGamma}.
 */
@RestController
@RequestMapping("/api/review-gammas")
public class ReviewGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewGammaResource.class);

    private static final String ENTITY_NAME = "reviewGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewGammaService reviewGammaService;

    private final ReviewGammaRepository reviewGammaRepository;

    private final ReviewGammaQueryService reviewGammaQueryService;

    public ReviewGammaResource(
        ReviewGammaService reviewGammaService,
        ReviewGammaRepository reviewGammaRepository,
        ReviewGammaQueryService reviewGammaQueryService
    ) {
        this.reviewGammaService = reviewGammaService;
        this.reviewGammaRepository = reviewGammaRepository;
        this.reviewGammaQueryService = reviewGammaQueryService;
    }

    /**
     * {@code POST  /review-gammas} : Create a new reviewGamma.
     *
     * @param reviewGamma the reviewGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewGamma, or with status {@code 400 (Bad Request)} if the reviewGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewGamma> createReviewGamma(@Valid @RequestBody ReviewGamma reviewGamma) throws URISyntaxException {
        LOG.debug("REST request to save ReviewGamma : {}", reviewGamma);
        if (reviewGamma.getId() != null) {
            throw new BadRequestAlertException("A new reviewGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewGamma = reviewGammaService.save(reviewGamma);
        return ResponseEntity.created(new URI("/api/review-gammas/" + reviewGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewGamma.getId().toString()))
            .body(reviewGamma);
    }

    /**
     * {@code PUT  /review-gammas/:id} : Updates an existing reviewGamma.
     *
     * @param id the id of the reviewGamma to save.
     * @param reviewGamma the reviewGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewGamma,
     * or with status {@code 400 (Bad Request)} if the reviewGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewGamma> updateReviewGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewGamma reviewGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewGamma : {}, {}", id, reviewGamma);
        if (reviewGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewGamma = reviewGammaService.update(reviewGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewGamma.getId().toString()))
            .body(reviewGamma);
    }

    /**
     * {@code PATCH  /review-gammas/:id} : Partial updates given fields of an existing reviewGamma, field will ignore if it is null
     *
     * @param id the id of the reviewGamma to save.
     * @param reviewGamma the reviewGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewGamma,
     * or with status {@code 400 (Bad Request)} if the reviewGamma is not valid,
     * or with status {@code 404 (Not Found)} if the reviewGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewGamma> partialUpdateReviewGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewGamma reviewGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewGamma partially : {}, {}", id, reviewGamma);
        if (reviewGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewGamma> result = reviewGammaService.partialUpdate(reviewGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /review-gammas} : get all the reviewGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewGamma>> getAllReviewGammas(
        ReviewGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewGammas by criteria: {}", criteria);

        Page<ReviewGamma> page = reviewGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-gammas/count} : count all the reviewGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewGammas(ReviewGammaCriteria criteria) {
        LOG.debug("REST request to count ReviewGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-gammas/:id} : get the "id" reviewGamma.
     *
     * @param id the id of the reviewGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewGamma> getReviewGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewGamma : {}", id);
        Optional<ReviewGamma> reviewGamma = reviewGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewGamma);
    }

    /**
     * {@code DELETE  /review-gammas/:id} : delete the "id" reviewGamma.
     *
     * @param id the id of the reviewGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewGamma : {}", id);
        reviewGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
