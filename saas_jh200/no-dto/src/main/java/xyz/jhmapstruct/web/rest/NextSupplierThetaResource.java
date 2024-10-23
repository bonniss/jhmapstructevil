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
import xyz.jhmapstruct.domain.NextSupplierTheta;
import xyz.jhmapstruct.repository.NextSupplierThetaRepository;
import xyz.jhmapstruct.service.NextSupplierThetaQueryService;
import xyz.jhmapstruct.service.NextSupplierThetaService;
import xyz.jhmapstruct.service.criteria.NextSupplierThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextSupplierTheta}.
 */
@RestController
@RequestMapping("/api/next-supplier-thetas")
public class NextSupplierThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextSupplierThetaResource.class);

    private static final String ENTITY_NAME = "nextSupplierTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextSupplierThetaService nextSupplierThetaService;

    private final NextSupplierThetaRepository nextSupplierThetaRepository;

    private final NextSupplierThetaQueryService nextSupplierThetaQueryService;

    public NextSupplierThetaResource(
        NextSupplierThetaService nextSupplierThetaService,
        NextSupplierThetaRepository nextSupplierThetaRepository,
        NextSupplierThetaQueryService nextSupplierThetaQueryService
    ) {
        this.nextSupplierThetaService = nextSupplierThetaService;
        this.nextSupplierThetaRepository = nextSupplierThetaRepository;
        this.nextSupplierThetaQueryService = nextSupplierThetaQueryService;
    }

    /**
     * {@code POST  /next-supplier-thetas} : Create a new nextSupplierTheta.
     *
     * @param nextSupplierTheta the nextSupplierTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextSupplierTheta, or with status {@code 400 (Bad Request)} if the nextSupplierTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextSupplierTheta> createNextSupplierTheta(@Valid @RequestBody NextSupplierTheta nextSupplierTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextSupplierTheta : {}", nextSupplierTheta);
        if (nextSupplierTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextSupplierTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextSupplierTheta = nextSupplierThetaService.save(nextSupplierTheta);
        return ResponseEntity.created(new URI("/api/next-supplier-thetas/" + nextSupplierTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextSupplierTheta.getId().toString()))
            .body(nextSupplierTheta);
    }

    /**
     * {@code PUT  /next-supplier-thetas/:id} : Updates an existing nextSupplierTheta.
     *
     * @param id the id of the nextSupplierTheta to save.
     * @param nextSupplierTheta the nextSupplierTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierTheta,
     * or with status {@code 400 (Bad Request)} if the nextSupplierTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextSupplierTheta> updateNextSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextSupplierTheta nextSupplierTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextSupplierTheta : {}, {}", id, nextSupplierTheta);
        if (nextSupplierTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextSupplierTheta = nextSupplierThetaService.update(nextSupplierTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierTheta.getId().toString()))
            .body(nextSupplierTheta);
    }

    /**
     * {@code PATCH  /next-supplier-thetas/:id} : Partial updates given fields of an existing nextSupplierTheta, field will ignore if it is null
     *
     * @param id the id of the nextSupplierTheta to save.
     * @param nextSupplierTheta the nextSupplierTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextSupplierTheta,
     * or with status {@code 400 (Bad Request)} if the nextSupplierTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextSupplierTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextSupplierTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextSupplierTheta> partialUpdateNextSupplierTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextSupplierTheta nextSupplierTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextSupplierTheta partially : {}, {}", id, nextSupplierTheta);
        if (nextSupplierTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextSupplierTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextSupplierThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextSupplierTheta> result = nextSupplierThetaService.partialUpdate(nextSupplierTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextSupplierTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-supplier-thetas} : get all the nextSupplierThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextSupplierThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextSupplierTheta>> getAllNextSupplierThetas(
        NextSupplierThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextSupplierThetas by criteria: {}", criteria);

        Page<NextSupplierTheta> page = nextSupplierThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-supplier-thetas/count} : count all the nextSupplierThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextSupplierThetas(NextSupplierThetaCriteria criteria) {
        LOG.debug("REST request to count NextSupplierThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextSupplierThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-supplier-thetas/:id} : get the "id" nextSupplierTheta.
     *
     * @param id the id of the nextSupplierTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextSupplierTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextSupplierTheta> getNextSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextSupplierTheta : {}", id);
        Optional<NextSupplierTheta> nextSupplierTheta = nextSupplierThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextSupplierTheta);
    }

    /**
     * {@code DELETE  /next-supplier-thetas/:id} : delete the "id" nextSupplierTheta.
     *
     * @param id the id of the nextSupplierTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextSupplierTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextSupplierTheta : {}", id);
        nextSupplierThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
