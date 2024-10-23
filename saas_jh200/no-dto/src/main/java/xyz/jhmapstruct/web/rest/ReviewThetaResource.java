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
import xyz.jhmapstruct.domain.ReviewTheta;
import xyz.jhmapstruct.repository.ReviewThetaRepository;
import xyz.jhmapstruct.service.ReviewThetaQueryService;
import xyz.jhmapstruct.service.ReviewThetaService;
import xyz.jhmapstruct.service.criteria.ReviewThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewTheta}.
 */
@RestController
@RequestMapping("/api/review-thetas")
public class ReviewThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewThetaResource.class);

    private static final String ENTITY_NAME = "reviewTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewThetaService reviewThetaService;

    private final ReviewThetaRepository reviewThetaRepository;

    private final ReviewThetaQueryService reviewThetaQueryService;

    public ReviewThetaResource(
        ReviewThetaService reviewThetaService,
        ReviewThetaRepository reviewThetaRepository,
        ReviewThetaQueryService reviewThetaQueryService
    ) {
        this.reviewThetaService = reviewThetaService;
        this.reviewThetaRepository = reviewThetaRepository;
        this.reviewThetaQueryService = reviewThetaQueryService;
    }

    /**
     * {@code POST  /review-thetas} : Create a new reviewTheta.
     *
     * @param reviewTheta the reviewTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewTheta, or with status {@code 400 (Bad Request)} if the reviewTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewTheta> createReviewTheta(@Valid @RequestBody ReviewTheta reviewTheta) throws URISyntaxException {
        LOG.debug("REST request to save ReviewTheta : {}", reviewTheta);
        if (reviewTheta.getId() != null) {
            throw new BadRequestAlertException("A new reviewTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewTheta = reviewThetaService.save(reviewTheta);
        return ResponseEntity.created(new URI("/api/review-thetas/" + reviewTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewTheta.getId().toString()))
            .body(reviewTheta);
    }

    /**
     * {@code PUT  /review-thetas/:id} : Updates an existing reviewTheta.
     *
     * @param id the id of the reviewTheta to save.
     * @param reviewTheta the reviewTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewTheta,
     * or with status {@code 400 (Bad Request)} if the reviewTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewTheta> updateReviewTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewTheta reviewTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewTheta : {}, {}", id, reviewTheta);
        if (reviewTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewTheta = reviewThetaService.update(reviewTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewTheta.getId().toString()))
            .body(reviewTheta);
    }

    /**
     * {@code PATCH  /review-thetas/:id} : Partial updates given fields of an existing reviewTheta, field will ignore if it is null
     *
     * @param id the id of the reviewTheta to save.
     * @param reviewTheta the reviewTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewTheta,
     * or with status {@code 400 (Bad Request)} if the reviewTheta is not valid,
     * or with status {@code 404 (Not Found)} if the reviewTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewTheta> partialUpdateReviewTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewTheta reviewTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewTheta partially : {}, {}", id, reviewTheta);
        if (reviewTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewTheta> result = reviewThetaService.partialUpdate(reviewTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /review-thetas} : get all the reviewThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewTheta>> getAllReviewThetas(
        ReviewThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewThetas by criteria: {}", criteria);

        Page<ReviewTheta> page = reviewThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-thetas/count} : count all the reviewThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewThetas(ReviewThetaCriteria criteria) {
        LOG.debug("REST request to count ReviewThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-thetas/:id} : get the "id" reviewTheta.
     *
     * @param id the id of the reviewTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewTheta> getReviewTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewTheta : {}", id);
        Optional<ReviewTheta> reviewTheta = reviewThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewTheta);
    }

    /**
     * {@code DELETE  /review-thetas/:id} : delete the "id" reviewTheta.
     *
     * @param id the id of the reviewTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewTheta : {}", id);
        reviewThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
