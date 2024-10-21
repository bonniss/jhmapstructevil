package ai.realworld.web.rest;

import ai.realworld.repository.AlLadyGagaRepository;
import ai.realworld.service.AlLadyGagaQueryService;
import ai.realworld.service.AlLadyGagaService;
import ai.realworld.service.criteria.AlLadyGagaCriteria;
import ai.realworld.service.dto.AlLadyGagaDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlLadyGaga}.
 */
@RestController
@RequestMapping("/api/al-lady-gagas")
public class AlLadyGagaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlLadyGagaResource.class);

    private static final String ENTITY_NAME = "alLadyGaga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlLadyGagaService alLadyGagaService;

    private final AlLadyGagaRepository alLadyGagaRepository;

    private final AlLadyGagaQueryService alLadyGagaQueryService;

    public AlLadyGagaResource(
        AlLadyGagaService alLadyGagaService,
        AlLadyGagaRepository alLadyGagaRepository,
        AlLadyGagaQueryService alLadyGagaQueryService
    ) {
        this.alLadyGagaService = alLadyGagaService;
        this.alLadyGagaRepository = alLadyGagaRepository;
        this.alLadyGagaQueryService = alLadyGagaQueryService;
    }

    /**
     * {@code POST  /al-lady-gagas} : Create a new alLadyGaga.
     *
     * @param alLadyGagaDTO the alLadyGagaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alLadyGagaDTO, or with status {@code 400 (Bad Request)} if the alLadyGaga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlLadyGagaDTO> createAlLadyGaga(@Valid @RequestBody AlLadyGagaDTO alLadyGagaDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlLadyGaga : {}", alLadyGagaDTO);
        if (alLadyGagaDTO.getId() != null) {
            throw new BadRequestAlertException("A new alLadyGaga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alLadyGagaDTO = alLadyGagaService.save(alLadyGagaDTO);
        return ResponseEntity.created(new URI("/api/al-lady-gagas/" + alLadyGagaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alLadyGagaDTO.getId().toString()))
            .body(alLadyGagaDTO);
    }

    /**
     * {@code PUT  /al-lady-gagas/:id} : Updates an existing alLadyGaga.
     *
     * @param id the id of the alLadyGagaDTO to save.
     * @param alLadyGagaDTO the alLadyGagaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGagaDTO,
     * or with status {@code 400 (Bad Request)} if the alLadyGagaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGagaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlLadyGagaDTO> updateAlLadyGaga(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlLadyGagaDTO alLadyGagaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlLadyGaga : {}, {}", id, alLadyGagaDTO);
        if (alLadyGagaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGagaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alLadyGagaDTO = alLadyGagaService.update(alLadyGagaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGagaDTO.getId().toString()))
            .body(alLadyGagaDTO);
    }

    /**
     * {@code PATCH  /al-lady-gagas/:id} : Partial updates given fields of an existing alLadyGaga, field will ignore if it is null
     *
     * @param id the id of the alLadyGagaDTO to save.
     * @param alLadyGagaDTO the alLadyGagaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alLadyGagaDTO,
     * or with status {@code 400 (Bad Request)} if the alLadyGagaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alLadyGagaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alLadyGagaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlLadyGagaDTO> partialUpdateAlLadyGaga(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlLadyGagaDTO alLadyGagaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlLadyGaga partially : {}, {}", id, alLadyGagaDTO);
        if (alLadyGagaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alLadyGagaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alLadyGagaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlLadyGagaDTO> result = alLadyGagaService.partialUpdate(alLadyGagaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alLadyGagaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-lady-gagas} : get all the alLadyGagas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alLadyGagas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlLadyGagaDTO>> getAllAlLadyGagas(
        AlLadyGagaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlLadyGagas by criteria: {}", criteria);

        Page<AlLadyGagaDTO> page = alLadyGagaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-lady-gagas/count} : count all the alLadyGagas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlLadyGagas(AlLadyGagaCriteria criteria) {
        LOG.debug("REST request to count AlLadyGagas by criteria: {}", criteria);
        return ResponseEntity.ok().body(alLadyGagaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-lady-gagas/:id} : get the "id" alLadyGaga.
     *
     * @param id the id of the alLadyGagaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alLadyGagaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlLadyGagaDTO> getAlLadyGaga(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlLadyGaga : {}", id);
        Optional<AlLadyGagaDTO> alLadyGagaDTO = alLadyGagaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alLadyGagaDTO);
    }

    /**
     * {@code DELETE  /al-lady-gagas/:id} : delete the "id" alLadyGaga.
     *
     * @param id the id of the alLadyGagaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlLadyGaga(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlLadyGaga : {}", id);
        alLadyGagaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
