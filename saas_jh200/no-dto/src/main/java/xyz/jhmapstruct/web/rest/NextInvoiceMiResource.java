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
import xyz.jhmapstruct.domain.NextInvoiceMi;
import xyz.jhmapstruct.repository.NextInvoiceMiRepository;
import xyz.jhmapstruct.service.NextInvoiceMiQueryService;
import xyz.jhmapstruct.service.NextInvoiceMiService;
import xyz.jhmapstruct.service.criteria.NextInvoiceMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextInvoiceMi}.
 */
@RestController
@RequestMapping("/api/next-invoice-mis")
public class NextInvoiceMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextInvoiceMiResource.class);

    private static final String ENTITY_NAME = "nextInvoiceMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextInvoiceMiService nextInvoiceMiService;

    private final NextInvoiceMiRepository nextInvoiceMiRepository;

    private final NextInvoiceMiQueryService nextInvoiceMiQueryService;

    public NextInvoiceMiResource(
        NextInvoiceMiService nextInvoiceMiService,
        NextInvoiceMiRepository nextInvoiceMiRepository,
        NextInvoiceMiQueryService nextInvoiceMiQueryService
    ) {
        this.nextInvoiceMiService = nextInvoiceMiService;
        this.nextInvoiceMiRepository = nextInvoiceMiRepository;
        this.nextInvoiceMiQueryService = nextInvoiceMiQueryService;
    }

    /**
     * {@code POST  /next-invoice-mis} : Create a new nextInvoiceMi.
     *
     * @param nextInvoiceMi the nextInvoiceMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextInvoiceMi, or with status {@code 400 (Bad Request)} if the nextInvoiceMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextInvoiceMi> createNextInvoiceMi(@Valid @RequestBody NextInvoiceMi nextInvoiceMi) throws URISyntaxException {
        LOG.debug("REST request to save NextInvoiceMi : {}", nextInvoiceMi);
        if (nextInvoiceMi.getId() != null) {
            throw new BadRequestAlertException("A new nextInvoiceMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextInvoiceMi = nextInvoiceMiService.save(nextInvoiceMi);
        return ResponseEntity.created(new URI("/api/next-invoice-mis/" + nextInvoiceMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextInvoiceMi.getId().toString()))
            .body(nextInvoiceMi);
    }

    /**
     * {@code PUT  /next-invoice-mis/:id} : Updates an existing nextInvoiceMi.
     *
     * @param id the id of the nextInvoiceMi to save.
     * @param nextInvoiceMi the nextInvoiceMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceMi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextInvoiceMi> updateNextInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextInvoiceMi nextInvoiceMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextInvoiceMi : {}, {}", id, nextInvoiceMi);
        if (nextInvoiceMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextInvoiceMi = nextInvoiceMiService.update(nextInvoiceMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceMi.getId().toString()))
            .body(nextInvoiceMi);
    }

    /**
     * {@code PATCH  /next-invoice-mis/:id} : Partial updates given fields of an existing nextInvoiceMi, field will ignore if it is null
     *
     * @param id the id of the nextInvoiceMi to save.
     * @param nextInvoiceMi the nextInvoiceMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextInvoiceMi,
     * or with status {@code 400 (Bad Request)} if the nextInvoiceMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextInvoiceMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextInvoiceMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextInvoiceMi> partialUpdateNextInvoiceMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextInvoiceMi nextInvoiceMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextInvoiceMi partially : {}, {}", id, nextInvoiceMi);
        if (nextInvoiceMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextInvoiceMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextInvoiceMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextInvoiceMi> result = nextInvoiceMiService.partialUpdate(nextInvoiceMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextInvoiceMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-invoice-mis} : get all the nextInvoiceMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextInvoiceMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextInvoiceMi>> getAllNextInvoiceMis(
        NextInvoiceMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextInvoiceMis by criteria: {}", criteria);

        Page<NextInvoiceMi> page = nextInvoiceMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-invoice-mis/count} : count all the nextInvoiceMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextInvoiceMis(NextInvoiceMiCriteria criteria) {
        LOG.debug("REST request to count NextInvoiceMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextInvoiceMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-invoice-mis/:id} : get the "id" nextInvoiceMi.
     *
     * @param id the id of the nextInvoiceMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextInvoiceMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextInvoiceMi> getNextInvoiceMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextInvoiceMi : {}", id);
        Optional<NextInvoiceMi> nextInvoiceMi = nextInvoiceMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextInvoiceMi);
    }

    /**
     * {@code DELETE  /next-invoice-mis/:id} : delete the "id" nextInvoiceMi.
     *
     * @param id the id of the nextInvoiceMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextInvoiceMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextInvoiceMi : {}", id);
        nextInvoiceMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
