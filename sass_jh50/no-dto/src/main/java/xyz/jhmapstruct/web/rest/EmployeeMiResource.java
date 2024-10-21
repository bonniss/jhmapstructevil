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
import xyz.jhmapstruct.domain.EmployeeMi;
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.EmployeeMiService;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeMi}.
 */
@RestController
@RequestMapping("/api/employee-mis")
public class EmployeeMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeMiResource.class);

    private static final String ENTITY_NAME = "employeeMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeMiService employeeMiService;

    private final EmployeeMiRepository employeeMiRepository;

    public EmployeeMiResource(EmployeeMiService employeeMiService, EmployeeMiRepository employeeMiRepository) {
        this.employeeMiService = employeeMiService;
        this.employeeMiRepository = employeeMiRepository;
    }

    /**
     * {@code POST  /employee-mis} : Create a new employeeMi.
     *
     * @param employeeMi the employeeMi to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeMi, or with status {@code 400 (Bad Request)} if the employeeMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeMi> createEmployeeMi(@Valid @RequestBody EmployeeMi employeeMi) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeMi : {}", employeeMi);
        if (employeeMi.getId() != null) {
            throw new BadRequestAlertException("A new employeeMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeMi = employeeMiService.save(employeeMi);
        return ResponseEntity.created(new URI("/api/employee-mis/" + employeeMi.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeMi.getId().toString()))
            .body(employeeMi);
    }

    /**
     * {@code PUT  /employee-mis/:id} : Updates an existing employeeMi.
     *
     * @param id the id of the employeeMi to save.
     * @param employeeMi the employeeMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMi,
     * or with status {@code 400 (Bad Request)} if the employeeMi is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeMi> updateEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeMi employeeMi
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeMi : {}, {}", id, employeeMi);
        if (employeeMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeMi = employeeMiService.update(employeeMi);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMi.getId().toString()))
            .body(employeeMi);
    }

    /**
     * {@code PATCH  /employee-mis/:id} : Partial updates given fields of an existing employeeMi, field will ignore if it is null
     *
     * @param id the id of the employeeMi to save.
     * @param employeeMi the employeeMi to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMi,
     * or with status {@code 400 (Bad Request)} if the employeeMi is not valid,
     * or with status {@code 404 (Not Found)} if the employeeMi is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeMi couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeMi> partialUpdateEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeMi employeeMi
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeMi partially : {}, {}", id, employeeMi);
        if (employeeMi.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMi.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeMi> result = employeeMiService.partialUpdate(employeeMi);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMi.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-mis} : get all the employeeMis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeMis in body.
     */
    @GetMapping("")
    public List<EmployeeMi> getAllEmployeeMis() {
        LOG.debug("REST request to get all EmployeeMis");
        return employeeMiService.findAll();
    }

    /**
     * {@code GET  /employee-mis/:id} : get the "id" employeeMi.
     *
     * @param id the id of the employeeMi to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeMi, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMi> getEmployeeMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeMi : {}", id);
        Optional<EmployeeMi> employeeMi = employeeMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeMi);
    }

    /**
     * {@code DELETE  /employee-mis/:id} : delete the "id" employeeMi.
     *
     * @param id the id of the employeeMi to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeMi : {}", id);
        employeeMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
