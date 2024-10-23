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
import xyz.jhmapstruct.domain.NextCategoryAlpha;
import xyz.jhmapstruct.repository.NextCategoryAlphaRepository;
import xyz.jhmapstruct.service.NextCategoryAlphaQueryService;
import xyz.jhmapstruct.service.NextCategoryAlphaService;
import xyz.jhmapstruct.service.criteria.NextCategoryAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryAlpha}.
 */
@RestController
@RequestMapping("/api/next-category-alphas")
public class NextCategoryAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryAlphaResource.class);

    private static final String ENTITY_NAME = "nextCategoryAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryAlphaService nextCategoryAlphaService;

    private final NextCategoryAlphaRepository nextCategoryAlphaRepository;

    private final NextCategoryAlphaQueryService nextCategoryAlphaQueryService;

    public NextCategoryAlphaResource(
        NextCategoryAlphaService nextCategoryAlphaService,
        NextCategoryAlphaRepository nextCategoryAlphaRepository,
        NextCategoryAlphaQueryService nextCategoryAlphaQueryService
    ) {
        this.nextCategoryAlphaService = nextCategoryAlphaService;
        this.nextCategoryAlphaRepository = nextCategoryAlphaRepository;
        this.nextCategoryAlphaQueryService = nextCategoryAlphaQueryService;
    }

    /**
     * {@code POST  /next-category-alphas} : Create a new nextCategoryAlpha.
     *
     * @param nextCategoryAlpha the nextCategoryAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryAlpha, or with status {@code 400 (Bad Request)} if the nextCategoryAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryAlpha> createNextCategoryAlpha(@Valid @RequestBody NextCategoryAlpha nextCategoryAlpha)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryAlpha : {}", nextCategoryAlpha);
        if (nextCategoryAlpha.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryAlpha = nextCategoryAlphaService.save(nextCategoryAlpha);
        return ResponseEntity.created(new URI("/api/next-category-alphas/" + nextCategoryAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryAlpha.getId().toString()))
            .body(nextCategoryAlpha);
    }

    /**
     * {@code PUT  /next-category-alphas/:id} : Updates an existing nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlpha to save.
     * @param nextCategoryAlpha the nextCategoryAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryAlpha,
     * or with status {@code 400 (Bad Request)} if the nextCategoryAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryAlpha> updateNextCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryAlpha nextCategoryAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryAlpha : {}, {}", id, nextCategoryAlpha);
        if (nextCategoryAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryAlpha = nextCategoryAlphaService.update(nextCategoryAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryAlpha.getId().toString()))
            .body(nextCategoryAlpha);
    }

    /**
     * {@code PATCH  /next-category-alphas/:id} : Partial updates given fields of an existing nextCategoryAlpha, field will ignore if it is null
     *
     * @param id the id of the nextCategoryAlpha to save.
     * @param nextCategoryAlpha the nextCategoryAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryAlpha,
     * or with status {@code 400 (Bad Request)} if the nextCategoryAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryAlpha> partialUpdateNextCategoryAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryAlpha nextCategoryAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryAlpha partially : {}, {}", id, nextCategoryAlpha);
        if (nextCategoryAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryAlpha> result = nextCategoryAlphaService.partialUpdate(nextCategoryAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-alphas} : get all the nextCategoryAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryAlpha>> getAllNextCategoryAlphas(
        NextCategoryAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryAlphas by criteria: {}", criteria);

        Page<NextCategoryAlpha> page = nextCategoryAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-alphas/count} : count all the nextCategoryAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryAlphas(NextCategoryAlphaCriteria criteria) {
        LOG.debug("REST request to count NextCategoryAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-alphas/:id} : get the "id" nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryAlpha> getNextCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryAlpha : {}", id);
        Optional<NextCategoryAlpha> nextCategoryAlpha = nextCategoryAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryAlpha);
    }

    /**
     * {@code DELETE  /next-category-alphas/:id} : delete the "id" nextCategoryAlpha.
     *
     * @param id the id of the nextCategoryAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryAlpha : {}", id);
        nextCategoryAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
