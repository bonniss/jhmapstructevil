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
import xyz.jhmapstruct.domain.ReviewBeta;
import xyz.jhmapstruct.repository.ReviewBetaRepository;
import xyz.jhmapstruct.service.ReviewBetaQueryService;
import xyz.jhmapstruct.service.ReviewBetaService;
import xyz.jhmapstruct.service.criteria.ReviewBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewBeta}.
 */
@RestController
@RequestMapping("/api/review-betas")
public class ReviewBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewBetaResource.class);

    private static final String ENTITY_NAME = "reviewBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewBetaService reviewBetaService;

    private final ReviewBetaRepository reviewBetaRepository;

    private final ReviewBetaQueryService reviewBetaQueryService;

    public ReviewBetaResource(
        ReviewBetaService reviewBetaService,
        ReviewBetaRepository reviewBetaRepository,
        ReviewBetaQueryService reviewBetaQueryService
    ) {
        this.reviewBetaService = reviewBetaService;
        this.reviewBetaRepository = reviewBetaRepository;
        this.reviewBetaQueryService = reviewBetaQueryService;
    }

    /**
     * {@code POST  /review-betas} : Create a new reviewBeta.
     *
     * @param reviewBeta the reviewBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewBeta, or with status {@code 400 (Bad Request)} if the reviewBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewBeta> createReviewBeta(@Valid @RequestBody ReviewBeta reviewBeta) throws URISyntaxException {
        LOG.debug("REST request to save ReviewBeta : {}", reviewBeta);
        if (reviewBeta.getId() != null) {
            throw new BadRequestAlertException("A new reviewBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewBeta = reviewBetaService.save(reviewBeta);
        return ResponseEntity.created(new URI("/api/review-betas/" + reviewBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewBeta.getId().toString()))
            .body(reviewBeta);
    }

    /**
     * {@code PUT  /review-betas/:id} : Updates an existing reviewBeta.
     *
     * @param id the id of the reviewBeta to save.
     * @param reviewBeta the reviewBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewBeta,
     * or with status {@code 400 (Bad Request)} if the reviewBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewBeta> updateReviewBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewBeta reviewBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewBeta : {}, {}", id, reviewBeta);
        if (reviewBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewBeta = reviewBetaService.update(reviewBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewBeta.getId().toString()))
            .body(reviewBeta);
    }

    /**
     * {@code PATCH  /review-betas/:id} : Partial updates given fields of an existing reviewBeta, field will ignore if it is null
     *
     * @param id the id of the reviewBeta to save.
     * @param reviewBeta the reviewBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewBeta,
     * or with status {@code 400 (Bad Request)} if the reviewBeta is not valid,
     * or with status {@code 404 (Not Found)} if the reviewBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewBeta> partialUpdateReviewBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewBeta reviewBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewBeta partially : {}, {}", id, reviewBeta);
        if (reviewBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewBeta> result = reviewBetaService.partialUpdate(reviewBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /review-betas} : get all the reviewBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewBeta>> getAllReviewBetas(
        ReviewBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewBetas by criteria: {}", criteria);

        Page<ReviewBeta> page = reviewBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-betas/count} : count all the reviewBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewBetas(ReviewBetaCriteria criteria) {
        LOG.debug("REST request to count ReviewBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-betas/:id} : get the "id" reviewBeta.
     *
     * @param id the id of the reviewBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewBeta> getReviewBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewBeta : {}", id);
        Optional<ReviewBeta> reviewBeta = reviewBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewBeta);
    }

    /**
     * {@code DELETE  /review-betas/:id} : delete the "id" reviewBeta.
     *
     * @param id the id of the reviewBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewBeta : {}", id);
        reviewBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
