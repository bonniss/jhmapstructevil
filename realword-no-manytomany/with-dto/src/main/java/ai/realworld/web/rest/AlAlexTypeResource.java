package ai.realworld.web.rest;

import ai.realworld.repository.AlAlexTypeRepository;
import ai.realworld.service.AlAlexTypeQueryService;
import ai.realworld.service.AlAlexTypeService;
import ai.realworld.service.criteria.AlAlexTypeCriteria;
import ai.realworld.service.dto.AlAlexTypeDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AlAlexType}.
 */
@RestController
@RequestMapping("/api/al-alex-types")
public class AlAlexTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(AlAlexTypeResource.class);

    private static final String ENTITY_NAME = "alAlexType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlAlexTypeService alAlexTypeService;

    private final AlAlexTypeRepository alAlexTypeRepository;

    private final AlAlexTypeQueryService alAlexTypeQueryService;

    public AlAlexTypeResource(
        AlAlexTypeService alAlexTypeService,
        AlAlexTypeRepository alAlexTypeRepository,
        AlAlexTypeQueryService alAlexTypeQueryService
    ) {
        this.alAlexTypeService = alAlexTypeService;
        this.alAlexTypeRepository = alAlexTypeRepository;
        this.alAlexTypeQueryService = alAlexTypeQueryService;
    }

    /**
     * {@code POST  /al-alex-types} : Create a new alAlexType.
     *
     * @param alAlexTypeDTO the alAlexTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new alAlexTypeDTO, or with status {@code 400 (Bad Request)} if the alAlexType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AlAlexTypeDTO> createAlAlexType(@Valid @RequestBody AlAlexTypeDTO alAlexTypeDTO) throws URISyntaxException {
        LOG.debug("REST request to save AlAlexType : {}", alAlexTypeDTO);
        if (alAlexTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new alAlexType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        alAlexTypeDTO = alAlexTypeService.save(alAlexTypeDTO);
        return ResponseEntity.created(new URI("/api/al-alex-types/" + alAlexTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, alAlexTypeDTO.getId().toString()))
            .body(alAlexTypeDTO);
    }

    /**
     * {@code PUT  /al-alex-types/:id} : Updates an existing alAlexType.
     *
     * @param id the id of the alAlexTypeDTO to save.
     * @param alAlexTypeDTO the alAlexTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAlexTypeDTO,
     * or with status {@code 400 (Bad Request)} if the alAlexTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the alAlexTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AlAlexTypeDTO> updateAlAlexType(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody AlAlexTypeDTO alAlexTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AlAlexType : {}, {}", id, alAlexTypeDTO);
        if (alAlexTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAlexTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAlexTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        alAlexTypeDTO = alAlexTypeService.update(alAlexTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAlexTypeDTO.getId().toString()))
            .body(alAlexTypeDTO);
    }

    /**
     * {@code PATCH  /al-alex-types/:id} : Partial updates given fields of an existing alAlexType, field will ignore if it is null
     *
     * @param id the id of the alAlexTypeDTO to save.
     * @param alAlexTypeDTO the alAlexTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated alAlexTypeDTO,
     * or with status {@code 400 (Bad Request)} if the alAlexTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the alAlexTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the alAlexTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlAlexTypeDTO> partialUpdateAlAlexType(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody AlAlexTypeDTO alAlexTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AlAlexType partially : {}, {}", id, alAlexTypeDTO);
        if (alAlexTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, alAlexTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!alAlexTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlAlexTypeDTO> result = alAlexTypeService.partialUpdate(alAlexTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, alAlexTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /al-alex-types} : get all the alAlexTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of alAlexTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AlAlexTypeDTO>> getAllAlAlexTypes(
        AlAlexTypeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AlAlexTypes by criteria: {}", criteria);

        Page<AlAlexTypeDTO> page = alAlexTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /al-alex-types/count} : count all the alAlexTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAlAlexTypes(AlAlexTypeCriteria criteria) {
        LOG.debug("REST request to count AlAlexTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(alAlexTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /al-alex-types/:id} : get the "id" alAlexType.
     *
     * @param id the id of the alAlexTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the alAlexTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AlAlexTypeDTO> getAlAlexType(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get AlAlexType : {}", id);
        Optional<AlAlexTypeDTO> alAlexTypeDTO = alAlexTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(alAlexTypeDTO);
    }

    /**
     * {@code DELETE  /al-alex-types/:id} : delete the "id" alAlexType.
     *
     * @param id the id of the alAlexTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlAlexType(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete AlAlexType : {}", id);
        alAlexTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
