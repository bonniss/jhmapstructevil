package ai.realworld.web.rest;

import ai.realworld.repository.AlAppleRepository;
import ai.realworld.service.AlAppleQueryService;
import ai.realworld.service.AlAppleService;
import ai.realworld.service.criteria.AlAppleCriteria;
import ai.realworld.service.dto.AlAppleDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlApple}.
 */
@RestController
@RequestMapping("/api/al-apples")
public class AlAppleResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlAppleResource.class);

    private static final String ENTITY_NAME = "alApple";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlAppleService alAppleService;

    private final AlAppleRepository alAppleRepository;

    private final AlAppleQueryService alAppleQueryService;

    public AlAppleResource(AlAppleService alAppleService, AlAppleRepository alAppleRepository, AlAppleQueryService alAppleQueryService) {
        this.alAppleService = alAppleService;
        this.alAppleRepository = alAppleRepository;
        this.alAppleQueryService = alAppleQueryService;
    }

    /**
     * {@code POST  /al-apples} : Create a new alApple.
     *
     * @param alAppleDTO the alAppleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alAppleDTO, or with status {@code 400 (Bad Request)} if the alApple has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlAppleDTO> createAlApple(@Valid @RequestBody AlAppleDTO alAppleDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlApple : {}", alAppleDTO);
        if (alAppleDTO.getId() != null) {
            throw new BadRequestAlertException("A new alApple cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alAppleDTO = alAppleService.save(alAppleDTO);
        return ResponseEntity.created(new URI("/api/al-apples/" + alAppleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alAppleDTO.getId().toString()))
            .body(alAppleDTO);
    }

    /**
     * {@code PUT  /al-apples/:id} : Updates an existing alApple.
     *
     * @param id the id of the alAppleDTO to save.
     * @param alAppleDTO the alAppleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAppleDTO,
     * or with status {@code 400 (Bad Request)} if the alAppleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alAppleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlAppleDTO> updateAlApple(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlAppleDTO alAppleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlApple : {}, {}", id, alAppleDTO);
        if (alAppleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAppleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alAppleDTO = alAppleService.update(alAppleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAppleDTO.getId().toString()))
            .body(alAppleDTO);
    }

    /**
     * {@code PATCH  /al-apples/:id} : Partial updates given fields of an existing alApple, field will ignore if it is null
     *
     * @param id the id of the alAppleDTO to save.
     * @param alAppleDTO the alAppleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAppleDTO,
     * or with status {@code 400 (Bad Request)} if the alAppleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alAppleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alAppleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlAppleDTO> partialUpdateAlApple(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlAppleDTO alAppleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlApple partially : {}, {}", id, alAppleDTO);
        if (alAppleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAppleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAppleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlAppleDTO> result = alAppleService.partialUpdate(alAppleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAppleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-apples} : get all the alApples.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alApples in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlAppleDTO>> getAllAlApples(
        AlAppleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlApples by criteria: {}", criteria);

        Page<AlAppleDTO> page = alAppleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-apples/count} : count all the alApples.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlApples(AlAppleCriteria criteria) {
        LOG.debug("REST request to count AlApples by criteria: {}", criteria);
        return ResponseEntity.ok().body(alAppleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-apples/:id} : get the "id" alApple.
     *
     * @param id the id of the alAppleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alAppleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlAppleDTO> getAlApple(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlApple : {}", id);
        Optional<AlAppleDTO> alAppleDTO = alAppleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alAppleDTO);
    }

    /**
     * {@code DELETE  /al-apples/:id} : delete the "id" alApple.
     *
     * @param id the id of the alAppleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlApple(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlApple : {}", id);
        alAppleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
