package ai.realworld.web.rest;

import static ai.realworld.domain.RihannaAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Rihanna;
import ai.realworld.repository.RihannaRepository;
import ai.realworld.service.dto.RihannaDTO;
import ai.realworld.service.mapper.RihannaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RihannaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RihannaResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSION_GRID_JASON = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSION_GRID_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/rihannas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RihannaRepository rihannaRepository;

    @Autowired
    private RihannaMapper rihannaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRihannaMockMvc;

    private Rihanna rihanna;

    private Rihanna insertedRihanna;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rihanna createEntity() {
        return new Rihanna().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION).permissionGridJason(DEFAULT_PERMISSION_GRID_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rihanna createUpdatedEntity() {
        return new Rihanna().name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
    }

    @BeforeEach
    public void initTest() {
        rihanna = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedRihanna != null) {
            rihannaRepository.delete(insertedRihanna);
            insertedRihanna = null;
        }
    }

    @Test
    @Transactional
    void createRihanna() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);
        var returnedRihannaDTO = om.readValue(
            restRihannaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RihannaDTO.class
        );

        // Validate the Rihanna in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRihanna = rihannaMapper.toEntity(returnedRihannaDTO);
        assertRihannaUpdatableFieldsEquals(returnedRihanna, getPersistedRihanna(returnedRihanna));

        insertedRihanna = returnedRihanna;
    }

    @Test
    @Transactional
    void createRihannaWithExistingId() throws Exception {
        // Create the Rihanna with an existing ID
        rihanna.setId(1L);
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRihannaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        rihanna.setName(null);

        // Create the Rihanna, which fails.
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        restRihannaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRihannas() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rihanna.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));
    }

    @Test
    @Transactional
    void getRihanna() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get the rihanna
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL_ID, rihanna.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rihanna.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.permissionGridJason").value(DEFAULT_PERMISSION_GRID_JASON));
    }

    @Test
    @Transactional
    void getRihannasByIdFiltering() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        Long id = rihanna.getId();

        defaultRihannaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRihannaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRihannaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRihannasByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where name equals to
        defaultRihannaFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannasByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where name in
        defaultRihannaFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannasByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where name is not null
        defaultRihannaFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannasByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where name contains
        defaultRihannaFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllRihannasByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where name does not contain
        defaultRihannaFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllRihannasByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where description equals to
        defaultRihannaFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannasByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where description in
        defaultRihannaFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllRihannasByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where description is not null
        defaultRihannaFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannasByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where description contains
        defaultRihannaFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannasByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where description does not contain
        defaultRihannaFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllRihannasByPermissionGridJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where permissionGridJason equals to
        defaultRihannaFiltering(
            "permissionGridJason.equals=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.equals=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannasByPermissionGridJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where permissionGridJason in
        defaultRihannaFiltering(
            "permissionGridJason.in=" + DEFAULT_PERMISSION_GRID_JASON + "," + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.in=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannasByPermissionGridJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where permissionGridJason is not null
        defaultRihannaFiltering("permissionGridJason.specified=true", "permissionGridJason.specified=false");
    }

    @Test
    @Transactional
    void getAllRihannasByPermissionGridJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where permissionGridJason contains
        defaultRihannaFiltering(
            "permissionGridJason.contains=" + DEFAULT_PERMISSION_GRID_JASON,
            "permissionGridJason.contains=" + UPDATED_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannasByPermissionGridJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        // Get all the rihannaList where permissionGridJason does not contain
        defaultRihannaFiltering(
            "permissionGridJason.doesNotContain=" + UPDATED_PERMISSION_GRID_JASON,
            "permissionGridJason.doesNotContain=" + DEFAULT_PERMISSION_GRID_JASON
        );
    }

    @Test
    @Transactional
    void getAllRihannasByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            rihannaRepository.saveAndFlush(rihanna);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        rihanna.setApplication(application);
        rihannaRepository.saveAndFlush(rihanna);
        UUID applicationId = application.getId();
        // Get all the rihannaList where application equals to applicationId
        defaultRihannaShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the rihannaList where application equals to UUID.randomUUID()
        defaultRihannaShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    private void defaultRihannaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRihannaShouldBeFound(shouldBeFound);
        defaultRihannaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRihannaShouldBeFound(String filter) throws Exception {
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rihanna.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].permissionGridJason").value(hasItem(DEFAULT_PERMISSION_GRID_JASON)));

        // Check, that the count call also returns 1
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRihannaShouldNotBeFound(String filter) throws Exception {
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRihannaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRihanna() throws Exception {
        // Get the rihanna
        restRihannaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRihanna() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihanna
        Rihanna updatedRihanna = rihannaRepository.findById(rihanna.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRihanna are not directly saved in db
        em.detach(updatedRihanna);
        updatedRihanna.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);
        RihannaDTO rihannaDTO = rihannaMapper.toDto(updatedRihanna);

        restRihannaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rihannaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRihannaToMatchAllProperties(updatedRihanna);
    }

    @Test
    @Transactional
    void putNonExistingRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rihannaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(rihannaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(rihannaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRihannaWithPatch() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihanna using partial update
        Rihanna partialUpdatedRihanna = new Rihanna();
        partialUpdatedRihanna.setId(rihanna.getId());

        partialUpdatedRihanna.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restRihannaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRihanna.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRihanna))
            )
            .andExpect(status().isOk());

        // Validate the Rihanna in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRihannaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRihanna, rihanna), getPersistedRihanna(rihanna));
    }

    @Test
    @Transactional
    void fullUpdateRihannaWithPatch() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the rihanna using partial update
        Rihanna partialUpdatedRihanna = new Rihanna();
        partialUpdatedRihanna.setId(rihanna.getId());

        partialUpdatedRihanna.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).permissionGridJason(UPDATED_PERMISSION_GRID_JASON);

        restRihannaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRihanna.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRihanna))
            )
            .andExpect(status().isOk());

        // Validate the Rihanna in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRihannaUpdatableFieldsEquals(partialUpdatedRihanna, getPersistedRihanna(partialUpdatedRihanna));
    }

    @Test
    @Transactional
    void patchNonExistingRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rihannaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rihannaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(rihannaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRihanna() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        rihanna.setId(longCount.incrementAndGet());

        // Create the Rihanna
        RihannaDTO rihannaDTO = rihannaMapper.toDto(rihanna);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRihannaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(rihannaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rihanna in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRihanna() throws Exception {
        // Initialize the database
        insertedRihanna = rihannaRepository.saveAndFlush(rihanna);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the rihanna
        restRihannaMockMvc
            .perform(delete(ENTITY_API_URL_ID, rihanna.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return rihannaRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Rihanna getPersistedRihanna(Rihanna rihanna) {
        return rihannaRepository.findById(rihanna.getId()).orElseThrow();
    }

    protected void assertPersistedRihannaToMatchAllProperties(Rihanna expectedRihanna) {
        assertRihannaAllPropertiesEquals(expectedRihanna, getPersistedRihanna(expectedRihanna));
    }

    protected void assertPersistedRihannaToMatchUpdatableProperties(Rihanna expectedRihanna) {
        assertRihannaAllUpdatablePropertiesEquals(expectedRihanna, getPersistedRihanna(expectedRihanna));
    }
}
