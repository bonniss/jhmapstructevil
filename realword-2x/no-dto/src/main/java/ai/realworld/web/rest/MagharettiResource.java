package ai.realworld.web.rest;

import ai.realworld.domain.Magharetti;
import ai.realworld.repository.MagharettiRepository;
import ai.realworld.service.MagharettiQueryService;
import ai.realworld.service.MagharettiService;
import ai.realworld.service.criteria.MagharettiCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.Magharetti}.
 */
@RestController
@RequestMapping("/api/magharettis")
public class MagharettiResource {

    private static final Logger LOG = LoggerFactory.getLogger(MagharettiResource.class);

    private static final String ENTITY_NAME = "magharetti";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MagharettiService magharettiService;

    private final MagharettiRepository magharettiRepository;

    private final MagharettiQueryService magharettiQueryService;

    public MagharettiResource(
        MagharettiService magharettiService,
        MagharettiRepository magharettiRepository,
        MagharettiQueryService magharettiQueryService
    ) {
        this.magharettiService = magharettiService;
        this.magharettiRepository = magharettiRepository;
        this.magharettiQueryService = magharettiQueryService;
    }

    /**
     * {@code POST  /magharettis} : Create a new magharetti.
     *
     * @param magharetti the magharetti to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new magharetti, or with status {@code 400 (Bad Request)} if the magharetti has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Magharetti> createMagharetti(@Valid @RequestBody Magharetti magharetti) throws URISyntaxException {
        LOG.debug("REST request to save Magharetti : {}", magharetti);
        if (magharetti.getId() != null) {
            throw new BadRequestAlertException("A new magharetti cannot already have an ID", ENTITY_NAME, "idexists");
        }
        magharetti = magharettiService.save(magharetti);
        return ResponseEntity.created(new URI("/api/magharettis/" + magharetti.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, magharetti.getId().toString()))
            .body(magharetti);
    }

    /**
     * {@code PUT  /magharettis/:id} : Updates an existing magharetti.
     *
     * @param id the id of the magharetti to save.
     * @param magharetti the magharetti to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharetti,
     * or with status {@code 400 (Bad Request)} if the magharetti is not valid,
     * or with status {@code 500 (Internal Server Error)} if the magharetti couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Magharetti> updateMagharetti(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Magharetti magharetti
    ) throws URISyntaxException {
        LOG.debug("REST request to update Magharetti : {}, {}", id, magharetti);
        if (magharetti.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharetti.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        magharetti = magharettiService.update(magharetti);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharetti.getId().toString()))
            .body(magharetti);
    }

    /**
     * {@code PATCH  /magharettis/:id} : Partial updates given fields of an existing magharetti, field will ignore if it is null
     *
     * @param id the id of the magharetti to save.
     * @param magharetti the magharetti to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated magharetti,
     * or with status {@code 400 (Bad Request)} if the magharetti is not valid,
     * or with status {@code 404 (Not Found)} if the magharetti is not found,
     * or with status {@code 500 (Internal Server Error)} if the magharetti couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Magharetti> partialUpdateMagharetti(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Magharetti magharetti
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Magharetti partially : {}, {}", id, magharetti);
        if (magharetti.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, magharetti.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!magharettiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Magharetti> result = magharettiService.partialUpdate(magharetti);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, magharetti.getId().toString())
        );
    }

    /**
     * {@code GET  /magharettis} : get all the magharettis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of magharettis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Magharetti>> getAllMagharettis(
        MagharettiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Magharettis by criteria: {}", criteria);

        Page<Magharetti> page = magharettiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /magharettis/count} : count all the magharettis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMagharettis(MagharettiCriteria criteria) {
        LOG.debug("REST request to count Magharettis by criteria: {}", criteria);
        return ResponseEntity.ok().body(magharettiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /magharettis/:id} : get the "id" magharetti.
     *
     * @param id the id of the magharetti to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the magharetti, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Magharetti> getMagharetti(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Magharetti : {}", id);
        Optional<Magharetti> magharetti = magharettiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(magharetti);
    }

    /**
     * {@code DELETE  /magharettis/:id} : delete the "id" magharetti.
     *
     * @param id the id of the magharetti to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMagharetti(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Magharetti : {}", id);
        magharettiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
