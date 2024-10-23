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
import xyz.jhmapstruct.domain.SupplierTheta;
import xyz.jhmapstruct.repository.SupplierThetaRepository;
import xyz.jhmapstruct.service.SupplierThetaQueryService;
import xyz.jhmapstruct.service.SupplierThetaService;
import xyz.jhmapstruct.service.criteria.SupplierThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierTheta}.
 */
@RestController
@RequestMapping("/api/supplier-thetas")
public class SupplierThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierThetaResource.class);

    private static final String ENTITY_NAME = "supplierTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierThetaService supplierThetaService;

    private final SupplierThetaRepository supplierThetaRepository;

    private final SupplierThetaQueryService supplierThetaQueryService;

    public SupplierThetaResource(
        SupplierThetaService supplierThetaService,
        SupplierThetaRepository supplierThetaRepository,
        SupplierThetaQueryService supplierThetaQueryService
    ) {
        this.supplierThetaService = supplierThetaService;
        this.supplierThetaRepository = supplierThetaRepository;
        this.supplierThetaQueryService = supplierThetaQueryService;
    }

    /**
     * {@code POST  /supplier-thetas} : Create a new supplierTheta.
     *
     * @param supplierTheta the supplierTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierTheta, or with status {@code 400 (Bad Request)} if the supplierTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierTheta> createSupplierTheta(@Valid @RequestBody SupplierTheta supplierTheta) throws URISyntaxException {
        LOG.debug("REST request to save SupplierTheta : {}", supplierTheta);
        if (supplierTheta.getId() != null) {
            throw new BadRequestAlertException("A new supplierTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierTheta = supplierThetaService.save(supplierTheta);
        return ResponseEntity.created(new URI("/api/supplier-thetas/" + supplierTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierTheta.getId().toString()))
            .body(supplierTheta);
    }

    /**
     * {@code PUT  /supplier-thetas/:id} : Updates an existing supplierTheta.
     *
     * @param id the id of the supplierTheta to save.
     * @param supplierTheta the supplierTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTheta,
     * or with status {@code 400 (Bad Request)} if the supplierTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierTheta> updateSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierTheta supplierTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierTheta : {}, {}", id, supplierTheta);
        if (supplierTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierTheta = supplierThetaService.update(supplierTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierTheta.getId().toString()))
            .body(supplierTheta);
    }

    /**
     * {@code PATCH  /supplier-thetas/:id} : Partial updates given fields of an existing supplierTheta, field will ignore if it is null
     *
     * @param id the id of the supplierTheta to save.
     * @param supplierTheta the supplierTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierTheta,
     * or with status {@code 400 (Bad Request)} if the supplierTheta is not valid,
     * or with status {@code 404 (Not Found)} if the supplierTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierTheta> partialUpdateSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierTheta supplierTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierTheta partially : {}, {}", id, supplierTheta);
        if (supplierTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierTheta> result = supplierThetaService.partialUpdate(supplierTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-thetas} : get all the supplierThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SupplierTheta>> getAllSupplierThetas(
        SupplierThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SupplierThetas by criteria: {}", criteria);

        Page<SupplierTheta> page = supplierThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /supplier-thetas/count} : count all the supplierThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSupplierThetas(SupplierThetaCriteria criteria) {
        LOG.debug("REST request to count SupplierThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(supplierThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /supplier-thetas/:id} : get the "id" supplierTheta.
     *
     * @param id the id of the supplierTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierTheta> getSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierTheta : {}", id);
        Optional<SupplierTheta> supplierTheta = supplierThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierTheta);
    }

    /**
     * {@code DELETE  /supplier-thetas/:id} : delete the "id" supplierTheta.
     *
     * @param id the id of the supplierTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierTheta : {}", id);
        supplierThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
