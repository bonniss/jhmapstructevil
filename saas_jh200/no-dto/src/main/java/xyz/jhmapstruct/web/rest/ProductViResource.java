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
import xyz.jhmapstruct.domain.ProductVi;
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViQueryService;
import xyz.jhmapstruct.service.ProductViService;
import xyz.jhmapstruct.service.criteria.ProductViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductVi}.
 */
@RestController
@RequestMapping("/api/product-vis")
public class ProductViResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductViResource.class);

    private static final String ENTITY_NAME = "productVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductViService productViService;

    private final ProductViRepository productViRepository;

    private final ProductViQueryService productViQueryService;

    public ProductViResource(
        ProductViService productViService,
        ProductViRepository productViRepository,
        ProductViQueryService productViQueryService
    ) {
        this.productViService = productViService;
        this.productViRepository = productViRepository;
        this.productViQueryService = productViQueryService;
    }

    /**
     * {@code POST  /product-vis} : Create a new productVi.
     *
     * @param productVi the productVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productVi, or with status {@code 400 (Bad Request)} if the productVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductVi> createProductVi(@Valid @RequestBody ProductVi productVi) throws URISyntaxException {
        LOG.debug("REST request to save ProductVi : {}", productVi);
        if (productVi.getId() != null) {
            throw new BadRequestAlertException("A new productVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productVi = productViService.save(productVi);
        return ResponseEntity.created(new URI("/api/product-vis/" + productVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productVi.getId().toString()))
            .body(productVi);
    }

    /**
     * {@code PUT  /product-vis/:id} : Updates an existing productVi.
     *
     * @param id the id of the productVi to save.
     * @param productVi the productVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVi,
     * or with status {@code 400 (Bad Request)} if the productVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductVi> updateProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductVi productVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductVi : {}, {}", id, productVi);
        if (productVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productVi = productViService.update(productVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVi.getId().toString()))
            .body(productVi);
    }

    /**
     * {@code PATCH  /product-vis/:id} : Partial updates given fields of an existing productVi, field will ignore if it is null
     *
     * @param id the id of the productVi to save.
     * @param productVi the productVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productVi,
     * or with status {@code 400 (Bad Request)} if the productVi is not valid,
     * or with status {@code 404 (Not Found)} if the productVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the productVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductVi> partialUpdateProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductVi productVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductVi partially : {}, {}", id, productVi);
        if (productVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductVi> result = productViService.partialUpdate(productVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productVi.getId().toString())
        );
    }

    /**
     * {@code GET  /product-vis} : get all the productVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductVi>> getAllProductVis(
        ProductViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductVis by criteria: {}", criteria);

        Page<ProductVi> page = productViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-vis/count} : count all the productVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductVis(ProductViCriteria criteria) {
        LOG.debug("REST request to count ProductVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(productViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-vis/:id} : get the "id" productVi.
     *
     * @param id the id of the productVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductVi> getProductVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductVi : {}", id);
        Optional<ProductVi> productVi = productViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productVi);
    }

    /**
     * {@code DELETE  /product-vis/:id} : delete the "id" productVi.
     *
     * @param id the id of the productVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductVi : {}", id);
        productViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
