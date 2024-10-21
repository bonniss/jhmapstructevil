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
import xyz.jhmapstruct.domain.SupplierMi;
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.SupplierMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierMi}.
 */
@RestController
@RequestMapping("/api/supplier-mis")
public class SupplierMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiResource.class);

    private static final String ENTITY_NAME = "supplierMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierMiService supplierMiService;

    private final SupplierMiRepository supplierMiRepository;

    public SupplierMiResource(SupplierMiService supplierMiService, SupplierMiRepository supplierMiRepository) {
        this.supplierMiService = supplierMiService;
        this.supplierMiRepository = supplierMiRepository;
    }

    /**
     * {@code POST  /supplier-mis} : Create a new supplierMi.
     *
     * @param supplierMi the supplierMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierMi, or with status {@code 400 (Bad Request)} if the supplierMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierMi> createSupplierMi(@Valid @RequestBody SupplierMi supplierMi) throws URISyntaxException {
        LOG.debug("REST request to save SupplierMi : {}", supplierMi);
        if (supplierMi.getId() != null) {
            throw new BadRequestAlertException("A new supplierMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierMi = supplierMiService.save(supplierMi);
        return ResponseEntity.created(new URI("/api/supplier-mis/" + supplierMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierMi.getId().toString()))
            .body(supplierMi);
    }

    /**
     * {@code PUT  /supplier-mis/:id} : Updates an existing supplierMi.
     *
     * @param id the id of the supplierMi to save.
     * @param supplierMi the supplierMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMi,
     * or with status {@code 400 (Bad Request)} if the supplierMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierMi> updateSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierMi supplierMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierMi : {}, {}", id, supplierMi);
        if (supplierMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierMi = supplierMiService.update(supplierMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMi.getId().toString()))
            .body(supplierMi);
    }

    /**
     * {@code PATCH  /supplier-mis/:id} : Partial updates given fields of an existing supplierMi, field will ignore if it is null
     *
     * @param id the id of the supplierMi to save.
     * @param supplierMi the supplierMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMi,
     * or with status {@code 400 (Bad Request)} if the supplierMi is not valid,
     * or with status {@code 404 (Not Found)} if the supplierMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierMi> partialUpdateSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierMi supplierMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierMi partially : {}, {}", id, supplierMi);
        if (supplierMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierMi> result = supplierMiService.partialUpdate(supplierMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMi.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-mis} : get all the supplierMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierMis in body.
     */
    @GetMapping("")
    public List<SupplierMi> getAllSupplierMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SupplierMis");
        return supplierMiService.findAll();
    }

    /**
     * {@code GET  /supplier-mis/:id} : get the "id" supplierMi.
     *
     * @param id the id of the supplierMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierMi> getSupplierMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierMi : {}", id);
        Optional<SupplierMi> supplierMi = supplierMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierMi);
    }

    /**
     * {@code DELETE  /supplier-mis/:id} : delete the "id" supplierMi.
     *
     * @param id the id of the supplierMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierMi : {}", id);
        supplierMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
