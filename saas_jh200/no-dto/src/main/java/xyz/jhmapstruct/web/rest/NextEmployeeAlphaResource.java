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
import xyz.jhmapstruct.domain.NextEmployeeAlpha;
import xyz.jhmapstruct.repository.NextEmployeeAlphaRepository;
import xyz.jhmapstruct.service.NextEmployeeAlphaQueryService;
import xyz.jhmapstruct.service.NextEmployeeAlphaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeAlpha}.
 */
@RestController
@RequestMapping("/api/next-employee-alphas")
public class NextEmployeeAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeAlphaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeAlphaService nextEmployeeAlphaService;

    private final NextEmployeeAlphaRepository nextEmployeeAlphaRepository;

    private final NextEmployeeAlphaQueryService nextEmployeeAlphaQueryService;

    public NextEmployeeAlphaResource(
        NextEmployeeAlphaService nextEmployeeAlphaService,
        NextEmployeeAlphaRepository nextEmployeeAlphaRepository,
        NextEmployeeAlphaQueryService nextEmployeeAlphaQueryService
    ) {
        this.nextEmployeeAlphaService = nextEmployeeAlphaService;
        this.nextEmployeeAlphaRepository = nextEmployeeAlphaRepository;
        this.nextEmployeeAlphaQueryService = nextEmployeeAlphaQueryService;
    }

    /**
     * {@code POST  /next-employee-alphas} : Create a new nextEmployeeAlpha.
     *
     * @param nextEmployeeAlpha the nextEmployeeAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeAlpha, or with status {@code 400 (Bad Request)} if the nextEmployeeAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeAlpha> createNextEmployeeAlpha(@Valid @RequestBody NextEmployeeAlpha nextEmployeeAlpha)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeAlpha : {}", nextEmployeeAlpha);
        if (nextEmployeeAlpha.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeAlpha = nextEmployeeAlphaService.save(nextEmployeeAlpha);
        return ResponseEntity.created(new URI("/api/next-employee-alphas/" + nextEmployeeAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeAlpha.getId().toString()))
            .body(nextEmployeeAlpha);
    }

    /**
     * {@code PUT  /next-employee-alphas/:id} : Updates an existing nextEmployeeAlpha.
     *
     * @param id the id of the nextEmployeeAlpha to save.
     * @param nextEmployeeAlpha the nextEmployeeAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeAlpha,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeAlpha> updateNextEmployeeAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeAlpha nextEmployeeAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeAlpha : {}, {}", id, nextEmployeeAlpha);
        if (nextEmployeeAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeAlpha = nextEmployeeAlphaService.update(nextEmployeeAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeAlpha.getId().toString()))
            .body(nextEmployeeAlpha);
    }

    /**
     * {@code PATCH  /next-employee-alphas/:id} : Partial updates given fields of an existing nextEmployeeAlpha, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeAlpha to save.
     * @param nextEmployeeAlpha the nextEmployeeAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeAlpha,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeAlpha> partialUpdateNextEmployeeAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeAlpha nextEmployeeAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeAlpha partially : {}, {}", id, nextEmployeeAlpha);
        if (nextEmployeeAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeAlpha> result = nextEmployeeAlphaService.partialUpdate(nextEmployeeAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-alphas} : get all the nextEmployeeAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeAlpha>> getAllNextEmployeeAlphas(
        NextEmployeeAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeAlphas by criteria: {}", criteria);

        Page<NextEmployeeAlpha> page = nextEmployeeAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-alphas/count} : count all the nextEmployeeAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeAlphas(NextEmployeeAlphaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-alphas/:id} : get the "id" nextEmployeeAlpha.
     *
     * @param id the id of the nextEmployeeAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeAlpha> getNextEmployeeAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeAlpha : {}", id);
        Optional<NextEmployeeAlpha> nextEmployeeAlpha = nextEmployeeAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeAlpha);
    }

    /**
     * {@code DELETE  /next-employee-alphas/:id} : delete the "id" nextEmployeeAlpha.
     *
     * @param id the id of the nextEmployeeAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeAlpha : {}", id);
        nextEmployeeAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
