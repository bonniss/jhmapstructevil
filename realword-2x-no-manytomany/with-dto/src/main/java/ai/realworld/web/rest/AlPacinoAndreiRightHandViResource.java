package ai.realworld.web.rest;

import ai.realworld.repository.AlPacinoAndreiRightHandViRepository;
import ai.realworld.service.AlPacinoAndreiRightHandViQueryService;
import ai.realworld.service.AlPacinoAndreiRightHandViService;
import ai.realworld.service.criteria.AlPacinoAndreiRightHandViCriteria;
import ai.realworld.service.dto.AlPacinoAndreiRightHandViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlPacinoAndreiRightHandVi}.
 */
@RestController
@RequestMapping("/api/al-pacino-andrei-right-hand-vis")
public class AlPacinoAndreiRightHandViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlPacinoAndreiRightHandViResource.class);

    private static final String ENTITY_NAME = "alPacinoAndreiRightHandVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlPacinoAndreiRightHandViService alPacinoAndreiRightHandViService;

    private final AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository;

    private final AlPacinoAndreiRightHandViQueryService alPacinoAndreiRightHandViQueryService;

    public AlPacinoAndreiRightHandViResource(
        AlPacinoAndreiRightHandViService alPacinoAndreiRightHandViService,
        AlPacinoAndreiRightHandViRepository alPacinoAndreiRightHandViRepository,
        AlPacinoAndreiRightHandViQueryService alPacinoAndreiRightHandViQueryService
    ) {
        this.alPacinoAndreiRightHandViService = alPacinoAndreiRightHandViService;
        this.alPacinoAndreiRightHandViRepository = alPacinoAndreiRightHandViRepository;
        this.alPacinoAndreiRightHandViQueryService = alPacinoAndreiRightHandViQueryService;
    }

    /**
     * {@code POST  /al-pacino-andrei-right-hand-vis} : Create a new alPacinoAndreiRightHandVi.
     *
     * @param alPacinoAndreiRightHandViDTO the alPacinoAndreiRightHandViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alPacinoAndreiRightHandViDTO, or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlPacinoAndreiRightHandViDTO> createAlPacinoAndreiRightHandVi(
        @Valid @RequestBody AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AlPacinoAndreiRightHandVi : {}", alPacinoAndreiRightHandViDTO);
        if (alPacinoAndreiRightHandViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alPacinoAndreiRightHandVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViService.save(alPacinoAndreiRightHandViDTO);
        return ResponseEntity.created(new URI("/api/al-pacino-andrei-right-hand-vis/" + alPacinoAndreiRightHandViDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandViDTO.getId().toString())
            )
            .body(alPacinoAndreiRightHandViDTO);
    }

    /**
     * {@code PUT  /al-pacino-andrei-right-hand-vis/:id} : Updates an existing alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandViDTO to save.
     * @param alPacinoAndreiRightHandViDTO the alPacinoAndreiRightHandViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandViDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandViDTO> updateAlPacinoAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlPacinoAndreiRightHandVi : {}, {}", id, alPacinoAndreiRightHandViDTO);
        if (alPacinoAndreiRightHandViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViService.update(alPacinoAndreiRightHandViDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandViDTO.getId().toString())
            )
            .body(alPacinoAndreiRightHandViDTO);
    }

    /**
     * {@code PATCH  /al-pacino-andrei-right-hand-vis/:id} : Partial updates given fields of an existing alPacinoAndreiRightHandVi, field will ignore if it is null
     *
     * @param id the id of the alPacinoAndreiRightHandViDTO to save.
     * @param alPacinoAndreiRightHandViDTO the alPacinoAndreiRightHandViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alPacinoAndreiRightHandViDTO,
     * or with status {@code 400 (Bad Request)} if the alPacinoAndreiRightHandViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alPacinoAndreiRightHandViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alPacinoAndreiRightHandViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlPacinoAndreiRightHandViDTO> partialUpdateAlPacinoAndreiRightHandVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlPacinoAndreiRightHandViDTO alPacinoAndreiRightHandViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlPacinoAndreiRightHandVi partially : {}, {}", id, alPacinoAndreiRightHandViDTO);
        if (alPacinoAndreiRightHandViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alPacinoAndreiRightHandViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alPacinoAndreiRightHandViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlPacinoAndreiRightHandViDTO> result = alPacinoAndreiRightHandViService.partialUpdate(alPacinoAndreiRightHandViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alPacinoAndreiRightHandViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis} : get all the alPacinoAndreiRightHandVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alPacinoAndreiRightHandVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlPacinoAndreiRightHandViDTO>> getAllAlPacinoAndreiRightHandVis(
        AlPacinoAndreiRightHandViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlPacinoAndreiRightHandVis by criteria: {}", criteria);

        Page<AlPacinoAndreiRightHandViDTO> page = alPacinoAndreiRightHandViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis/count} : count all the alPacinoAndreiRightHandVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlPacinoAndreiRightHandVis(AlPacinoAndreiRightHandViCriteria criteria) {
        LOG.debug("REST request to count AlPacinoAndreiRightHandVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alPacinoAndreiRightHandViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pacino-andrei-right-hand-vis/:id} : get the "id" alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alPacinoAndreiRightHandViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlPacinoAndreiRightHandViDTO> getAlPacinoAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlPacinoAndreiRightHandVi : {}", id);
        Optional<AlPacinoAndreiRightHandViDTO> alPacinoAndreiRightHandViDTO = alPacinoAndreiRightHandViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alPacinoAndreiRightHandViDTO);
    }

    /**
     * {@code DELETE  /al-pacino-andrei-right-hand-vis/:id} : delete the "id" alPacinoAndreiRightHandVi.
     *
     * @param id the id of the alPacinoAndreiRightHandViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlPacinoAndreiRightHandVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlPacinoAndreiRightHandVi : {}", id);
        alPacinoAndreiRightHandViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
