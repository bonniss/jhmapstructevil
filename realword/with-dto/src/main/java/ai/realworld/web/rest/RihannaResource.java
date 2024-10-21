package ai.realworld.web.rest;

import ai.realworld.repository.RihannaRepository;
import ai.realworld.service.RihannaQueryService;
import ai.realworld.service.RihannaService;
import ai.realworld.service.criteria.RihannaCriteria;
import ai.realworld.service.dto.RihannaDTO;
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
 * REST controller for managing {@link ai.realworld.domain.Rihanna}.
 */
@RestController
@RequestMapping("/api/rihannas")
public class RihannaResource {

    private static final Logger LOG = LoggerFactory.getLogger(RihannaResource.class);

    private static final String ENTITY_NAME = "rihanna";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RihannaService rihannaService;

    private final RihannaRepository rihannaRepository;

    private final RihannaQueryService rihannaQueryService;

    public RihannaResource(RihannaService rihannaService, RihannaRepository rihannaRepository, RihannaQueryService rihannaQueryService) {
        this.rihannaService = rihannaService;
        this.rihannaRepository = rihannaRepository;
        this.rihannaQueryService = rihannaQueryService;
    }

    /**
     * {@code POST  /rihannas} : Create a new rihanna.
     *
     * @param rihannaDTO the rihannaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rihannaDTO, or with status {@code 400 (Bad Request)} if the rihanna has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RihannaDTO> createRihanna(@Valid @RequestBody RihannaDTO rihannaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Rihanna : {}", rihannaDTO);
        if (rihannaDTO.getId() != null) {
            throw new BadRequestAlertException("A new rihanna cannot already have an ID", ENTITY_NAME, "idexists");
        }
        rihannaDTO = rihannaService.save(rihannaDTO);
        return ResponseEntity.created(new URI("/api/rihannas/" + rihannaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, rihannaDTO.getId().toString()))
            .body(rihannaDTO);
    }

    /**
     * {@code PUT  /rihannas/:id} : Updates an existing rihanna.
     *
     * @param id the id of the rihannaDTO to save.
     * @param rihannaDTO the rihannaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rihannaDTO,
     * or with status {@code 400 (Bad Request)} if the rihannaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rihannaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RihannaDTO> updateRihanna(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RihannaDTO rihannaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Rihanna : {}, {}", id, rihannaDTO);
        if (rihannaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rihannaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rihannaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        rihannaDTO = rihannaService.update(rihannaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rihannaDTO.getId().toString()))
            .body(rihannaDTO);
    }

    /**
     * {@code PATCH  /rihannas/:id} : Partial updates given fields of an existing rihanna, field will ignore if it is null
     *
     * @param id the id of the rihannaDTO to save.
     * @param rihannaDTO the rihannaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rihannaDTO,
     * or with status {@code 400 (Bad Request)} if the rihannaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rihannaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rihannaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RihannaDTO> partialUpdateRihanna(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RihannaDTO rihannaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Rihanna partially : {}, {}", id, rihannaDTO);
        if (rihannaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rihannaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rihannaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RihannaDTO> result = rihannaService.partialUpdate(rihannaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rihannaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rihannas} : get all the rihannas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rihannas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RihannaDTO>> getAllRihannas(
        RihannaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Rihannas by criteria: {}", criteria);

        Page<RihannaDTO> page = rihannaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rihannas/count} : count all the rihannas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRihannas(RihannaCriteria criteria) {
        LOG.debug("REST request to count Rihannas by criteria: {}", criteria);
        return ResponseEntity.ok().body(rihannaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rihannas/:id} : get the "id" rihanna.
     *
     * @param id the id of the rihannaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rihannaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RihannaDTO> getRihanna(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Rihanna : {}", id);
        Optional<RihannaDTO> rihannaDTO = rihannaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rihannaDTO);
    }

    /**
     * {@code DELETE  /rihannas/:id} : delete the "id" rihanna.
     *
     * @param id the id of the rihannaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRihanna(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Rihanna : {}", id);
        rihannaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
