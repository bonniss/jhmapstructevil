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
import xyz.jhmapstruct.repository.NextProductRepository;
import xyz.jhmapstruct.service.NextProductQueryService;
import xyz.jhmapstruct.service.NextProductService;
import xyz.jhmapstruct.service.criteria.NextProductCriteria;
import xyz.jhmapstruct.service.dto.NextProductDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProduct}.
 */
@RestController
@RequestMapping("/api/next-products")
public class NextProductResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductResource.class);

    private static final String ENTITY_NAME = "nextProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductService nextProductService;

    private final NextProductRepository nextProductRepository;

    private final NextProductQueryService nextProductQueryService;

    public NextProductResource(
        NextProductService nextProductService,
        NextProductRepository nextProductRepository,
        NextProductQueryService nextProductQueryService
    ) {
        this.nextProductService = nextProductService;
        this.nextProductRepository = nextProductRepository;
        this.nextProductQueryService = nextProductQueryService;
    }

    /**
     * {@code POST  /next-products} : Create a new nextProduct.
     *
     * @param nextProductDTO the nextProductDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductDTO, or with status {@code 400 (Bad Request)} if the nextProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductDTO> createNextProduct(@Valid @RequestBody NextProductDTO nextProductDTO) throws URISyntaxException {
        LOG.debug("REST request to save NextProduct : {}", nextProductDTO);
        if (nextProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductDTO = nextProductService.save(nextProductDTO);
        return ResponseEntity.created(new URI("/api/next-products/" + nextProductDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductDTO.getId().toString()))
            .body(nextProductDTO);
    }

    /**
     * {@code PUT  /next-products/:id} : Updates an existing nextProduct.
     *
     * @param id the id of the nextProductDTO to save.
     * @param nextProductDTO the nextProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductDTO> updateNextProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductDTO nextProductDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProduct : {}, {}", id, nextProductDTO);
        if (nextProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductDTO = nextProductService.update(nextProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductDTO.getId().toString()))
            .body(nextProductDTO);
    }

    /**
     * {@code PATCH  /next-products/:id} : Partial updates given fields of an existing nextProduct, field will ignore if it is null
     *
     * @param id the id of the nextProductDTO to save.
     * @param nextProductDTO the nextProductDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductDTO> partialUpdateNextProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductDTO nextProductDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProduct partially : {}, {}", id, nextProductDTO);
        if (nextProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductDTO> result = nextProductService.partialUpdate(nextProductDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-products} : get all the nextProducts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProducts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductDTO>> getAllNextProducts(
        NextProductCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProducts by criteria: {}", criteria);

        Page<NextProductDTO> page = nextProductQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-products/count} : count all the nextProducts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProducts(NextProductCriteria criteria) {
        LOG.debug("REST request to count NextProducts by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-products/:id} : get the "id" nextProduct.
     *
     * @param id the id of the nextProductDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductDTO> getNextProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProduct : {}", id);
        Optional<NextProductDTO> nextProductDTO = nextProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductDTO);
    }

    /**
     * {@code DELETE  /next-products/:id} : delete the "id" nextProduct.
     *
     * @param id the id of the nextProductDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProduct(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProduct : {}", id);
        nextProductService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
