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
import xyz.jhmapstruct.repository.NextSupplierBetaRepository;
import xyz.jhmapstruct.service.NextSupplierBetaQueryService;
import xyz.jhmapstruct.service.NextSupplierBetaService;
import xyz.jhmapstruct.service.criteria.NextSupplierBetaCriteria;
import xyz.jhmapstruct.service.dto.NextSupplierBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierBeta}.
 */
@RestController
@RequestMapping("/api/next-supplier-betas")
public class NextSupplierBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierBetaResource.class);

    private static final String ENTITY_NAME = "nextSupplierBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierBetaService nextSupplierBetaService;

    private final NextSupplierBetaRepository nextSupplierBetaRepository;

    private final NextSupplierBetaQueryService nextSupplierBetaQueryService;

    public NextSupplierBetaResource(
        NextSupplierBetaService nextSupplierBetaService,
        NextSupplierBetaRepository nextSupplierBetaRepository,
        NextSupplierBetaQueryService nextSupplierBetaQueryService
    ) {
        this.nextSupplierBetaService = nextSupplierBetaService;
        this.nextSupplierBetaRepository = nextSupplierBetaRepository;
        this.nextSupplierBetaQueryService = nextSupplierBetaQueryService;
    }

    /**
     * {@code POST  /next-supplier-betas} : Create a new nextSupplierBeta.
     *
     * @param nextSupplierBetaDTO the nextSupplierBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierBetaDTO, or with status {@code 400 (Bad Request)} if the nextSupplierBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierBetaDTO> createNextSupplierBeta(@Valid @RequestBody NextSupplierBetaDTO nextSupplierBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierBeta : {}", nextSupplierBetaDTO);
        if (nextSupplierBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierBetaDTO = nextSupplierBetaService.save(nextSupplierBetaDTO);
        return ResponseEntity.created(new URI("/api/next-supplier-betas/" + nextSupplierBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierBetaDTO.getId().toString()))
            .body(nextSupplierBetaDTO);
    }

    /**
     * {@code PUT  /next-supplier-betas/:id} : Updates an existing nextSupplierBeta.
     *
     * @param id the id of the nextSupplierBetaDTO to save.
     * @param nextSupplierBetaDTO the nextSupplierBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierBetaDTO> updateNextSupplierBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierBetaDTO nextSupplierBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierBeta : {}, {}", id, nextSupplierBetaDTO);
        if (nextSupplierBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierBetaDTO = nextSupplierBetaService.update(nextSupplierBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierBetaDTO.getId().toString()))
            .body(nextSupplierBetaDTO);
    }

    /**
     * {@code PATCH  /next-supplier-betas/:id} : Partial updates given fields of an existing nextSupplierBeta, field will ignore if it is null
     *
     * @param id the id of the nextSupplierBetaDTO to save.
     * @param nextSupplierBetaDTO the nextSupplierBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextSupplierBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierBetaDTO> partialUpdateNextSupplierBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierBetaDTO nextSupplierBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierBeta partially : {}, {}", id, nextSupplierBetaDTO);
        if (nextSupplierBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierBetaDTO> result = nextSupplierBetaService.partialUpdate(nextSupplierBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-betas} : get all the nextSupplierBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierBetaDTO>> getAllNextSupplierBetas(
        NextSupplierBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierBetas by criteria: {}", criteria);

        Page<NextSupplierBetaDTO> page = nextSupplierBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-betas/count} : count all the nextSupplierBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierBetas(NextSupplierBetaCriteria criteria) {
        LOG.debug("REST request to count NextSupplierBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-betas/:id} : get the "id" nextSupplierBeta.
     *
     * @param id the id of the nextSupplierBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierBetaDTO> getNextSupplierBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierBeta : {}", id);
        Optional<NextSupplierBetaDTO> nextSupplierBetaDTO = nextSupplierBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierBetaDTO);
    }

    /**
     * {@code DELETE  /next-supplier-betas/:id} : delete the "id" nextSupplierBeta.
     *
     * @param id the id of the nextSupplierBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierBeta : {}", id);
        nextSupplierBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
