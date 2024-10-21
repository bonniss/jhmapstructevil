package ai.realworld.web.rest;

import ai.realworld.domain.AlPedroTaxVi;
import ai.realworld.repository.AlPedroTaxViRepository;
import ai.realworld.service.AlPedroTaxViQueryService;
import ai.realworld.service.AlPedroTaxViService;
import ai.realworld.service.criteria.AlPedroTaxViCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.AlPedroTaxVi}.
 */
@RestController
@RequestMapping("/api/al-pedro-tax-vis")
public class AlPedroTaxViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPedroTaxViResource.class);

    private static final String ENTITY_NAME = "alPedroTaxVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPedroTaxViService alPedroTaxViService;

    private final AlPedroTaxViRepository alPedroTaxViRepository;

    private final AlPedroTaxViQueryService alPedroTaxViQueryService;

    public AlPedroTaxViResource(
        AlPedroTaxViService alPedroTaxViService,
        AlPedroTaxViRepository alPedroTaxViRepository,
        AlPedroTaxViQueryService alPedroTaxViQueryService
    ) {
        this.alPedroTaxViService = alPedroTaxViService;
        this.alPedroTaxViRepository = alPedroTaxViRepository;
        this.alPedroTaxViQueryService = alPedroTaxViQueryService;
    }

    /**
     * {@code POST  /al-pedro-tax-vis} : Create a new alPedroTaxVi.
     *
     * @param alPedroTaxVi the alPedroTaxVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPedroTaxVi, or with status {@code 400 (Bad Request)} if the alPedroTaxVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPedroTaxVi> createAlPedroTaxVi(@Valid @RequestBody AlPedroTaxVi alPedroTaxVi) throws URISyntaxException {
        LOG.debug("REST request to save AlPedroTaxVi : {}", alPedroTaxVi);
        if (alPedroTaxVi.getId() != null) {
            throw new BadRequestAlertException("A new alPedroTaxVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPedroTaxVi = alPedroTaxViService.save(alPedroTaxVi);
        return ResponseEntity.created(new URI("/api/al-pedro-tax-vis/" + alPedroTaxVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPedroTaxVi.getId().toString()))
            .body(alPedroTaxVi);
    }

    /**
     * {@code PUT  /al-pedro-tax-vis/:id} : Updates an existing alPedroTaxVi.
     *
     * @param id the id of the alPedroTaxVi to save.
     * @param alPedroTaxVi the alPedroTaxVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTaxVi,
     * or with status {@code 400 (Bad Request)} if the alPedroTaxVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTaxVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPedroTaxVi> updateAlPedroTaxVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPedroTaxVi alPedroTaxVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPedroTaxVi : {}, {}", id, alPedroTaxVi);
        if (alPedroTaxVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTaxVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPedroTaxVi = alPedroTaxViService.update(alPedroTaxVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTaxVi.getId().toString()))
            .body(alPedroTaxVi);
    }

    /**
     * {@code PATCH  /al-pedro-tax-vis/:id} : Partial updates given fields of an existing alPedroTaxVi, field will ignore if it is null
     *
     * @param id the id of the alPedroTaxVi to save.
     * @param alPedroTaxVi the alPedroTaxVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPedroTaxVi,
     * or with status {@code 400 (Bad Request)} if the alPedroTaxVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPedroTaxVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPedroTaxVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPedroTaxVi> partialUpdateAlPedroTaxVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPedroTaxVi alPedroTaxVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPedroTaxVi partially : {}, {}", id, alPedroTaxVi);
        if (alPedroTaxVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPedroTaxVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPedroTaxViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPedroTaxVi> result = alPedroTaxViService.partialUpdate(alPedroTaxVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPedroTaxVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pedro-tax-vis} : get all the alPedroTaxVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPedroTaxVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPedroTaxVi>> getAllAlPedroTaxVis(
        AlPedroTaxViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPedroTaxVis by criteria: {}", criteria);

        Page<AlPedroTaxVi> page = alPedroTaxViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pedro-tax-vis/count} : count all the alPedroTaxVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPedroTaxVis(AlPedroTaxViCriteria criteria) {
        LOG.debug("REST request to count AlPedroTaxVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPedroTaxViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pedro-tax-vis/:id} : get the "id" alPedroTaxVi.
     *
     * @param id the id of the alPedroTaxVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPedroTaxVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPedroTaxVi> getAlPedroTaxVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPedroTaxVi : {}", id);
        Optional<AlPedroTaxVi> alPedroTaxVi = alPedroTaxViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPedroTaxVi);
    }

    /**
     * {@code DELETE  /al-pedro-tax-vis/:id} : delete the "id" alPedroTaxVi.
     *
     * @param id the id of the alPedroTaxVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPedroTaxVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPedroTaxVi : {}", id);
        alPedroTaxViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
