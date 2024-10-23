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
import xyz.jhmapstruct.repository.EmployeeViRepository;
import xyz.jhmapstruct.service.EmployeeViQueryService;
import xyz.jhmapstruct.service.EmployeeViService;
import xyz.jhmapstruct.service.criteria.EmployeeViCriteria;
import xyz.jhmapstruct.service.dto.EmployeeViDTO;
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

    private final EmployeeViQueryService employeeViQueryService;

    public EmployeeViResource(
        EmployeeViService employeeViService,
        EmployeeViRepository employeeViRepository,
        EmployeeViQueryService employeeViQueryService
    ) {
        this.employeeViService = employeeViService;
        this.employeeViRepository = employeeViRepository;
        this.employeeViQueryService = employeeViQueryService;
    }

    /**
     * {@code POST  /employee-vis} : Create a new employeeVi.
     *
     * @param employeeViDTO the employeeViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeViDTO, or with status {@code 400 (Bad Request)} if the employeeVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeViDTO> createEmployeeVi(@Valid @RequestBody EmployeeViDTO employeeViDTO) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeVi : {}", employeeViDTO);
        if (employeeViDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeViDTO = employeeViService.save(employeeViDTO);
        return ResponseEntity.created(new URI("/api/employee-vis/" + employeeViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeViDTO.getId().toString()))
            .body(employeeViDTO);
    }

    /**
     * {@code PUT  /employee-vis/:id} : Updates an existing employeeVi.
     *
     * @param id the id of the employeeViDTO to save.
     * @param employeeViDTO the employeeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeViDTO,
     * or with status {@code 400 (Bad Request)} if the employeeViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeViDTO> updateEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeViDTO employeeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeVi : {}, {}", id, employeeViDTO);
        if (employeeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeViDTO = employeeViService.update(employeeViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeViDTO.getId().toString()))
            .body(employeeViDTO);
    }

    /**
     * {@code PATCH  /employee-vis/:id} : Partial updates given fields of an existing employeeVi, field will ignore if it is null
     *
     * @param id the id of the employeeViDTO to save.
     * @param employeeViDTO the employeeViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeViDTO,
     * or with status {@code 400 (Bad Request)} if the employeeViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeViDTO> partialUpdateEmployeeVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeViDTO employeeViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeVi partially : {}, {}", id, employeeViDTO);
        if (employeeViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeViDTO> result = employeeViService.partialUpdate(employeeViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-vis} : get all the employeeVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeViDTO>> getAllEmployeeVis(
        EmployeeViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeVis by criteria: {}", criteria);

        Page<EmployeeViDTO> page = employeeViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-vis/count} : count all the employeeVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeVis(EmployeeViCriteria criteria) {
        LOG.debug("REST request to count EmployeeVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-vis/:id} : get the "id" employeeVi.
     *
     * @param id the id of the employeeViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeViDTO> getEmployeeVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeVi : {}", id);
        Optional<EmployeeViDTO> employeeViDTO = employeeViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeViDTO);
    }

    /**
     * {@code DELETE  /employee-vis/:id} : delete the "id" employeeVi.
     *
     * @param id the id of the employeeViDTO to delete.
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
