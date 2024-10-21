package ai.realworld.web.rest;

import ai.realworld.repository.MetaverseRepository;
import ai.realworld.service.MetaverseQueryService;
import ai.realworld.service.MetaverseService;
import ai.realworld.service.criteria.MetaverseCriteria;
import ai.realworld.service.dto.MetaverseDTO;
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
 * REST controller for managing {@link ai.realworld.domain.Metaverse}.
 */
@RestController
@RequestMapping("/api/metaverses")
public class MetaverseResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaverseResource.class);

    private static final String ENTITY_NAME = "metaverse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaverseService metaverseService;

    private final MetaverseRepository metaverseRepository;

    private final MetaverseQueryService metaverseQueryService;

    public MetaverseResource(
        MetaverseService metaverseService,
        MetaverseRepository metaverseRepository,
        MetaverseQueryService metaverseQueryService
    ) {
        this.metaverseService = metaverseService;
        this.metaverseRepository = metaverseRepository;
        this.metaverseQueryService = metaverseQueryService;
    }

    /**
     * {@code POST  /metaverses} : Create a new metaverse.
     *
     * @param metaverseDTO the metaverseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaverseDTO, or with status {@code 400 (Bad Request)} if the metaverse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaverseDTO> createMetaverse(@Valid @RequestBody MetaverseDTO metaverseDTO) throws URISyntaxException {
        LOG.debug("REST request to save Metaverse : {}", metaverseDTO);
        if (metaverseDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaverse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaverseDTO = metaverseService.save(metaverseDTO);
        return ResponseEntity.created(new URI("/api/metaverses/" + metaverseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaverseDTO.getId().toString()))
            .body(metaverseDTO);
    }

    /**
     * {@code PUT  /metaverses/:id} : Updates an existing metaverse.
     *
     * @param id the id of the metaverseDTO to save.
     * @param metaverseDTO the metaverseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaverseDTO,
     * or with status {@code 400 (Bad Request)} if the metaverseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaverseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaverseDTO> updateMetaverse(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaverseDTO metaverseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Metaverse : {}, {}", id, metaverseDTO);
        if (metaverseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaverseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaverseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaverseDTO = metaverseService.update(metaverseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaverseDTO.getId().toString()))
            .body(metaverseDTO);
    }

    /**
     * {@code PATCH  /metaverses/:id} : Partial updates given fields of an existing metaverse, field will ignore if it is null
     *
     * @param id the id of the metaverseDTO to save.
     * @param metaverseDTO the metaverseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaverseDTO,
     * or with status {@code 400 (Bad Request)} if the metaverseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaverseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaverseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaverseDTO> partialUpdateMetaverse(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaverseDTO metaverseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Metaverse partially : {}, {}", id, metaverseDTO);
        if (metaverseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaverseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaverseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaverseDTO> result = metaverseService.partialUpdate(metaverseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaverseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /metaverses} : get all the metaverses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaverses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaverseDTO>> getAllMetaverses(
        MetaverseCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Metaverses by criteria: {}", criteria);

        Page<MetaverseDTO> page = metaverseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /metaverses/count} : count all the metaverses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaverses(MetaverseCriteria criteria) {
        LOG.debug("REST request to count Metaverses by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaverseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /metaverses/:id} : get the "id" metaverse.
     *
     * @param id the id of the metaverseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaverseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaverseDTO> getMetaverse(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Metaverse : {}", id);
        Optional<MetaverseDTO> metaverseDTO = metaverseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaverseDTO);
    }

    /**
     * {@code DELETE  /metaverses/:id} : delete the "id" metaverse.
     *
     * @param id the id of the metaverseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaverse(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Metaverse : {}", id);
        metaverseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
