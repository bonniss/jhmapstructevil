package ai.realworld.web.rest;

import ai.realworld.repository.AntonioBanderasViRepository;
import ai.realworld.service.AntonioBanderasViQueryService;
import ai.realworld.service.AntonioBanderasViService;
import ai.realworld.service.criteria.AntonioBanderasViCriteria;
import ai.realworld.service.dto.AntonioBanderasViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AntonioBanderasVi}.
 */
@RestController
@RequestMapping("/api/antonio-banderas-vis")
public class AntonioBanderasViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasViResource.class);

    private static final String ENTITY_NAME = "antonioBanderasVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AntonioBanderasViService antonioBanderasViService;

    private final AntonioBanderasViRepository antonioBanderasViRepository;

    private final AntonioBanderasViQueryService antonioBanderasViQueryService;

    public AntonioBanderasViResource(
        AntonioBanderasViService antonioBanderasViService,
        AntonioBanderasViRepository antonioBanderasViRepository,
        AntonioBanderasViQueryService antonioBanderasViQueryService
    ) {
        this.antonioBanderasViService = antonioBanderasViService;
        this.antonioBanderasViRepository = antonioBanderasViRepository;
        this.antonioBanderasViQueryService = antonioBanderasViQueryService;
    }

    /**
     * {@code POST  /antonio-banderas-vis} : Create a new antonioBanderasVi.
     *
     * @param antonioBanderasViDTO the antonioBanderasViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new antonioBanderasViDTO, or with status {@code 400 (Bad Request)} if the antonioBanderasVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AntonioBanderasViDTO> createAntonioBanderasVi(@Valid @RequestBody AntonioBanderasViDTO antonioBanderasViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AntonioBanderasVi : {}", antonioBanderasViDTO);
        if (antonioBanderasViDTO.getId() != null) {
            throw new BadRequestAlertException("A new antonioBanderasVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        antonioBanderasViDTO = antonioBanderasViService.save(antonioBanderasViDTO);
        return ResponseEntity.created(new URI("/api/antonio-banderas-vis/" + antonioBanderasViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, antonioBanderasViDTO.getId().toString()))
            .body(antonioBanderasViDTO);
    }

    /**
     * {@code PUT  /antonio-banderas-vis/:id} : Updates an existing antonioBanderasVi.
     *
     * @param id the id of the antonioBanderasViDTO to save.
     * @param antonioBanderasViDTO the antonioBanderasViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antonioBanderasViDTO,
     * or with status {@code 400 (Bad Request)} if the antonioBanderasViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the antonioBanderasViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AntonioBanderasViDTO> updateAntonioBanderasVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AntonioBanderasViDTO antonioBanderasViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AntonioBanderasVi : {}, {}", id, antonioBanderasViDTO);
        if (antonioBanderasViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antonioBanderasViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antonioBanderasViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        antonioBanderasViDTO = antonioBanderasViService.update(antonioBanderasViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, antonioBanderasViDTO.getId().toString()))
            .body(antonioBanderasViDTO);
    }

    /**
     * {@code PATCH  /antonio-banderas-vis/:id} : Partial updates given fields of an existing antonioBanderasVi, field will ignore if it is null
     *
     * @param id the id of the antonioBanderasViDTO to save.
     * @param antonioBanderasViDTO the antonioBanderasViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antonioBanderasViDTO,
     * or with status {@code 400 (Bad Request)} if the antonioBanderasViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the antonioBanderasViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the antonioBanderasViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AntonioBanderasViDTO> partialUpdateAntonioBanderasVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AntonioBanderasViDTO antonioBanderasViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AntonioBanderasVi partially : {}, {}", id, antonioBanderasViDTO);
        if (antonioBanderasViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antonioBanderasViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antonioBanderasViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AntonioBanderasViDTO> result = antonioBanderasViService.partialUpdate(antonioBanderasViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, antonioBanderasViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /antonio-banderas-vis} : get all the antonioBanderasVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of antonioBanderasVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AntonioBanderasViDTO>> getAllAntonioBanderasVis(
        AntonioBanderasViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AntonioBanderasVis by criteria: {}", criteria);

        Page<AntonioBanderasViDTO> page = antonioBanderasViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /antonio-banderas-vis/count} : count all the antonioBanderasVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAntonioBanderasVis(AntonioBanderasViCriteria criteria) {
        LOG.debug("REST request to count AntonioBanderasVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(antonioBanderasViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /antonio-banderas-vis/:id} : get the "id" antonioBanderasVi.
     *
     * @param id the id of the antonioBanderasViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the antonioBanderasViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AntonioBanderasViDTO> getAntonioBanderasVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AntonioBanderasVi : {}", id);
        Optional<AntonioBanderasViDTO> antonioBanderasViDTO = antonioBanderasViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(antonioBanderasViDTO);
    }

    /**
     * {@code DELETE  /antonio-banderas-vis/:id} : delete the "id" antonioBanderasVi.
     *
     * @param id the id of the antonioBanderasViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAntonioBanderasVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AntonioBanderasVi : {}", id);
        antonioBanderasViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
