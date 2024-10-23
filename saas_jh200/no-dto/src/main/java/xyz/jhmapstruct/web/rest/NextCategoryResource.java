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
import xyz.jhmapstruct.domain.NextCategory;
import xyz.jhmapstruct.repository.NextCategoryRepository;
import xyz.jhmapstruct.service.NextCategoryQueryService;
import xyz.jhmapstruct.service.NextCategoryService;
import xyz.jhmapstruct.service.criteria.NextCategoryCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategory}.
 */
@RestController
@RequestMapping("/api/next-categories")
public class NextCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryResource.class);

    private static final String ENTITY_NAME = "nextCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryService nextCategoryService;

    private final NextCategoryRepository nextCategoryRepository;

    private final NextCategoryQueryService nextCategoryQueryService;

    public NextCategoryResource(
        NextCategoryService nextCategoryService,
        NextCategoryRepository nextCategoryRepository,
        NextCategoryQueryService nextCategoryQueryService
    ) {
        this.nextCategoryService = nextCategoryService;
        this.nextCategoryRepository = nextCategoryRepository;
        this.nextCategoryQueryService = nextCategoryQueryService;
    }

    /**
     * {@code POST  /next-categories} : Create a new nextCategory.
     *
     * @param nextCategory the nextCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategory, or with status {@code 400 (Bad Request)} if the nextCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategory> createNextCategory(@Valid @RequestBody NextCategory nextCategory) throws URISyntaxException {
        LOG.debug("REST request to save NextCategory : {}", nextCategory);
        if (nextCategory.getId() != null) {
            throw new BadRequestAlertException("A new nextCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategory = nextCategoryService.save(nextCategory);
        return ResponseEntity.created(new URI("/api/next-categories/" + nextCategory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategory.getId().toString()))
            .body(nextCategory);
    }

    /**
     * {@code PUT  /next-categories/:id} : Updates an existing nextCategory.
     *
     * @param id the id of the nextCategory to save.
     * @param nextCategory the nextCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategory,
     * or with status {@code 400 (Bad Request)} if the nextCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategory> updateNextCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategory nextCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategory : {}, {}", id, nextCategory);
        if (nextCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategory = nextCategoryService.update(nextCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategory.getId().toString()))
            .body(nextCategory);
    }

    /**
     * {@code PATCH  /next-categories/:id} : Partial updates given fields of an existing nextCategory, field will ignore if it is null
     *
     * @param id the id of the nextCategory to save.
     * @param nextCategory the nextCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategory,
     * or with status {@code 400 (Bad Request)} if the nextCategory is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategory is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategory> partialUpdateNextCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategory nextCategory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategory partially : {}, {}", id, nextCategory);
        if (nextCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategory> result = nextCategoryService.partialUpdate(nextCategory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategory.getId().toString())
        );
    }

    /**
     * {@code GET  /next-categories} : get all the nextCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategory>> getAllNextCategories(
        NextCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategories by criteria: {}", criteria);

        Page<NextCategory> page = nextCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-categories/count} : count all the nextCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategories(NextCategoryCriteria criteria) {
        LOG.debug("REST request to count NextCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-categories/:id} : get the "id" nextCategory.
     *
     * @param id the id of the nextCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategory> getNextCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategory : {}", id);
        Optional<NextCategory> nextCategory = nextCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategory);
    }

    /**
     * {@code DELETE  /next-categories/:id} : delete the "id" nextCategory.
     *
     * @param id the id of the nextCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategory : {}", id);
        nextCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
