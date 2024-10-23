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
import xyz.jhmapstruct.domain.ProductTheta;
import xyz.jhmapstruct.repository.ProductThetaRepository;
import xyz.jhmapstruct.service.ProductThetaQueryService;
import xyz.jhmapstruct.service.ProductThetaService;
import xyz.jhmapstruct.service.criteria.ProductThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductTheta}.
 */
@RestController
@RequestMapping("/api/product-thetas")
public class ProductThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductThetaResource.class);

    private static final String ENTITY_NAME = "productTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductThetaService productThetaService;

    private final ProductThetaRepository productThetaRepository;

    private final ProductThetaQueryService productThetaQueryService;

    public ProductThetaResource(
        ProductThetaService productThetaService,
        ProductThetaRepository productThetaRepository,
        ProductThetaQueryService productThetaQueryService
    ) {
        this.productThetaService = productThetaService;
        this.productThetaRepository = productThetaRepository;
        this.productThetaQueryService = productThetaQueryService;
    }

    /**
     * {@code POST  /product-thetas} : Create a new productTheta.
     *
     * @param productTheta the productTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productTheta, or with status {@code 400 (Bad Request)} if the productTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductTheta> createProductTheta(@Valid @RequestBody ProductTheta productTheta) throws URISyntaxException {
        LOG.debug("REST request to save ProductTheta : {}", productTheta);
        if (productTheta.getId() != null) {
            throw new BadRequestAlertException("A new productTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productTheta = productThetaService.save(productTheta);
        return ResponseEntity.created(new URI("/api/product-thetas/" + productTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productTheta.getId().toString()))
            .body(productTheta);
    }

    /**
     * {@code PUT  /product-thetas/:id} : Updates an existing productTheta.
     *
     * @param id the id of the productTheta to save.
     * @param productTheta the productTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTheta,
     * or with status {@code 400 (Bad Request)} if the productTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductTheta> updateProductTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductTheta productTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductTheta : {}, {}", id, productTheta);
        if (productTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productTheta = productThetaService.update(productTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTheta.getId().toString()))
            .body(productTheta);
    }

    /**
     * {@code PATCH  /product-thetas/:id} : Partial updates given fields of an existing productTheta, field will ignore if it is null
     *
     * @param id the id of the productTheta to save.
     * @param productTheta the productTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productTheta,
     * or with status {@code 400 (Bad Request)} if the productTheta is not valid,
     * or with status {@code 404 (Not Found)} if the productTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the productTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductTheta> partialUpdateProductTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductTheta productTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductTheta partially : {}, {}", id, productTheta);
        if (productTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductTheta> result = productThetaService.partialUpdate(productTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /product-thetas} : get all the productThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductTheta>> getAllProductThetas(
        ProductThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductThetas by criteria: {}", criteria);

        Page<ProductTheta> page = productThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-thetas/count} : count all the productThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductThetas(ProductThetaCriteria criteria) {
        LOG.debug("REST request to count ProductThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(productThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-thetas/:id} : get the "id" productTheta.
     *
     * @param id the id of the productTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductTheta> getProductTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductTheta : {}", id);
        Optional<ProductTheta> productTheta = productThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productTheta);
    }

    /**
     * {@code DELETE  /product-thetas/:id} : delete the "id" productTheta.
     *
     * @param id the id of the productTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductTheta : {}", id);
        productThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
