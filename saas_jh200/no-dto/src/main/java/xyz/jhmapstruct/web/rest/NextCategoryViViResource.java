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
import xyz.jhmapstruct.domain.NextCategoryViVi;
import xyz.jhmapstruct.repository.NextCategoryViViRepository;
import xyz.jhmapstruct.service.NextCategoryViViQueryService;
import xyz.jhmapstruct.service.NextCategoryViViService;
import xyz.jhmapstruct.service.criteria.NextCategoryViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryViVi}.
 */
@RestController
@RequestMapping("/api/next-category-vi-vis")
public class NextCategoryViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViViResource.class);

    private static final String ENTITY_NAME = "nextCategoryViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryViViService nextCategoryViViService;

    private final NextCategoryViViRepository nextCategoryViViRepository;

    private final NextCategoryViViQueryService nextCategoryViViQueryService;

    public NextCategoryViViResource(
        NextCategoryViViService nextCategoryViViService,
        NextCategoryViViRepository nextCategoryViViRepository,
        NextCategoryViViQueryService nextCategoryViViQueryService
    ) {
        this.nextCategoryViViService = nextCategoryViViService;
        this.nextCategoryViViRepository = nextCategoryViViRepository;
        this.nextCategoryViViQueryService = nextCategoryViViQueryService;
    }

    /**
     * {@code POST  /next-category-vi-vis} : Create a new nextCategoryViVi.
     *
     * @param nextCategoryViVi the nextCategoryViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryViVi, or with status {@code 400 (Bad Request)} if the nextCategoryViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryViVi> createNextCategoryViVi(@Valid @RequestBody NextCategoryViVi nextCategoryViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryViVi : {}", nextCategoryViVi);
        if (nextCategoryViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryViVi = nextCategoryViViService.save(nextCategoryViVi);
        return ResponseEntity.created(new URI("/api/next-category-vi-vis/" + nextCategoryViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryViVi.getId().toString()))
            .body(nextCategoryViVi);
    }

    /**
     * {@code PUT  /next-category-vi-vis/:id} : Updates an existing nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViVi to save.
     * @param nextCategoryViVi the nextCategoryViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryViVi,
     * or with status {@code 400 (Bad Request)} if the nextCategoryViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryViVi> updateNextCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryViVi nextCategoryViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryViVi : {}, {}", id, nextCategoryViVi);
        if (nextCategoryViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryViVi = nextCategoryViViService.update(nextCategoryViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryViVi.getId().toString()))
            .body(nextCategoryViVi);
    }

    /**
     * {@code PATCH  /next-category-vi-vis/:id} : Partial updates given fields of an existing nextCategoryViVi, field will ignore if it is null
     *
     * @param id the id of the nextCategoryViVi to save.
     * @param nextCategoryViVi the nextCategoryViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryViVi,
     * or with status {@code 400 (Bad Request)} if the nextCategoryViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryViVi> partialUpdateNextCategoryViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryViVi nextCategoryViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryViVi partially : {}, {}", id, nextCategoryViVi);
        if (nextCategoryViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryViVi> result = nextCategoryViViService.partialUpdate(nextCategoryViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-vi-vis} : get all the nextCategoryViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryViVi>> getAllNextCategoryViVis(
        NextCategoryViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryViVis by criteria: {}", criteria);

        Page<NextCategoryViVi> page = nextCategoryViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-vi-vis/count} : count all the nextCategoryViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryViVis(NextCategoryViViCriteria criteria) {
        LOG.debug("REST request to count NextCategoryViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-vi-vis/:id} : get the "id" nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryViVi> getNextCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryViVi : {}", id);
        Optional<NextCategoryViVi> nextCategoryViVi = nextCategoryViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryViVi);
    }

    /**
     * {@code DELETE  /next-category-vi-vis/:id} : delete the "id" nextCategoryViVi.
     *
     * @param id the id of the nextCategoryViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryViVi : {}", id);
        nextCategoryViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
