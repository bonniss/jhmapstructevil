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
import xyz.jhmapstruct.repository.NextEmployeeMiMiRepository;
import xyz.jhmapstruct.service.NextEmployeeMiMiQueryService;
import xyz.jhmapstruct.service.NextEmployeeMiMiService;
import xyz.jhmapstruct.service.criteria.NextEmployeeMiMiCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeMiMiDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeMiMi}.
 */
@RestController
@RequestMapping("/api/next-employee-mi-mis")
public class NextEmployeeMiMiResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeMiMiResource.class);

    private static final String ENTITY_NAME = "nextEmployeeMiMi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeMiMiService nextEmployeeMiMiService;

    private final NextEmployeeMiMiRepository nextEmployeeMiMiRepository;

    private final NextEmployeeMiMiQueryService nextEmployeeMiMiQueryService;

    public NextEmployeeMiMiResource(
        NextEmployeeMiMiService nextEmployeeMiMiService,
        NextEmployeeMiMiRepository nextEmployeeMiMiRepository,
        NextEmployeeMiMiQueryService nextEmployeeMiMiQueryService
    ) {
        this.nextEmployeeMiMiService = nextEmployeeMiMiService;
        this.nextEmployeeMiMiRepository = nextEmployeeMiMiRepository;
        this.nextEmployeeMiMiQueryService = nextEmployeeMiMiQueryService;
    }

    /**
     * {@code POST  /next-employee-mi-mis} : Create a new nextEmployeeMiMi.
     *
     * @param nextEmployeeMiMiDTO the nextEmployeeMiMiDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeMiMiDTO, or with status {@code 400 (Bad Request)} if the nextEmployeeMiMi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeMiMiDTO> createNextEmployeeMiMi(@Valid @RequestBody NextEmployeeMiMiDTO nextEmployeeMiMiDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeMiMi : {}", nextEmployeeMiMiDTO);
        if (nextEmployeeMiMiDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeMiMi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeMiMiDTO = nextEmployeeMiMiService.save(nextEmployeeMiMiDTO);
        return ResponseEntity.created(new URI("/api/next-employee-mi-mis/" + nextEmployeeMiMiDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeMiMiDTO.getId().toString()))
            .body(nextEmployeeMiMiDTO);
    }

    /**
     * {@code PUT  /next-employee-mi-mis/:id} : Updates an existing nextEmployeeMiMi.
     *
     * @param id the id of the nextEmployeeMiMiDTO to save.
     * @param nextEmployeeMiMiDTO the nextEmployeeMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeMiMiDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeMiMiDTO> updateNextEmployeeMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeMiMiDTO nextEmployeeMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeMiMi : {}, {}", id, nextEmployeeMiMiDTO);
        if (nextEmployeeMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeMiMiDTO = nextEmployeeMiMiService.update(nextEmployeeMiMiDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeMiMiDTO.getId().toString()))
            .body(nextEmployeeMiMiDTO);
    }

    /**
     * {@code PATCH  /next-employee-mi-mis/:id} : Partial updates given fields of an existing nextEmployeeMiMi, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeMiMiDTO to save.
     * @param nextEmployeeMiMiDTO the nextEmployeeMiMiDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeMiMiDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeMiMiDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeMiMiDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeMiMiDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeMiMiDTO> partialUpdateNextEmployeeMiMi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeMiMiDTO nextEmployeeMiMiDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeMiMi partially : {}, {}", id, nextEmployeeMiMiDTO);
        if (nextEmployeeMiMiDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeMiMiDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeMiMiRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeMiMiDTO> result = nextEmployeeMiMiService.partialUpdate(nextEmployeeMiMiDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeMiMiDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-mi-mis} : get all the nextEmployeeMiMis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeMiMis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeMiMiDTO>> getAllNextEmployeeMiMis(
        NextEmployeeMiMiCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeMiMis by criteria: {}", criteria);

        Page<NextEmployeeMiMiDTO> page = nextEmployeeMiMiQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-mi-mis/count} : count all the nextEmployeeMiMis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeMiMis(NextEmployeeMiMiCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeMiMis by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeMiMiQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-mi-mis/:id} : get the "id" nextEmployeeMiMi.
     *
     * @param id the id of the nextEmployeeMiMiDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeMiMiDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeMiMiDTO> getNextEmployeeMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeMiMi : {}", id);
        Optional<NextEmployeeMiMiDTO> nextEmployeeMiMiDTO = nextEmployeeMiMiService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeMiMiDTO);
    }

    /**
     * {@code DELETE  /next-employee-mi-mis/:id} : delete the "id" nextEmployeeMiMi.
     *
     * @param id the id of the nextEmployeeMiMiDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeMiMi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeMiMi : {}", id);
        nextEmployeeMiMiService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
