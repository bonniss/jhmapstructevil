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
import xyz.jhmapstruct.repository.EmployeeViViRepository;
import xyz.jhmapstruct.service.EmployeeViViService;
import xyz.jhmapstruct.service.dto.EmployeeViViDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeViVi}.
 */
@RestController
@RequestMapping("/api/employee-vi-vis")
public class EmployeeViViResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeViViResource.class);

    private static final String ENTITY_NAME = "employeeViVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeViViService employeeViViService;

    private final EmployeeViViRepository employeeViViRepository;

    public EmployeeViViResource(EmployeeViViService employeeViViService, EmployeeViViRepository employeeViViRepository) {
        this.employeeViViService = employeeViViService;
        this.employeeViViRepository = employeeViViRepository;
    }

    /**
     * {@code POST  /employee-vi-vis} : Create a new employeeViVi.
     *
     * @param employeeViViDTO the employeeViViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeViViDTO, or with status {@code 400 (Bad Request)} if the employeeViVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeViViDTO> createEmployeeViVi(@Valid @RequestBody EmployeeViViDTO employeeViViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmployeeViVi : {}", employeeViViDTO);
        if (employeeViViDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeViVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeViViDTO = employeeViViService.save(employeeViViDTO);
        return ResponseEntity.created(new URI("/api/employee-vi-vis/" + employeeViViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeViViDTO.getId().toString()))
            .body(employeeViViDTO);
    }

    /**
     * {@code PUT  /employee-vi-vis/:id} : Updates an existing employeeViVi.
     *
     * @param id the id of the employeeViViDTO to save.
     * @param employeeViViDTO the employeeViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeViViDTO,
     * or with status {@code 400 (Bad Request)} if the employeeViViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeViViDTO> updateEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeViViDTO employeeViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeViVi : {}, {}", id, employeeViViDTO);
        if (employeeViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeViViDTO = employeeViViService.update(employeeViViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeViViDTO.getId().toString()))
            .body(employeeViViDTO);
    }

    /**
     * {@code PATCH  /employee-vi-vis/:id} : Partial updates given fields of an existing employeeViVi, field will ignore if it is null
     *
     * @param id the id of the employeeViViDTO to save.
     * @param employeeViViDTO the employeeViViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeViViDTO,
     * or with status {@code 400 (Bad Request)} if the employeeViViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeViViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeViViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeViViDTO> partialUpdateEmployeeViVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeViViDTO employeeViViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeViVi partially : {}, {}", id, employeeViViDTO);
        if (employeeViViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeViViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeViViDTO> result = employeeViViService.partialUpdate(employeeViViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeViViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-vi-vis} : get all the employeeViVis.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeViVis in body.
     */
    @GetMapping("")
    public List<EmployeeViViDTO> getAllEmployeeViVis() {
        LOG.debug("REST request to get all EmployeeViVis");
        return employeeViViService.findAll();
    }

    /**
     * {@code GET  /employee-vi-vis/:id} : get the "id" employeeViVi.
     *
     * @param id the id of the employeeViViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeViViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeViViDTO> getEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeViVi : {}", id);
        Optional<EmployeeViViDTO> employeeViViDTO = employeeViViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeViViDTO);
    }

    /**
     * {@code DELETE  /employee-vi-vis/:id} : delete the "id" employeeViVi.
     *
     * @param id the id of the employeeViViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeViVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeViVi : {}", id);
        employeeViViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
