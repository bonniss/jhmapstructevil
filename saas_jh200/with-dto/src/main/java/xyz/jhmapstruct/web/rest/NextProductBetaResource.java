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
import xyz.jhmapstruct.repository.NextProductBetaRepository;
import xyz.jhmapstruct.service.NextProductBetaQueryService;
import xyz.jhmapstruct.service.NextProductBetaService;
import xyz.jhmapstruct.service.criteria.NextProductBetaCriteria;
import xyz.jhmapstruct.service.dto.NextProductBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextProductBeta}.
 */
@RestController
@RequestMapping("/api/next-product-betas")
public class NextProductBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextProductBetaResource.class);

    private static final String ENTITY_NAME = "nextProductBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextProductBetaService nextProductBetaService;

    private final NextProductBetaRepository nextProductBetaRepository;

    private final NextProductBetaQueryService nextProductBetaQueryService;

    public NextProductBetaResource(
        NextProductBetaService nextProductBetaService,
        NextProductBetaRepository nextProductBetaRepository,
        NextProductBetaQueryService nextProductBetaQueryService
    ) {
        this.nextProductBetaService = nextProductBetaService;
        this.nextProductBetaRepository = nextProductBetaRepository;
        this.nextProductBetaQueryService = nextProductBetaQueryService;
    }

    /**
     * {@code POST  /next-product-betas} : Create a new nextProductBeta.
     *
     * @param nextProductBetaDTO the nextProductBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextProductBetaDTO, or with status {@code 400 (Bad Request)} if the nextProductBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextProductBetaDTO> createNextProductBeta(@Valid @RequestBody NextProductBetaDTO nextProductBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextProductBeta : {}", nextProductBetaDTO);
        if (nextProductBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextProductBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextProductBetaDTO = nextProductBetaService.save(nextProductBetaDTO);
        return ResponseEntity.created(new URI("/api/next-product-betas/" + nextProductBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextProductBetaDTO.getId().toString()))
            .body(nextProductBetaDTO);
    }

    /**
     * {@code PUT  /next-product-betas/:id} : Updates an existing nextProductBeta.
     *
     * @param id the id of the nextProductBetaDTO to save.
     * @param nextProductBetaDTO the nextProductBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextProductBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextProductBetaDTO> updateNextProductBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextProductBetaDTO nextProductBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextProductBeta : {}, {}", id, nextProductBetaDTO);
        if (nextProductBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextProductBetaDTO = nextProductBetaService.update(nextProductBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductBetaDTO.getId().toString()))
            .body(nextProductBetaDTO);
    }

    /**
     * {@code PATCH  /next-product-betas/:id} : Partial updates given fields of an existing nextProductBeta, field will ignore if it is null
     *
     * @param id the id of the nextProductBetaDTO to save.
     * @param nextProductBetaDTO the nextProductBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextProductBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextProductBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextProductBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextProductBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextProductBetaDTO> partialUpdateNextProductBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextProductBetaDTO nextProductBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextProductBeta partially : {}, {}", id, nextProductBetaDTO);
        if (nextProductBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextProductBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextProductBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextProductBetaDTO> result = nextProductBetaService.partialUpdate(nextProductBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextProductBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-product-betas} : get all the nextProductBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextProductBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextProductBetaDTO>> getAllNextProductBetas(
        NextProductBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextProductBetas by criteria: {}", criteria);

        Page<NextProductBetaDTO> page = nextProductBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-product-betas/count} : count all the nextProductBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextProductBetas(NextProductBetaCriteria criteria) {
        LOG.debug("REST request to count NextProductBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextProductBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-product-betas/:id} : get the "id" nextProductBeta.
     *
     * @param id the id of the nextProductBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextProductBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextProductBetaDTO> getNextProductBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextProductBeta : {}", id);
        Optional<NextProductBetaDTO> nextProductBetaDTO = nextProductBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextProductBetaDTO);
    }

    /**
     * {@code DELETE  /next-product-betas/:id} : delete the "id" nextProductBeta.
     *
     * @param id the id of the nextProductBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextProductBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextProductBeta : {}", id);
        nextProductBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
