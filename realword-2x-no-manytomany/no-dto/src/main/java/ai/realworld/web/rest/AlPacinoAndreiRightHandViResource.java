package ai.realworld.web.rest;

import ai.realworld.domain.AlPacinoAndreiRightHandVi;
import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import ai.realworld.service.AlPacinoAndreiRightHandViQueryService;
import ai.realworld.service.AlPacinoAndreiRightHandViService;
import ai.realworld.service.criteria.AlPacinoAndreiRightHandViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlPacinoAndreiRightHandVi}.
 */
@RestController
@RequestMapping("/api/al-pacino-andrei-right-hand-vis")
public class AlPacinoAndreiRightHandViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandViResource.class);

    private static final String ENTITY_NAME = "alPacinoAndreiRightHandVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoAndreiRightHandViService alPacinoAndreiRightHandViService;

    private final AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    private final AlPacinoAndreiRightHandViQueryService alPacinoAndreiRightHandViQueryService;

    public AlPacinoAndreiRightHandViResource(
        AlPacinoAndreiRightHandViService alPacinoAndreiRightHandViService,
        AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository,
        AlPacinoAndreiRightHandViQueryService alPacinoAndreiRightHandViQueryService
    ) {
        this.alPacinoAndreiRightHandViService = alPacinoAndreiRightHandViService;
        this.alPacinoAndreiRightHandViRepository = alPacinoAndreiRightHandViRepository;
        this.alPacinoAndreiRightHandViQueryService = alPacinoAndreiRightHandViQueryService;
    }

    /**
     * {@code POST  /al-pacino-andrei-right-hand-vis} : Create a new alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandVi the alPacinoAndreiRightHandVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoAndreiRightHandVi, or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoAndreiRightHandVi> createAlPacinoAndreiRightHandVi(
        @Valid @RequestBody AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandVi);
        if (alPacinoAndreiRightHandVi.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoAndreiRightHandVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViService.save(alPacinoAndreiRightHandVi);
        return ResponseEntity.created(new URI("/api/al-pacino-andrei-right-hand-vis/" + alPacinoAndreiRightHandVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandVi.getId().toString()))
            .body(alPacinoAndreiRightHandVi);
    }

    /**
     * {@code PUT  /al-pacino-andrei-right-hand-vis/:id} : Updates an existing alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandVi to save.
     * @param alPacinoAndreiRightHandVi the alPacinoAndreiRightHandVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandVi,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandVi> updateAlPacinoAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoAndreiRightHandVi : {}, {}", id, alPacinoAndreiRightHandVi);
        if (alPacinoAndreiRightHandVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViService.update(alPacinoAndreiRightHandVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandVi.getId().toString()))
            .body(alPacinoAndreiRightHandVi);
    }

    /**
     * {@code PATCH  /al-pacino-andrei-right-hand-vis/:id} : Partial updates given fields of an existing alPacinoAndreiRightHandVi, field will ignore if it is null
     *
     * @param id the id of the alPacinoAndreiRightHandVi to save.
     * @param alPacinoAndreiRightHandVi the alPacinoAndreiRightHandVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandVi,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoAndreiRightHandVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoAndreiRightHandVi> partialUpdateAlPacinoAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPacinoAndreiRightHandVi alPacinoAndreiRightHandVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoAndreiRightHandVi partially : {}, {}", id, alPacinoAndreiRightHandVi);
        if (alPacinoAndreiRightHandVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoAndreiRightHandVi> result = alPacinoAndreiRightHandViService.partialUpdate(alPacinoAndreiRightHandVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis} : get all the alPacinoAndreiRightHandVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoAndreiRightHandVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoAndreiRightHandVi>> getAllAlPacinoAndreiRightHandVis(
        AlPacinoAndreiRightHandViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoAndreiRightHandVis by criteria: {}", criteria);

        Page<AlPacinoAndreiRightHandVi> page = alPacinoAndreiRightHandViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis/count} : count all the alPacinoAndreiRightHandVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoAndreiRightHandVis(AlPacinoAndreiRightHandViCriteria criteria) {
        LOG.debug("REST request to count AlPacinoAndreiRightHandVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoAndreiRightHandViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis/:id} : get the "id" alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoAndreiRightHandVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandVi> getAlPacinoAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPacinoAndreiRightHandVi : {}", id);
        Optional<AlPacinoAndreiRightHandVi> alPacinoAndreiRightHandVi = alPacinoAndreiRightHandViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoAndreiRightHandVi);
    }

    /**
     * {@code DELETE  /al-pacino-andrei-right-hand-vis/:id} : delete the "id" alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPacinoAndreiRightHandVi : {}", id);
        alPacinoAndreiRightHandViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
