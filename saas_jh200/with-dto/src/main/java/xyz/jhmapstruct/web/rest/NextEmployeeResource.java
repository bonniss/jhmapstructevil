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
import xyz.jhmapstruct.repository.NextEmployeeRepository;
import xyz.jhmapstruct.service.NextEmployeeQueryService;
import xyz.jhmapstruct.service.NextEmployeeService;
import xyz.jhmapstruct.service.criteria.NextEmployeeCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeDTO;
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
     * @param nextEmployeeDTO the nextEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeDTO, or with status {@code 400 (Bad Request)} if the nextEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeDTO> createNextEmployee(@Valid @RequestBody NextEmployeeDTO nextEmployeeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployee : {}", nextEmployeeDTO);
        if (nextEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeDTO = nextEmployeeService.save(nextEmployeeDTO);
        return ResponseEntity.created(new URI("/api/next-employees/" + nextEmployeeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeDTO.getId().toString()))
            .body(nextEmployeeDTO);
    }

    /**
     * {@code PUT  /next-employees/:id} : Updates an existing nextEmployee.
     *
     * @param id the id of the nextEmployeeDTO to save.
     * @param nextEmployeeDTO the nextEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeDTO> updateNextEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeDTO nextEmployeeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployee : {}, {}", id, nextEmployeeDTO);
        if (nextEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeDTO = nextEmployeeService.update(nextEmployeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeDTO.getId().toString()))
            .body(nextEmployeeDTO);
    }

    /**
     * {@code PATCH  /next-employees/:id} : Partial updates given fields of an existing nextEmployee, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeDTO to save.
     * @param nextEmployeeDTO the nextEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeDTO> partialUpdateNextEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeDTO nextEmployeeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployee partially : {}, {}", id, nextEmployeeDTO);
        if (nextEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeDTO> result = nextEmployeeService.partialUpdate(nextEmployeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeDTO.getId().toString())
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
    public ResponseEntity<List<NextEmployeeDTO>> getAllNextEmployees(
        NextEmployeeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployees by criteria: {}", criteria);

        Page<NextEmployeeDTO> page = nextEmployeeQueryService.findByCriteria(criteria, pageable);
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
     * @param id the id of the nextEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeDTO> getNextEmployee(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployee : {}", id);
        Optional<NextEmployeeDTO> nextEmployeeDTO = nextEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeDTO);
    }

    /**
     * {@code DELETE  /next-employees/:id} : delete the "id" nextEmployee.
     *
     * @param id the id of the nextEmployeeDTO to delete.
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
