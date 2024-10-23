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
import xyz.jhmapstruct.repository.EmployeeSigmaRepository;
import xyz.jhmapstruct.service.EmployeeSigmaQueryService;
import xyz.jhmapstruct.service.EmployeeSigmaService;
import xyz.jhmapstruct.service.criteria.EmployeeSigmaCriteria;
import xyz.jhmapstruct.service.dto.EmployeeSigmaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.EmployeeSigma}.
 */
@RestController
@RequestMapping("/api/employee-sigmas")
public class EmployeeSigmaResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeSigmaResource.class);

    private static final String ENTITY_NAME = "employeeSigma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployeeSigmaService employeeSigmaService;

    private final EmployeeSigmaRepository employeeSigmaRepository;

    private final EmployeeSigmaQueryService employeeSigmaQueryService;

    public EmployeeSigmaResource(
        EmployeeSigmaService employeeSigmaService,
        EmployeeSigmaRepository employeeSigmaRepository,
        EmployeeSigmaQueryService employeeSigmaQueryService
    ) {
        this.employeeSigmaService = employeeSigmaService;
        this.employeeSigmaRepository = employeeSigmaRepository;
        this.employeeSigmaQueryService = employeeSigmaQueryService;
    }

    /**
     * {@code POST  /employee-sigmas} : Create a new employeeSigma.
     *
     * @param employeeSigmaDTO the employeeSigmaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeSigmaDTO, or with status {@code 400 (Bad Request)} if the employeeSigma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeSigmaDTO> createEmployeeSigma(@Valid @RequestBody EmployeeSigmaDTO employeeSigmaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EmployeeSigma : {}", employeeSigmaDTO);
        if (employeeSigmaDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeSigma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeSigmaDTO = employeeSigmaService.save(employeeSigmaDTO);
        return ResponseEntity.created(new URI("/api/employee-sigmas/" + employeeSigmaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeSigmaDTO.getId().toString()))
            .body(employeeSigmaDTO);
    }

    /**
     * {@code PUT  /employee-sigmas/:id} : Updates an existing employeeSigma.
     *
     * @param id the id of the employeeSigmaDTO to save.
     * @param employeeSigmaDTO the employeeSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeSigmaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeSigmaDTO> updateEmployeeSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeSigmaDTO employeeSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeSigma : {}, {}", id, employeeSigmaDTO);
        if (employeeSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeSigmaDTO = employeeSigmaService.update(employeeSigmaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeSigmaDTO.getId().toString()))
            .body(employeeSigmaDTO);
    }

    /**
     * {@code PATCH  /employee-sigmas/:id} : Partial updates given fields of an existing employeeSigma, field will ignore if it is null
     *
     * @param id the id of the employeeSigmaDTO to save.
     * @param employeeSigmaDTO the employeeSigmaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeSigmaDTO,
     * or with status {@code 400 (Bad Request)} if the employeeSigmaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeSigmaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeSigmaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeSigmaDTO> partialUpdateEmployeeSigma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeSigmaDTO employeeSigmaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeSigma partially : {}, {}", id, employeeSigmaDTO);
        if (employeeSigmaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeSigmaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeSigmaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeSigmaDTO> result = employeeSigmaService.partialUpdate(employeeSigmaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeSigmaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-sigmas} : get all the employeeSigmas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeSigmas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeSigmaDTO>> getAllEmployeeSigmas(
        EmployeeSigmaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeSigmas by criteria: {}", criteria);

        Page<EmployeeSigmaDTO> page = employeeSigmaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-sigmas/count} : count all the employeeSigmas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeSigmas(EmployeeSigmaCriteria criteria) {
        LOG.debug("REST request to count EmployeeSigmas by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeSigmaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-sigmas/:id} : get the "id" employeeSigma.
     *
     * @param id the id of the employeeSigmaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeSigmaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSigmaDTO> getEmployeeSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeSigma : {}", id);
        Optional<EmployeeSigmaDTO> employeeSigmaDTO = employeeSigmaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeSigmaDTO);
    }

    /**
     * {@code DELETE  /employee-sigmas/:id} : delete the "id" employeeSigma.
     *
     * @param id the id of the employeeSigmaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployeeSigma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete EmployeeSigma : {}", id);
        employeeSigmaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
