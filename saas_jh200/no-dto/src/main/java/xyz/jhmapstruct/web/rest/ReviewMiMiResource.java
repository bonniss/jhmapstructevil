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
import xyz.jhmapstruct.domain.ReviewMiMi;
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.ReviewMiMiQueryService;
import xyz.jhmapstruct.service.ReviewMiMiService;
import xyz.jhmapstruct.service.criteria.ReviewMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewMiMi}.
 */
@RestController
@RequestMapping("/api/review-mi-mis")
public class ReviewMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiMiResource.class);

    private static final String ENTITY_NAME = "reviewMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewMiMiService reviewMiMiService;

    private final ReviewMiMiRepository reviewMiMiRepository;

    private final ReviewMiMiQueryService reviewMiMiQueryService;

    public ReviewMiMiResource(
        ReviewMiMiService reviewMiMiService,
        ReviewMiMiRepository reviewMiMiRepository,
        ReviewMiMiQueryService reviewMiMiQueryService
    ) {
        this.reviewMiMiService = reviewMiMiService;
        this.reviewMiMiRepository = reviewMiMiRepository;
        this.reviewMiMiQueryService = reviewMiMiQueryService;
    }

    /**
     * {@code POST  /review-mi-mis} : Create a new reviewMiMi.
     *
     * @param reviewMiMi the reviewMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewMiMi, or with status {@code 400 (Bad Request)} if the reviewMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewMiMi> createReviewMiMi(@Valid @RequestBody ReviewMiMi reviewMiMi) throws URISyntaxException {
        LOG.debug("REST request to save ReviewMiMi : {}", reviewMiMi);
        if (reviewMiMi.getId() != null) {
            throw new BadRequestAlertException("A new reviewMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewMiMi = reviewMiMiService.save(reviewMiMi);
        return ResponseEntity.created(new URI("/api/review-mi-mis/" + reviewMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewMiMi.getId().toString()))
            .body(reviewMiMi);
    }

    /**
     * {@code PUT  /review-mi-mis/:id} : Updates an existing reviewMiMi.
     *
     * @param id the id of the reviewMiMi to save.
     * @param reviewMiMi the reviewMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiMi,
     * or with status {@code 400 (Bad Request)} if the reviewMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewMiMi> updateReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewMiMi reviewMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewMiMi : {}, {}", id, reviewMiMi);
        if (reviewMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewMiMi = reviewMiMiService.update(reviewMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiMi.getId().toString()))
            .body(reviewMiMi);
    }

    /**
     * {@code PATCH  /review-mi-mis/:id} : Partial updates given fields of an existing reviewMiMi, field will ignore if it is null
     *
     * @param id the id of the reviewMiMi to save.
     * @param reviewMiMi the reviewMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiMi,
     * or with status {@code 400 (Bad Request)} if the reviewMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the reviewMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewMiMi> partialUpdateReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewMiMi reviewMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewMiMi partially : {}, {}", id, reviewMiMi);
        if (reviewMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewMiMi> result = reviewMiMiService.partialUpdate(reviewMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /review-mi-mis} : get all the reviewMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewMiMi>> getAllReviewMiMis(
        ReviewMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewMiMis by criteria: {}", criteria);

        Page<ReviewMiMi> page = reviewMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-mi-mis/count} : count all the reviewMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewMiMis(ReviewMiMiCriteria criteria) {
        LOG.debug("REST request to count ReviewMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-mi-mis/:id} : get the "id" reviewMiMi.
     *
     * @param id the id of the reviewMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewMiMi> getReviewMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewMiMi : {}", id);
        Optional<ReviewMiMi> reviewMiMi = reviewMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewMiMi);
    }

    /**
     * {@code DELETE  /review-mi-mis/:id} : delete the "id" reviewMiMi.
     *
     * @param id the id of the reviewMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewMiMi : {}", id);
        reviewMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
