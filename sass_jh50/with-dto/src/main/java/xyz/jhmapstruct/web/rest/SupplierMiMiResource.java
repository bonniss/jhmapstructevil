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
import xyz.jhmapstruct.repository.SupplierMiMiRepository;
import xyz.jhmapstruct.service.SupplierMiMiService;
import xyz.jhmapstruct.service.dto.SupplierMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.SupplierMiMi}.
 */
@RestController
@RequestMapping("/api/supplier-mi-mis")
public class SupplierMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupplierMiMiResource.class);

    private static final String ENTITY_NAME = "supplierMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupplierMiMiService supplierMiMiService;

    private final SupplierMiMiRepository supplierMiMiRepository;

    public SupplierMiMiResource(SupplierMiMiService supplierMiMiService, SupplierMiMiRepository supplierMiMiRepository) {
        this.supplierMiMiService = supplierMiMiService;
        this.supplierMiMiRepository = supplierMiMiRepository;
    }

    /**
     * {@code POST  /supplier-mi-mis} : Create a new supplierMiMi.
     *
     * @param supplierMiMiDTO the supplierMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierMiMiDTO, or with status {@code 400 (Bad Request)} if the supplierMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierMiMiDTO> createSupplierMiMi(@Valid @RequestBody SupplierMiMiDTO supplierMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save SupplierMiMi : {}", supplierMiMiDTO);
        if (supplierMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierMiMiDTO = supplierMiMiService.save(supplierMiMiDTO);
        return ResponseEntity.created(new URI("/api/supplier-mi-mis/" + supplierMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierMiMiDTO.getId().toString()))
            .body(supplierMiMiDTO);
    }

    /**
     * {@code PUT  /supplier-mi-mis/:id} : Updates an existing supplierMiMi.
     *
     * @param id the id of the supplierMiMiDTO to save.
     * @param supplierMiMiDTO the supplierMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the supplierMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierMiMiDTO> updateSupplierMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierMiMiDTO supplierMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierMiMi : {}, {}", id, supplierMiMiDTO);
        if (supplierMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierMiMiDTO = supplierMiMiService.update(supplierMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMiMiDTO.getId().toString()))
            .body(supplierMiMiDTO);
    }

    /**
     * {@code PATCH  /supplier-mi-mis/:id} : Partial updates given fields of an existing supplierMiMi, field will ignore if it is null
     *
     * @param id the id of the supplierMiMiDTO to save.
     * @param supplierMiMiDTO the supplierMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the supplierMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierMiMiDTO> partialUpdateSupplierMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierMiMiDTO supplierMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierMiMi partially : {}, {}", id, supplierMiMiDTO);
        if (supplierMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierMiMiDTO> result = supplierMiMiService.partialUpdate(supplierMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-mi-mis} : get all the supplierMiMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierMiMis in body.
     */
    @GetMapping("")
    public List<SupplierMiMiDTO> getAllSupplierMiMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SupplierMiMis");
        return supplierMiMiService.findAll();
    }

    /**
     * {@code GET  /supplier-mi-mis/:id} : get the "id" supplierMiMi.
     *
     * @param id the id of the supplierMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierMiMiDTO> getSupplierMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierMiMi : {}", id);
        Optional<SupplierMiMiDTO> supplierMiMiDTO = supplierMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierMiMiDTO);
    }

    /**
     * {@code DELETE  /supplier-mi-mis/:id} : delete the "id" supplierMiMi.
     *
     * @param id the id of the supplierMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupplierMiMi : {}", id);
        supplierMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
