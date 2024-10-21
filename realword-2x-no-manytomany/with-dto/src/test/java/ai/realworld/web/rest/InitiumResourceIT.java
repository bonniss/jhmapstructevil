package ai.realworld.web.rest;

import static ai.realworld.domain.InitiumAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.Initium;
import ai.realworld.repository.InitiumRepository;
import ai.realworld.service.dto.InitiumDTO;
import ai.realworld.service.mapper.InitiumMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
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
 * Integration tests for the {@link InitiumResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InitiumResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_JELLO_SUPPORTED = false;
    private static final Boolean UPDATED_IS_JELLO_SUPPORTED = true;

    private static final String ENTITY_API_URL = "/api/initiums";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InitiumRepository initiumRepository;

    @Autowired
    private InitiumMapper initiumMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInitiumMockMvc;

    private Initium initium;

    private Initium insertedInitium;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Initium createEntity() {
        return new Initium()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .description(DEFAULT_DESCRIPTION)
            .isJelloSupported(DEFAULT_IS_JELLO_SUPPORTED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Initium createUpdatedEntity() {
        return new Initium()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);
    }

    @BeforeEach
    public void initTest() {
        initium = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInitium != null) {
            initiumRepository.delete(insertedInitium);
            insertedInitium = null;
        }
    }

    @Test
    @Transactional
    void createInitium() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);
        var returnedInitiumDTO = om.readValue(
            restInitiumMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InitiumDTO.class
        );

        // Validate the Initium in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInitium = initiumMapper.toEntity(returnedInitiumDTO);
        assertInitiumUpdatableFieldsEquals(returnedInitium, getPersistedInitium(returnedInitium));

        insertedInitium = returnedInitium;
    }

    @Test
    @Transactional
    void createInitiumWithExistingId() throws Exception {
        // Create the Initium with an existing ID
        initium.setId(1L);
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInitiumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        initium.setName(null);

        // Create the Initium, which fails.
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        restInitiumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        initium.setSlug(null);

        // Create the Initium, which fails.
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        restInitiumMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInitiums() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isJelloSupported").value(hasItem(DEFAULT_IS_JELLO_SUPPORTED.booleanValue())));
    }

    @Test
    @Transactional
    void getInitium() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get the initium
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL_ID, initium.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(initium.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isJelloSupported").value(DEFAULT_IS_JELLO_SUPPORTED.booleanValue()));
    }

    @Test
    @Transactional
    void getInitiumsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        Long id = initium.getId();

        defaultInitiumFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInitiumFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInitiumFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInitiumsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where name equals to
        defaultInitiumFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where name in
        defaultInitiumFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where name is not null
        defaultInitiumFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where name contains
        defaultInitiumFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where name does not contain
        defaultInitiumFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumsBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where slug equals to
        defaultInitiumFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumsBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where slug in
        defaultInitiumFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumsBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where slug is not null
        defaultInitiumFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumsBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where slug contains
        defaultInitiumFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumsBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where slug does not contain
        defaultInitiumFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where description equals to
        defaultInitiumFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where description in
        defaultInitiumFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInitiumsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where description is not null
        defaultInitiumFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where description contains
        defaultInitiumFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where description does not contain
        defaultInitiumFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumsByIsJelloSupportedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where isJelloSupported equals to
        defaultInitiumFiltering(
            "isJelloSupported.equals=" + DEFAULT_IS_JELLO_SUPPORTED,
            "isJelloSupported.equals=" + UPDATED_IS_JELLO_SUPPORTED
        );
    }

    @Test
    @Transactional
    void getAllInitiumsByIsJelloSupportedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where isJelloSupported in
        defaultInitiumFiltering(
            "isJelloSupported.in=" + DEFAULT_IS_JELLO_SUPPORTED + "," + UPDATED_IS_JELLO_SUPPORTED,
            "isJelloSupported.in=" + UPDATED_IS_JELLO_SUPPORTED
        );
    }

    @Test
    @Transactional
    void getAllInitiumsByIsJelloSupportedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        // Get all the initiumList where isJelloSupported is not null
        defaultInitiumFiltering("isJelloSupported.specified=true", "isJelloSupported.specified=false");
    }

    private void defaultInitiumFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInitiumShouldBeFound(shouldBeFound);
        defaultInitiumShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInitiumShouldBeFound(String filter) throws Exception {
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initium.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isJelloSupported").value(hasItem(DEFAULT_IS_JELLO_SUPPORTED.booleanValue())));

        // Check, that the count call also returns 1
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInitiumShouldNotBeFound(String filter) throws Exception {
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInitiumMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInitium() throws Exception {
        // Get the initium
        restInitiumMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInitium() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initium
        Initium updatedInitium = initiumRepository.findById(initium.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInitium are not directly saved in db
        em.detach(updatedInitium);
        updatedInitium.name(UPDATED_NAME).slug(UPDATED_SLUG).description(UPDATED_DESCRIPTION).isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);
        InitiumDTO initiumDTO = initiumMapper.toDto(updatedInitium);

        restInitiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, initiumDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO))
            )
            .andExpect(status().isOk());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInitiumToMatchAllProperties(updatedInitium);
    }

    @Test
    @Transactional
    void putNonExistingInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, initiumDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(initiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInitiumWithPatch() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initium using partial update
        Initium partialUpdatedInitium = new Initium();
        partialUpdatedInitium.setId(initium.getId());

        partialUpdatedInitium.name(UPDATED_NAME);

        restInitiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInitium.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInitium))
            )
            .andExpect(status().isOk());

        // Validate the Initium in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInitiumUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedInitium, initium), getPersistedInitium(initium));
    }

    @Test
    @Transactional
    void fullUpdateInitiumWithPatch() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initium using partial update
        Initium partialUpdatedInitium = new Initium();
        partialUpdatedInitium.setId(initium.getId());

        partialUpdatedInitium
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);

        restInitiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInitium.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInitium))
            )
            .andExpect(status().isOk());

        // Validate the Initium in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInitiumUpdatableFieldsEquals(partialUpdatedInitium, getPersistedInitium(partialUpdatedInitium));
    }

    @Test
    @Transactional
    void patchNonExistingInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, initiumDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(initiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(initiumDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInitium() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initium.setId(longCount.incrementAndGet());

        // Create the Initium
        InitiumDTO initiumDTO = initiumMapper.toDto(initium);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(initiumDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Initium in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInitium() throws Exception {
        // Initialize the database
        insertedInitium = initiumRepository.saveAndFlush(initium);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the initium
        restInitiumMockMvc
            .perform(delete(ENTITY_API_URL_ID, initium.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return initiumRepository.count();
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

    protected Initium getPersistedInitium(Initium initium) {
        return initiumRepository.findById(initium.getId()).orElseThrow();
    }

    protected void assertPersistedInitiumToMatchAllProperties(Initium expectedInitium) {
        assertInitiumAllPropertiesEquals(expectedInitium, getPersistedInitium(expectedInitium));
    }

    protected void assertPersistedInitiumToMatchUpdatableProperties(Initium expectedInitium) {
        assertInitiumAllUpdatablePropertiesEquals(expectedInitium, getPersistedInitium(expectedInitium));
    }
}
