package ai.realworld.web.rest;

import ai.realworld.domain.AlVueVueVi;
import ai.realworld.repository.AlVueVueViRepository;
import ai.realworld.service.AlVueVueViQueryService;
import ai.realworld.service.AlVueVueViService;
import ai.realworld.service.criteria.AlVueVueViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlVueVueVi}.
 */
@RestController
@RequestMapping("/api/al-vue-vue-vis")
public class AlVueVueViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueViResource.class);

    private static final String ENTITY_NAME = "alVueVueVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueViService alVueVueViService;

    private final AlVueVueViRepository alVueVueViRepository;

    private final AlVueVueViQueryService alVueVueViQueryService;

    public AlVueVueViResource(
        AlVueVueViService alVueVueViService,
        AlVueVueViRepository alVueVueViRepository,
        AlVueVueViQueryService alVueVueViQueryService
    ) {
        this.alVueVueViService = alVueVueViService;
        this.alVueVueViRepository = alVueVueViRepository;
        this.alVueVueViQueryService = alVueVueViQueryService;
    }

    /**
     * {@code POST  /al-vue-vue-vis} : Create a new alVueVueVi.
     *
     * @param alVueVueVi the alVueVueVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueVi, or with status {@code 400 (Bad Request)} if the alVueVueVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueVi> createAlVueVueVi(@Valid @RequestBody AlVueVueVi alVueVueVi) throws URISyntaxException {
        LOG.debug("REST request to save AlVueVueVi : {}", alVueVueVi);
        if (alVueVueVi.getId() != null) {
            throw new BadRequestAlertException("A new alVueVueVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueVi = alVueVueViService.save(alVueVueVi);
        return ResponseEntity.created(new URI("/api/al-vue-vue-vis/" + alVueVueVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueVi.getId().toString()))
            .body(alVueVueVi);
    }

    /**
     * {@code PUT  /al-vue-vue-vis/:id} : Updates an existing alVueVueVi.
     *
     * @param id the id of the alVueVueVi to save.
     * @param alVueVueVi the alVueVueVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueVi,
     * or with status {@code 400 (Bad Request)} if the alVueVueVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueVi> updateAlVueVueVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlVueVueVi alVueVueVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVueVi : {}, {}", id, alVueVueVi);
        if (alVueVueVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueVi = alVueVueViService.update(alVueVueVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueVi.getId().toString()))
            .body(alVueVueVi);
    }

    /**
     * {@code PATCH  /al-vue-vue-vis/:id} : Partial updates given fields of an existing alVueVueVi, field will ignore if it is null
     *
     * @param id the id of the alVueVueVi to save.
     * @param alVueVueVi the alVueVueVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueVi,
     * or with status {@code 400 (Bad Request)} if the alVueVueVi is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueVi> partialUpdateAlVueVueVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlVueVueVi alVueVueVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVueVi partially : {}, {}", id, alVueVueVi);
        if (alVueVueVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueVi> result = alVueVueViService.partialUpdate(alVueVueVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vue-vis} : get all the alVueVueVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVueVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueVi>> getAllAlVueVueVis(
        AlVueVueViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVueVis by criteria: {}", criteria);

        Page<AlVueVueVi> page = alVueVueViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vue-vis/count} : count all the alVueVueVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVueVis(AlVueVueViCriteria criteria) {
        LOG.debug("REST request to count AlVueVueVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vue-vis/:id} : get the "id" alVueVueVi.
     *
     * @param id the id of the alVueVueVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueVi> getAlVueVueVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlVueVueVi : {}", id);
        Optional<AlVueVueVi> alVueVueVi = alVueVueViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueVi);
    }

    /**
     * {@code DELETE  /al-vue-vue-vis/:id} : delete the "id" alVueVueVi.
     *
     * @param id the id of the alVueVueVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVueVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlVueVueVi : {}", id);
        alVueVueViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
