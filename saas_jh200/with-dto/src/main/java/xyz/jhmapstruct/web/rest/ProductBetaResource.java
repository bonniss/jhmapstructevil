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
import xyz.jhmapstruct.repository.ProductBetaRepository;
import xyz.jhmapstruct.service.ProductBetaQueryService;
import xyz.jhmapstruct.service.ProductBetaService;
import xyz.jhmapstruct.service.criteria.ProductBetaCriteria;
import xyz.jhmapstruct.service.dto.ProductBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.ProductBeta}.
 */
@RestController
@RequestMapping("/api/product-betas")
public class ProductBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProductBetaResource.class);

    private static final String ENTITY_NAME = "productBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductBetaService productBetaService;

    private final ProductBetaRepository productBetaRepository;

    private final ProductBetaQueryService productBetaQueryService;

    public ProductBetaResource(
        ProductBetaService productBetaService,
        ProductBetaRepository productBetaRepository,
        ProductBetaQueryService productBetaQueryService
    ) {
        this.productBetaService = productBetaService;
        this.productBetaRepository = productBetaRepository;
        this.productBetaQueryService = productBetaQueryService;
    }

    /**
     * {@code POST  /product-betas} : Create a new productBeta.
     *
     * @param productBetaDTO the productBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productBetaDTO, or with status {@code 400 (Bad Request)} if the productBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductBetaDTO> createProductBeta(@Valid @RequestBody ProductBetaDTO productBetaDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductBeta : {}", productBetaDTO);
        if (productBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new productBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productBetaDTO = productBetaService.save(productBetaDTO);
        return ResponseEntity.created(new URI("/api/product-betas/" + productBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productBetaDTO.getId().toString()))
            .body(productBetaDTO);
    }

    /**
     * {@code PUT  /product-betas/:id} : Updates an existing productBeta.
     *
     * @param id the id of the productBetaDTO to save.
     * @param productBetaDTO the productBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBetaDTO,
     * or with status {@code 400 (Bad Request)} if the productBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductBetaDTO> updateProductBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductBetaDTO productBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductBeta : {}, {}", id, productBetaDTO);
        if (productBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productBetaDTO = productBetaService.update(productBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBetaDTO.getId().toString()))
            .body(productBetaDTO);
    }

    /**
     * {@code PATCH  /product-betas/:id} : Partial updates given fields of an existing productBeta, field will ignore if it is null
     *
     * @param id the id of the productBetaDTO to save.
     * @param productBetaDTO the productBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productBetaDTO,
     * or with status {@code 400 (Bad Request)} if the productBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductBetaDTO> partialUpdateProductBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductBetaDTO productBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductBeta partially : {}, {}", id, productBetaDTO);
        if (productBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductBetaDTO> result = productBetaService.partialUpdate(productBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-betas} : get all the productBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProductBetaDTO>> getAllProductBetas(
        ProductBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ProductBetas by criteria: {}", criteria);

        Page<ProductBetaDTO> page = productBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /product-betas/count} : count all the productBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countProductBetas(ProductBetaCriteria criteria) {
        LOG.debug("REST request to count ProductBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(productBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /product-betas/:id} : get the "id" productBeta.
     *
     * @param id the id of the productBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductBetaDTO> getProductBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductBeta : {}", id);
        Optional<ProductBetaDTO> productBetaDTO = productBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productBetaDTO);
    }

    /**
     * {@code DELETE  /product-betas/:id} : delete the "id" productBeta.
     *
     * @param id the id of the productBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProductBeta : {}", id);
        productBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
