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
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.repository.MasterTenantRepository;
import xyz.jhmapstruct.service.MasterTenantQueryService;
import xyz.jhmapstruct.service.MasterTenantService;
import xyz.jhmapstruct.service.criteria.MasterTenantCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.MasterTenant}.
 */
@RestController
@RequestMapping("/api/master-tenants")
public class MasterTenantResource {

    private static final Logger LOG = LoggerFactory.getLogger(MasterTenantResource.class);

    private static final String ENTITY_NAME = "masterTenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MasterTenantService masterTenantService;

    private final MasterTenantRepository masterTenantRepository;

    private final MasterTenantQueryService masterTenantQueryService;

    public MasterTenantResource(
        MasterTenantService masterTenantService,
        MasterTenantRepository masterTenantRepository,
        MasterTenantQueryService masterTenantQueryService
    ) {
        this.masterTenantService = masterTenantService;
        this.masterTenantRepository = masterTenantRepository;
        this.masterTenantQueryService = masterTenantQueryService;
    }

    /**
     * {@code POST  /master-tenants} : Create a new masterTenant.
     *
     * @param masterTenant the masterTenant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new masterTenant, or with status {@code 400 (Bad Request)} if the masterTenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MasterTenant> createMasterTenant(@Valid @RequestBody MasterTenant masterTenant) throws URISyntaxException {
        LOG.debug("REST request to save MasterTenant : {}", masterTenant);
        if (masterTenant.getId() != null) {
            throw new BadRequestAlertException("A new masterTenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        masterTenant = masterTenantService.save(masterTenant);
        return ResponseEntity.created(new URI("/api/master-tenants/" + masterTenant.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, masterTenant.getId().toString()))
            .body(masterTenant);
    }

    /**
     * {@code PUT  /master-tenants/:id} : Updates an existing masterTenant.
     *
     * @param id the id of the masterTenant to save.
     * @param masterTenant the masterTenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterTenant,
     * or with status {@code 400 (Bad Request)} if the masterTenant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the masterTenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MasterTenant> updateMasterTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MasterTenant masterTenant
    ) throws URISyntaxException {
        LOG.debug("REST request to update MasterTenant : {}, {}", id, masterTenant);
        if (masterTenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterTenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        masterTenant = masterTenantService.update(masterTenant);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterTenant.getId().toString()))
            .body(masterTenant);
    }

    /**
     * {@code PATCH  /master-tenants/:id} : Partial updates given fields of an existing masterTenant, field will ignore if it is null
     *
     * @param id the id of the masterTenant to save.
     * @param masterTenant the masterTenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated masterTenant,
     * or with status {@code 400 (Bad Request)} if the masterTenant is not valid,
     * or with status {@code 404 (Not Found)} if the masterTenant is not found,
     * or with status {@code 500 (Internal Server Error)} if the masterTenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MasterTenant> partialUpdateMasterTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MasterTenant masterTenant
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MasterTenant partially : {}, {}", id, masterTenant);
        if (masterTenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, masterTenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!masterTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MasterTenant> result = masterTenantService.partialUpdate(masterTenant);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, masterTenant.getId().toString())
        );
    }

    /**
     * {@code GET  /master-tenants} : get all the masterTenants.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of masterTenants in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MasterTenant>> getAllMasterTenants(
        MasterTenantCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MasterTenants by criteria: {}", criteria);

        Page<MasterTenant> page = masterTenantQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /master-tenants/count} : count all the masterTenants.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMasterTenants(MasterTenantCriteria criteria) {
        LOG.debug("REST request to count MasterTenants by criteria: {}", criteria);
        return ResponseEntity.ok().body(masterTenantQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /master-tenants/:id} : get the "id" masterTenant.
     *
     * @param id the id of the masterTenant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the masterTenant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MasterTenant> getMasterTenant(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MasterTenant : {}", id);
        Optional<MasterTenant> masterTenant = masterTenantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(masterTenant);
    }

    /**
     * {@code DELETE  /master-tenants/:id} : delete the "id" masterTenant.
     *
     * @param id the id of the masterTenant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMasterTenant(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MasterTenant : {}", id);
        masterTenantService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
