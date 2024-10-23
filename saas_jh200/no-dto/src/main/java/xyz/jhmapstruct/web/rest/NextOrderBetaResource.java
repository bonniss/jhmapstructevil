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
import xyz.jhmapstruct.domain.NextOrderBeta;
import xyz.jhmapstruct.repository.NextOrderBetaRepository;
import xyz.jhmapstruct.service.NextOrderBetaQueryService;
import xyz.jhmapstruct.service.NextOrderBetaService;
import xyz.jhmapstruct.service.criteria.NextOrderBetaCriteria;
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
     * @param nextOrderBeta the nextOrderBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderBeta, or with status {@code 400 (Bad Request)} if the nextOrderBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderBeta> createNextOrderBeta(@Valid @RequestBody NextOrderBeta nextOrderBeta) throws URISyntaxException {
        LOG.debug("REST request to save NextOrderBeta : {}", nextOrderBeta);
        if (nextOrderBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderBeta = nextOrderBetaService.save(nextOrderBeta);
        return ResponseEntity.created(new URI("/api/next-order-betas/" + nextOrderBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderBeta.getId().toString()))
            .body(nextOrderBeta);
    }

    /**
     * {@code PUT  /next-order-betas/:id} : Updates an existing nextOrderBeta.
     *
     * @param id the id of the nextOrderBeta to save.
     * @param nextOrderBeta the nextOrderBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderBeta,
     * or with status {@code 400 (Bad Request)} if the nextOrderBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderBeta> updateNextOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderBeta nextOrderBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderBeta : {}, {}", id, nextOrderBeta);
        if (nextOrderBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderBeta = nextOrderBetaService.update(nextOrderBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderBeta.getId().toString()))
            .body(nextOrderBeta);
    }

    /**
     * {@code PATCH  /next-order-betas/:id} : Partial updates given fields of an existing nextOrderBeta, field will ignore if it is null
     *
     * @param id the id of the nextOrderBeta to save.
     * @param nextOrderBeta the nextOrderBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderBeta,
     * or with status {@code 400 (Bad Request)} if the nextOrderBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderBeta> partialUpdateNextOrderBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderBeta nextOrderBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderBeta partially : {}, {}", id, nextOrderBeta);
        if (nextOrderBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderBeta> result = nextOrderBetaService.partialUpdate(nextOrderBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderBeta.getId().toString())
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
    public ResponseEntity<List<NextOrderBeta>> getAllNextOrderBetas(
        NextOrderBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderBetas by criteria: {}", criteria);

        Page<NextOrderBeta> page = nextOrderBetaQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the nextOrderBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderBeta> getNextOrderBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderBeta : {}", id);
        Optional<NextOrderBeta> nextOrderBeta = nextOrderBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderBeta);
    }

    /**
     * {@code DELETE  /next-order-betas/:id} : delete the "id" nextOrderBeta.
     *
     * @param id the id of the nextOrderBeta to delete.
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
