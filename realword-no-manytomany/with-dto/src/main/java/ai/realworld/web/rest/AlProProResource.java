package ai.realworld.web.rest;

import ai.realworld.repository.AlProProRepository;
import ai.realworld.service.AlProProQueryService;
import ai.realworld.service.AlProProService;
import ai.realworld.service.criteria.AlProProCriteria;
import ai.realworld.service.dto.AlProProDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlProPro}.
 */
@RestController
@RequestMapping("/api/al-pro-pros")
public class AlProProResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlProProResource.class);

    private static final String ENTITY_NAME = "alProPro";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlProProService alProProService;

    private final AlProProRepository alProProRepository;

    private final AlProProQueryService alProProQueryService;

    public AlProProResource(
        AlProProService alProProService,
        AlProProRepository alProProRepository,
        AlProProQueryService alProProQueryService
    ) {
        this.alProProService = alProProService;
        this.alProProRepository = alProProRepository;
        this.alProProQueryService = alProProQueryService;
    }

    /**
     * {@code POST  /al-pro-pros} : Create a new alProPro.
     *
     * @param alProProDTO the alProProDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alProProDTO, or with status {@code 400 (Bad Request)} if the alProPro has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlProProDTO> createAlProPro(@Valid @RequestBody AlProProDTO alProProDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlProPro : {}", alProProDTO);
        if (alProProDTO.getId() != null) {
            throw new BadRequestAlertException("A new alProPro cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alProProDTO = alProProService.save(alProProDTO);
        return ResponseEntity.created(new URI("/api/al-pro-pros/" + alProProDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alProProDTO.getId().toString()))
            .body(alProProDTO);
    }

    /**
     * {@code PUT  /al-pro-pros/:id} : Updates an existing alProPro.
     *
     * @param id the id of the alProProDTO to save.
     * @param alProProDTO the alProProDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProDTO,
     * or with status {@code 400 (Bad Request)} if the alProProDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alProProDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlProProDTO> updateAlProPro(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlProProDTO alProProDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlProPro : {}, {}", id, alProProDTO);
        if (alProProDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alProProDTO = alProProService.update(alProProDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProDTO.getId().toString()))
            .body(alProProDTO);
    }

    /**
     * {@code PATCH  /al-pro-pros/:id} : Partial updates given fields of an existing alProPro, field will ignore if it is null
     *
     * @param id the id of the alProProDTO to save.
     * @param alProProDTO the alProProDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alProProDTO,
     * or with status {@code 400 (Bad Request)} if the alProProDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alProProDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alProProDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlProProDTO> partialUpdateAlProPro(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlProProDTO alProProDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlProPro partially : {}, {}", id, alProProDTO);
        if (alProProDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alProProDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alProProRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlProProDTO> result = alProProService.partialUpdate(alProProDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alProProDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-pro-pros} : get all the alProPros.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alProPros in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlProProDTO>> getAllAlProPros(
        AlProProCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlProPros by criteria: {}", criteria);

        Page<AlProProDTO> page = alProProQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-pro-pros/count} : count all the alProPros.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlProPros(AlProProCriteria criteria) {
        LOG.debug("REST request to count AlProPros by criteria: {}", criteria);
        return ResponseEntity.ok().body(alProProQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-pro-pros/:id} : get the "id" alProPro.
     *
     * @param id the id of the alProProDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alProProDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlProProDTO> getAlProPro(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlProPro : {}", id);
        Optional<AlProProDTO> alProProDTO = alProProService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alProProDTO);
    }

    /**
     * {@code DELETE  /al-pro-pros/:id} : delete the "id" alProPro.
     *
     * @param id the id of the alProProDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlProPro(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlProPro : {}", id);
        alProProService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
