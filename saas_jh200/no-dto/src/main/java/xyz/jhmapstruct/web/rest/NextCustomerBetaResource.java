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
import xyz.jhmapstruct.domain.NextCustomerBeta;
import xyz.jhmapstruct.repository.NextCustomerBetaRepository;
import xyz.jhmapstruct.service.NextCustomerBetaQueryService;
import xyz.jhmapstruct.service.NextCustomerBetaService;
import xyz.jhmapstruct.service.criteria.NextCustomerBetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextCustomerBeta}.
 */
@RestController
@RequestMapping("/api/next-customer-betas")
public class NextCustomerBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextCustomerBetaResource.class);

    private static final String ENTITY_NAME = "nextCustomerBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextCustomerBetaService nextCustomerBetaService;

    private final NextCustomerBetaRepository nextCustomerBetaRepository;

    private final NextCustomerBetaQueryService nextCustomerBetaQueryService;

    public NextCustomerBetaResource(
        NextCustomerBetaService nextCustomerBetaService,
        NextCustomerBetaRepository nextCustomerBetaRepository,
        NextCustomerBetaQueryService nextCustomerBetaQueryService
    ) {
        this.nextCustomerBetaService = nextCustomerBetaService;
        this.nextCustomerBetaRepository = nextCustomerBetaRepository;
        this.nextCustomerBetaQueryService = nextCustomerBetaQueryService;
    }

    /**
     * {@code POST  /next-customer-betas} : Create a new nextCustomerBeta.
     *
     * @param nextCustomerBeta the nextCustomerBeta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextCustomerBeta, or with status {@code 400 (Bad Request)} if the nextCustomerBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextCustomerBeta> createNextCustomerBeta(@Valid @RequestBody NextCustomerBeta nextCustomerBeta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextCustomerBeta : {}", nextCustomerBeta);
        if (nextCustomerBeta.getId() != null) {
            throw new BadRequestAlertException("A new nextCustomerBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextCustomerBeta = nextCustomerBetaService.save(nextCustomerBeta);
        return ResponseEntity.created(new URI("/api/next-customer-betas/" + nextCustomerBeta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextCustomerBeta.getId().toString()))
            .body(nextCustomerBeta);
    }

    /**
     * {@code PUT  /next-customer-betas/:id} : Updates an existing nextCustomerBeta.
     *
     * @param id the id of the nextCustomerBeta to save.
     * @param nextCustomerBeta the nextCustomerBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerBeta,
     * or with status {@code 400 (Bad Request)} if the nextCustomerBeta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextCustomerBeta> updateNextCustomerBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextCustomerBeta nextCustomerBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextCustomerBeta : {}, {}", id, nextCustomerBeta);
        if (nextCustomerBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextCustomerBeta = nextCustomerBetaService.update(nextCustomerBeta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerBeta.getId().toString()))
            .body(nextCustomerBeta);
    }

    /**
     * {@code PATCH  /next-customer-betas/:id} : Partial updates given fields of an existing nextCustomerBeta, field will ignore if it is null
     *
     * @param id the id of the nextCustomerBeta to save.
     * @param nextCustomerBeta the nextCustomerBeta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextCustomerBeta,
     * or with status {@code 400 (Bad Request)} if the nextCustomerBeta is not valid,
     * or with status {@code 404 (Not Found)} if the nextCustomerBeta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextCustomerBeta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextCustomerBeta> partialUpdateNextCustomerBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextCustomerBeta nextCustomerBeta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextCustomerBeta partially : {}, {}", id, nextCustomerBeta);
        if (nextCustomerBeta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextCustomerBeta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextCustomerBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextCustomerBeta> result = nextCustomerBetaService.partialUpdate(nextCustomerBeta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextCustomerBeta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-customer-betas} : get all the nextCustomerBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextCustomerBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextCustomerBeta>> getAllNextCustomerBetas(
        NextCustomerBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextCustomerBetas by criteria: {}", criteria);

        Page<NextCustomerBeta> page = nextCustomerBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-customer-betas/count} : count all the nextCustomerBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextCustomerBetas(NextCustomerBetaCriteria criteria) {
        LOG.debug("REST request to count NextCustomerBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextCustomerBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-customer-betas/:id} : get the "id" nextCustomerBeta.
     *
     * @param id the id of the nextCustomerBeta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextCustomerBeta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextCustomerBeta> getNextCustomerBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextCustomerBeta : {}", id);
        Optional<NextCustomerBeta> nextCustomerBeta = nextCustomerBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextCustomerBeta);
    }

    /**
     * {@code DELETE  /next-customer-betas/:id} : delete the "id" nextCustomerBeta.
     *
     * @param id the id of the nextCustomerBeta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextCustomerBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextCustomerBeta : {}", id);
        nextCustomerBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
