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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.EmployeeViService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeVi}.
 */
@RestController
@RequestMapping("/api/employee-vis")
public class EmployeeViResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViResource.class);

    private static final String ENTITY_NAME = "employeeVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeViService employeeViService;

    private final EmployeeViRepository employeeViRepository;

    public EmployeeViResource(EmployeeViService employeeViService, EmployeeViRepository employeeViRepository) {
        this.employeeViService = employeeViService;
        this.employeeViRepository = employeeViRepository;
    }

    /**
     * {@code POST  /employee-vis} : Create a new employeeVi.
     *
     * @param employeeVi the employeeVi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeVi, or with status {@code 400 (Bad Request)} if the employeeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeVi> createEmployeeVi(@Valid @RequestBody EmployeeVi employeeVi) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeVi : {}", employeeVi);
        if (employeeVi.getId() != null) {
            throw new BadRequestAlertException("A new employeeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeVi = employeeViService.save(employeeVi);
        return ResponseEntity.created(new URI("/api/employee-vis/" + employeeVi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeVi.getId().toString()))
            .body(employeeVi);
    }

    /**
     * {@code PUT  /employee-vis/:id} : Updates an existing employeeVi.
     *
     * @param id the id of the employeeVi to save.
     * @param employeeVi the employeeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeVi,
     * or with status {@code 400 (Bad Request)} if the employeeVi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeVi> updateEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeVi employeeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeVi : {}, {}", id, employeeVi);
        if (employeeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeVi = employeeViService.update(employeeVi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeVi.getId().toString()))
            .body(employeeVi);
    }

    /**
     * {@code PATCH  /employee-vis/:id} : Partial updates given fields of an existing employeeVi, field will ignore if it is null
     *
     * @param id the id of the employeeVi to save.
     * @param employeeVi the employeeVi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeVi,
     * or with status {@code 400 (Bad Request)} if the employeeVi is not valid,
     * or with status {@code 404 (Not Found)} if the employeeVi is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeVi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeVi> partialUpdateEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeVi employeeVi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeVi partially : {}, {}", id, employeeVi);
        if (employeeVi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeVi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeVi> result = employeeViService.partialUpdate(employeeVi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeVi.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-vis} : get all the employeeVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeVis in body.
     */
    @GetMapping("")
    public List<EmployeeVi> getAllEmployeeVis() {
        LOG.debug("REST request to get all EmployeeVis");
        return employeeViService.findAll();
    }

    /**
     * {@code GET  /employee-vis/:id} : get the "id" employeeVi.
     *
     * @param id the id of the employeeVi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeVi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeVi> getEmployeeVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeVi : {}", id);
        Optional<EmployeeVi> employeeVi = employeeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeVi);
    }

    /**
     * {@code DELETE  /employee-vis/:id} : delete the "id" employeeVi.
     *
     * @param id the id of the employeeVi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeVi : {}", id);
        employeeViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
