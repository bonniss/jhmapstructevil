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
import xyz.jhmapstruct.repository.EmployeeThetaRepository;
import xyz.jhmapstruct.service.EmployeeThetaQueryService;
import xyz.jhmapstruct.service.EmployeeThetaService;
import xyz.jhmapstruct.service.criteria.EmployeeThetaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeTheta}.
 */
@RestController
@RequestMapping("/api/employee-thetas")
public class EmployeeThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeThetaResource.class);

    private static final String ENTITY_NAME = "employeeTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeThetaService employeeThetaService;

    private final EmployeeThetaRepository employeeThetaRepository;

    private final EmployeeThetaQueryService employeeThetaQueryService;

    public EmployeeThetaResource(
        EmployeeThetaService employeeThetaService,
        EmployeeThetaRepository employeeThetaRepository,
        EmployeeThetaQueryService employeeThetaQueryService
    ) {
        this.employeeThetaService = employeeThetaService;
        this.employeeThetaRepository = employeeThetaRepository;
        this.employeeThetaQueryService = employeeThetaQueryService;
    }

    /**
     * {@code POST  /employee-thetas} : Create a new employeeTheta.
     *
     * @param employeeThetaDTO the employeeThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeThetaDTO, or with status {@code 400 (Bad Request)} if the employeeTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeThetaDTO> createEmployeeTheta(@Valid @RequestBody EmployeeThetaDTO employeeThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmployeeTheta : {}", employeeThetaDTO);
        if (employeeThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeThetaDTO = employeeThetaService.save(employeeThetaDTO);
        return ResponseEntity.created(new URI("/api/employee-thetas/" + employeeThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeThetaDTO.getId().toString()))
            .body(employeeThetaDTO);
    }

    /**
     * {@code PUT  /employee-thetas/:id} : Updates an existing employeeTheta.
     *
     * @param id the id of the employeeThetaDTO to save.
     * @param employeeThetaDTO the employeeThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeThetaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeThetaDTO> updateEmployeeTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeThetaDTO employeeThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeTheta : {}, {}", id, employeeThetaDTO);
        if (employeeThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeThetaDTO = employeeThetaService.update(employeeThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeThetaDTO.getId().toString()))
            .body(employeeThetaDTO);
    }

    /**
     * {@code PATCH  /employee-thetas/:id} : Partial updates given fields of an existing employeeTheta, field will ignore if it is null
     *
     * @param id the id of the employeeThetaDTO to save.
     * @param employeeThetaDTO the employeeThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeThetaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeThetaDTO> partialUpdateEmployeeTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeThetaDTO employeeThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeTheta partially : {}, {}", id, employeeThetaDTO);
        if (employeeThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeThetaDTO> result = employeeThetaService.partialUpdate(employeeThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-thetas} : get all the employeeThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeThetaDTO>> getAllEmployeeThetas(
        EmployeeThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeThetas by criteria: {}", criteria);

        Page<EmployeeThetaDTO> page = employeeThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-thetas/count} : count all the employeeThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeThetas(EmployeeThetaCriteria criteria) {
        LOG.debug("REST request to count EmployeeThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-thetas/:id} : get the "id" employeeTheta.
     *
     * @param id the id of the employeeThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeThetaDTO> getEmployeeTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeTheta : {}", id);
        Optional<EmployeeThetaDTO> employeeThetaDTO = employeeThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeThetaDTO);
    }

    /**
     * {@code DELETE  /employee-thetas/:id} : delete the "id" employeeTheta.
     *
     * @param id the id of the employeeThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeTheta : {}", id);
        employeeThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
