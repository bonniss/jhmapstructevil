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
import xyz.jhmapstruct.repository.ReviewViRepository;
import xyz.jhmapstruct.service.ReviewViQueryService;
import xyz.jhmapstruct.service.ReviewViService;
import xyz.jhmapstruct.service.criteria.ReviewViCriteria;
import xyz.jhmapstruct.service.dto.ReviewViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewVi}.
 */
@RestController
@RequestMapping("/api/review-vis")
public class ReviewViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewViResource.class);

    private static final String ENTITY_NAME = "reviewVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewViService reviewViService;

    private final ReviewViRepository reviewViRepository;

    private final ReviewViQueryService reviewViQueryService;

    public ReviewViResource(
        ReviewViService reviewViService,
        ReviewViRepository reviewViRepository,
        ReviewViQueryService reviewViQueryService
    ) {
        this.reviewViService = reviewViService;
        this.reviewViRepository = reviewViRepository;
        this.reviewViQueryService = reviewViQueryService;
    }

    /**
     * {@code POST  /review-vis} : Create a new reviewVi.
     *
     * @param reviewViDTO the reviewViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewViDTO, or with status {@code 400 (Bad Request)} if the reviewVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewViDTO> createReviewVi(@Valid @RequestBody ReviewViDTO reviewViDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewVi : {}", reviewViDTO);
        if (reviewViDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewViDTO = reviewViService.save(reviewViDTO);
        return ResponseEntity.created(new URI("/api/review-vis/" + reviewViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewViDTO.getId().toString()))
            .body(reviewViDTO);
    }

    /**
     * {@code PUT  /review-vis/:id} : Updates an existing reviewVi.
     *
     * @param id the id of the reviewViDTO to save.
     * @param reviewViDTO the reviewViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewViDTO,
     * or with status {@code 400 (Bad Request)} if the reviewViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewViDTO> updateReviewVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewViDTO reviewViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewVi : {}, {}", id, reviewViDTO);
        if (reviewViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewViDTO = reviewViService.update(reviewViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewViDTO.getId().toString()))
            .body(reviewViDTO);
    }

    /**
     * {@code PATCH  /review-vis/:id} : Partial updates given fields of an existing reviewVi, field will ignore if it is null
     *
     * @param id the id of the reviewViDTO to save.
     * @param reviewViDTO the reviewViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewViDTO,
     * or with status {@code 400 (Bad Request)} if the reviewViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewViDTO> partialUpdateReviewVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewViDTO reviewViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewVi partially : {}, {}", id, reviewViDTO);
        if (reviewViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewViDTO> result = reviewViService.partialUpdate(reviewViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-vis} : get all the reviewVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewViDTO>> getAllReviewVis(
        ReviewViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewVis by criteria: {}", criteria);

        Page<ReviewViDTO> page = reviewViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-vis/count} : count all the reviewVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewVis(ReviewViCriteria criteria) {
        LOG.debug("REST request to count ReviewVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-vis/:id} : get the "id" reviewVi.
     *
     * @param id the id of the reviewViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewViDTO> getReviewVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewVi : {}", id);
        Optional<ReviewViDTO> reviewViDTO = reviewViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewViDTO);
    }

    /**
     * {@code DELETE  /review-vis/:id} : delete the "id" reviewVi.
     *
     * @param id the id of the reviewViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewVi : {}", id);
        reviewViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
