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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.ReviewMi;
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.ReviewMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewMi}.
 */
@RestController
@RequestMapping("/api/review-mis")
public class ReviewMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewMiResource.class);

    private static final String ENTITY_NAME = "reviewMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewMiService reviewMiService;

    private final ReviewMiRepository reviewMiRepository;

    public ReviewMiResource(ReviewMiService reviewMiService, ReviewMiRepository reviewMiRepository) {
        this.reviewMiService = reviewMiService;
        this.reviewMiRepository = reviewMiRepository;
    }

    /**
     * {@code POST  /review-mis} : Create a new reviewMi.
     *
     * @param reviewMi the reviewMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewMi, or with status {@code 400 (Bad Request)} if the reviewMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewMi> createReviewMi(@Valid @RequestBody ReviewMi reviewMi) throws URISyntaxException {
        LOG.debug("REST request to save ReviewMi : {}", reviewMi);
        if (reviewMi.getId() != null) {
            throw new BadRequestAlertException("A new reviewMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewMi = reviewMiService.save(reviewMi);
        return ResponseEntity.created(new URI("/api/review-mis/" + reviewMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewMi.getId().toString()))
            .body(reviewMi);
    }

    /**
     * {@code PUT  /review-mis/:id} : Updates an existing reviewMi.
     *
     * @param id the id of the reviewMi to save.
     * @param reviewMi the reviewMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMi,
     * or with status {@code 400 (Bad Request)} if the reviewMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewMi> updateReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewMi reviewMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewMi : {}, {}", id, reviewMi);
        if (reviewMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewMi = reviewMiService.update(reviewMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMi.getId().toString()))
            .body(reviewMi);
    }

    /**
     * {@code PATCH  /review-mis/:id} : Partial updates given fields of an existing reviewMi, field will ignore if it is null
     *
     * @param id the id of the reviewMi to save.
     * @param reviewMi the reviewMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMi,
     * or with status {@code 400 (Bad Request)} if the reviewMi is not valid,
     * or with status {@code 404 (Not Found)} if the reviewMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewMi> partialUpdateReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewMi reviewMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewMi partially : {}, {}", id, reviewMi);
        if (reviewMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewMi> result = reviewMiService.partialUpdate(reviewMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMi.getId().toString())
        );
    }

    /**
     * {@code GET  /review-mis} : get all the reviewMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewMis in body.
     */
    @GetMapping("")
    public List<ReviewMi> getAllReviewMis(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all ReviewMis");
        return reviewMiService.findAll();
    }

    /**
     * {@code GET  /review-mis/:id} : get the "id" reviewMi.
     *
     * @param id the id of the reviewMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewMi> getReviewMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewMi : {}", id);
        Optional<ReviewMi> reviewMi = reviewMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewMi);
    }

    /**
     * {@code DELETE  /review-mis/:id} : delete the "id" reviewMi.
     *
     * @param id the id of the reviewMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewMi : {}", id);
        reviewMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
