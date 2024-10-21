package ai.realworld.web.rest;

import ai.realworld.repository.AlLexFergRepository;
import ai.realworld.service.AlLexFergQueryService;
import ai.realworld.service.AlLexFergService;
import ai.realworld.service.criteria.AlLexFergCriteria;
import ai.realworld.service.dto.AlLexFergDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlLexFerg}.
 */
@RestController
@RequestMapping("/api/al-lex-fergs")
public class AlLexFergResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLexFergResource.class);

    private static final String ENTITY_NAME = "alLexFerg";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLexFergService alLexFergService;

    private final AlLexFergRepository alLexFergRepository;

    private final AlLexFergQueryService alLexFergQueryService;

    public AlLexFergResource(
        AlLexFergService alLexFergService,
        AlLexFergRepository alLexFergRepository,
        AlLexFergQueryService alLexFergQueryService
    ) {
        this.alLexFergService = alLexFergService;
        this.alLexFergRepository = alLexFergRepository;
        this.alLexFergQueryService = alLexFergQueryService;
    }

    /**
     * {@code POST  /al-lex-fergs} : Create a new alLexFerg.
     *
     * @param alLexFergDTO the alLexFergDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLexFergDTO, or with status {@code 400 (Bad Request)} if the alLexFerg has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLexFergDTO> createAlLexFerg(@Valid @RequestBody AlLexFergDTO alLexFergDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlLexFerg : {}", alLexFergDTO);
        if (alLexFergDTO.getId() != null) {
            throw new BadRequestAlertException("A new alLexFerg cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLexFergDTO = alLexFergService.save(alLexFergDTO);
        return ResponseEntity.created(new URI("/api/al-lex-fergs/" + alLexFergDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLexFergDTO.getId().toString()))
            .body(alLexFergDTO);
    }

    /**
     * {@code PUT  /al-lex-fergs/:id} : Updates an existing alLexFerg.
     *
     * @param id the id of the alLexFergDTO to save.
     * @param alLexFergDTO the alLexFergDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLexFergDTO,
     * or with status {@code 400 (Bad Request)} if the alLexFergDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLexFergDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLexFergDTO> updateAlLexFerg(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlLexFergDTO alLexFergDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLexFerg : {}, {}", id, alLexFergDTO);
        if (alLexFergDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLexFergDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLexFergRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLexFergDTO = alLexFergService.update(alLexFergDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLexFergDTO.getId().toString()))
            .body(alLexFergDTO);
    }

    /**
     * {@code PATCH  /al-lex-fergs/:id} : Partial updates given fields of an existing alLexFerg, field will ignore if it is null
     *
     * @param id the id of the alLexFergDTO to save.
     * @param alLexFergDTO the alLexFergDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLexFergDTO,
     * or with status {@code 400 (Bad Request)} if the alLexFergDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alLexFergDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLexFergDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLexFergDTO> partialUpdateAlLexFerg(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlLexFergDTO alLexFergDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLexFerg partially : {}, {}", id, alLexFergDTO);
        if (alLexFergDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLexFergDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLexFergRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLexFergDTO> result = alLexFergService.partialUpdate(alLexFergDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLexFergDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-lex-fergs} : get all the alLexFergs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLexFergs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLexFergDTO>> getAllAlLexFergs(
        AlLexFergCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLexFergs by criteria: {}", criteria);

        Page<AlLexFergDTO> page = alLexFergQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-lex-fergs/count} : count all the alLexFergs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLexFergs(AlLexFergCriteria criteria) {
        LOG.debug("REST request to count AlLexFergs by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLexFergQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-lex-fergs/:id} : get the "id" alLexFerg.
     *
     * @param id the id of the alLexFergDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLexFergDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLexFergDTO> getAlLexFerg(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlLexFerg : {}", id);
        Optional<AlLexFergDTO> alLexFergDTO = alLexFergService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLexFergDTO);
    }

    /**
     * {@code DELETE  /al-lex-fergs/:id} : delete the "id" alLexFerg.
     *
     * @param id the id of the alLexFergDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLexFerg(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlLexFerg : {}", id);
        alLexFergService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}