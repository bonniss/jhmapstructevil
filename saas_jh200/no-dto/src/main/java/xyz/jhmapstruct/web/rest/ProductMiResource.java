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
import xyz.jhmapstruct.domain.ProductMi;
import xyz.jhmapstruct.repository.ProductMiRepository;
import xyz.jhmapstruct.service.ProductMiQueryService;
import xyz.jhmapstruct.service.ProductMiService;
import xyz.jhmapstruct.service.criteria.ProductMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductMi}.
 */
@RestController
@RequestMapping("/api/product-mis")
public class ProductMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiResource.class);

    private static final String ENTITY_NAME = "productMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMiService productMiService;

    private final ProductMiRepository productMiRepository;

    private final ProductMiQueryService productMiQueryService;

    public ProductMiResource(
        ProductMiService productMiService,
        ProductMiRepository productMiRepository,
        ProductMiQueryService productMiQueryService
    ) {
        this.productMiService = productMiService;
        this.productMiRepository = productMiRepository;
        this.productMiQueryService = productMiQueryService;
    }

    /**
     * {@code POST  /product-mis} : Create a new productMi.
     *
     * @param productMi the productMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMi, or with status {@code 400 (Bad Request)} if the productMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductMi> createProductMi(@Valid @RequestBody ProductMi productMi) throws URISyntaxException {
        LOG.debug("REST request to save ProductMi : {}", productMi);
        if (productMi.getId() != null) {
            throw new BadRequestAlertException("A new productMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productMi = productMiService.save(productMi);
        return ResponseEntity.created(new URI("/api/product-mis/" + productMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productMi.getId().toString()))
            .body(productMi);
    }

    /**
     * {@code PUT  /product-mis/:id} : Updates an existing productMi.
     *
     * @param id the id of the productMi to save.
     * @param productMi the productMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMi,
     * or with status {@code 400 (Bad Request)} if the productMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductMi> updateProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductMi productMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductMi : {}, {}", id, productMi);
        if (productMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productMi = productMiService.update(productMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMi.getId().toString()))
            .body(productMi);
    }

    /**
     * {@code PATCH  /product-mis/:id} : Partial updates given fields of an existing productMi, field will ignore if it is null
     *
     * @param id the id of the productMi to save.
     * @param productMi the productMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMi,
     * or with status {@code 400 (Bad Request)} if the productMi is not valid,
     * or with status {@code 404 (Not Found)} if the productMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductMi> partialUpdateProductMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductMi productMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductMi partially : {}, {}", id, productMi);
        if (productMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductMi> result = productMiService.partialUpdate(productMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMi.getId().toString())
        );
    }

    /**
     * {@code GET  /product-mis} : get all the productMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductMi>> getAllProductMis(
        ProductMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductMis by criteria: {}", criteria);

        Page<ProductMi> page = productMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-mis/count} : count all the productMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductMis(ProductMiCriteria criteria) {
        LOG.debug("REST request to count ProductMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(productMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-mis/:id} : get the "id" productMi.
     *
     * @param id the id of the productMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductMi> getProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductMi : {}", id);
        Optional<ProductMi> productMi = productMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productMi);
    }

    /**
     * {@code DELETE  /product-mis/:id} : delete the "id" productMi.
     *
     * @param id the id of the productMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductMi : {}", id);
        productMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
