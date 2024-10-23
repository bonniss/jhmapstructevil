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
import xyz.jhmapstruct.repository.EmployeeMiRepository;
import xyz.jhmapstruct.service.EmployeeMiQueryService;
import xyz.jhmapstruct.service.EmployeeMiService;
import xyz.jhmapstruct.service.criteria.EmployeeMiCriteria;
import xyz.jhmapstruct.service.dto.EmployeeMiDTO;
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

    private final EmployeeMiQueryService employeeMiQueryService;

    public EmployeeMiResource(
        EmployeeMiService employeeMiService,
        EmployeeMiRepository employeeMiRepository,
        EmployeeMiQueryService employeeMiQueryService
    ) {
        this.employeeMiService = employeeMiService;
        this.employeeMiRepository = employeeMiRepository;
        this.employeeMiQueryService = employeeMiQueryService;
    }

    /**
     * {@code POST  /employee-mis} : Create a new employeeMi.
     *
     * @param employeeMiDTO the employeeMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employeeMiDTO, or with status {@code 400 (Bad Request)} if the employeeMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EmployeeMiDTO> createEmployeeMi(@Valid @RequestBody EmployeeMiDTO employeeMiDTO) throws URISyntaxException {
        LOG.debug("REST request to save EmployeeMi : {}", employeeMiDTO);
        if (employeeMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new employeeMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employeeMiDTO = employeeMiService.save(employeeMiDTO);
        return ResponseEntity.created(new URI("/api/employee-mis/" + employeeMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employeeMiDTO.getId().toString()))
            .body(employeeMiDTO);
    }

    /**
     * {@code PUT  /employee-mis/:id} : Updates an existing employeeMi.
     *
     * @param id the id of the employeeMiDTO to save.
     * @param employeeMiDTO the employeeMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMiDTO,
     * or with status {@code 400 (Bad Request)} if the employeeMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employeeMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeMiDTO> updateEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmployeeMiDTO employeeMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EmployeeMi : {}, {}", id, employeeMiDTO);
        if (employeeMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employeeMiDTO = employeeMiService.update(employeeMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMiDTO.getId().toString()))
            .body(employeeMiDTO);
    }

    /**
     * {@code PATCH  /employee-mis/:id} : Partial updates given fields of an existing employeeMi, field will ignore if it is null
     *
     * @param id the id of the employeeMiDTO to save.
     * @param employeeMiDTO the employeeMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employeeMiDTO,
     * or with status {@code 400 (Bad Request)} if the employeeMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employeeMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employeeMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmployeeMiDTO> partialUpdateEmployeeMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmployeeMiDTO employeeMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EmployeeMi partially : {}, {}", id, employeeMiDTO);
        if (employeeMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employeeMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employeeMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmployeeMiDTO> result = employeeMiService.partialUpdate(employeeMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employeeMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employee-mis} : get all the employeeMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employeeMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EmployeeMiDTO>> getAllEmployeeMis(
        EmployeeMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EmployeeMis by criteria: {}", criteria);

        Page<EmployeeMiDTO> page = employeeMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employee-mis/count} : count all the employeeMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countEmployeeMis(EmployeeMiCriteria criteria) {
        LOG.debug("REST request to count EmployeeMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(employeeMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employee-mis/:id} : get the "id" employeeMi.
     *
     * @param id the id of the employeeMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employeeMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeMiDTO> getEmployeeMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get EmployeeMi : {}", id);
        Optional<EmployeeMiDTO> employeeMiDTO = employeeMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employeeMiDTO);
    }

    /**
     * {@code DELETE  /employee-mis/:id} : delete the "id" employeeMi.
     *
     * @param id the id of the employeeMiDTO to delete.
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
