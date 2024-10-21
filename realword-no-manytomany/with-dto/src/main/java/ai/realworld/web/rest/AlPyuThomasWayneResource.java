package ai.realworld.web.rest;

import ai.realworld.repository.AlPyuThomasWayneRepository;
import ai.realworld.service.AlPyuThomasWayneQueryService;
import ai.realworld.service.AlPyuThomasWayneService;
import ai.realworld.service.criteria.AlPyuThomasWayneCriteria;
import ai.realworld.service.dto.AlPyuThomasWayneDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
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

/**
 * REST controller for managing {@link ai.realworld.domain.AlPyuThomasWayne}.
 */
@RestController
@RequestMapping("/api/al-pyu-thomas-waynes")
public class AlPyuThomasWayneResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPyuThomasWayneResource.class);

    private static final String ENTITY_NAME = "alPyuThomasWayne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPyuThomasWayneService alPyuThomasWayneService;

    private final AlPyuThomasWayneRepository alPyuThomasWayneRepository;

    private final AlPyuThomasWayneQueryService alPyuThomasWayneQueryService;

    public AlPyuThomasWayneResource(
        AlPyuThomasWayneService alPyuThomasWayneService,
        AlPyuThomasWayneRepository alPyuThomasWayneRepository,
        AlPyuThomasWayneQueryService alPyuThomasWayneQueryService
    ) {
        this.alPyuThomasWayneService = alPyuThomasWayneService;
        this.alPyuThomasWayneRepository = alPyuThomasWayneRepository;
        this.alPyuThomasWayneQueryService = alPyuThomasWayneQueryService;
    }

    /**
     * {@code POST  /al-pyu-thomas-waynes} : Create a new alPyuThomasWayne.
     *
     * @param alPyuThomasWayneDTO the alPyuThomasWayneDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPyuThomasWayneDTO, or with status {@code 400 (Bad Request)} if the alPyuThomasWayne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPyuThomasWayneDTO> createAlPyuThomasWayne(@Valid @RequestBody AlPyuThomasWayneDTO alPyuThomasWayneDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AlPyuThomasWayne : {}", alPyuThomasWayneDTO);
        if (alPyuThomasWayneDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPyuThomasWayne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPyuThomasWayneDTO = alPyuThomasWayneService.save(alPyuThomasWayneDTO);
        return ResponseEntity.created(new URI("/api/al-pyu-thomas-waynes/" + alPyuThomasWayneDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneDTO.getId().toString()))
            .body(alPyuThomasWayneDTO);
    }

    /**
     * {@code PUT  /al-pyu-thomas-waynes/:id} : Updates an existing alPyuThomasWayne.
     *
     * @param id the id of the alPyuThomasWayneDTO to save.
     * @param alPyuThomasWayneDTO the alPyuThomasWayneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuThomasWayneDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuThomasWayneDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPyuThomasWayneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPyuThomasWayneDTO> updateAlPyuThomasWayne(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPyuThomasWayneDTO alPyuThomasWayneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPyuThomasWayne : {}, {}", id, alPyuThomasWayneDTO);
        if (alPyuThomasWayneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuThomasWayneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuThomasWayneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPyuThomasWayneDTO = alPyuThomasWayneService.update(alPyuThomasWayneDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneDTO.getId().toString()))
            .body(alPyuThomasWayneDTO);
    }

    /**
     * {@code PATCH  /al-pyu-thomas-waynes/:id} : Partial updates given fields of an existing alPyuThomasWayne, field will ignore if it is null
     *
     * @param id the id of the alPyuThomasWayneDTO to save.
     * @param alPyuThomasWayneDTO the alPyuThomasWayneDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPyuThomasWayneDTO,
     * or with status {@code 400 (Bad Request)} if the alPyuThomasWayneDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPyuThomasWayneDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPyuThomasWayneDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPyuThomasWayneDTO> partialUpdateAlPyuThomasWayne(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPyuThomasWayneDTO alPyuThomasWayneDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPyuThomasWayne partially : {}, {}", id, alPyuThomasWayneDTO);
        if (alPyuThomasWayneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPyuThomasWayneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPyuThomasWayneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPyuThomasWayneDTO> result = alPyuThomasWayneService.partialUpdate(alPyuThomasWayneDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPyuThomasWayneDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pyu-thomas-waynes} : get all the alPyuThomasWaynes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPyuThomasWaynes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPyuThomasWayneDTO>> getAllAlPyuThomasWaynes(
        AlPyuThomasWayneCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPyuThomasWaynes by criteria: {}", criteria);

        Page<AlPyuThomasWayneDTO> page = alPyuThomasWayneQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pyu-thomas-waynes/count} : count all the alPyuThomasWaynes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPyuThomasWaynes(AlPyuThomasWayneCriteria criteria) {
        LOG.debug("REST request to count AlPyuThomasWaynes by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPyuThomasWayneQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pyu-thomas-waynes/:id} : get the "id" alPyuThomasWayne.
     *
     * @param id the id of the alPyuThomasWayneDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPyuThomasWayneDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPyuThomasWayneDTO> getAlPyuThomasWayne(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPyuThomasWayne : {}", id);
        Optional<AlPyuThomasWayneDTO> alPyuThomasWayneDTO = alPyuThomasWayneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPyuThomasWayneDTO);
    }

    /**
     * {@code DELETE  /al-pyu-thomas-waynes/:id} : delete the "id" alPyuThomasWayne.
     *
     * @param id the id of the alPyuThomasWayneDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPyuThomasWayne(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPyuThomasWayne : {}", id);
        alPyuThomasWayneService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
