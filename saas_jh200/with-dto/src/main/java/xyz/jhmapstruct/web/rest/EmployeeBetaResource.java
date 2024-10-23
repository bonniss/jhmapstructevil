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
import xyz.jhmapstruct.repository.EmployeeBetaRepository;
import xyz.jhmapstruct.service.EmployeeBetaQueryService;
import xyz.jhmapstruct.service.EmployeeBetaService;
import xyz.jhmapstruct.service.criteria.EmployeeBetaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeBeta}.
 */
@RestController
@RequestMapping("/api/employee-betas")
public class EmployeeBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeBetaResource.class);

    private static final String ENTITY_NAME = "employeeBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeBetaService employeeBetaService;

    private final EmployeeBetaRepository employeeBetaRepository;

    private final EmployeeBetaQueryService employeeBetaQueryService;

    public EmployeeBetaResource(
        EmployeeBetaService employeeBetaService,
        EmployeeBetaRepository employeeBetaRepository,
        EmployeeBetaQueryService employeeBetaQueryService
    ) {
        this.employeeBetaService = employeeBetaService;
        this.employeeBetaRepository = employeeBetaRepository;
        this.employeeBetaQueryService = employeeBetaQueryService;
    }

    /**
     * {@code POST  /employee-betas} : Create a new employeeBeta.
     *
     * @param employeeBetaDTO the employeeBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeBetaDTO, or with status {@code 400 (Bad Request)} if the employeeBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeBetaDTO> createEmployeeBeta(@Valid @RequestBody EmployeeBetaDTO employeeBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmployeeBeta : {}", employeeBetaDTO);
        if (employeeBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeBetaDTO = employeeBetaService.save(employeeBetaDTO);
        return ResponseEntity.created(new URI("/api/employee-betas/" + employeeBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeBetaDTO.getId().toString()))
            .body(employeeBetaDTO);
    }

    /**
     * {@code PUT  /employee-betas/:id} : Updates an existing employeeBeta.
     *
     * @param id the id of the employeeBetaDTO to save.
     * @param employeeBetaDTO the employeeBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeBetaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeBetaDTO> updateEmployeeBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeBetaDTO employeeBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeBeta : {}, {}", id, employeeBetaDTO);
        if (employeeBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeBetaDTO = employeeBetaService.update(employeeBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeBetaDTO.getId().toString()))
            .body(employeeBetaDTO);
    }

    /**
     * {@code PATCH  /employee-betas/:id} : Partial updates given fields of an existing employeeBeta, field will ignore if it is null
     *
     * @param id the id of the employeeBetaDTO to save.
     * @param employeeBetaDTO the employeeBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeBetaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeBetaDTO> partialUpdateEmployeeBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeBetaDTO employeeBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeBeta partially : {}, {}", id, employeeBetaDTO);
        if (employeeBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeBetaDTO> result = employeeBetaService.partialUpdate(employeeBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-betas} : get all the employeeBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeBetaDTO>> getAllEmployeeBetas(
        EmployeeBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeBetas by criteria: {}", criteria);

        Page<EmployeeBetaDTO> page = employeeBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-betas/count} : count all the employeeBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeBetas(EmployeeBetaCriteria criteria) {
        LOG.debug("REST request to count EmployeeBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-betas/:id} : get the "id" employeeBeta.
     *
     * @param id the id of the employeeBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeBetaDTO> getEmployeeBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeBeta : {}", id);
        Optional<EmployeeBetaDTO> employeeBetaDTO = employeeBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeBetaDTO);
    }

    /**
     * {@code DELETE  /employee-betas/:id} : delete the "id" employeeBeta.
     *
     * @param id the id of the employeeBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeBeta : {}", id);
        employeeBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
