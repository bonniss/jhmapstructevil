package ai.realworld.web.rest;

import ai.realworld.repository.JohnLennonRepository;
import ai.realworld.service.JohnLennonQueryService;
import ai.realworld.service.JohnLennonService;
import ai.realworld.service.criteria.JohnLennonCriteria;
import ai.realworld.service.dto.JohnLennonDTO;
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
 * REST controller for managing {@link ai.realworld.domain.JohnLennon}.
 */
@RestController
@RequestMapping("/api/john-lennons")
public class JohnLennonResource {

    private static final Logger LOG = LoggerFactory.getLogger(JohnLennonResource.class);

    private static final String ENTITY_NAME = "johnLennon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JohnLennonService johnLennonService;

    private final JohnLennonRepository johnLennonRepository;

    private final JohnLennonQueryService johnLennonQueryService;

    public JohnLennonResource(
        JohnLennonService johnLennonService,
        JohnLennonRepository johnLennonRepository,
        JohnLennonQueryService johnLennonQueryService
    ) {
        this.johnLennonService = johnLennonService;
        this.johnLennonRepository = johnLennonRepository;
        this.johnLennonQueryService = johnLennonQueryService;
    }

    /**
     * {@code POST  /john-lennons} : Create a new johnLennon.
     *
     * @param johnLennonDTO the johnLennonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new johnLennonDTO, or with status {@code 400 (Bad Request)} if the johnLennon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<JohnLennonDTO> createJohnLennon(@Valid @RequestBody JohnLennonDTO johnLennonDTO) throws URISyntaxException {
        LOG.debug("REST request to save JohnLennon : {}", johnLennonDTO);
        if (johnLennonDTO.getId() != null) {
            throw new BadRequestAlertException("A new johnLennon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        johnLennonDTO = johnLennonService.save(johnLennonDTO);
        return ResponseEntity.created(new URI("/api/john-lennons/" + johnLennonDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, johnLennonDTO.getId().toString()))
            .body(johnLennonDTO);
    }

    /**
     * {@code PUT  /john-lennons/:id} : Updates an existing johnLennon.
     *
     * @param id the id of the johnLennonDTO to save.
     * @param johnLennonDTO the johnLennonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated johnLennonDTO,
     * or with status {@code 400 (Bad Request)} if the johnLennonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the johnLennonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<JohnLennonDTO> updateJohnLennon(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody JohnLennonDTO johnLennonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update JohnLennon : {}, {}", id, johnLennonDTO);
        if (johnLennonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, johnLennonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!johnLennonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        johnLennonDTO = johnLennonService.update(johnLennonDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, johnLennonDTO.getId().toString()))
            .body(johnLennonDTO);
    }

    /**
     * {@code PATCH  /john-lennons/:id} : Partial updates given fields of an existing johnLennon, field will ignore if it is null
     *
     * @param id the id of the johnLennonDTO to save.
     * @param johnLennonDTO the johnLennonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated johnLennonDTO,
     * or with status {@code 400 (Bad Request)} if the johnLennonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the johnLennonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the johnLennonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JohnLennonDTO> partialUpdateJohnLennon(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody JohnLennonDTO johnLennonDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update JohnLennon partially : {}, {}", id, johnLennonDTO);
        if (johnLennonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, johnLennonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!johnLennonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JohnLennonDTO> result = johnLennonService.partialUpdate(johnLennonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, johnLennonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /john-lennons} : get all the johnLennons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of johnLennons in body.
     */
    @GetMapping("")
    public ResponseEntity<List<JohnLennonDTO>> getAllJohnLennons(
        JohnLennonCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get JohnLennons by criteria: {}", criteria);

        Page<JohnLennonDTO> page = johnLennonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /john-lennons/count} : count all the johnLennons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countJohnLennons(JohnLennonCriteria criteria) {
        LOG.debug("REST request to count JohnLennons by criteria: {}", criteria);
        return ResponseEntity.ok().body(johnLennonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /john-lennons/:id} : get the "id" johnLennon.
     *
     * @param id the id of the johnLennonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the johnLennonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<JohnLennonDTO> getJohnLennon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get JohnLennon : {}", id);
        Optional<JohnLennonDTO> johnLennonDTO = johnLennonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(johnLennonDTO);
    }

    /**
     * {@code DELETE  /john-lennons/:id} : delete the "id" johnLennon.
     *
     * @param id the id of the johnLennonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJohnLennon(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete JohnLennon : {}", id);
        johnLennonService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
