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
import xyz.jhmapstruct.domain.NextReviewGamma;
import xyz.jhmapstruct.repository.NextReviewGammaRepository;
import xyz.jhmapstruct.service.NextReviewGammaQueryService;
import xyz.jhmapstruct.service.NextReviewGammaService;
import xyz.jhmapstruct.service.criteria.NextReviewGammaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextReviewGamma}.
 */
@RestController
@RequestMapping("/api/next-review-gammas")
public class NextReviewGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextReviewGammaResource.class);

    private static final String ENTITY_NAME = "nextReviewGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextReviewGammaService nextReviewGammaService;

    private final NextReviewGammaRepository nextReviewGammaRepository;

    private final NextReviewGammaQueryService nextReviewGammaQueryService;

    public NextReviewGammaResource(
        NextReviewGammaService nextReviewGammaService,
        NextReviewGammaRepository nextReviewGammaRepository,
        NextReviewGammaQueryService nextReviewGammaQueryService
    ) {
        this.nextReviewGammaService = nextReviewGammaService;
        this.nextReviewGammaRepository = nextReviewGammaRepository;
        this.nextReviewGammaQueryService = nextReviewGammaQueryService;
    }

    /**
     * {@code POST  /next-review-gammas} : Create a new nextReviewGamma.
     *
     * @param nextReviewGamma the nextReviewGamma to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextReviewGamma, or with status {@code 400 (Bad Request)} if the nextReviewGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextReviewGamma> createNextReviewGamma(@Valid @RequestBody NextReviewGamma nextReviewGamma)
        throws URISyntaxException {
        LOG.debug("REST request to save NextReviewGamma : {}", nextReviewGamma);
        if (nextReviewGamma.getId() != null) {
            throw new BadRequestAlertException("A new nextReviewGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextReviewGamma = nextReviewGammaService.save(nextReviewGamma);
        return ResponseEntity.created(new URI("/api/next-review-gammas/" + nextReviewGamma.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextReviewGamma.getId().toString()))
            .body(nextReviewGamma);
    }

    /**
     * {@code PUT  /next-review-gammas/:id} : Updates an existing nextReviewGamma.
     *
     * @param id the id of the nextReviewGamma to save.
     * @param nextReviewGamma the nextReviewGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewGamma,
     * or with status {@code 400 (Bad Request)} if the nextReviewGamma is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextReviewGamma> updateNextReviewGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextReviewGamma nextReviewGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextReviewGamma : {}, {}", id, nextReviewGamma);
        if (nextReviewGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextReviewGamma = nextReviewGammaService.update(nextReviewGamma);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewGamma.getId().toString()))
            .body(nextReviewGamma);
    }

    /**
     * {@code PATCH  /next-review-gammas/:id} : Partial updates given fields of an existing nextReviewGamma, field will ignore if it is null
     *
     * @param id the id of the nextReviewGamma to save.
     * @param nextReviewGamma the nextReviewGamma to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextReviewGamma,
     * or with status {@code 400 (Bad Request)} if the nextReviewGamma is not valid,
     * or with status {@code 404 (Not Found)} if the nextReviewGamma is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextReviewGamma couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextReviewGamma> partialUpdateNextReviewGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextReviewGamma nextReviewGamma
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextReviewGamma partially : {}, {}", id, nextReviewGamma);
        if (nextReviewGamma.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextReviewGamma.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextReviewGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextReviewGamma> result = nextReviewGammaService.partialUpdate(nextReviewGamma);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextReviewGamma.getId().toString())
        );
    }

    /**
     * {@code GET  /next-review-gammas} : get all the nextReviewGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextReviewGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextReviewGamma>> getAllNextReviewGammas(
        NextReviewGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextReviewGammas by criteria: {}", criteria);

        Page<NextReviewGamma> page = nextReviewGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-review-gammas/count} : count all the nextReviewGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextReviewGammas(NextReviewGammaCriteria criteria) {
        LOG.debug("REST request to count NextReviewGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextReviewGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-review-gammas/:id} : get the "id" nextReviewGamma.
     *
     * @param id the id of the nextReviewGamma to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextReviewGamma, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextReviewGamma> getNextReviewGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextReviewGamma : {}", id);
        Optional<NextReviewGamma> nextReviewGamma = nextReviewGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextReviewGamma);
    }

    /**
     * {@code DELETE  /next-review-gammas/:id} : delete the "id" nextReviewGamma.
     *
     * @param id the id of the nextReviewGamma to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextReviewGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextReviewGamma : {}", id);
        nextReviewGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
