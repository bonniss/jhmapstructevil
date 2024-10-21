package ai.realworld.web.rest;

import ai.realworld.repository.OlAlmantinoMiloRepository;
import ai.realworld.service.OlAlmantinoMiloQueryService;
import ai.realworld.service.OlAlmantinoMiloService;
import ai.realworld.service.criteria.OlAlmantinoMiloCriteria;
import ai.realworld.service.dto.OlAlmantinoMiloDTO;
import ai.realworld.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
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
 * REST controller for managing {@link ai.realworld.domain.OlAlmantinoMilo}.
 */
@RestController
@RequestMapping("/api/ol-almantino-milos")
public class OlAlmantinoMiloResource {

    private static final Logger LOG = LoggerFactory.getLogger(OlAlmantinoMiloResource.class);

    private static final String ENTITY_NAME = "olAlmantinoMilo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OlAlmantinoMiloService olAlmantinoMiloService;

    private final OlAlmantinoMiloRepository olAlmantinoMiloRepository;

    private final OlAlmantinoMiloQueryService olAlmantinoMiloQueryService;

    public OlAlmantinoMiloResource(
        OlAlmantinoMiloService olAlmantinoMiloService,
        OlAlmantinoMiloRepository olAlmantinoMiloRepository,
        OlAlmantinoMiloQueryService olAlmantinoMiloQueryService
    ) {
        this.olAlmantinoMiloService = olAlmantinoMiloService;
        this.olAlmantinoMiloRepository = olAlmantinoMiloRepository;
        this.olAlmantinoMiloQueryService = olAlmantinoMiloQueryService;
    }

    /**
     * {@code POST  /ol-almantino-milos} : Create a new olAlmantinoMilo.
     *
     * @param olAlmantinoMiloDTO the olAlmantinoMiloDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new olAlmantinoMiloDTO, or with status {@code 400 (Bad Request)} if the olAlmantinoMilo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OlAlmantinoMiloDTO> createOlAlmantinoMilo(@Valid @RequestBody OlAlmantinoMiloDTO olAlmantinoMiloDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OlAlmantinoMilo : {}", olAlmantinoMiloDTO);
        if (olAlmantinoMiloDTO.getId() != null) {
            throw new BadRequestAlertException("A new olAlmantinoMilo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        olAlmantinoMiloDTO = olAlmantinoMiloService.save(olAlmantinoMiloDTO);
        return ResponseEntity.created(new URI("/api/ol-almantino-milos/" + olAlmantinoMiloDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, olAlmantinoMiloDTO.getId().toString()))
            .body(olAlmantinoMiloDTO);
    }

    /**
     * {@code PUT  /ol-almantino-milos/:id} : Updates an existing olAlmantinoMilo.
     *
     * @param id the id of the olAlmantinoMiloDTO to save.
     * @param olAlmantinoMiloDTO the olAlmantinoMiloDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated olAlmantinoMiloDTO,
     * or with status {@code 400 (Bad Request)} if the olAlmantinoMiloDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the olAlmantinoMiloDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OlAlmantinoMiloDTO> updateOlAlmantinoMilo(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody OlAlmantinoMiloDTO olAlmantinoMiloDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OlAlmantinoMilo : {}, {}", id, olAlmantinoMiloDTO);
        if (olAlmantinoMiloDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, olAlmantinoMiloDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!olAlmantinoMiloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        olAlmantinoMiloDTO = olAlmantinoMiloService.update(olAlmantinoMiloDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, olAlmantinoMiloDTO.getId().toString()))
            .body(olAlmantinoMiloDTO);
    }

    /**
     * {@code PATCH  /ol-almantino-milos/:id} : Partial updates given fields of an existing olAlmantinoMilo, field will ignore if it is null
     *
     * @param id the id of the olAlmantinoMiloDTO to save.
     * @param olAlmantinoMiloDTO the olAlmantinoMiloDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated olAlmantinoMiloDTO,
     * or with status {@code 400 (Bad Request)} if the olAlmantinoMiloDTO is not valid,
     * or with status {@code 404 (Not Found)} if the olAlmantinoMiloDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the olAlmantinoMiloDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OlAlmantinoMiloDTO> partialUpdateOlAlmantinoMilo(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody OlAlmantinoMiloDTO olAlmantinoMiloDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OlAlmantinoMilo partially : {}, {}", id, olAlmantinoMiloDTO);
        if (olAlmantinoMiloDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, olAlmantinoMiloDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!olAlmantinoMiloRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OlAlmantinoMiloDTO> result = olAlmantinoMiloService.partialUpdate(olAlmantinoMiloDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, olAlmantinoMiloDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ol-almantino-milos} : get all the olAlmantinoMilos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of olAlmantinoMilos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OlAlmantinoMiloDTO>> getAllOlAlmantinoMilos(
        OlAlmantinoMiloCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OlAlmantinoMilos by criteria: {}", criteria);

        Page<OlAlmantinoMiloDTO> page = olAlmantinoMiloQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ol-almantino-milos/count} : count all the olAlmantinoMilos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOlAlmantinoMilos(OlAlmantinoMiloCriteria criteria) {
        LOG.debug("REST request to count OlAlmantinoMilos by criteria: {}", criteria);
        return ResponseEntity.ok().body(olAlmantinoMiloQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ol-almantino-milos/:id} : get the "id" olAlmantinoMilo.
     *
     * @param id the id of the olAlmantinoMiloDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the olAlmantinoMiloDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OlAlmantinoMiloDTO> getOlAlmantinoMilo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get OlAlmantinoMilo : {}", id);
        Optional<OlAlmantinoMiloDTO> olAlmantinoMiloDTO = olAlmantinoMiloService.findOne(id);
        return ResponseUtil.wrapOrNotFound(olAlmantinoMiloDTO);
    }

    /**
     * {@code DELETE  /ol-almantino-milos/:id} : delete the "id" olAlmantinoMilo.
     *
     * @param id the id of the olAlmantinoMiloDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOlAlmantinoMilo(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete OlAlmantinoMilo : {}", id);
        olAlmantinoMiloService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
