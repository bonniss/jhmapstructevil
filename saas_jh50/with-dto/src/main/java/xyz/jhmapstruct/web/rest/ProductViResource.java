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
import xyz.jhmapstruct.repository.ProductViRepository;
import xyz.jhmapstruct.service.ProductViService;
import xyz.jhmapstruct.service.dto.ProductViDTO;
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

    public ProductViResource(ProductViService productViService, ProductViRepository productViRepository) {
        this.productViService = productViService;
        this.productViRepository = productViRepository;
    }

    /**
     * {@code POST  /product-vis} : Create a new productVi.
     *
     * @param productViDTO the productViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productViDTO, or with status {@code 400 (Bad Request)} if the productVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProductViDTO> createProductVi(@Valid @RequestBody ProductViDTO productViDTO) throws URISyntaxException {
        LOG.debug("REST request to save ProductVi : {}", productViDTO);
        if (productViDTO.getId() != null) {
            throw new BadRequestAlertException("A new productVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        productViDTO = productViService.save(productViDTO);
        return ResponseEntity.created(new URI("/api/product-vis/" + productViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, productViDTO.getId().toString()))
            .body(productViDTO);
    }

    /**
     * {@code PUT  /product-vis/:id} : Updates an existing productVi.
     *
     * @param id the id of the productViDTO to save.
     * @param productViDTO the productViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViDTO,
     * or with status {@code 400 (Bad Request)} if the productViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductViDTO> updateProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProductViDTO productViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProductVi : {}, {}", id, productViDTO);
        if (productViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        productViDTO = productViService.update(productViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViDTO.getId().toString()))
            .body(productViDTO);
    }

    /**
     * {@code PATCH  /product-vis/:id} : Partial updates given fields of an existing productVi, field will ignore if it is null
     *
     * @param id the id of the productViDTO to save.
     * @param productViDTO the productViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productViDTO,
     * or with status {@code 400 (Bad Request)} if the productViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the productViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the productViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProductViDTO> partialUpdateProductVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProductViDTO productViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProductVi partially : {}, {}", id, productViDTO);
        if (productViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, productViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!productViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProductViDTO> result = productViService.partialUpdate(productViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /product-vis} : get all the productVis.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productVis in body.
     */
    @GetMapping("")
    public List<ProductViDTO> getAllProductVis(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all ProductVis");
        return productViService.findAll();
    }

    /**
     * {@code GET  /product-vis/:id} : get the "id" productVi.
     *
     * @param id the id of the productViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductViDTO> getProductVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProductVi : {}", id);
        Optional<ProductViDTO> productViDTO = productViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productViDTO);
    }

    /**
     * {@code DELETE  /product-vis/:id} : delete the "id" productVi.
     *
     * @param id the id of the productViDTO to delete.
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
