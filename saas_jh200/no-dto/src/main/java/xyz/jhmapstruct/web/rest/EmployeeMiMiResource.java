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
import xyz.jhmapstruct.domain.EmployeeMiMi;
import xyz.jhmapstruct.repository.EmployeeMiMiRepository;
import xyz.jhmapstruct.service.EmployeeMiMiQueryService;
import xyz.jhmapstruct.service.EmployeeMiMiService;
import xyz.jhmapstruct.service.criteria.EmployeeMiMiCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeMiMi}.
 */
@RestController
@RequestMapping("/api/employee-mi-mis")
public class EmployeeMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiMiResource.class);

    private static final String ENTITY_NAME = "employeeMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeMiMiService employeeMiMiService;

    private final EmployeeMiMiRepository employeeMiMiRepository;

    private final EmployeeMiMiQueryService employeeMiMiQueryService;

    public EmployeeMiMiResource(
        EmployeeMiMiService employeeMiMiService,
        EmployeeMiMiRepository employeeMiMiRepository,
        EmployeeMiMiQueryService employeeMiMiQueryService
    ) {
        this.employeeMiMiService = employeeMiMiService;
        this.employeeMiMiRepository = employeeMiMiRepository;
        this.employeeMiMiQueryService = employeeMiMiQueryService;
    }

    /**
     * {@code POST  /employee-mi-mis} : Create a new employeeMiMi.
     *
     * @param employeeMiMi the employeeMiMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeMiMi, or with status {@code 400 (Bad Request)} if the employeeMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeMiMi> createEmployeeMiMi(@Valid @RequestBody EmployeeMiMi employeeMiMi) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeMiMi : {}", employeeMiMi);
        if (employeeMiMi.getId() != null) {
            throw new BadRequestAlertException("A new employeeMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeMiMi = employeeMiMiService.save(employeeMiMi);
        return ResponseEntity.created(new URI("/api/employee-mi-mis/" + employeeMiMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeMiMi.getId().toString()))
            .body(employeeMiMi);
    }

    /**
     * {@code PUT  /employee-mi-mis/:id} : Updates an existing employeeMiMi.
     *
     * @param id the id of the employeeMiMi to save.
     * @param employeeMiMi the employeeMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMiMi,
     * or with status {@code 400 (Bad Request)} if the employeeMiMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeMiMi> updateEmployeeMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeMiMi employeeMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeMiMi : {}, {}", id, employeeMiMi);
        if (employeeMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeMiMi = employeeMiMiService.update(employeeMiMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMiMi.getId().toString()))
            .body(employeeMiMi);
    }

    /**
     * {@code PATCH  /employee-mi-mis/:id} : Partial updates given fields of an existing employeeMiMi, field will ignore if it is null
     *
     * @param id the id of the employeeMiMi to save.
     * @param employeeMiMi the employeeMiMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMiMi,
     * or with status {@code 400 (Bad Request)} if the employeeMiMi is not valid,
     * or with status {@code 404 (Not Found)} if the employeeMiMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeMiMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeMiMi> partialUpdateEmployeeMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeMiMi employeeMiMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeMiMi partially : {}, {}", id, employeeMiMi);
        if (employeeMiMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMiMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeMiMi> result = employeeMiMiService.partialUpdate(employeeMiMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMiMi.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-mi-mis} : get all the employeeMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeMiMi>> getAllEmployeeMiMis(
        EmployeeMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeMiMis by criteria: {}", criteria);

        Page<EmployeeMiMi> page = employeeMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-mi-mis/count} : count all the employeeMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeMiMis(EmployeeMiMiCriteria criteria) {
        LOG.debug("REST request to count EmployeeMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-mi-mis/:id} : get the "id" employeeMiMi.
     *
     * @param id the id of the employeeMiMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeMiMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMiMi> getEmployeeMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeMiMi : {}", id);
        Optional<EmployeeMiMi> employeeMiMi = employeeMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeMiMi);
    }

    /**
     * {@code DELETE  /employee-mi-mis/:id} : delete the "id" employeeMiMi.
     *
     * @param id the id of the employeeMiMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeMiMi : {}", id);
        employeeMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
