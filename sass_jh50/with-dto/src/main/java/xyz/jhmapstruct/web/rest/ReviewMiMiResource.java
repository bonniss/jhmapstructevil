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
import xyz.jhmapstruct.repository.ReviewMiMiRepository;
import xyz.jhmapstruct.service.ReviewMiMiService;
import xyz.jhmapstruct.service.dto.ReviewMiMiDTO;
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

    public ReviewMiMiResource(ReviewMiMiService reviewMiMiService, ReviewMiMiRepository reviewMiMiRepository) {
        this.reviewMiMiService = reviewMiMiService;
        this.reviewMiMiRepository = reviewMiMiRepository;
    }

    /**
     * {@code POST  /review-mi-mis} : Create a new reviewMiMi.
     *
     * @param reviewMiMiDTO the reviewMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewMiMiDTO, or with status {@code 400 (Bad Request)} if the reviewMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewMiMiDTO> createReviewMiMi(@Valid @RequestBody ReviewMiMiDTO reviewMiMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewMiMi : {}", reviewMiMiDTO);
        if (reviewMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewMiMiDTO = reviewMiMiService.save(reviewMiMiDTO);
        return ResponseEntity.created(new URI("/api/review-mi-mis/" + reviewMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewMiMiDTO.getId().toString()))
            .body(reviewMiMiDTO);
    }

    /**
     * {@code PUT  /review-mi-mis/:id} : Updates an existing reviewMiMi.
     *
     * @param id the id of the reviewMiMiDTO to save.
     * @param reviewMiMiDTO the reviewMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the reviewMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewMiMiDTO> updateReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewMiMiDTO reviewMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewMiMi : {}, {}", id, reviewMiMiDTO);
        if (reviewMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewMiMiDTO = reviewMiMiService.update(reviewMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiMiDTO.getId().toString()))
            .body(reviewMiMiDTO);
    }

    /**
     * {@code PATCH  /review-mi-mis/:id} : Partial updates given fields of an existing reviewMiMi, field will ignore if it is null
     *
     * @param id the id of the reviewMiMiDTO to save.
     * @param reviewMiMiDTO the reviewMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the reviewMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewMiMiDTO> partialUpdateReviewMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewMiMiDTO reviewMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewMiMi partially : {}, {}", id, reviewMiMiDTO);
        if (reviewMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewMiMiDTO> result = reviewMiMiService.partialUpdate(reviewMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-mi-mis} : get all the reviewMiMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewMiMis in body.
     */
    @GetMapping("")
    public List<ReviewMiMiDTO> getAllReviewMiMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ReviewMiMis");
        return reviewMiMiService.findAll();
    }

    /**
     * {@code GET  /review-mi-mis/:id} : get the "id" reviewMiMi.
     *
     * @param id the id of the reviewMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewMiMiDTO> getReviewMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewMiMi : {}", id);
        Optional<ReviewMiMiDTO> reviewMiMiDTO = reviewMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewMiMiDTO);
    }

    /**
     * {@code DELETE  /review-mi-mis/:id} : delete the "id" reviewMiMi.
     *
     * @param id the id of the reviewMiMiDTO to delete.
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
