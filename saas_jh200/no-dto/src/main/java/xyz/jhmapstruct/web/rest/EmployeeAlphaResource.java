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
import xyz.jhmapstruct.domain.EmployeeAlpha;
import xyz.jhmapstruct.repository.EmployeeAlphaRepository;
import xyz.jhmapstruct.service.EmployeeAlphaQueryService;
import xyz.jhmapstruct.service.EmployeeAlphaService;
import xyz.jhmapstruct.service.criteria.EmployeeAlphaCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeAlpha}.
 */
@RestController
@RequestMapping("/api/employee-alphas")
public class EmployeeAlphaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAlphaResource.class);

    private static final String ENTITY_NAME = "employeeAlpha";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeAlphaService employeeAlphaService;

    private final EmployeeAlphaRepository employeeAlphaRepository;

    private final EmployeeAlphaQueryService employeeAlphaQueryService;

    public EmployeeAlphaResource(
        EmployeeAlphaService employeeAlphaService,
        EmployeeAlphaRepository employeeAlphaRepository,
        EmployeeAlphaQueryService employeeAlphaQueryService
    ) {
        this.employeeAlphaService = employeeAlphaService;
        this.employeeAlphaRepository = employeeAlphaRepository;
        this.employeeAlphaQueryService = employeeAlphaQueryService;
    }

    /**
     * {@code POST  /employee-alphas} : Create a new employeeAlpha.
     *
     * @param employeeAlpha the employeeAlpha to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeAlpha, or with status {@code 400 (Bad Request)} if the employeeAlpha has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeAlpha> createEmployeeAlpha(@Valid @RequestBody EmployeeAlpha employeeAlpha) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeAlpha : {}", employeeAlpha);
        if (employeeAlpha.getId() != null) {
            throw new BadRequestAlertException("A new employeeAlpha cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeAlpha = employeeAlphaService.save(employeeAlpha);
        return ResponseEntity.created(new URI("/api/employee-alphas/" + employeeAlpha.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeAlpha.getId().toString()))
            .body(employeeAlpha);
    }

    /**
     * {@code PUT  /employee-alphas/:id} : Updates an existing employeeAlpha.
     *
     * @param id the id of the employeeAlpha to save.
     * @param employeeAlpha the employeeAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAlpha,
     * or with status {@code 400 (Bad Request)} if the employeeAlpha is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeAlpha> updateEmployeeAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeAlpha employeeAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeAlpha : {}, {}", id, employeeAlpha);
        if (employeeAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeAlpha = employeeAlphaService.update(employeeAlpha);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeAlpha.getId().toString()))
            .body(employeeAlpha);
    }

    /**
     * {@code PATCH  /employee-alphas/:id} : Partial updates given fields of an existing employeeAlpha, field will ignore if it is null
     *
     * @param id the id of the employeeAlpha to save.
     * @param employeeAlpha the employeeAlpha to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeAlpha,
     * or with status {@code 400 (Bad Request)} if the employeeAlpha is not valid,
     * or with status {@code 404 (Not Found)} if the employeeAlpha is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeAlpha couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeAlpha> partialUpdateEmployeeAlpha(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeAlpha employeeAlpha
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeAlpha partially : {}, {}", id, employeeAlpha);
        if (employeeAlpha.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeAlpha.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeAlphaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeAlpha> result = employeeAlphaService.partialUpdate(employeeAlpha);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeAlpha.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-alphas} : get all the employeeAlphas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeAlphas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeAlpha>> getAllEmployeeAlphas(
        EmployeeAlphaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeAlphas by criteria: {}", criteria);

        Page<EmployeeAlpha> page = employeeAlphaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-alphas/count} : count all the employeeAlphas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeAlphas(EmployeeAlphaCriteria criteria) {
        LOG.debug("REST request to count EmployeeAlphas by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeAlphaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-alphas/:id} : get the "id" employeeAlpha.
     *
     * @param id the id of the employeeAlpha to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeAlpha, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeAlpha> getEmployeeAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeAlpha : {}", id);
        Optional<EmployeeAlpha> employeeAlpha = employeeAlphaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeAlpha);
    }

    /**
     * {@code DELETE  /employee-alphas/:id} : delete the "id" employeeAlpha.
     *
     * @param id the id of the employeeAlpha to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeAlpha(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeAlpha : {}", id);
        employeeAlphaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
