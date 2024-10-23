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
import xyz.jhmapstruct.domain.ProductMiMi;
import xyz.jhmapstruct.repository.ProductMiMiRepository;
import xyz.jhmapstruct.service.ProductMiMiQueryService;
import xyz.jhmapstruct.service.ProductMiMiService;
import xyz.jhmapstruct.service.criteria.ProductMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductMiMi}.
 */
@RestController
@RequestMapping("/api/product-mi-mis")
public class ProductMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductMiMiResource.class);

    private static final String ENTITY_NAME = "productMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductMiMiService productMiMiService;

    private final ProductMiMiRepository productMiMiRepository;

    private final ProductMiMiQueryService productMiMiQueryService;

    public ProductMiMiResource(
        ProductMiMiService productMiMiService,
        ProductMiMiRepository productMiMiRepository,
        ProductMiMiQueryService productMiMiQueryService
    ) {
        this.productMiMiService = productMiMiService;
        this.productMiMiRepository = productMiMiRepository;
        this.productMiMiQueryService = productMiMiQueryService;
    }

    /**
     * {@code POST  /product-mi-mis} : Create a new productMiMi.
     *
     * @param productMiMi the productMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productMiMi, or with status {@code 400 (Bad Request)} if the productMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductMiMi> createProductMiMi(@Valid @RequestBody ProductMiMi productMiMi) throws URISyntaxException {
        LOG.debug("REST request to save ProductMiMi : {}", productMiMi);
        if (productMiMi.getId() != null) {
            throw new BadRequestAlertException("A new productMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productMiMi = productMiMiService.save(productMiMi);
        return ResponseEntity.created(new URI("/api/product-mi-mis/" + productMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productMiMi.getId().toString()))
            .body(productMiMi);
    }

    /**
     * {@code PUT  /product-mi-mis/:id} : Updates an existing productMiMi.
     *
     * @param id the id of the productMiMi to save.
     * @param productMiMi the productMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiMi,
     * or with status {@code 400 (Bad Request)} if the productMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductMiMi> updateProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductMiMi productMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductMiMi : {}, {}", id, productMiMi);
        if (productMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productMiMi = productMiMiService.update(productMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiMi.getId().toString()))
            .body(productMiMi);
    }

    /**
     * {@code PATCH  /product-mi-mis/:id} : Partial updates given fields of an existing productMiMi, field will ignore if it is null
     *
     * @param id the id of the productMiMi to save.
     * @param productMiMi the productMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productMiMi,
     * or with status {@code 400 (Bad Request)} if the productMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the productMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the productMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductMiMi> partialUpdateProductMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductMiMi productMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductMiMi partially : {}, {}", id, productMiMi);
        if (productMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductMiMi> result = productMiMiService.partialUpdate(productMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /product-mi-mis} : get all the productMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductMiMi>> getAllProductMiMis(
        ProductMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductMiMis by criteria: {}", criteria);

        Page<ProductMiMi> page = productMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-mi-mis/count} : count all the productMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductMiMis(ProductMiMiCriteria criteria) {
        LOG.debug("REST request to count ProductMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(productMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-mi-mis/:id} : get the "id" productMiMi.
     *
     * @param id the id of the productMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductMiMi> getProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductMiMi : {}", id);
        Optional<ProductMiMi> productMiMi = productMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productMiMi);
    }

    /**
     * {@code DELETE  /product-mi-mis/:id} : delete the "id" productMiMi.
     *
     * @param id the id of the productMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductMiMi : {}", id);
        productMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
