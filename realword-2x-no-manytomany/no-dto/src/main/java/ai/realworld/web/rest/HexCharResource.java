package ai.realworld.web.rest;

import ai.realworld.domain.HexChar;
import ai.realworld.repository.HexCharRepository;
import ai.realworld.service.HexCharQueryService;
import ai.realworld.service.HexCharService;
import ai.realworld.service.criteria.HexCharCriteria;
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
 * REST controller for managing {@link ai.realworld.domain.HexChar}.
 */
@RestController
@RequestMapping("/api/hex-chars")
public class HexCharResource {

    private static final Logger LOG = LoggerFactory.getLogger(HexCharResource.class);

    private static final String ENTITY_NAME = "hexChar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HexCharService hexCharService;

    private final HexCharRepository hexCharRepository;

    private final HexCharQueryService hexCharQueryService;

    public HexCharResource(HexCharService hexCharService, HexCharRepository hexCharRepository, HexCharQueryService hexCharQueryService) {
        this.hexCharService = hexCharService;
        this.hexCharRepository = hexCharRepository;
        this.hexCharQueryService = hexCharQueryService;
    }

    /**
     * {@code POST  /hex-chars} : Create a new hexChar.
     *
     * @param hexChar the hexChar to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hexChar, or with status {@code 400 (Bad Request)} if the hexChar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HexChar> createHexChar(@Valid @RequestBody HexChar hexChar) throws URISyntaxException {
        LOG.debug("REST request to save HexChar : {}", hexChar);
        if (hexChar.getId() != null) {
            throw new BadRequestAlertException("A new hexChar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hexChar = hexCharService.save(hexChar);
        return ResponseEntity.created(new URI("/api/hex-chars/" + hexChar.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hexChar.getId().toString()))
            .body(hexChar);
    }

    /**
     * {@code PUT  /hex-chars/:id} : Updates an existing hexChar.
     *
     * @param id the id of the hexChar to save.
     * @param hexChar the hexChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hexChar,
     * or with status {@code 400 (Bad Request)} if the hexChar is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hexChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HexChar> updateHexChar(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HexChar hexChar
    ) throws URISyntaxException {
        LOG.debug("REST request to update HexChar : {}, {}", id, hexChar);
        if (hexChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hexChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hexCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hexChar = hexCharService.update(hexChar);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hexChar.getId().toString()))
            .body(hexChar);
    }

    /**
     * {@code PATCH  /hex-chars/:id} : Partial updates given fields of an existing hexChar, field will ignore if it is null
     *
     * @param id the id of the hexChar to save.
     * @param hexChar the hexChar to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hexChar,
     * or with status {@code 400 (Bad Request)} if the hexChar is not valid,
     * or with status {@code 404 (Not Found)} if the hexChar is not found,
     * or with status {@code 500 (Internal Server Error)} if the hexChar couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HexChar> partialUpdateHexChar(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HexChar hexChar
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HexChar partially : {}, {}", id, hexChar);
        if (hexChar.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hexChar.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hexCharRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HexChar> result = hexCharService.partialUpdate(hexChar);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hexChar.getId().toString())
        );
    }

    /**
     * {@code GET  /hex-chars} : get all the hexChars.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hexChars in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HexChar>> getAllHexChars(
        HexCharCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HexChars by criteria: {}", criteria);

        Page<HexChar> page = hexCharQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hex-chars/count} : count all the hexChars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHexChars(HexCharCriteria criteria) {
        LOG.debug("REST request to count HexChars by criteria: {}", criteria);
        return ResponseEntity.ok().body(hexCharQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hex-chars/:id} : get the "id" hexChar.
     *
     * @param id the id of the hexChar to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hexChar, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HexChar> getHexChar(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HexChar : {}", id);
        Optional<HexChar> hexChar = hexCharService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hexChar);
    }

    /**
     * {@code DELETE  /hex-chars/:id} : delete the "id" hexChar.
     *
     * @param id the id of the hexChar to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHexChar(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HexChar : {}", id);
        hexCharService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
