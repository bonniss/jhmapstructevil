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
import xyz.jhmapstruct.repository.NextEmployeeThetaRepository;
import xyz.jhmapstruct.service.NextEmployeeThetaQueryService;
import xyz.jhmapstruct.service.NextEmployeeThetaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeThetaCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeThetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeTheta}.
 */
@RestController
@RequestMapping("/api/next-employee-thetas")
public class NextEmployeeThetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeThetaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeTheta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeThetaService nextEmployeeThetaService;

    private final NextEmployeeThetaRepository nextEmployeeThetaRepository;

    private final NextEmployeeThetaQueryService nextEmployeeThetaQueryService;

    public NextEmployeeThetaResource(
        NextEmployeeThetaService nextEmployeeThetaService,
        NextEmployeeThetaRepository nextEmployeeThetaRepository,
        NextEmployeeThetaQueryService nextEmployeeThetaQueryService
    ) {
        this.nextEmployeeThetaService = nextEmployeeThetaService;
        this.nextEmployeeThetaRepository = nextEmployeeThetaRepository;
        this.nextEmployeeThetaQueryService = nextEmployeeThetaQueryService;
    }

    /**
     * {@code POST  /next-employee-thetas} : Create a new nextEmployeeTheta.
     *
     * @param nextEmployeeThetaDTO the nextEmployeeThetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeThetaDTO, or with status {@code 400 (Bad Request)} if the nextEmployeeTheta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeThetaDTO> createNextEmployeeTheta(@Valid @RequestBody NextEmployeeThetaDTO nextEmployeeThetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeTheta : {}", nextEmployeeThetaDTO);
        if (nextEmployeeThetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeTheta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeThetaDTO = nextEmployeeThetaService.save(nextEmployeeThetaDTO);
        return ResponseEntity.created(new URI("/api/next-employee-thetas/" + nextEmployeeThetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeThetaDTO.getId().toString()))
            .body(nextEmployeeThetaDTO);
    }

    /**
     * {@code PUT  /next-employee-thetas/:id} : Updates an existing nextEmployeeTheta.
     *
     * @param id the id of the nextEmployeeThetaDTO to save.
     * @param nextEmployeeThetaDTO the nextEmployeeThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeThetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeThetaDTO> updateNextEmployeeTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeThetaDTO nextEmployeeThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeTheta : {}, {}", id, nextEmployeeThetaDTO);
        if (nextEmployeeThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeThetaDTO = nextEmployeeThetaService.update(nextEmployeeThetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeThetaDTO.getId().toString()))
            .body(nextEmployeeThetaDTO);
    }

    /**
     * {@code PATCH  /next-employee-thetas/:id} : Partial updates given fields of an existing nextEmployeeTheta, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeThetaDTO to save.
     * @param nextEmployeeThetaDTO the nextEmployeeThetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeThetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeThetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeThetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeThetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeThetaDTO> partialUpdateNextEmployeeTheta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeThetaDTO nextEmployeeThetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeTheta partially : {}, {}", id, nextEmployeeThetaDTO);
        if (nextEmployeeThetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeThetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeThetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeThetaDTO> result = nextEmployeeThetaService.partialUpdate(nextEmployeeThetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeThetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-thetas} : get all the nextEmployeeThetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeThetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeThetaDTO>> getAllNextEmployeeThetas(
        NextEmployeeThetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeThetas by criteria: {}", criteria);

        Page<NextEmployeeThetaDTO> page = nextEmployeeThetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-thetas/count} : count all the nextEmployeeThetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeThetas(NextEmployeeThetaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeThetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeThetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-thetas/:id} : get the "id" nextEmployeeTheta.
     *
     * @param id the id of the nextEmployeeThetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeThetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeThetaDTO> getNextEmployeeTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeTheta : {}", id);
        Optional<NextEmployeeThetaDTO> nextEmployeeThetaDTO = nextEmployeeThetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeThetaDTO);
    }

    /**
     * {@code DELETE  /next-employee-thetas/:id} : delete the "id" nextEmployeeTheta.
     *
     * @param id the id of the nextEmployeeThetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeTheta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeTheta : {}", id);
        nextEmployeeThetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
