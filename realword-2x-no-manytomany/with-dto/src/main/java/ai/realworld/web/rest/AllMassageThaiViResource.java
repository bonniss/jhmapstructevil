package ai.realworld.web.rest;

import ai.realworld.repository.AllMassageThaiViRepository;
import ai.realworld.service.AllMassageThaiViQueryService;
import ai.realworld.service.AllMassageThaiViService;
import ai.realworld.service.criteria.AllMassageThaiViCriteria;
import ai.realworld.service.dto.AllMassageThaiViDTO;
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
 * REST controller for managing {@link ai.realworld.domain.AllMassageThaiVi}.
 */
@RestController
@RequestMapping("/api/all-massage-thai-vis")
public class AllMassageThaiViResource {

    private static final Logger LOG = LoggerFactory.getLogger(AllMassageThaiViResource.class);

    private static final String ENTITY_NAME = "allMassageThaiVi";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AllMassageThaiViService allMassageThaiViService;

    private final AllMassageThaiViRepository allMassageThaiViRepository;

    private final AllMassageThaiViQueryService allMassageThaiViQueryService;

    public AllMassageThaiViResource(
        AllMassageThaiViService allMassageThaiViService,
        AllMassageThaiViRepository allMassageThaiViRepository,
        AllMassageThaiViQueryService allMassageThaiViQueryService
    ) {
        this.allMassageThaiViService = allMassageThaiViService;
        this.allMassageThaiViRepository = allMassageThaiViRepository;
        this.allMassageThaiViQueryService = allMassageThaiViQueryService;
    }

    /**
     * {@code POST  /all-massage-thai-vis} : Create a new allMassageThaiVi.
     *
     * @param allMassageThaiViDTO the allMassageThaiViDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new allMassageThaiViDTO, or with status {@code 400 (Bad Request)} if the allMassageThaiVi has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AllMassageThaiViDTO> createAllMassageThaiVi(@Valid @RequestBody AllMassageThaiViDTO allMassageThaiViDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AllMassageThaiVi : {}", allMassageThaiViDTO);
        if (allMassageThaiViDTO.getId() != null) {
            throw new BadRequestAlertException("A new allMassageThaiVi cannot already have an ID", ENTITY_NAME, "idexists");
        }
        allMassageThaiViDTO = allMassageThaiViService.save(allMassageThaiViDTO);
        return ResponseEntity.created(new URI("/api/all-massage-thai-vis/" + allMassageThaiViDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, allMassageThaiViDTO.getId().toString()))
            .body(allMassageThaiViDTO);
    }

    /**
     * {@code PUT  /all-massage-thai-vis/:id} : Updates an existing allMassageThaiVi.
     *
     * @param id the id of the allMassageThaiViDTO to save.
     * @param allMassageThaiViDTO the allMassageThaiViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allMassageThaiViDTO,
     * or with status {@code 400 (Bad Request)} if the allMassageThaiViDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the allMassageThaiViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AllMassageThaiViDTO> updateAllMassageThaiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AllMassageThaiViDTO allMassageThaiViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AllMassageThaiVi : {}, {}", id, allMassageThaiViDTO);
        if (allMassageThaiViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allMassageThaiViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allMassageThaiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        allMassageThaiViDTO = allMassageThaiViService.update(allMassageThaiViDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allMassageThaiViDTO.getId().toString()))
            .body(allMassageThaiViDTO);
    }

    /**
     * {@code PATCH  /all-massage-thai-vis/:id} : Partial updates given fields of an existing allMassageThaiVi, field will ignore if it is null
     *
     * @param id the id of the allMassageThaiViDTO to save.
     * @param allMassageThaiViDTO the allMassageThaiViDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated allMassageThaiViDTO,
     * or with status {@code 400 (Bad Request)} if the allMassageThaiViDTO is not valid,
     * or with status {@code 404 (Not Found)} if the allMassageThaiViDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the allMassageThaiViDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AllMassageThaiViDTO> partialUpdateAllMassageThaiVi(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AllMassageThaiViDTO allMassageThaiViDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AllMassageThaiVi partially : {}, {}", id, allMassageThaiViDTO);
        if (allMassageThaiViDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, allMassageThaiViDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!allMassageThaiViRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AllMassageThaiViDTO> result = allMassageThaiViService.partialUpdate(allMassageThaiViDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, allMassageThaiViDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /all-massage-thai-vis} : get all the allMassageThaiVis.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of allMassageThaiVis in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AllMassageThaiViDTO>> getAllAllMassageThaiVis(
        AllMassageThaiViCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get AllMassageThaiVis by criteria: {}", criteria);

        Page<AllMassageThaiViDTO> page = allMassageThaiViQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /all-massage-thai-vis/count} : count all the allMassageThaiVis.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countAllMassageThaiVis(AllMassageThaiViCriteria criteria) {
        LOG.debug("REST request to count AllMassageThaiVis by criteria: {}", criteria);
        return ResponseEntity.ok().body(allMassageThaiViQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /all-massage-thai-vis/:id} : get the "id" allMassageThaiVi.
     *
     * @param id the id of the allMassageThaiViDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the allMassageThaiViDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AllMassageThaiViDTO> getAllMassageThaiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AllMassageThaiVi : {}", id);
        Optional<AllMassageThaiViDTO> allMassageThaiViDTO = allMassageThaiViService.findOne(id);
        return ResponseUtil.wrapOrNotFound(allMassageThaiViDTO);
    }

    /**
     * {@code DELETE  /all-massage-thai-vis/:id} : delete the "id" allMassageThaiVi.
     *
     * @param id the id of the allMassageThaiViDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAllMassageThaiVi(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AllMassageThaiVi : {}", id);
        allMassageThaiViService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
