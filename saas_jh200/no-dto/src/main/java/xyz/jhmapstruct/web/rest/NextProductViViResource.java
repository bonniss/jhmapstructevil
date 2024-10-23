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
import xyz.jhmapstruct.domain.NextProductViVi;
import xyz.jhmapstruct.repository.NextProductViViRepository;
import xyz.jhmapstruct.service.NextProductViViQueryService;
import xyz.jhmapstruct.service.NextProductViViService;
import xyz.jhmapstruct.service.criteria.NextProductViViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductViVi}.
 */
@RestController
@RequestMapping("/api/next-product-vi-vis")
public class NextProductViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductViViResource.class);

    private static final String ENTITY_NAME = "nextProductViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductViViService nextProductViViService;

    private final NextProductViViRepository nextProductViViRepository;

    private final NextProductViViQueryService nextProductViViQueryService;

    public NextProductViViResource(
        NextProductViViService nextProductViViService,
        NextProductViViRepository nextProductViViRepository,
        NextProductViViQueryService nextProductViViQueryService
    ) {
        this.nextProductViViService = nextProductViViService;
        this.nextProductViViRepository = nextProductViViRepository;
        this.nextProductViViQueryService = nextProductViViQueryService;
    }

    /**
     * {@code POST  /next-product-vi-vis} : Create a new nextProductViVi.
     *
     * @param nextProductViVi the nextProductViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductViVi, or with status {@code 400 (Bad Request)} if the nextProductViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductViVi> createNextProductViVi(@Valid @RequestBody NextProductViVi nextProductViVi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductViVi : {}", nextProductViVi);
        if (nextProductViVi.getId() != null) {
            throw new BadRequestAlertException("A new nextProductViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductViVi = nextProductViViService.save(nextProductViVi);
        return ResponseEntity.created(new URI("/api/next-product-vi-vis/" + nextProductViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductViVi.getId().toString()))
            .body(nextProductViVi);
    }

    /**
     * {@code PUT  /next-product-vi-vis/:id} : Updates an existing nextProductViVi.
     *
     * @param id the id of the nextProductViVi to save.
     * @param nextProductViVi the nextProductViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductViVi,
     * or with status {@code 400 (Bad Request)} if the nextProductViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductViVi> updateNextProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductViVi nextProductViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductViVi : {}, {}", id, nextProductViVi);
        if (nextProductViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductViVi = nextProductViViService.update(nextProductViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductViVi.getId().toString()))
            .body(nextProductViVi);
    }

    /**
     * {@code PATCH  /next-product-vi-vis/:id} : Partial updates given fields of an existing nextProductViVi, field will ignore if it is null
     *
     * @param id the id of the nextProductViVi to save.
     * @param nextProductViVi the nextProductViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductViVi,
     * or with status {@code 400 (Bad Request)} if the nextProductViVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductViVi> partialUpdateNextProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductViVi nextProductViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductViVi partially : {}, {}", id, nextProductViVi);
        if (nextProductViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductViVi> result = nextProductViViService.partialUpdate(nextProductViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-vi-vis} : get all the nextProductViVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductViVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductViVi>> getAllNextProductViVis(
        NextProductViViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductViVis by criteria: {}", criteria);

        Page<NextProductViVi> page = nextProductViViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-vi-vis/count} : count all the nextProductViVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductViVis(NextProductViViCriteria criteria) {
        LOG.debug("REST request to count NextProductViVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductViViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-vi-vis/:id} : get the "id" nextProductViVi.
     *
     * @param id the id of the nextProductViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductViVi> getNextProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductViVi : {}", id);
        Optional<NextProductViVi> nextProductViVi = nextProductViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductViVi);
    }

    /**
     * {@code DELETE  /next-product-vi-vis/:id} : delete the "id" nextProductViVi.
     *
     * @param id the id of the nextProductViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductViVi : {}", id);
        nextProductViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
