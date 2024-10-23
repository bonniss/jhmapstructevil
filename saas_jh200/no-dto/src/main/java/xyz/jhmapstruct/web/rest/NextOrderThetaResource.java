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
import xyz.jhmapstruct.domain.NextOrderTheta;
import xyz.jhmapstruct.repository.NextOrderThetaRepository;
import xyz.jhmapstruct.service.NextOrderThetaQueryService;
import xyz.jhmapstruct.service.NextOrderThetaService;
import xyz.jhmapstruct.service.criteria.NextOrderThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextOrderTheta}.
 */
@RestController
@RequestMapping("/api/next-order-thetas")
public class NextOrderThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextOrderThetaResource.class);

    private static final String ENTITY_NAME = "nextOrderTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOrderThetaService nextOrderThetaService;

    private final NextOrderThetaRepository nextOrderThetaRepository;

    private final NextOrderThetaQueryService nextOrderThetaQueryService;

    public NextOrderThetaResource(
        NextOrderThetaService nextOrderThetaService,
        NextOrderThetaRepository nextOrderThetaRepository,
        NextOrderThetaQueryService nextOrderThetaQueryService
    ) {
        this.nextOrderThetaService = nextOrderThetaService;
        this.nextOrderThetaRepository = nextOrderThetaRepository;
        this.nextOrderThetaQueryService = nextOrderThetaQueryService;
    }

    /**
     * {@code POST  /next-order-thetas} : Create a new nextOrderTheta.
     *
     * @param nextOrderTheta the nextOrderTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOrderTheta, or with status {@code 400 (Bad Request)} if the nextOrderTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextOrderTheta> createNextOrderTheta(@Valid @RequestBody NextOrderTheta nextOrderTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextOrderTheta : {}", nextOrderTheta);
        if (nextOrderTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextOrderTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextOrderTheta = nextOrderThetaService.save(nextOrderTheta);
        return ResponseEntity.created(new URI("/api/next-order-thetas/" + nextOrderTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextOrderTheta.getId().toString()))
            .body(nextOrderTheta);
    }

    /**
     * {@code PUT  /next-order-thetas/:id} : Updates an existing nextOrderTheta.
     *
     * @param id the id of the nextOrderTheta to save.
     * @param nextOrderTheta the nextOrderTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderTheta,
     * or with status {@code 400 (Bad Request)} if the nextOrderTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextOrderTheta> updateNextOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOrderTheta nextOrderTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextOrderTheta : {}, {}", id, nextOrderTheta);
        if (nextOrderTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextOrderTheta = nextOrderThetaService.update(nextOrderTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderTheta.getId().toString()))
            .body(nextOrderTheta);
    }

    /**
     * {@code PATCH  /next-order-thetas/:id} : Partial updates given fields of an existing nextOrderTheta, field will ignore if it is null
     *
     * @param id the id of the nextOrderTheta to save.
     * @param nextOrderTheta the nextOrderTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOrderTheta,
     * or with status {@code 400 (Bad Request)} if the nextOrderTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextOrderTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOrderTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextOrderTheta> partialUpdateNextOrderTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOrderTheta nextOrderTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextOrderTheta partially : {}, {}", id, nextOrderTheta);
        if (nextOrderTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOrderTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOrderThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOrderTheta> result = nextOrderThetaService.partialUpdate(nextOrderTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextOrderTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-order-thetas} : get all the nextOrderThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOrderThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextOrderTheta>> getAllNextOrderThetas(
        NextOrderThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextOrderThetas by criteria: {}", criteria);

        Page<NextOrderTheta> page = nextOrderThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-order-thetas/count} : count all the nextOrderThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextOrderThetas(NextOrderThetaCriteria criteria) {
        LOG.debug("REST request to count NextOrderThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextOrderThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-order-thetas/:id} : get the "id" nextOrderTheta.
     *
     * @param id the id of the nextOrderTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOrderTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextOrderTheta> getNextOrderTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextOrderTheta : {}", id);
        Optional<NextOrderTheta> nextOrderTheta = nextOrderThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOrderTheta);
    }

    /**
     * {@code DELETE  /next-order-thetas/:id} : delete the "id" nextOrderTheta.
     *
     * @param id the id of the nextOrderTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextOrderTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextOrderTheta : {}", id);
        nextOrderThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
