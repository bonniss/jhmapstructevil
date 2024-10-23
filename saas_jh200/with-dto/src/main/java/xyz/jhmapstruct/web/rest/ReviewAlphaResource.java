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
import xyz.jhmapstruct.repository.ReviewAlphaRepository;
import xyz.jhmapstruct.service.ReviewAlphaQueryService;
import xyz.jhmapstruct.service.ReviewAlphaService;
import xyz.jhmapstruct.service.criteria.ReviewAlphaCriteria;
import xyz.jhmapstruct.service.dto.ReviewAlphaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewAlpha}.
 */
@RestController
@RequestMapping("/api/review-alphas")
public class ReviewAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewAlphaResource.class);

    private static final String ENTITY_NAME = "reviewAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewAlphaService reviewAlphaService;

    private final ReviewAlphaRepository reviewAlphaRepository;

    private final ReviewAlphaQueryService reviewAlphaQueryService;

    public ReviewAlphaResource(
        ReviewAlphaService reviewAlphaService,
        ReviewAlphaRepository reviewAlphaRepository,
        ReviewAlphaQueryService reviewAlphaQueryService
    ) {
        this.reviewAlphaService = reviewAlphaService;
        this.reviewAlphaRepository = reviewAlphaRepository;
        this.reviewAlphaQueryService = reviewAlphaQueryService;
    }

    /**
     * {@code POST  /review-alphas} : Create a new reviewAlpha.
     *
     * @param reviewAlphaDTO the reviewAlphaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewAlphaDTO, or with status {@code 400 (Bad Request)} if the reviewAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewAlphaDTO> createReviewAlpha(@Valid @RequestBody ReviewAlphaDTO reviewAlphaDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewAlpha : {}", reviewAlphaDTO);
        if (reviewAlphaDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewAlphaDTO = reviewAlphaService.save(reviewAlphaDTO);
        return ResponseEntity.created(new URI("/api/review-alphas/" + reviewAlphaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewAlphaDTO.getId().toString()))
            .body(reviewAlphaDTO);
    }

    /**
     * {@code PUT  /review-alphas/:id} : Updates an existing reviewAlpha.
     *
     * @param id the id of the reviewAlphaDTO to save.
     * @param reviewAlphaDTO the reviewAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the reviewAlphaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewAlphaDTO> updateReviewAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewAlphaDTO reviewAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewAlpha : {}, {}", id, reviewAlphaDTO);
        if (reviewAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewAlphaDTO = reviewAlphaService.update(reviewAlphaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewAlphaDTO.getId().toString()))
            .body(reviewAlphaDTO);
    }

    /**
     * {@code PATCH  /review-alphas/:id} : Partial updates given fields of an existing reviewAlpha, field will ignore if it is null
     *
     * @param id the id of the reviewAlphaDTO to save.
     * @param reviewAlphaDTO the reviewAlphaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewAlphaDTO,
     * or with status {@code 400 (Bad Request)} if the reviewAlphaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewAlphaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewAlphaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewAlphaDTO> partialUpdateReviewAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewAlphaDTO reviewAlphaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewAlpha partially : {}, {}", id, reviewAlphaDTO);
        if (reviewAlphaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewAlphaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewAlphaDTO> result = reviewAlphaService.partialUpdate(reviewAlphaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewAlphaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-alphas} : get all the reviewAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewAlphaDTO>> getAllReviewAlphas(
        ReviewAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewAlphas by criteria: {}", criteria);

        Page<ReviewAlphaDTO> page = reviewAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-alphas/count} : count all the reviewAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewAlphas(ReviewAlphaCriteria criteria) {
        LOG.debug("REST request to count ReviewAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-alphas/:id} : get the "id" reviewAlpha.
     *
     * @param id the id of the reviewAlphaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewAlphaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewAlphaDTO> getReviewAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewAlpha : {}", id);
        Optional<ReviewAlphaDTO> reviewAlphaDTO = reviewAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewAlphaDTO);
    }

    /**
     * {@code DELETE  /review-alphas/:id} : delete the "id" reviewAlpha.
     *
     * @param id the id of the reviewAlphaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewAlpha : {}", id);
        reviewAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
