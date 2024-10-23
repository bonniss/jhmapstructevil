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
import xyz.jhmapstruct.domain.NextPaymentMi;
import xyz.jhmapstruct.repository.NextPaymentMiRepository;
import xyz.jhmapstruct.service.NextPaymentMiQueryService;
import xyz.jhmapstruct.service.NextPaymentMiService;
import xyz.jhmapstruct.service.criteria.NextPaymentMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextPaymentMi}.
 */
@RestController
@RequestMapping("/api/next-payment-mis")
public class NextPaymentMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextPaymentMiResource.class);

    private static final String ENTITY_NAME = "nextPaymentMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextPaymentMiService nextPaymentMiService;

    private final NextPaymentMiRepository nextPaymentMiRepository;

    private final NextPaymentMiQueryService nextPaymentMiQueryService;

    public NextPaymentMiResource(
        NextPaymentMiService nextPaymentMiService,
        NextPaymentMiRepository nextPaymentMiRepository,
        NextPaymentMiQueryService nextPaymentMiQueryService
    ) {
        this.nextPaymentMiService = nextPaymentMiService;
        this.nextPaymentMiRepository = nextPaymentMiRepository;
        this.nextPaymentMiQueryService = nextPaymentMiQueryService;
    }

    /**
     * {@code POST  /next-payment-mis} : Create a new nextPaymentMi.
     *
     * @param nextPaymentMi the nextPaymentMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextPaymentMi, or with status {@code 400 (Bad Request)} if the nextPaymentMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextPaymentMi> createNextPaymentMi(@Valid @RequestBody NextPaymentMi nextPaymentMi) throws URISyntaxException {
        LOG.debug("REST request to save NextPaymentMi : {}", nextPaymentMi);
        if (nextPaymentMi.getId() != null) {
            throw new BadRequestAlertException("A new nextPaymentMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextPaymentMi = nextPaymentMiService.save(nextPaymentMi);
        return ResponseEntity.created(new URI("/api/next-payment-mis/" + nextPaymentMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextPaymentMi.getId().toString()))
            .body(nextPaymentMi);
    }

    /**
     * {@code PUT  /next-payment-mis/:id} : Updates an existing nextPaymentMi.
     *
     * @param id the id of the nextPaymentMi to save.
     * @param nextPaymentMi the nextPaymentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentMi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextPaymentMi> updateNextPaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextPaymentMi nextPaymentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextPaymentMi : {}, {}", id, nextPaymentMi);
        if (nextPaymentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextPaymentMi = nextPaymentMiService.update(nextPaymentMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentMi.getId().toString()))
            .body(nextPaymentMi);
    }

    /**
     * {@code PATCH  /next-payment-mis/:id} : Partial updates given fields of an existing nextPaymentMi, field will ignore if it is null
     *
     * @param id the id of the nextPaymentMi to save.
     * @param nextPaymentMi the nextPaymentMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextPaymentMi,
     * or with status {@code 400 (Bad Request)} if the nextPaymentMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextPaymentMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextPaymentMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextPaymentMi> partialUpdateNextPaymentMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextPaymentMi nextPaymentMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextPaymentMi partially : {}, {}", id, nextPaymentMi);
        if (nextPaymentMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextPaymentMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextPaymentMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextPaymentMi> result = nextPaymentMiService.partialUpdate(nextPaymentMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextPaymentMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-payment-mis} : get all the nextPaymentMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextPaymentMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextPaymentMi>> getAllNextPaymentMis(
        NextPaymentMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextPaymentMis by criteria: {}", criteria);

        Page<NextPaymentMi> page = nextPaymentMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-payment-mis/count} : count all the nextPaymentMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextPaymentMis(NextPaymentMiCriteria criteria) {
        LOG.debug("REST request to count NextPaymentMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextPaymentMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-payment-mis/:id} : get the "id" nextPaymentMi.
     *
     * @param id the id of the nextPaymentMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextPaymentMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextPaymentMi> getNextPaymentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextPaymentMi : {}", id);
        Optional<NextPaymentMi> nextPaymentMi = nextPaymentMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextPaymentMi);
    }

    /**
     * {@code DELETE  /next-payment-mis/:id} : delete the "id" nextPaymentMi.
     *
     * @param id the id of the nextPaymentMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextPaymentMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextPaymentMi : {}", id);
        nextPaymentMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
