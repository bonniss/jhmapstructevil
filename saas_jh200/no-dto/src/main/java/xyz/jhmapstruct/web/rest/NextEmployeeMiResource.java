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
import xyz.jhmapstruct.domain.NextEmployeeMi;
import xyz.jhmapstruct.repository.NextEmployeeMiRepository;
import xyz.jhmapstruct.service.NextEmployeeMiQueryService;
import xyz.jhmapstruct.service.NextEmployeeMiService;
import xyz.jhmapstruct.service.criteria.NextEmployeeMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeMi}.
 */
@RestController
@RequestMapping("/api/next-employee-mis")
public class NextEmployeeMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiResource.class);

    private static final String ENTITY_NAME = "nextEmployeeMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeMiService nextEmployeeMiService;

    private final NextEmployeeMiRepository nextEmployeeMiRepository;

    private final NextEmployeeMiQueryService nextEmployeeMiQueryService;

    public NextEmployeeMiResource(
        NextEmployeeMiService nextEmployeeMiService,
        NextEmployeeMiRepository nextEmployeeMiRepository,
        NextEmployeeMiQueryService nextEmployeeMiQueryService
    ) {
        this.nextEmployeeMiService = nextEmployeeMiService;
        this.nextEmployeeMiRepository = nextEmployeeMiRepository;
        this.nextEmployeeMiQueryService = nextEmployeeMiQueryService;
    }

    /**
     * {@code POST  /next-employee-mis} : Create a new nextEmployeeMi.
     *
     * @param nextEmployeeMi the nextEmployeeMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeMi, or with status {@code 400 (Bad Request)} if the nextEmployeeMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeMi> createNextEmployeeMi(@Valid @RequestBody NextEmployeeMi nextEmployeeMi)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeMi : {}", nextEmployeeMi);
        if (nextEmployeeMi.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeMi = nextEmployeeMiService.save(nextEmployeeMi);
        return ResponseEntity.created(new URI("/api/next-employee-mis/" + nextEmployeeMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeMi.getId().toString()))
            .body(nextEmployeeMi);
    }

    /**
     * {@code PUT  /next-employee-mis/:id} : Updates an existing nextEmployeeMi.
     *
     * @param id the id of the nextEmployeeMi to save.
     * @param nextEmployeeMi the nextEmployeeMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeMi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeMi> updateNextEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeMi nextEmployeeMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeMi : {}, {}", id, nextEmployeeMi);
        if (nextEmployeeMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeMi = nextEmployeeMiService.update(nextEmployeeMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeMi.getId().toString()))
            .body(nextEmployeeMi);
    }

    /**
     * {@code PATCH  /next-employee-mis/:id} : Partial updates given fields of an existing nextEmployeeMi, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeMi to save.
     * @param nextEmployeeMi the nextEmployeeMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeMi,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeMi is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeMi> partialUpdateNextEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeMi nextEmployeeMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeMi partially : {}, {}", id, nextEmployeeMi);
        if (nextEmployeeMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeMi> result = nextEmployeeMiService.partialUpdate(nextEmployeeMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeMi.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-mis} : get all the nextEmployeeMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeMi>> getAllNextEmployeeMis(
        NextEmployeeMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeMis by criteria: {}", criteria);

        Page<NextEmployeeMi> page = nextEmployeeMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-mis/count} : count all the nextEmployeeMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeMis(NextEmployeeMiCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-mis/:id} : get the "id" nextEmployeeMi.
     *
     * @param id the id of the nextEmployeeMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeMi> getNextEmployeeMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeMi : {}", id);
        Optional<NextEmployeeMi> nextEmployeeMi = nextEmployeeMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeMi);
    }

    /**
     * {@code DELETE  /next-employee-mis/:id} : delete the "id" nextEmployeeMi.
     *
     * @param id the id of the nextEmployeeMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeMi : {}", id);
        nextEmployeeMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
