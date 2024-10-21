package ai.realworld.web.rest;

import ai.realworld.domain.SaisanCog;
import ai.realworld.repository.SaisanCogRepository;
import ai.realworld.service.SaisanCogQueryService;
import ai.realworld.service.SaisanCogService;
import ai.realworld.service.criteria.SaisanCogCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.SaisanCog}.
 */
@RestController
@RequestMapping("/api/saisan-cogs")
public class SaisanCogResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaisanCogResource.class);

    private static final String ENTITY_NAME = "saisanCog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaisanCogService saisanCogService;

    private final SaisanCogRepository saisanCogRepository;

    private final SaisanCogQueryService saisanCogQueryService;

    public SaisanCogResource(
        SaisanCogService saisanCogService,
        SaisanCogRepository saisanCogRepository,
        SaisanCogQueryService saisanCogQueryService
    ) {
        this.saisanCogService = saisanCogService;
        this.saisanCogRepository = saisanCogRepository;
        this.saisanCogQueryService = saisanCogQueryService;
    }

    /**
     * {@code POST  /saisan-cogs} : Create a new saisanCog.
     *
     * @param saisanCog the saisanCog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saisanCog, or with status {@code 400 (Bad Request)} if the saisanCog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaisanCog> createSaisanCog(@Valid @RequestBody SaisanCog saisanCog) throws URISyntaxException {
        LOG.debug("REST request to save SaisanCog : {}", saisanCog);
        if (saisanCog.getId() != null) {
            throw new BadRequestAlertException("A new saisanCog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saisanCog = saisanCogService.save(saisanCog);
        return ResponseEntity.created(new URI("/api/saisan-cogs/" + saisanCog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saisanCog.getId().toString()))
            .body(saisanCog);
    }

    /**
     * {@code PUT  /saisan-cogs/:id} : Updates an existing saisanCog.
     *
     * @param id the id of the saisanCog to save.
     * @param saisanCog the saisanCog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisanCog,
     * or with status {@code 400 (Bad Request)} if the saisanCog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saisanCog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaisanCog> updateSaisanCog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaisanCog saisanCog
    ) throws URISyntaxException {
        LOG.debug("REST request to update SaisanCog : {}, {}", id, saisanCog);
        if (saisanCog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisanCog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisanCogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saisanCog = saisanCogService.update(saisanCog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisanCog.getId().toString()))
            .body(saisanCog);
    }

    /**
     * {@code PATCH  /saisan-cogs/:id} : Partial updates given fields of an existing saisanCog, field will ignore if it is null
     *
     * @param id the id of the saisanCog to save.
     * @param saisanCog the saisanCog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saisanCog,
     * or with status {@code 400 (Bad Request)} if the saisanCog is not valid,
     * or with status {@code 404 (Not Found)} if the saisanCog is not found,
     * or with status {@code 500 (Internal Server Error)} if the saisanCog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaisanCog> partialUpdateSaisanCog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaisanCog saisanCog
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SaisanCog partially : {}, {}", id, saisanCog);
        if (saisanCog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saisanCog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saisanCogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaisanCog> result = saisanCogService.partialUpdate(saisanCog);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saisanCog.getId().toString())
        );
    }

    /**
     * {@code GET  /saisan-cogs} : get all the saisanCogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saisanCogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SaisanCog>> getAllSaisanCogs(
        SaisanCogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SaisanCogs by criteria: {}", criteria);

        Page<SaisanCog> page = saisanCogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saisan-cogs/count} : count all the saisanCogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSaisanCogs(SaisanCogCriteria criteria) {
        LOG.debug("REST request to count SaisanCogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(saisanCogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /saisan-cogs/:id} : get the "id" saisanCog.
     *
     * @param id the id of the saisanCog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saisanCog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaisanCog> getSaisanCog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SaisanCog : {}", id);
        Optional<SaisanCog> saisanCog = saisanCogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saisanCog);
    }

    /**
     * {@code DELETE  /saisan-cogs/:id} : delete the "id" saisanCog.
     *
     * @param id the id of the saisanCog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaisanCog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SaisanCog : {}", id);
        saisanCogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
