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
import xyz.jhmapstruct.repository.ReviewMiRepository;
import xyz.jhmapstruct.service.ReviewMiService;
import xyz.jhmapstruct.service.dto.ReviewMiDTO;
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
     * @param reviewMiDTO the reviewMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewMiDTO, or with status {@code 400 (Bad Request)} if the reviewMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewMiDTO> createReviewMi(@Valid @RequestBody ReviewMiDTO reviewMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewMi : {}", reviewMiDTO);
        if (reviewMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewMiDTO = reviewMiService.save(reviewMiDTO);
        return ResponseEntity.created(new URI("/api/review-mis/" + reviewMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewMiDTO.getId().toString()))
            .body(reviewMiDTO);
    }

    /**
     * {@code PUT  /review-mis/:id} : Updates an existing reviewMi.
     *
     * @param id the id of the reviewMiDTO to save.
     * @param reviewMiDTO the reviewMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiDTO,
     * or with status {@code 400 (Bad Request)} if the reviewMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewMiDTO> updateReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewMiDTO reviewMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewMi : {}, {}", id, reviewMiDTO);
        if (reviewMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewMiDTO = reviewMiService.update(reviewMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiDTO.getId().toString()))
            .body(reviewMiDTO);
    }

    /**
     * {@code PATCH  /review-mis/:id} : Partial updates given fields of an existing reviewMi, field will ignore if it is null
     *
     * @param id the id of the reviewMiDTO to save.
     * @param reviewMiDTO the reviewMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiDTO,
     * or with status {@code 400 (Bad Request)} if the reviewMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewMiDTO> partialUpdateReviewMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewMiDTO reviewMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewMi partially : {}, {}", id, reviewMiDTO);
        if (reviewMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewMiDTO> result = reviewMiService.partialUpdate(reviewMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-mis} : get all the reviewMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewMis in body.
     */
    @GetMapping("")
    public List<ReviewMiDTO> getAllReviewMis(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all ReviewMis");
        return reviewMiService.findAll();
    }

    /**
     * {@code GET  /review-mis/:id} : get the "id" reviewMi.
     *
     * @param id the id of the reviewMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewMiDTO> getReviewMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewMi : {}", id);
        Optional<ReviewMiDTO> reviewMiDTO = reviewMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewMiDTO);
    }

    /**
     * {@code DELETE  /review-mis/:id} : delete the "id" reviewMi.
     *
     * @param id the id of the reviewMiDTO to delete.
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
