package ai.realworld.web.rest;

import ai.realworld.repository.AntonioBanderasRepository;
import ai.realworld.service.AntonioBanderasQueryService;
import ai.realworld.service.AntonioBanderasService;
import ai.realworld.service.criteria.AntonioBanderasCriteria;
import ai.realworld.service.dto.AntonioBanderasDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AntonioBanderas}.
 */
@RestController
@RequestMapping("/api/antonio-banderas")
public class AntonioBanderasResource {

    private static final Logger LOG = LoggerFactory.getLogger(AntonioBanderasResource.class);

    private static final String ENTITY_NAME = "antonioBanderas";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AntonioBanderasService antonioBanderasService;

    private final AntonioBanderasRepository antonioBanderasRepository;

    private final AntonioBanderasQueryService antonioBanderasQueryService;

    public AntonioBanderasResource(
        AntonioBanderasService antonioBanderasService,
        AntonioBanderasRepository antonioBanderasRepository,
        AntonioBanderasQueryService antonioBanderasQueryService
    ) {
        this.antonioBanderasService = antonioBanderasService;
        this.antonioBanderasRepository = antonioBanderasRepository;
        this.antonioBanderasQueryService = antonioBanderasQueryService;
    }

    /**
     * {@code POST  /antonio-banderas} : Create a new antonioBanderas.
     *
     * @param antonioBanderasDTO the antonioBanderasDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new antonioBanderasDTO, or with status {@code 400 (Bad Request)} if the antonioBanderas has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AntonioBanderasDTO> createAntonioBanderas(@Valid @RequestBody AntonioBanderasDTO antonioBanderasDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AntonioBanderas : {}", antonioBanderasDTO);
        if (antonioBanderasDTO.getId() != null) {
            throw new BadRequestAlertException("A new antonioBanderas cannot already have an ID", ENTITY_NAME, "idexists");
        }
        antonioBanderasDTO = antonioBanderasService.save(antonioBanderasDTO);
        return ResponseEntity.created(new URI("/api/antonio-banderas/" + antonioBanderasDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, antonioBanderasDTO.getId().toString()))
            .body(antonioBanderasDTO);
    }

    /**
     * {@code PUT  /antonio-banderas/:id} : Updates an existing antonioBanderas.
     *
     * @param id the id of the antonioBanderasDTO to save.
     * @param antonioBanderasDTO the antonioBanderasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antonioBanderasDTO,
     * or with status {@code 400 (Bad Request)} if the antonioBanderasDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the antonioBanderasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AntonioBanderasDTO> updateAntonioBanderas(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AntonioBanderasDTO antonioBanderasDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AntonioBanderas : {}, {}", id, antonioBanderasDTO);
        if (antonioBanderasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antonioBanderasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antonioBanderasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        antonioBanderasDTO = antonioBanderasService.update(antonioBanderasDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, antonioBanderasDTO.getId().toString()))
            .body(antonioBanderasDTO);
    }

    /**
     * {@code PATCH  /antonio-banderas/:id} : Partial updates given fields of an existing antonioBanderas, field will ignore if it is null
     *
     * @param id the id of the antonioBanderasDTO to save.
     * @param antonioBanderasDTO the antonioBanderasDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated antonioBanderasDTO,
     * or with status {@code 400 (Bad Request)} if the antonioBanderasDTO is not valid,
     * or with status {@code 404 (Not Found)} if the antonioBanderasDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the antonioBanderasDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AntonioBanderasDTO> partialUpdateAntonioBanderas(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AntonioBanderasDTO antonioBanderasDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AntonioBanderas partially : {}, {}", id, antonioBanderasDTO);
        if (antonioBanderasDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, antonioBanderasDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!antonioBanderasRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AntonioBanderasDTO> result = antonioBanderasService.partialUpdate(antonioBanderasDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, antonioBanderasDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /antonio-banderas} : get all the antonioBanderas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of antonioBanderas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AntonioBanderasDTO>> getAllAntonioBanderas(
        AntonioBanderasCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AntonioBanderas by criteria: {}", criteria);

        Page<AntonioBanderasDTO> page = antonioBanderasQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /antonio-banderas/count} : count all the antonioBanderas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAntonioBanderas(AntonioBanderasCriteria criteria) {
        LOG.debug("REST request to count AntonioBanderas by criteria: {}", criteria);
        return ResponseEntity.ok().body(antonioBanderasQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /antonio-banderas/:id} : get the "id" antonioBanderas.
     *
     * @param id the id of the antonioBanderasDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the antonioBanderasDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AntonioBanderasDTO> getAntonioBanderas(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AntonioBanderas : {}", id);
        Optional<AntonioBanderasDTO> antonioBanderasDTO = antonioBanderasService.findOne(id);
        return ResponseUtil.wrapOrNotFound(antonioBanderasDTO);
    }

    /**
     * {@code DELETE  /antonio-banderas/:id} : delete the "id" antonioBanderas.
     *
     * @param id the id of the antonioBanderasDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAntonioBanderas(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AntonioBanderas : {}", id);
        antonioBanderasService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
