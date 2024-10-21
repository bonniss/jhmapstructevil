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
import xyz.jhmapstruct.domain.ProductViVi;
import xyz.jhmapstruct.repository.ProductViViRepository;
import xyz.jhmapstruct.service.ProductViViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductViVi}.
 */
@RestController
@RequestMapping("/api/product-vi-vis")
public class ProductViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViViResource.class);

    private static final String ENTITY_NAME = "productViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductViViService productViViService;

    private final ProductViViRepository productViViRepository;

    public ProductViViResource(ProductViViService productViViService, ProductViViRepository productViViRepository) {
        this.productViViService = productViViService;
        this.productViViRepository = productViViRepository;
    }

    /**
     * {@code POST  /product-vi-vis} : Create a new productViVi.
     *
     * @param productViVi the productViVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productViVi, or with status {@code 400 (Bad Request)} if the productViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductViVi> createProductViVi(@Valid @RequestBody ProductViVi productViVi) throws URISyntaxException {
        LOG.debug("REST request to save ProductViVi : {}", productViVi);
        if (productViVi.getId() != null) {
            throw new BadRequestAlertException("A new productViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productViVi = productViViService.save(productViVi);
        return ResponseEntity.created(new URI("/api/product-vi-vis/" + productViVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productViVi.getId().toString()))
            .body(productViVi);
    }

    /**
     * {@code PUT  /product-vi-vis/:id} : Updates an existing productViVi.
     *
     * @param id the id of the productViVi to save.
     * @param productViVi the productViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViVi,
     * or with status {@code 400 (Bad Request)} if the productViVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductViVi> updateProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductViVi productViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductViVi : {}, {}", id, productViVi);
        if (productViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productViVi = productViViService.update(productViVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViVi.getId().toString()))
            .body(productViVi);
    }

    /**
     * {@code PATCH  /product-vi-vis/:id} : Partial updates given fields of an existing productViVi, field will ignore if it is null
     *
     * @param id the id of the productViVi to save.
     * @param productViVi the productViVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViVi,
     * or with status {@code 400 (Bad Request)} if the productViVi is not valid,
     * or with status {@code 404 (Not Found)} if the productViVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the productViVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductViVi> partialUpdateProductViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductViVi productViVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductViVi partially : {}, {}", id, productViVi);
        if (productViVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductViVi> result = productViViService.partialUpdate(productViVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViVi.getId().toString())
        );
    }

    /**
     * {@code GET  /product-vi-vis} : get all the productViVis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productViVis in body.
     */
    @GetMapping("")
    public List<ProductViVi> getAllProductViVis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ProductViVis");
        return productViViService.findAll();
    }

    /**
     * {@code GET  /product-vi-vis/:id} : get the "id" productViVi.
     *
     * @param id the id of the productViVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productViVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductViVi> getProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductViVi : {}", id);
        Optional<ProductViVi> productViVi = productViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productViVi);
    }

    /**
     * {@code DELETE  /product-vi-vis/:id} : delete the "id" productViVi.
     *
     * @param id the id of the productViVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductViVi : {}", id);
        productViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
