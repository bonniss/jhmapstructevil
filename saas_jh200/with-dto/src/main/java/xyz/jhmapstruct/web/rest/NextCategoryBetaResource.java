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
import xyz.jhmapstruct.repository.NextCategoryBetaRepository;
import xyz.jhmapstruct.service.NextCategoryBetaQueryService;
import xyz.jhmapstruct.service.NextCategoryBetaService;
import xyz.jhmapstruct.service.criteria.NextCategoryBetaCriteria;
import xyz.jhmapstruct.service.dto.NextCategoryBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCategoryBeta}.
 */
@RestController
@RequestMapping("/api/next-category-betas")
public class NextCategoryBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCategoryBetaResource.class);

    private static final String ENTITY_NAME = "nextCategoryBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCategoryBetaService nextCategoryBetaService;

    private final NextCategoryBetaRepository nextCategoryBetaRepository;

    private final NextCategoryBetaQueryService nextCategoryBetaQueryService;

    public NextCategoryBetaResource(
        NextCategoryBetaService nextCategoryBetaService,
        NextCategoryBetaRepository nextCategoryBetaRepository,
        NextCategoryBetaQueryService nextCategoryBetaQueryService
    ) {
        this.nextCategoryBetaService = nextCategoryBetaService;
        this.nextCategoryBetaRepository = nextCategoryBetaRepository;
        this.nextCategoryBetaQueryService = nextCategoryBetaQueryService;
    }

    /**
     * {@code POST  /next-category-betas} : Create a new nextCategoryBeta.
     *
     * @param nextCategoryBetaDTO the nextCategoryBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCategoryBetaDTO, or with status {@code 400 (Bad Request)} if the nextCategoryBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCategoryBetaDTO> createNextCategoryBeta(@Valid @RequestBody NextCategoryBetaDTO nextCategoryBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCategoryBeta : {}", nextCategoryBetaDTO);
        if (nextCategoryBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextCategoryBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCategoryBetaDTO = nextCategoryBetaService.save(nextCategoryBetaDTO);
        return ResponseEntity.created(new URI("/api/next-category-betas/" + nextCategoryBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCategoryBetaDTO.getId().toString()))
            .body(nextCategoryBetaDTO);
    }

    /**
     * {@code PUT  /next-category-betas/:id} : Updates an existing nextCategoryBeta.
     *
     * @param id the id of the nextCategoryBetaDTO to save.
     * @param nextCategoryBetaDTO the nextCategoryBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCategoryBetaDTO> updateNextCategoryBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCategoryBetaDTO nextCategoryBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCategoryBeta : {}, {}", id, nextCategoryBetaDTO);
        if (nextCategoryBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCategoryBetaDTO = nextCategoryBetaService.update(nextCategoryBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryBetaDTO.getId().toString()))
            .body(nextCategoryBetaDTO);
    }

    /**
     * {@code PATCH  /next-category-betas/:id} : Partial updates given fields of an existing nextCategoryBeta, field will ignore if it is null
     *
     * @param id the id of the nextCategoryBetaDTO to save.
     * @param nextCategoryBetaDTO the nextCategoryBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCategoryBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextCategoryBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextCategoryBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCategoryBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCategoryBetaDTO> partialUpdateNextCategoryBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCategoryBetaDTO nextCategoryBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCategoryBeta partially : {}, {}", id, nextCategoryBetaDTO);
        if (nextCategoryBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCategoryBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCategoryBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCategoryBetaDTO> result = nextCategoryBetaService.partialUpdate(nextCategoryBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCategoryBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-category-betas} : get all the nextCategoryBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCategoryBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCategoryBetaDTO>> getAllNextCategoryBetas(
        NextCategoryBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCategoryBetas by criteria: {}", criteria);

        Page<NextCategoryBetaDTO> page = nextCategoryBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-category-betas/count} : count all the nextCategoryBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCategoryBetas(NextCategoryBetaCriteria criteria) {
        LOG.debug("REST request to count NextCategoryBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCategoryBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-category-betas/:id} : get the "id" nextCategoryBeta.
     *
     * @param id the id of the nextCategoryBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCategoryBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCategoryBetaDTO> getNextCategoryBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCategoryBeta : {}", id);
        Optional<NextCategoryBetaDTO> nextCategoryBetaDTO = nextCategoryBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCategoryBetaDTO);
    }

    /**
     * {@code DELETE  /next-category-betas/:id} : delete the "id" nextCategoryBeta.
     *
     * @param id the id of the nextCategoryBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCategoryBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCategoryBeta : {}", id);
        nextCategoryBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
