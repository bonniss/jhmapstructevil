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
import xyz.jhmapstruct.domain.NextPaymentVi;
import xyz.jhmapstruct.repository.NextPaymentViRepository;
import xyz.jhmapstruct.service.NextPaymentViQueryService;
import xyz.jhmapstruct.service.NextPaymentViService;
import xyz.jhmapstruct.service.criteria.NextPaymentViCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentVi}.
 */
@RestController
@RequestMapping("/api/next-payment-vis")
public class NextPaymentViResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentViResource.class);

    private static final String ENTITY_NAME = "nextPaymentVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentViService nextPaymentViService;

    private final NextPaymentViRepository nextPaymentViRepository;

    private final NextPaymentViQueryService nextPaymentViQueryService;

    public NextPaymentViResource(
        NextPaymentViService nextPaymentViService,
        NextPaymentViRepository nextPaymentViRepository,
        NextPaymentViQueryService nextPaymentViQueryService
    ) {
        this.nextPaymentViService = nextPaymentViService;
        this.nextPaymentViRepository = nextPaymentViRepository;
        this.nextPaymentViQueryService = nextPaymentViQueryService;
    }

    /**
     * {@code POST  /next-payment-vis} : Create a new nextPaymentVi.
     *
     * @param nextPaymentVi the nextPaymentVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentVi, or with status {@code 400 (Bad Request)} if the nextPaymentVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentVi> createNextPaymentVi(@Valid @RequestBody NextPaymentVi nextPaymentVi) throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentVi : {}", nextPaymentVi);
        if (nextPaymentVi.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentVi = nextPaymentViService.save(nextPaymentVi);
        return ResponseEntity.created(new URI("/api/next-payment-vis/" + nextPaymentVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentVi.getId().toString()))
            .body(nextPaymentVi);
    }

    /**
     * {@code PUT  /next-payment-vis/:id} : Updates an existing nextPaymentVi.
     *
     * @param id the id of the nextPaymentVi to save.
     * @param nextPaymentVi the nextPaymentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentVi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentVi> updateNextPaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentVi nextPaymentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentVi : {}, {}", id, nextPaymentVi);
        if (nextPaymentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentVi = nextPaymentViService.update(nextPaymentVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentVi.getId().toString()))
            .body(nextPaymentVi);
    }

    /**
     * {@code PATCH  /next-payment-vis/:id} : Partial updates given fields of an existing nextPaymentVi, field will ignore if it is null
     *
     * @param id the id of the nextPaymentVi to save.
     * @param nextPaymentVi the nextPaymentVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentVi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentVi is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentVi> partialUpdateNextPaymentVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentVi nextPaymentVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentVi partially : {}, {}", id, nextPaymentVi);
        if (nextPaymentVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentVi> result = nextPaymentViService.partialUpdate(nextPaymentVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentVi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-vis} : get all the nextPaymentVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentVi>> getAllNextPaymentVis(
        NextPaymentViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentVis by criteria: {}", criteria);

        Page<NextPaymentVi> page = nextPaymentViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-vis/count} : count all the nextPaymentVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentVis(NextPaymentViCriteria criteria) {
        LOG.debug("REST request to count NextPaymentVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-vis/:id} : get the "id" nextPaymentVi.
     *
     * @param id the id of the nextPaymentVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentVi> getNextPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentVi : {}", id);
        Optional<NextPaymentVi> nextPaymentVi = nextPaymentViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentVi);
    }

    /**
     * {@code DELETE  /next-payment-vis/:id} : delete the "id" nextPaymentVi.
     *
     * @param id the id of the nextPaymentVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentVi : {}", id);
        nextPaymentViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
