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
import xyz.jhmapstruct.repository.ReviewSigmaRepository;
import xyz.jhmapstruct.service.ReviewSigmaQueryService;
import xyz.jhmapstruct.service.ReviewSigmaService;
import xyz.jhmapstruct.service.criteria.ReviewSigmaCriteria;
import xyz.jhmapstruct.service.dto.ReviewSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ReviewSigma}.
 */
@RestController
@RequestMapping("/api/review-sigmas")
public class ReviewSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewSigmaResource.class);

    private static final String ENTITY_NAME = "reviewSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewSigmaService reviewSigmaService;

    private final ReviewSigmaRepository reviewSigmaRepository;

    private final ReviewSigmaQueryService reviewSigmaQueryService;

    public ReviewSigmaResource(
        ReviewSigmaService reviewSigmaService,
        ReviewSigmaRepository reviewSigmaRepository,
        ReviewSigmaQueryService reviewSigmaQueryService
    ) {
        this.reviewSigmaService = reviewSigmaService;
        this.reviewSigmaRepository = reviewSigmaRepository;
        this.reviewSigmaQueryService = reviewSigmaQueryService;
    }

    /**
     * {@code POST  /review-sigmas} : Create a new reviewSigma.
     *
     * @param reviewSigmaDTO the reviewSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewSigmaDTO, or with status {@code 400 (Bad Request)} if the reviewSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ReviewSigmaDTO> createReviewSigma(@Valid @RequestBody ReviewSigmaDTO reviewSigmaDTO) throws URISyntaxException {
        LOG.debug("REST request to save ReviewSigma : {}", reviewSigmaDTO);
        if (reviewSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new reviewSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reviewSigmaDTO = reviewSigmaService.save(reviewSigmaDTO);
        return ResponseEntity.created(new URI("/api/review-sigmas/" + reviewSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reviewSigmaDTO.getId().toString()))
            .body(reviewSigmaDTO);
    }

    /**
     * {@code PUT  /review-sigmas/:id} : Updates an existing reviewSigma.
     *
     * @param id the id of the reviewSigmaDTO to save.
     * @param reviewSigmaDTO the reviewSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the reviewSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewSigmaDTO> updateReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReviewSigmaDTO reviewSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ReviewSigma : {}, {}", id, reviewSigmaDTO);
        if (reviewSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reviewSigmaDTO = reviewSigmaService.update(reviewSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewSigmaDTO.getId().toString()))
            .body(reviewSigmaDTO);
    }

    /**
     * {@code PATCH  /review-sigmas/:id} : Partial updates given fields of an existing reviewSigma, field will ignore if it is null
     *
     * @param id the id of the reviewSigmaDTO to save.
     * @param reviewSigmaDTO the reviewSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the reviewSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReviewSigmaDTO> partialUpdateReviewSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReviewSigmaDTO reviewSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ReviewSigma partially : {}, {}", id, reviewSigmaDTO);
        if (reviewSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reviewSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewSigmaDTO> result = reviewSigmaService.partialUpdate(reviewSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reviewSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /review-sigmas} : get all the reviewSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviewSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ReviewSigmaDTO>> getAllReviewSigmas(
        ReviewSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ReviewSigmas by criteria: {}", criteria);

        Page<ReviewSigmaDTO> page = reviewSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /review-sigmas/count} : count all the reviewSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countReviewSigmas(ReviewSigmaCriteria criteria) {
        LOG.debug("REST request to count ReviewSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(reviewSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /review-sigmas/:id} : get the "id" reviewSigma.
     *
     * @param id the id of the reviewSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewSigmaDTO> getReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ReviewSigma : {}", id);
        Optional<ReviewSigmaDTO> reviewSigmaDTO = reviewSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewSigmaDTO);
    }

    /**
     * {@code DELETE  /review-sigmas/:id} : delete the "id" reviewSigma.
     *
     * @param id the id of the reviewSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReviewSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ReviewSigma : {}", id);
        reviewSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
