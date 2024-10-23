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
import xyz.jhmapstruct.domain.NextEmployee;
import xyz.jhmapstruct.repository.NextEmployeeRepository;
import xyz.jhmapstruct.service.NextEmployeeQueryService;
import xyz.jhmapstruct.service.NextEmployeeService;
import xyz.jhmapstruct.service.criteria.NextEmployeeCriteria;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployee}.
 */
@RestController
@RequestMapping("/api/next-employees")
public class NextEmployeeResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeResource.class);

    private static final String ENTITY_NAME = "nextEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeService nextEmployeeService;

    private final NextEmployeeRepository nextEmployeeRepository;

    private final NextEmployeeQueryService nextEmployeeQueryService;

    public NextEmployeeResource(
        NextEmployeeService nextEmployeeService,
        NextEmployeeRepository nextEmployeeRepository,
        NextEmployeeQueryService nextEmployeeQueryService
    ) {
        this.nextEmployeeService = nextEmployeeService;
        this.nextEmployeeRepository = nextEmployeeRepository;
        this.nextEmployeeQueryService = nextEmployeeQueryService;
    }

    /**
     * {@code POST  /next-employees} : Create a new nextEmployee.
     *
     * @param nextEmployee the nextEmployee to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployee, or with status {@code 400 (Bad Request)} if the nextEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployee> createNextEmployee(@Valid @RequestBody NextEmployee nextEmployee) throws URISyntaxException {
        LOG.debug("REST request to save NextEmployee : {}", nextEmployee);
        if (nextEmployee.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployee = nextEmployeeService.save(nextEmployee);
        return ResponseEntity.created(new URI("/api/next-employees/" + nextEmployee.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployee.getId().toString()))
            .body(nextEmployee);
    }

    /**
     * {@code PUT  /next-employees/:id} : Updates an existing nextEmployee.
     *
     * @param id the id of the nextEmployee to save.
     * @param nextEmployee the nextEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployee,
     * or with status {@code 400 (Bad Request)} if the nextEmployee is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployee> updateNextEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployee nextEmployee
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployee : {}, {}", id, nextEmployee);
        if (nextEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployee = nextEmployeeService.update(nextEmployee);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployee.getId().toString()))
            .body(nextEmployee);
    }

    /**
     * {@code PATCH  /next-employees/:id} : Partial updates given fields of an existing nextEmployee, field will ignore if it is null
     *
     * @param id the id of the nextEmployee to save.
     * @param nextEmployee the nextEmployee to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployee,
     * or with status {@code 400 (Bad Request)} if the nextEmployee is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployee is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployee couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployee> partialUpdateNextEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployee nextEmployee
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployee partially : {}, {}", id, nextEmployee);
        if (nextEmployee.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployee.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployee> result = nextEmployeeService.partialUpdate(nextEmployee);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployee.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employees} : get all the nextEmployees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployee>> getAllNextEmployees(
        NextEmployeeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployees by criteria: {}", criteria);

        Page<NextEmployee> page = nextEmployeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employees/count} : count all the nextEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployees(NextEmployeeCriteria criteria) {
        LOG.debug("REST request to count NextEmployees by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employees/:id} : get the "id" nextEmployee.
     *
     * @param id the id of the nextEmployee to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployee, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployee> getNextEmployee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployee : {}", id);
        Optional<NextEmployee> nextEmployee = nextEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployee);
    }

    /**
     * {@code DELETE  /next-employees/:id} : delete the "id" nextEmployee.
     *
     * @param id the id of the nextEmployee to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployee(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployee : {}", id);
        nextEmployeeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
