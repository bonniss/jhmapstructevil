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
import xyz.jhmapstruct.repository.ReviewViViRepository;
import xyz.jhmapstruct.service.ReviewViViQueryService;
import xyz.jhmapstruct.service.ReviewViViService;
import xyz.jhmapstruct.service.criteria.ReviewViViCriteria;
import xyz.jhmapstruct.service.dto.ReviewViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewViVi}.
 */
@RestController
@RequestMapping("/api/review-vi-vis")
public class ReviewViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViViResource.class);

    private static final String ENTITY_NAME = "reviewViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewViViService reviewViViService;

    private final ReviewViViRepository reviewViViRepository;

    private final ReviewViViQueryService reviewViViQueryService;

    public ReviewViViResource(
        ReviewViViService reviewViViService,
        ReviewViViRepository reviewViViRepository,
        ReviewViViQueryService reviewViViQueryService
    ) {
        this.reviewViViService = reviewViViService;
        this.reviewViViRepository = reviewViViRepository;
        this.reviewViViQueryService = reviewViViQueryService;
    }

    /**
     * {@code POST  /review-vi-vis} : Create a new reviewViVi.
     *
     * @param reviewViViDTO the reviewViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewViViDTO, or with status {@code 400 (Bad Request)} if the reviewViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewViViDTO> createReviewViVi(@Valid @RequestBody ReviewViViDTO reviewViViDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewViVi : {}", reviewViViDTO);
        if (reviewViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewViViDTO = reviewViViService.save(reviewViViDTO);
        return ResponseEntity.created(new URI("/api/review-vi-vis/" + reviewViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewViViDTO.getId().toString()))
            .body(reviewViViDTO);
    }

    /**
     * {@code PUT  /review-vi-vis/:id} : Updates an existing reviewViVi.
     *
     * @param id the id of the reviewViViDTO to save.
     * @param reviewViViDTO the reviewViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewViViDTO,
     * or with status {@code 400 (Bad Request)} if the reviewViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewViViDTO> updateReviewViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewViViDTO reviewViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewViVi : {}, {}", id, reviewViViDTO);
        if (reviewViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewViViDTO = reviewViViService.update(reviewViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewViViDTO.getId().toString()))
            .body(reviewViViDTO);
    }

    /**
     * {@code PATCH  /review-vi-vis/:id} : Partial updates given fields of an existing reviewViVi, field will ignore if it is null
     *
     * @param id the id of the reviewViViDTO to save.
     * @param reviewViViDTO the reviewViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewViViDTO,
     * or with status {@code 400 (Bad Request)} if the reviewViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewViViDTO> partialUpdateReviewViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewViViDTO reviewViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewViVi partially : {}, {}", id, reviewViViDTO);
        if (reviewViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewViViDTO> result = reviewViViService.partialUpdate(reviewViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-vi-vis} : get all the reviewViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewViViDTO>> getAllReviewViVis(
        ReviewViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewViVis by criteria: {}", criteria);

        Page<ReviewViViDTO> page = reviewViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-vi-vis/count} : count all the reviewViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewViVis(ReviewViViCriteria criteria) {
        LOG.debug("REST request to count ReviewViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-vi-vis/:id} : get the "id" reviewViVi.
     *
     * @param id the id of the reviewViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewViViDTO> getReviewViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewViVi : {}", id);
        Optional<ReviewViViDTO> reviewViViDTO = reviewViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewViViDTO);
    }

    /**
     * {@code DELETE  /review-vi-vis/:id} : delete the "id" reviewViVi.
     *
     * @param id the id of the reviewViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewViVi : {}", id);
        reviewViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
