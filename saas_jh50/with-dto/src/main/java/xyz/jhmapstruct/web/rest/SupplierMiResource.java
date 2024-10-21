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
import xyz.jhmapstruct.repository.SupplierMiRepository;
import xyz.jhmapstruct.service.SupplierMiService;
import xyz.jhmapstruct.service.dto.SupplierMiDTO;
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
     * @param supplierMiDTO the supplierMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supplierMiDTO, or with status {@code 400 (Bad Request)} if the supplierMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupplierMiDTO> createSupplierMi(@Valid @RequestBody SupplierMiDTO supplierMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save SupplierMi : {}", supplierMiDTO);
        if (supplierMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new supplierMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supplierMiDTO = supplierMiService.save(supplierMiDTO);
        return ResponseEntity.created(new URI("/api/supplier-mis/" + supplierMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supplierMiDTO.getId().toString()))
            .body(supplierMiDTO);
    }

    /**
     * {@code PUT  /supplier-mis/:id} : Updates an existing supplierMi.
     *
     * @param id the id of the supplierMiDTO to save.
     * @param supplierMiDTO the supplierMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMiDTO,
     * or with status {@code 400 (Bad Request)} if the supplierMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supplierMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupplierMiDTO> updateSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SupplierMiDTO supplierMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupplierMi : {}, {}", id, supplierMiDTO);
        if (supplierMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supplierMiDTO = supplierMiService.update(supplierMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMiDTO.getId().toString()))
            .body(supplierMiDTO);
    }

    /**
     * {@code PATCH  /supplier-mis/:id} : Partial updates given fields of an existing supplierMi, field will ignore if it is null
     *
     * @param id the id of the supplierMiDTO to save.
     * @param supplierMiDTO the supplierMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supplierMiDTO,
     * or with status {@code 400 (Bad Request)} if the supplierMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the supplierMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the supplierMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupplierMiDTO> partialUpdateSupplierMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SupplierMiDTO supplierMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupplierMi partially : {}, {}", id, supplierMiDTO);
        if (supplierMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supplierMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supplierMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupplierMiDTO> result = supplierMiService.partialUpdate(supplierMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supplierMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /supplier-mis} : get all the supplierMis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supplierMis in body.
     */
    @GetMapping("")
    public List<SupplierMiDTO> getAllSupplierMis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all SupplierMis");
        return supplierMiService.findAll();
    }

    /**
     * {@code GET  /supplier-mis/:id} : get the "id" supplierMi.
     *
     * @param id the id of the supplierMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supplierMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupplierMiDTO> getSupplierMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupplierMi : {}", id);
        Optional<SupplierMiDTO> supplierMiDTO = supplierMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(supplierMiDTO);
    }

    /**
     * {@code DELETE  /supplier-mis/:id} : delete the "id" supplierMi.
     *
     * @param id the id of the supplierMiDTO to delete.
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
