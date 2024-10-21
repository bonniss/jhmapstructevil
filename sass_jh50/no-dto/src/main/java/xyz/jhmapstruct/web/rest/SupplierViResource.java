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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.SupplierVi;
import xyz.jhmapstruct.repository.SupplierViRepository;
import xyz.jhmapstruct.service.SupplierViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierVi}.
 */
@RestController
@RequestMapping("/api/supplier-vis")
public class SupplierViResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierViResource.class);

    private static final String ENTITY_NAME = "supplierVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierViService supplierViService;

    private final SupplierViRepository supplierViRepository;

    public SupplierViResource(SupplierViService supplierViService, SupplierViRepository supplierViRepository) {
        this.supplierViService = supplierViService;
        this.supplierViRepository = supplierViRepository;
    }

    /**
     * {@code POST  /supplier-vis} : Create a new supplierVi.
     *
     * @param supplierVi the supplierVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierVi, or with status {@code 400 (Bad Request)} if the supplierVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierVi> createSupplierVi(@Valid @RequestBody SupplierVi supplierVi) throws URISyntaxException {
        LOG.debug("REST request to save SupplierVi : {}", supplierVi);
        if (supplierVi.getId() != null) {
            throw new BadRequestAlertException("A new supplierVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierVi = supplierViService.save(supplierVi);
        return ResponseEntity.created(new URI("/api/supplier-vis/" + supplierVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierVi.getId().toString()))
            .body(supplierVi);
    }

    /**
     * {@code PUT  /supplier-vis/:id} : Updates an existing supplierVi.
     *
     * @param id the id of the supplierVi to save.
     * @param supplierVi the supplierVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierVi,
     * or with status {@code 400 (Bad Request)} if the supplierVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierVi> updateSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierVi supplierVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierVi : {}, {}", id, supplierVi);
        if (supplierVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierVi = supplierViService.update(supplierVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierVi.getId().toString()))
            .body(supplierVi);
    }

    /**
     * {@code PATCH  /supplier-vis/:id} : Partial updates given fields of an existing supplierVi, field will ignore if it is null
     *
     * @param id the id of the supplierVi to save.
     * @param supplierVi the supplierVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierVi,
     * or with status {@code 400 (Bad Request)} if the supplierVi is not valid,
     * or with status {@code 404 (Not Found)} if the supplierVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierVi> partialUpdateSupplierVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierVi supplierVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierVi partially : {}, {}", id, supplierVi);
        if (supplierVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierVi> result = supplierViService.partialUpdate(supplierVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierVi.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-vis} : get all the supplierVis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierVis in body.
     */
    @GetMapping("")
    public List<SupplierVi> getAllSupplierVis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SupplierVis");
        return supplierViService.findAll();
    }

    /**
     * {@code GET  /supplier-vis/:id} : get the "id" supplierVi.
     *
     * @param id the id of the supplierVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierVi> getSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierVi : {}", id);
        Optional<SupplierVi> supplierVi = supplierViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierVi);
    }

    /**
     * {@code DELETE  /supplier-vis/:id} : delete the "id" supplierVi.
     *
     * @param id the id of the supplierVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierVi : {}", id);
        supplierViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
