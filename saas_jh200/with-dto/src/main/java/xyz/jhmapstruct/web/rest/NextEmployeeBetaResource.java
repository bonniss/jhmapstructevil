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
import xyz.jhmapstruct.repository.NextEmployeeBetaRepository;
import xyz.jhmapstruct.service.NextEmployeeBetaQueryService;
import xyz.jhmapstruct.service.NextEmployeeBetaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeBetaCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeBetaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeBeta}.
 */
@RestController
@RequestMapping("/api/next-employee-betas")
public class NextEmployeeBetaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeBetaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeBeta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeBetaService nextEmployeeBetaService;

    private final NextEmployeeBetaRepository nextEmployeeBetaRepository;

    private final NextEmployeeBetaQueryService nextEmployeeBetaQueryService;

    public NextEmployeeBetaResource(
        NextEmployeeBetaService nextEmployeeBetaService,
        NextEmployeeBetaRepository nextEmployeeBetaRepository,
        NextEmployeeBetaQueryService nextEmployeeBetaQueryService
    ) {
        this.nextEmployeeBetaService = nextEmployeeBetaService;
        this.nextEmployeeBetaRepository = nextEmployeeBetaRepository;
        this.nextEmployeeBetaQueryService = nextEmployeeBetaQueryService;
    }

    /**
     * {@code POST  /next-employee-betas} : Create a new nextEmployeeBeta.
     *
     * @param nextEmployeeBetaDTO the nextEmployeeBetaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeBetaDTO, or with status {@code 400 (Bad Request)} if the nextEmployeeBeta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeBetaDTO> createNextEmployeeBeta(@Valid @RequestBody NextEmployeeBetaDTO nextEmployeeBetaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeBeta : {}", nextEmployeeBetaDTO);
        if (nextEmployeeBetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeBeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeBetaDTO = nextEmployeeBetaService.save(nextEmployeeBetaDTO);
        return ResponseEntity.created(new URI("/api/next-employee-betas/" + nextEmployeeBetaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeBetaDTO.getId().toString()))
            .body(nextEmployeeBetaDTO);
    }

    /**
     * {@code PUT  /next-employee-betas/:id} : Updates an existing nextEmployeeBeta.
     *
     * @param id the id of the nextEmployeeBetaDTO to save.
     * @param nextEmployeeBetaDTO the nextEmployeeBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeBetaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeBetaDTO> updateNextEmployeeBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeBetaDTO nextEmployeeBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeBeta : {}, {}", id, nextEmployeeBetaDTO);
        if (nextEmployeeBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeBetaDTO = nextEmployeeBetaService.update(nextEmployeeBetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeBetaDTO.getId().toString()))
            .body(nextEmployeeBetaDTO);
    }

    /**
     * {@code PATCH  /next-employee-betas/:id} : Partial updates given fields of an existing nextEmployeeBeta, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeBetaDTO to save.
     * @param nextEmployeeBetaDTO the nextEmployeeBetaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeBetaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeBetaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeBetaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeBetaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeBetaDTO> partialUpdateNextEmployeeBeta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeBetaDTO nextEmployeeBetaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeBeta partially : {}, {}", id, nextEmployeeBetaDTO);
        if (nextEmployeeBetaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeBetaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeBetaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeBetaDTO> result = nextEmployeeBetaService.partialUpdate(nextEmployeeBetaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeBetaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-betas} : get all the nextEmployeeBetas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeBetas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeBetaDTO>> getAllNextEmployeeBetas(
        NextEmployeeBetaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeBetas by criteria: {}", criteria);

        Page<NextEmployeeBetaDTO> page = nextEmployeeBetaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-betas/count} : count all the nextEmployeeBetas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeBetas(NextEmployeeBetaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeBetas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeBetaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-betas/:id} : get the "id" nextEmployeeBeta.
     *
     * @param id the id of the nextEmployeeBetaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeBetaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeBetaDTO> getNextEmployeeBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeBeta : {}", id);
        Optional<NextEmployeeBetaDTO> nextEmployeeBetaDTO = nextEmployeeBetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeBetaDTO);
    }

    /**
     * {@code DELETE  /next-employee-betas/:id} : delete the "id" nextEmployeeBeta.
     *
     * @param id the id of the nextEmployeeBetaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeBeta(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeBeta : {}", id);
        nextEmployeeBetaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
