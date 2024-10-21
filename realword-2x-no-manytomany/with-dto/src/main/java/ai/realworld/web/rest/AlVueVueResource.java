package ai.realworld.web.rest;

import ai.realworld.repository.AlVueVueRepository;
import ai.realworld.service.AlVueVueQueryService;
import ai.realworld.service.AlVueVueService;
import ai.realworld.service.criteria.AlVueVueCriteria;
import ai.realworld.service.dto.AlVueVueDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlVueVue}.
 */
@RestController
@RequestMapping("/api/al-vue-vues")
public class AlVueVueResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlVueVueResource.class);

    private static final String ENTITY_NAME = "alVueVue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlVueVueService alVueVueService;

    private final AlVueVueRepository alVueVueRepository;

    private final AlVueVueQueryService alVueVueQueryService;

    public AlVueVueResource(
        AlVueVueService alVueVueService,
        AlVueVueRepository alVueVueRepository,
        AlVueVueQueryService alVueVueQueryService
    ) {
        this.alVueVueService = alVueVueService;
        this.alVueVueRepository = alVueVueRepository;
        this.alVueVueQueryService = alVueVueQueryService;
    }

    /**
     * {@code POST  /al-vue-vues} : Create a new alVueVue.
     *
     * @param alVueVueDTO the alVueVueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alVueVueDTO, or with status {@code 400 (Bad Request)} if the alVueVue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlVueVueDTO> createAlVueVue(@Valid @RequestBody AlVueVueDTO alVueVueDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlVueVue : {}", alVueVueDTO);
        if (alVueVueDTO.getId() != null) {
            throw new BadRequestAlertException("A new alVueVue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alVueVueDTO = alVueVueService.save(alVueVueDTO);
        return ResponseEntity.created(new URI("/api/al-vue-vues/" + alVueVueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alVueVueDTO.getId().toString()))
            .body(alVueVueDTO);
    }

    /**
     * {@code PUT  /al-vue-vues/:id} : Updates an existing alVueVue.
     *
     * @param id the id of the alVueVueDTO to save.
     * @param alVueVueDTO the alVueVueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueDTO,
     * or with status {@code 400 (Bad Request)} if the alVueVueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlVueVueDTO> updateAlVueVue(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlVueVueDTO alVueVueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlVueVue : {}, {}", id, alVueVueDTO);
        if (alVueVueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alVueVueDTO = alVueVueService.update(alVueVueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueDTO.getId().toString()))
            .body(alVueVueDTO);
    }

    /**
     * {@code PATCH  /al-vue-vues/:id} : Partial updates given fields of an existing alVueVue, field will ignore if it is null
     *
     * @param id the id of the alVueVueDTO to save.
     * @param alVueVueDTO the alVueVueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alVueVueDTO,
     * or with status {@code 400 (Bad Request)} if the alVueVueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alVueVueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alVueVueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlVueVueDTO> partialUpdateAlVueVue(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlVueVueDTO alVueVueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlVueVue partially : {}, {}", id, alVueVueDTO);
        if (alVueVueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alVueVueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alVueVueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlVueVueDTO> result = alVueVueService.partialUpdate(alVueVueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alVueVueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-vue-vues} : get all the alVueVues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alVueVues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlVueVueDTO>> getAllAlVueVues(
        AlVueVueCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlVueVues by criteria: {}", criteria);

        Page<AlVueVueDTO> page = alVueVueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-vue-vues/count} : count all the alVueVues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlVueVues(AlVueVueCriteria criteria) {
        LOG.debug("REST request to count AlVueVues by criteria: {}", criteria);
        return ResponseEntity.ok().body(alVueVueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-vue-vues/:id} : get the "id" alVueVue.
     *
     * @param id the id of the alVueVueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alVueVueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlVueVueDTO> getAlVueVue(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlVueVue : {}", id);
        Optional<AlVueVueDTO> alVueVueDTO = alVueVueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alVueVueDTO);
    }

    /**
     * {@code DELETE  /al-vue-vues/:id} : delete the "id" alVueVue.
     *
     * @param id the id of the alVueVueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlVueVue(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlVueVue : {}", id);
        alVueVueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
