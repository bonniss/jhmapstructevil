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
import xyz.jhmapstruct.repository.EmployeeGammaRepository;
import xyz.jhmapstruct.service.EmployeeGammaQueryService;
import xyz.jhmapstruct.service.EmployeeGammaService;
import xyz.jhmapstruct.service.criteria.EmployeeGammaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeGamma}.
 */
@RestController
@RequestMapping("/api/employee-gammas")
public class EmployeeGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeGammaResource.class);

    private static final String ENTITY_NAME = "employeeGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeGammaService employeeGammaService;

    private final EmployeeGammaRepository employeeGammaRepository;

    private final EmployeeGammaQueryService employeeGammaQueryService;

    public EmployeeGammaResource(
        EmployeeGammaService employeeGammaService,
        EmployeeGammaRepository employeeGammaRepository,
        EmployeeGammaQueryService employeeGammaQueryService
    ) {
        this.employeeGammaService = employeeGammaService;
        this.employeeGammaRepository = employeeGammaRepository;
        this.employeeGammaQueryService = employeeGammaQueryService;
    }

    /**
     * {@code POST  /employee-gammas} : Create a new employeeGamma.
     *
     * @param employeeGammaDTO the employeeGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeGammaDTO, or with status {@code 400 (Bad Request)} if the employeeGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeGammaDTO> createEmployeeGamma(@Valid @RequestBody EmployeeGammaDTO employeeGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmployeeGamma : {}", employeeGammaDTO);
        if (employeeGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeGammaDTO = employeeGammaService.save(employeeGammaDTO);
        return ResponseEntity.created(new URI("/api/employee-gammas/" + employeeGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeGammaDTO.getId().toString()))
            .body(employeeGammaDTO);
    }

    /**
     * {@code PUT  /employee-gammas/:id} : Updates an existing employeeGamma.
     *
     * @param id the id of the employeeGammaDTO to save.
     * @param employeeGammaDTO the employeeGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeGammaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeGammaDTO> updateEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeGammaDTO employeeGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeGamma : {}, {}", id, employeeGammaDTO);
        if (employeeGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeGammaDTO = employeeGammaService.update(employeeGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeGammaDTO.getId().toString()))
            .body(employeeGammaDTO);
    }

    /**
     * {@code PATCH  /employee-gammas/:id} : Partial updates given fields of an existing employeeGamma, field will ignore if it is null
     *
     * @param id the id of the employeeGammaDTO to save.
     * @param employeeGammaDTO the employeeGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeGammaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeGammaDTO> partialUpdateEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeGammaDTO employeeGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeGamma partially : {}, {}", id, employeeGammaDTO);
        if (employeeGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeGammaDTO> result = employeeGammaService.partialUpdate(employeeGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-gammas} : get all the employeeGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeGammaDTO>> getAllEmployeeGammas(
        EmployeeGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeGammas by criteria: {}", criteria);

        Page<EmployeeGammaDTO> page = employeeGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-gammas/count} : count all the employeeGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeGammas(EmployeeGammaCriteria criteria) {
        LOG.debug("REST request to count EmployeeGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-gammas/:id} : get the "id" employeeGamma.
     *
     * @param id the id of the employeeGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeGammaDTO> getEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeGamma : {}", id);
        Optional<EmployeeGammaDTO> employeeGammaDTO = employeeGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeGammaDTO);
    }

    /**
     * {@code DELETE  /employee-gammas/:id} : delete the "id" employeeGamma.
     *
     * @param id the id of the employeeGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeGamma : {}", id);
        employeeGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
