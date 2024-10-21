package ai.realworld.web.rest;

import ai.realworld.repository.AlDesireRepository;
import ai.realworld.service.AlDesireQueryService;
import ai.realworld.service.AlDesireService;
import ai.realworld.service.criteria.AlDesireCriteria;
import ai.realworld.service.dto.AlDesireDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlDesire}.
 */
@RestController
@RequestMapping("/api/al-desires")
public class AlDesireResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlDesireResource.class);

    private static final String ENTITY_NAME = "alDesire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlDesireService alDesireService;

    private final AlDesireRepository alDesireRepository;

    private final AlDesireQueryService alDesireQueryService;

    public AlDesireResource(
        AlDesireService alDesireService,
        AlDesireRepository alDesireRepository,
        AlDesireQueryService alDesireQueryService
    ) {
        this.alDesireService = alDesireService;
        this.alDesireRepository = alDesireRepository;
        this.alDesireQueryService = alDesireQueryService;
    }

    /**
     * {@code POST  /al-desires} : Create a new alDesire.
     *
     * @param alDesireDTO the alDesireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alDesireDTO, or with status {@code 400 (Bad Request)} if the alDesire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlDesireDTO> createAlDesire(@Valid @RequestBody AlDesireDTO alDesireDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlDesire : {}", alDesireDTO);
        if (alDesireDTO.getId() != null) {
            throw new BadRequestAlertException("A new alDesire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alDesireDTO = alDesireService.save(alDesireDTO);
        return ResponseEntity.created(new URI("/api/al-desires/" + alDesireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alDesireDTO.getId().toString()))
            .body(alDesireDTO);
    }

    /**
     * {@code PUT  /al-desires/:id} : Updates an existing alDesire.
     *
     * @param id the id of the alDesireDTO to save.
     * @param alDesireDTO the alDesireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alDesireDTO,
     * or with status {@code 400 (Bad Request)} if the alDesireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alDesireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlDesireDTO> updateAlDesire(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlDesireDTO alDesireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlDesire : {}, {}", id, alDesireDTO);
        if (alDesireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alDesireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alDesireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alDesireDTO = alDesireService.update(alDesireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alDesireDTO.getId().toString()))
            .body(alDesireDTO);
    }

    /**
     * {@code PATCH  /al-desires/:id} : Partial updates given fields of an existing alDesire, field will ignore if it is null
     *
     * @param id the id of the alDesireDTO to save.
     * @param alDesireDTO the alDesireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alDesireDTO,
     * or with status {@code 400 (Bad Request)} if the alDesireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alDesireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alDesireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlDesireDTO> partialUpdateAlDesire(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlDesireDTO alDesireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlDesire partially : {}, {}", id, alDesireDTO);
        if (alDesireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alDesireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alDesireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlDesireDTO> result = alDesireService.partialUpdate(alDesireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alDesireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-desires} : get all the alDesires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alDesires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlDesireDTO>> getAllAlDesires(
        AlDesireCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlDesires by criteria: {}", criteria);

        Page<AlDesireDTO> page = alDesireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-desires/count} : count all the alDesires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlDesires(AlDesireCriteria criteria) {
        LOG.debug("REST request to count AlDesires by criteria: {}", criteria);
        return ResponseEntity.ok().body(alDesireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-desires/:id} : get the "id" alDesire.
     *
     * @param id the id of the alDesireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alDesireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlDesireDTO> getAlDesire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlDesire : {}", id);
        Optional<AlDesireDTO> alDesireDTO = alDesireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alDesireDTO);
    }

    /**
     * {@code DELETE  /al-desires/:id} : delete the "id" alDesire.
     *
     * @param id the id of the alDesireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlDesire(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlDesire : {}", id);
        alDesireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
