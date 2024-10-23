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
import xyz.jhmapstruct.domain.NextPaymentTheta;
import xyz.jhmapstruct.repository.NextPaymentThetaRepository;
import xyz.jhmapstruct.service.NextPaymentThetaQueryService;
import xyz.jhmapstruct.service.NextPaymentThetaService;
import xyz.jhmapstruct.service.criteria.NextPaymentThetaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentTheta}.
 */
@RestController
@RequestMapping("/api/next-payment-thetas")
public class NextPaymentThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentThetaResource.class);

    private static final String ENTITY_NAME = "nextPaymentTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentThetaService nextPaymentThetaService;

    private final NextPaymentThetaRepository nextPaymentThetaRepository;

    private final NextPaymentThetaQueryService nextPaymentThetaQueryService;

    public NextPaymentThetaResource(
        NextPaymentThetaService nextPaymentThetaService,
        NextPaymentThetaRepository nextPaymentThetaRepository,
        NextPaymentThetaQueryService nextPaymentThetaQueryService
    ) {
        this.nextPaymentThetaService = nextPaymentThetaService;
        this.nextPaymentThetaRepository = nextPaymentThetaRepository;
        this.nextPaymentThetaQueryService = nextPaymentThetaQueryService;
    }

    /**
     * {@code POST  /next-payment-thetas} : Create a new nextPaymentTheta.
     *
     * @param nextPaymentTheta the nextPaymentTheta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentTheta, or with status {@code 400 (Bad Request)} if the nextPaymentTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentTheta> createNextPaymentTheta(@Valid @RequestBody NextPaymentTheta nextPaymentTheta)
        throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentTheta : {}", nextPaymentTheta);
        if (nextPaymentTheta.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentTheta = nextPaymentThetaService.save(nextPaymentTheta);
        return ResponseEntity.created(new URI("/api/next-payment-thetas/" + nextPaymentTheta.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentTheta.getId().toString()))
            .body(nextPaymentTheta);
    }

    /**
     * {@code PUT  /next-payment-thetas/:id} : Updates an existing nextPaymentTheta.
     *
     * @param id the id of the nextPaymentTheta to save.
     * @param nextPaymentTheta the nextPaymentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentTheta,
     * or with status {@code 400 (Bad Request)} if the nextPaymentTheta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentTheta> updateNextPaymentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentTheta nextPaymentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentTheta : {}, {}", id, nextPaymentTheta);
        if (nextPaymentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentTheta = nextPaymentThetaService.update(nextPaymentTheta);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentTheta.getId().toString()))
            .body(nextPaymentTheta);
    }

    /**
     * {@code PATCH  /next-payment-thetas/:id} : Partial updates given fields of an existing nextPaymentTheta, field will ignore if it is null
     *
     * @param id the id of the nextPaymentTheta to save.
     * @param nextPaymentTheta the nextPaymentTheta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentTheta,
     * or with status {@code 400 (Bad Request)} if the nextPaymentTheta is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentTheta is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentTheta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentTheta> partialUpdateNextPaymentTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentTheta nextPaymentTheta
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentTheta partially : {}, {}", id, nextPaymentTheta);
        if (nextPaymentTheta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentTheta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentTheta> result = nextPaymentThetaService.partialUpdate(nextPaymentTheta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentTheta.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-thetas} : get all the nextPaymentThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentTheta>> getAllNextPaymentThetas(
        NextPaymentThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentThetas by criteria: {}", criteria);

        Page<NextPaymentTheta> page = nextPaymentThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-thetas/count} : count all the nextPaymentThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentThetas(NextPaymentThetaCriteria criteria) {
        LOG.debug("REST request to count NextPaymentThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-thetas/:id} : get the "id" nextPaymentTheta.
     *
     * @param id the id of the nextPaymentTheta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentTheta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentTheta> getNextPaymentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentTheta : {}", id);
        Optional<NextPaymentTheta> nextPaymentTheta = nextPaymentThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentTheta);
    }

    /**
     * {@code DELETE  /next-payment-thetas/:id} : delete the "id" nextPaymentTheta.
     *
     * @param id the id of the nextPaymentTheta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentTheta : {}", id);
        nextPaymentThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
