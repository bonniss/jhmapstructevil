package ai.realworld.web.rest;

import ai.realworld.repository.AlCatalinaRepository;
import ai.realworld.service.AlCatalinaQueryService;
import ai.realworld.service.AlCatalinaService;
import ai.realworld.service.criteria.AlCatalinaCriteria;
import ai.realworld.service.dto.AlCatalinaDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlCatalina}.
 */
@RestController
@RequestMapping("/api/al-catalinas")
public class AlCatalinaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlCatalinaResource.class);

    private static final String ENTITY_NAME = "alCatalina";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlCatalinaService alCatalinaService;

    private final AlCatalinaRepository alCatalinaRepository;

    private final AlCatalinaQueryService alCatalinaQueryService;

    public AlCatalinaResource(
        AlCatalinaService alCatalinaService,
        AlCatalinaRepository alCatalinaRepository,
        AlCatalinaQueryService alCatalinaQueryService
    ) {
        this.alCatalinaService = alCatalinaService;
        this.alCatalinaRepository = alCatalinaRepository;
        this.alCatalinaQueryService = alCatalinaQueryService;
    }

    /**
     * {@code POST  /al-catalinas} : Create a new alCatalina.
     *
     * @param alCatalinaDTO the alCatalinaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alCatalinaDTO, or with status {@code 400 (Bad Request)} if the alCatalina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlCatalinaDTO> createAlCatalina(@Valid @RequestBody AlCatalinaDTO alCatalinaDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlCatalina : {}", alCatalinaDTO);
        if (alCatalinaDTO.getId() != null) {
            throw new BadRequestAlertException("A new alCatalina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alCatalinaDTO = alCatalinaService.save(alCatalinaDTO);
        return ResponseEntity.created(new URI("/api/al-catalinas/" + alCatalinaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alCatalinaDTO.getId().toString()))
            .body(alCatalinaDTO);
    }

    /**
     * {@code PUT  /al-catalinas/:id} : Updates an existing alCatalina.
     *
     * @param id the id of the alCatalinaDTO to save.
     * @param alCatalinaDTO the alCatalinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaDTO,
     * or with status {@code 400 (Bad Request)} if the alCatalinaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlCatalinaDTO> updateAlCatalina(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlCatalinaDTO alCatalinaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlCatalina : {}, {}", id, alCatalinaDTO);
        if (alCatalinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alCatalinaDTO = alCatalinaService.update(alCatalinaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaDTO.getId().toString()))
            .body(alCatalinaDTO);
    }

    /**
     * {@code PATCH  /al-catalinas/:id} : Partial updates given fields of an existing alCatalina, field will ignore if it is null
     *
     * @param id the id of the alCatalinaDTO to save.
     * @param alCatalinaDTO the alCatalinaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alCatalinaDTO,
     * or with status {@code 400 (Bad Request)} if the alCatalinaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alCatalinaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alCatalinaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlCatalinaDTO> partialUpdateAlCatalina(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlCatalinaDTO alCatalinaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlCatalina partially : {}, {}", id, alCatalinaDTO);
        if (alCatalinaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alCatalinaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alCatalinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlCatalinaDTO> result = alCatalinaService.partialUpdate(alCatalinaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alCatalinaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-catalinas} : get all the alCatalinas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alCatalinas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlCatalinaDTO>> getAllAlCatalinas(
        AlCatalinaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlCatalinas by criteria: {}", criteria);

        Page<AlCatalinaDTO> page = alCatalinaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-catalinas/count} : count all the alCatalinas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlCatalinas(AlCatalinaCriteria criteria) {
        LOG.debug("REST request to count AlCatalinas by criteria: {}", criteria);
        return ResponseEntity.ok().body(alCatalinaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-catalinas/:id} : get the "id" alCatalina.
     *
     * @param id the id of the alCatalinaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alCatalinaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlCatalinaDTO> getAlCatalina(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AlCatalina : {}", id);
        Optional<AlCatalinaDTO> alCatalinaDTO = alCatalinaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alCatalinaDTO);
    }

    /**
     * {@code DELETE  /al-catalinas/:id} : delete the "id" alCatalina.
     *
     * @param id the id of the alCatalinaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlCatalina(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AlCatalina : {}", id);
        alCatalinaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
