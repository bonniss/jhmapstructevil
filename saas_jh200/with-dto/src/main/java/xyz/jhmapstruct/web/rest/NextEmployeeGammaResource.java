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
import xyz.jhmapstruct.repository.NextEmployeeGammaRepository;
import xyz.jhmapstruct.service.NextEmployeeGammaQueryService;
import xyz.jhmapstruct.service.NextEmployeeGammaService;
import xyz.jhmapstruct.service.criteria.NextEmployeeGammaCriteria;
import xyz.jhmapstruct.service.dto.NextEmployeeGammaDTO;
import xyz.jhmapstruct.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link xyz.jhmapstruct.domain.NextEmployeeGamma}.
 */
@RestController
@RequestMapping("/api/next-employee-gammas")
public class NextEmployeeGammaResource {

    private static final Logger LOG = LoggerFactory.getLogger(NextEmployeeGammaResource.class);

    private static final String ENTITY_NAME = "nextEmployeeGamma";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextEmployeeGammaService nextEmployeeGammaService;

    private final NextEmployeeGammaRepository nextEmployeeGammaRepository;

    private final NextEmployeeGammaQueryService nextEmployeeGammaQueryService;

    public NextEmployeeGammaResource(
        NextEmployeeGammaService nextEmployeeGammaService,
        NextEmployeeGammaRepository nextEmployeeGammaRepository,
        NextEmployeeGammaQueryService nextEmployeeGammaQueryService
    ) {
        this.nextEmployeeGammaService = nextEmployeeGammaService;
        this.nextEmployeeGammaRepository = nextEmployeeGammaRepository;
        this.nextEmployeeGammaQueryService = nextEmployeeGammaQueryService;
    }

    /**
     * {@code POST  /next-employee-gammas} : Create a new nextEmployeeGamma.
     *
     * @param nextEmployeeGammaDTO the nextEmployeeGammaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextEmployeeGammaDTO, or with status {@code 400 (Bad Request)} if the nextEmployeeGamma has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NextEmployeeGammaDTO> createNextEmployeeGamma(@Valid @RequestBody NextEmployeeGammaDTO nextEmployeeGammaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NextEmployeeGamma : {}", nextEmployeeGammaDTO);
        if (nextEmployeeGammaDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextEmployeeGamma cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nextEmployeeGammaDTO = nextEmployeeGammaService.save(nextEmployeeGammaDTO);
        return ResponseEntity.created(new URI("/api/next-employee-gammas/" + nextEmployeeGammaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nextEmployeeGammaDTO.getId().toString()))
            .body(nextEmployeeGammaDTO);
    }

    /**
     * {@code PUT  /next-employee-gammas/:id} : Updates an existing nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGammaDTO to save.
     * @param nextEmployeeGammaDTO the nextEmployeeGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeGammaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NextEmployeeGammaDTO> updateNextEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextEmployeeGammaDTO nextEmployeeGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NextEmployeeGamma : {}, {}", id, nextEmployeeGammaDTO);
        if (nextEmployeeGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nextEmployeeGammaDTO = nextEmployeeGammaService.update(nextEmployeeGammaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeGammaDTO.getId().toString()))
            .body(nextEmployeeGammaDTO);
    }

    /**
     * {@code PATCH  /next-employee-gammas/:id} : Partial updates given fields of an existing nextEmployeeGamma, field will ignore if it is null
     *
     * @param id the id of the nextEmployeeGammaDTO to save.
     * @param nextEmployeeGammaDTO the nextEmployeeGammaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextEmployeeGammaDTO,
     * or with status {@code 400 (Bad Request)} if the nextEmployeeGammaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextEmployeeGammaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextEmployeeGammaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NextEmployeeGammaDTO> partialUpdateNextEmployeeGamma(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextEmployeeGammaDTO nextEmployeeGammaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NextEmployeeGamma partially : {}, {}", id, nextEmployeeGammaDTO);
        if (nextEmployeeGammaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextEmployeeGammaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextEmployeeGammaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextEmployeeGammaDTO> result = nextEmployeeGammaService.partialUpdate(nextEmployeeGammaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nextEmployeeGammaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-employee-gammas} : get all the nextEmployeeGammas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextEmployeeGammas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NextEmployeeGammaDTO>> getAllNextEmployeeGammas(
        NextEmployeeGammaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NextEmployeeGammas by criteria: {}", criteria);

        Page<NextEmployeeGammaDTO> page = nextEmployeeGammaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-employee-gammas/count} : count all the nextEmployeeGammas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNextEmployeeGammas(NextEmployeeGammaCriteria criteria) {
        LOG.debug("REST request to count NextEmployeeGammas by criteria: {}", criteria);
        return ResponseEntity.ok().body(nextEmployeeGammaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /next-employee-gammas/:id} : get the "id" nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGammaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextEmployeeGammaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NextEmployeeGammaDTO> getNextEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NextEmployeeGamma : {}", id);
        Optional<NextEmployeeGammaDTO> nextEmployeeGammaDTO = nextEmployeeGammaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextEmployeeGammaDTO);
    }

    /**
     * {@code DELETE  /next-employee-gammas/:id} : delete the "id" nextEmployeeGamma.
     *
     * @param id the id of the nextEmployeeGammaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNextEmployeeGamma(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NextEmployeeGamma : {}", id);
        nextEmployeeGammaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
