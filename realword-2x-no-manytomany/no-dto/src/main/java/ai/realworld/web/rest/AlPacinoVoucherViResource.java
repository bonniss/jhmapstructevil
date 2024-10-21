package ai.realworld.web.rest;

import ai.realworld.domain.AlPacinoVoucherVi;
import ai.realworld.repository.AlPacinoVoucherViRepository;
import ai.realworld.service.AlPacinoVoucherViQueryService;
import ai.realworld.service.AlPacinoVoucherViService;
import ai.realworld.service.criteria.AlPacinoVoucherViCriteria;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacinoVoucherVi}.
 */
@RestController
@RequestMapping("/api/al-pacino-voucher-vis")
public class AlPacinoVoucherViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoVoucherViResource.class);

    private static final String ENTITY_NAME = "alPacinoVoucherVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoVoucherViService alPacinoVoucherViService;

    private final AlPacinoVoucherViRepository alPacinoVoucherViRepository;

    private final AlPacinoVoucherViQueryService alPacinoVoucherViQueryService;

    public AlPacinoVoucherViResource(
        AlPacinoVoucherViService alPacinoVoucherViService,
        AlPacinoVoucherViRepository alPacinoVoucherViRepository,
        AlPacinoVoucherViQueryService alPacinoVoucherViQueryService
    ) {
        this.alPacinoVoucherViService = alPacinoVoucherViService;
        this.alPacinoVoucherViRepository = alPacinoVoucherViRepository;
        this.alPacinoVoucherViQueryService = alPacinoVoucherViQueryService;
    }

    /**
     * {@code POST  /al-pacino-voucher-vis} : Create a new alPacinoVoucherVi.
     *
     * @param alPacinoVoucherVi the alPacinoVoucherVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoVoucherVi, or with status {@code 400 (Bad Request)} if the alPacinoVoucherVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoVoucherVi> createAlPacinoVoucherVi(@RequestBody AlPacinoVoucherVi alPacinoVoucherVi)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoVoucherVi : {}", alPacinoVoucherVi);
        if (alPacinoVoucherVi.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoVoucherVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoVoucherVi = alPacinoVoucherViService.save(alPacinoVoucherVi);
        return ResponseEntity.created(new URI("/api/al-pacino-voucher-vis/" + alPacinoVoucherVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherVi.getId().toString()))
            .body(alPacinoVoucherVi);
    }

    /**
     * {@code PUT  /al-pacino-voucher-vis/:id} : Updates an existing alPacinoVoucherVi.
     *
     * @param id the id of the alPacinoVoucherVi to save.
     * @param alPacinoVoucherVi the alPacinoVoucherVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoVoucherVi,
     * or with status {@code 400 (Bad Request)} if the alPacinoVoucherVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoVoucherVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoVoucherVi> updateAlPacinoVoucherVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlPacinoVoucherVi alPacinoVoucherVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoVoucherVi : {}, {}", id, alPacinoVoucherVi);
        if (alPacinoVoucherVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoVoucherVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoVoucherViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoVoucherVi = alPacinoVoucherViService.update(alPacinoVoucherVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherVi.getId().toString()))
            .body(alPacinoVoucherVi);
    }

    /**
     * {@code PATCH  /al-pacino-voucher-vis/:id} : Partial updates given fields of an existing alPacinoVoucherVi, field will ignore if it is null
     *
     * @param id the id of the alPacinoVoucherVi to save.
     * @param alPacinoVoucherVi the alPacinoVoucherVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoVoucherVi,
     * or with status {@code 400 (Bad Request)} if the alPacinoVoucherVi is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoVoucherVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoVoucherVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoVoucherVi> partialUpdateAlPacinoVoucherVi(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody AlPacinoVoucherVi alPacinoVoucherVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoVoucherVi partially : {}, {}", id, alPacinoVoucherVi);
        if (alPacinoVoucherVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoVoucherVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoVoucherViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoVoucherVi> result = alPacinoVoucherViService.partialUpdate(alPacinoVoucherVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoVoucherVi.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-voucher-vis} : get all the alPacinoVoucherVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoVoucherVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoVoucherVi>> getAllAlPacinoVoucherVis(
        AlPacinoVoucherViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoVoucherVis by criteria: {}", criteria);

        Page<AlPacinoVoucherVi> page = alPacinoVoucherViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-voucher-vis/count} : count all the alPacinoVoucherVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoVoucherVis(AlPacinoVoucherViCriteria criteria) {
        LOG.debug("REST request to count AlPacinoVoucherVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoVoucherViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-voucher-vis/:id} : get the "id" alPacinoVoucherVi.
     *
     * @param id the id of the alPacinoVoucherVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoVoucherVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoVoucherVi> getAlPacinoVoucherVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlPacinoVoucherVi : {}", id);
        Optional<AlPacinoVoucherVi> alPacinoVoucherVi = alPacinoVoucherViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoVoucherVi);
    }

    /**
     * {@code DELETE  /al-pacino-voucher-vis/:id} : delete the "id" alPacinoVoucherVi.
     *
     * @param id the id of the alPacinoVoucherVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoVoucherVi(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlPacinoVoucherVi : {}", id);
        alPacinoVoucherViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
