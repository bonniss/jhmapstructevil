package ai.realworld.web.rest;

import ai.realworld.repository.AlLexFergViRepository;
import ai.realworld.service.AlLexFergViQueryService;
import ai.realworld.service.AlLexFergViService;
import ai.realworld.service.criteria.AlLexFergViCriteria;
import ai.realworld.service.dto.AlLexFergViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlLexFergVi}.
 */
@RestController
@RequestMapping("/api/al-lex-ferg-vis")
public class AlLexFergViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergViResource.class);

    private static final String ENTITY_NAME = "alLexFergVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLexFergViService alLexFergViService;

    private final AlLexFergViRepository alLexFergViRepository;

    private final AlLexFergViQueryService alLexFergViQueryService;

    public AlLexFergViResource(
        AlLexFergViService alLexFergViService,
        AlLexFergViRepository alLexFergViRepository,
        AlLexFergViQueryService alLexFergViQueryService
    ) {
        this.alLexFergViService = alLexFergViService;
        this.alLexFergViRepository = alLexFergViRepository;
        this.alLexFergViQueryService = alLexFergViQueryService;
    }

    /**
     * {@code POST  /al-lex-ferg-vis} : Create a new alLexFergVi.
     *
     * @param alLexFergViDTO the alLexFergViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLexFergViDTO, or with status {@code 400 (Bad Request)} if the alLexFergVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLexFergViDTO> createAlLexFergVi(@Valid @RequestBody AlLexFergViDTO alLexFergViDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlLexFergVi : {}", alLexFergViDTO);
        if (alLexFergViDTO.getId() != null) {
            throw new BadRequestAlertException("A new alLexFergVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLexFergViDTO = alLexFergViService.save(alLexFergViDTO);
        return ResponseEntity.created(new URI("/api/al-lex-ferg-vis/" + alLexFergViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLexFergViDTO.getId().toString()))
            .body(alLexFergViDTO);
    }

    /**
     * {@code PUT  /al-lex-ferg-vis/:id} : Updates an existing alLexFergVi.
     *
     * @param id the id of the alLexFergViDTO to save.
     * @param alLexFergViDTO the alLexFergViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLexFergViDTO,
     * or with status {@code 400 (Bad Request)} if the alLexFergViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLexFergViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLexFergViDTO> updateAlLexFergVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlLexFergViDTO alLexFergViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLexFergVi : {}, {}", id, alLexFergViDTO);
        if (alLexFergViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLexFergViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLexFergViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLexFergViDTO = alLexFergViService.update(alLexFergViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLexFergViDTO.getId().toString()))
            .body(alLexFergViDTO);
    }

    /**
     * {@code PATCH  /al-lex-ferg-vis/:id} : Partial updates given fields of an existing alLexFergVi, field will ignore if it is null
     *
     * @param id the id of the alLexFergViDTO to save.
     * @param alLexFergViDTO the alLexFergViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLexFergViDTO,
     * or with status {@code 400 (Bad Request)} if the alLexFergViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alLexFergViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLexFergViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLexFergViDTO> partialUpdateAlLexFergVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlLexFergViDTO alLexFergViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLexFergVi partially : {}, {}", id, alLexFergViDTO);
        if (alLexFergViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLexFergViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLexFergViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLexFergViDTO> result = alLexFergViService.partialUpdate(alLexFergViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLexFergViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-lex-ferg-vis} : get all the alLexFergVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLexFergVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLexFergViDTO>> getAllAlLexFergVis(
        AlLexFergViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLexFergVis by criteria: {}", criteria);

        Page<AlLexFergViDTO> page = alLexFergViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-lex-ferg-vis/count} : count all the alLexFergVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLexFergVis(AlLexFergViCriteria criteria) {
        LOG.debug("REST request to count AlLexFergVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLexFergViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-lex-ferg-vis/:id} : get the "id" alLexFergVi.
     *
     * @param id the id of the alLexFergViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLexFergViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLexFergViDTO> getAlLexFergVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlLexFergVi : {}", id);
        Optional<AlLexFergViDTO> alLexFergViDTO = alLexFergViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLexFergViDTO);
    }

    /**
     * {@code DELETE  /al-lex-ferg-vis/:id} : delete the "id" alLexFergVi.
     *
     * @param id the id of the alLexFergViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLexFergVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlLexFergVi : {}", id);
        alLexFergViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
