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
import xyz.jhmapstruct.domain.NextCategoryVi;
import xyz.jhmapstruct.repository.NextCategoryViRepository;
import xyz.jhmapstruct.service.NextCategoryViQueryService;
import xyz.jhmapstruct.service.NextCategoryViService;
import xyz.jhmapstruct.service.criteria.NextCategoryViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryVi}.
 */
@RestController
@RequestMapping("/api/next-category-vis")
public class NextCategoryViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryViResource.class);

    private static final String ENTITY_NAME = "nextCategoryVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryViService nextCategoryViService;

    private final NextCategoryViRepository nextCategoryViRepository;

    private final NextCategoryViQueryService nextCategoryViQueryService;

    public NextCategoryViResource(
        NextCategoryViService nextCategoryViService,
        NextCategoryViRepository nextCategoryViRepository,
        NextCategoryViQueryService nextCategoryViQueryService
    ) {
        this.nextCategoryViService = nextCategoryViService;
        this.nextCategoryViRepository = nextCategoryViRepository;
        this.nextCategoryViQueryService = nextCategoryViQueryService;
    }

    /**
     * {@code POST  /next-category-vis} : Create a new nextCategoryVi.
     *
     * @param nextCategoryVi the nextCategoryVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryVi, or with status {@code 400 (Bad Request)} if the nextCategoryVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryVi> createNextCategoryVi(@Valid @RequestBody NextCategoryVi nextCategoryVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryVi : {}", nextCategoryVi);
        if (nextCategoryVi.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryVi = nextCategoryViService.save(nextCategoryVi);
        return ResponseEntity.created(new URI("/api/next-category-vis/" + nextCategoryVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryVi.getId().toString()))
            .body(nextCategoryVi);
    }

    /**
     * {@code PUT  /next-category-vis/:id} : Updates an existing nextCategoryVi.
     *
     * @param id the id of the nextCategoryVi to save.
     * @param nextCategoryVi the nextCategoryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryVi,
     * or with status {@code 400 (Bad Request)} if the nextCategoryVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryVi> updateNextCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryVi nextCategoryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryVi : {}, {}", id, nextCategoryVi);
        if (nextCategoryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryVi = nextCategoryViService.update(nextCategoryVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryVi.getId().toString()))
            .body(nextCategoryVi);
    }

    /**
     * {@code PATCH  /next-category-vis/:id} : Partial updates given fields of an existing nextCategoryVi, field will ignore if it is null
     *
     * @param id the id of the nextCategoryVi to save.
     * @param nextCategoryVi the nextCategoryVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryVi,
     * or with status {@code 400 (Bad Request)} if the nextCategoryVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryVi> partialUpdateNextCategoryVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryVi nextCategoryVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryVi partially : {}, {}", id, nextCategoryVi);
        if (nextCategoryVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryVi> result = nextCategoryViService.partialUpdate(nextCategoryVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-vis} : get all the nextCategoryVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryVi>> getAllNextCategoryVis(
        NextCategoryViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryVis by criteria: {}", criteria);

        Page<NextCategoryVi> page = nextCategoryViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-vis/count} : count all the nextCategoryVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryVis(NextCategoryViCriteria criteria) {
        LOG.debug("REST request to count NextCategoryVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-vis/:id} : get the "id" nextCategoryVi.
     *
     * @param id the id of the nextCategoryVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryVi> getNextCategoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryVi : {}", id);
        Optional<NextCategoryVi> nextCategoryVi = nextCategoryViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryVi);
    }

    /**
     * {@code DELETE  /next-category-vis/:id} : delete the "id" nextCategoryVi.
     *
     * @param id the id of the nextCategoryVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryVi : {}", id);
        nextCategoryViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
