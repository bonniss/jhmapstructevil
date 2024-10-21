package ai.realworld.web.rest;

import ai.realworld.repository.AlPowerShellRepository;
import ai.realworld.service.AlPowerShellQueryService;
import ai.realworld.service.AlPowerShellService;
import ai.realworld.service.criteria.AlPowerShellCriteria;
import ai.realworld.service.dto.AlPowerShellDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlPowerShell}.
 */
@RestController
@RequestMapping("/api/al-power-shells")
public class AlPowerShellResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPowerShellResource.class);

    private static final String ENTITY_NAME = "alPowerShell";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPowerShellService alPowerShellService;

    private final AlPowerShellRepository alPowerShellRepository;

    private final AlPowerShellQueryService alPowerShellQueryService;

    public AlPowerShellResource(
        AlPowerShellService alPowerShellService,
        AlPowerShellRepository alPowerShellRepository,
        AlPowerShellQueryService alPowerShellQueryService
    ) {
        this.alPowerShellService = alPowerShellService;
        this.alPowerShellRepository = alPowerShellRepository;
        this.alPowerShellQueryService = alPowerShellQueryService;
    }

    /**
     * {@code POST  /al-power-shells} : Create a new alPowerShell.
     *
     * @param alPowerShellDTO the alPowerShellDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPowerShellDTO, or with status {@code 400 (Bad Request)} if the alPowerShell has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPowerShellDTO> createAlPowerShell(@RequestBody AlPowerShellDTO alPowerShellDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlPowerShell : {}", alPowerShellDTO);
        if (alPowerShellDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPowerShell cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPowerShellDTO = alPowerShellService.save(alPowerShellDTO);
        return ResponseEntity.created(new URI("/api/al-power-shells/" + alPowerShellDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPowerShellDTO.getId().toString()))
            .body(alPowerShellDTO);
    }

    /**
     * {@code PUT  /al-power-shells/:id} : Updates an existing alPowerShell.
     *
     * @param id the id of the alPowerShellDTO to save.
     * @param alPowerShellDTO the alPowerShellDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPowerShellDTO,
     * or with status {@code 400 (Bad Request)} if the alPowerShellDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPowerShellDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPowerShellDTO> updateAlPowerShell(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPowerShellDTO alPowerShellDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPowerShell : {}, {}", id, alPowerShellDTO);
        if (alPowerShellDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPowerShellDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPowerShellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPowerShellDTO = alPowerShellService.update(alPowerShellDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPowerShellDTO.getId().toString()))
            .body(alPowerShellDTO);
    }

    /**
     * {@code PATCH  /al-power-shells/:id} : Partial updates given fields of an existing alPowerShell, field will ignore if it is null
     *
     * @param id the id of the alPowerShellDTO to save.
     * @param alPowerShellDTO the alPowerShellDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPowerShellDTO,
     * or with status {@code 400 (Bad Request)} if the alPowerShellDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPowerShellDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPowerShellDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPowerShellDTO> partialUpdateAlPowerShell(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AlPowerShellDTO alPowerShellDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPowerShell partially : {}, {}", id, alPowerShellDTO);
        if (alPowerShellDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPowerShellDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPowerShellRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPowerShellDTO> result = alPowerShellService.partialUpdate(alPowerShellDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPowerShellDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-power-shells} : get all the alPowerShells.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPowerShells in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPowerShellDTO>> getAllAlPowerShells(
        AlPowerShellCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPowerShells by criteria: {}", criteria);

        Page<AlPowerShellDTO> page = alPowerShellQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-power-shells/count} : count all the alPowerShells.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPowerShells(AlPowerShellCriteria criteria) {
        LOG.debug("REST request to count AlPowerShells by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPowerShellQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-power-shells/:id} : get the "id" alPowerShell.
     *
     * @param id the id of the alPowerShellDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPowerShellDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPowerShellDTO> getAlPowerShell(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPowerShell : {}", id);
        Optional<AlPowerShellDTO> alPowerShellDTO = alPowerShellService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPowerShellDTO);
    }

    /**
     * {@code DELETE  /al-power-shells/:id} : delete the "id" alPowerShell.
     *
     * @param id the id of the alPowerShellDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPowerShell(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPowerShell : {}", id);
        alPowerShellService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
