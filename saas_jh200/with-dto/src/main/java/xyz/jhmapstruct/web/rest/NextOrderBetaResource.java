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
import xyz.jhmapstruct.repository.NextOrderBetaRepository;
import xyz.jhmapstruct.service.NextOrderBetaQueryService;
import xyz.jhmapstruct.service.NextOrderBetaService;
import xyz.jhmapstruct.service.criteria.NextOrderBetaCriteria;
import xyz.jhmapstruct.service.dto.NextOrderBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderBeta}.
 */
@RestController
@RequestMapping("/api/next-order-betas")
public class NextOrderBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderBetaResource.class);

    private static final String ENTITY_NAME = "nextOrderBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderBetaService nextOrderBetaService;

    private final NextOrderBetaRepository nextOrderBetaRepository;

    private final NextOrderBetaQueryService nextOrderBetaQueryService;

    public NextOrderBetaResource(
        NextOrderBetaService nextOrderBetaService,
        NextOrderBetaRepository nextOrderBetaRepository,
        NextOrderBetaQueryService nextOrderBetaQueryService
    ) {
        this.nextOrderBetaService = nextOrderBetaService;
        this.nextOrderBetaRepository = nextOrderBetaRepository;
        this.nextOrderBetaQueryService = nextOrderBetaQueryService;
    }

    /**
     * {@code POST  /next-order-betas} : Create a new nextOrderBeta.
     *
     * @param nextOrderBetaDTO the nextOrderBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderBetaDTO, or with status {@code 400 (Bad Request)} if the nextOrderBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderBetaDTO> createNextOrderBeta(@Valid @RequestBody NextOrderBetaDTO nextOrderBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderBeta : {}", nextOrderBetaDTO);
        if (nextOrderBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderBetaDTO = nextOrderBetaService.save(nextOrderBetaDTO);
        return ResponseEntity.created(new URI("/api/next-order-betas/" + nextOrderBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderBetaDTO.getId().toString()))
            .body(nextOrderBetaDTO);
    }

    /**
     * {@code PUT  /next-order-betas/:id} : Updates an existing nextOrderBeta.
     *
     * @param id the id of the nextOrderBetaDTO to save.
     * @param nextOrderBetaDTO the nextOrderBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderBetaDTO> updateNextOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderBetaDTO nextOrderBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderBeta : {}, {}", id, nextOrderBetaDTO);
        if (nextOrderBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderBetaDTO = nextOrderBetaService.update(nextOrderBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderBetaDTO.getId().toString()))
            .body(nextOrderBetaDTO);
    }

    /**
     * {@code PATCH  /next-order-betas/:id} : Partial updates given fields of an existing nextOrderBeta, field will ignore if it is null
     *
     * @param id the id of the nextOrderBetaDTO to save.
     * @param nextOrderBetaDTO the nextOrderBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextOrderBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderBetaDTO> partialUpdateNextOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderBetaDTO nextOrderBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderBeta partially : {}, {}", id, nextOrderBetaDTO);
        if (nextOrderBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderBetaDTO> result = nextOrderBetaService.partialUpdate(nextOrderBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-betas} : get all the nextOrderBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderBetaDTO>> getAllNextOrderBetas(
        NextOrderBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderBetas by criteria: {}", criteria);

        Page<NextOrderBetaDTO> page = nextOrderBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-betas/count} : count all the nextOrderBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderBetas(NextOrderBetaCriteria criteria) {
        LOG.debug("REST request to count NextOrderBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-betas/:id} : get the "id" nextOrderBeta.
     *
     * @param id the id of the nextOrderBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderBetaDTO> getNextOrderBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderBeta : {}", id);
        Optional<NextOrderBetaDTO> nextOrderBetaDTO = nextOrderBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderBetaDTO);
    }

    /**
     * {@code DELETE  /next-order-betas/:id} : delete the "id" nextOrderBeta.
     *
     * @param id the id of the nextOrderBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderBeta : {}", id);
        nextOrderBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
