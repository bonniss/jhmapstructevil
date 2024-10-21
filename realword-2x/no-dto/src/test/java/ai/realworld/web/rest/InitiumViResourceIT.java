package ai.realworld.web.rest;

import static ai.realworld.domain.InitiumViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.InitiumVi;
import ai.realworld.repository.InitiumViRepository;
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
 * Integration tests for the {@link InitiumViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InitiumViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_JELLO_SUPPORTED = false;
    private static final Boolean UPDATED_IS_JELLO_SUPPORTED = true;

    private static final String ENTITY_API_URL = "/api/initium-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InitiumViRepository initiumViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInitiumViMockMvc;

    private InitiumVi initiumVi;

    private InitiumVi insertedInitiumVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InitiumVi createEntity() {
        return new InitiumVi()
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
    public static InitiumVi createUpdatedEntity() {
        return new InitiumVi()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);
    }

    @BeforeEach
    public void initTest() {
        initiumVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInitiumVi != null) {
            initiumViRepository.delete(insertedInitiumVi);
            insertedInitiumVi = null;
        }
    }

    @Test
    @Transactional
    void createInitiumVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InitiumVi
        var returnedInitiumVi = om.readValue(
            restInitiumViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InitiumVi.class
        );

        // Validate the InitiumVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInitiumViUpdatableFieldsEquals(returnedInitiumVi, getPersistedInitiumVi(returnedInitiumVi));

        insertedInitiumVi = returnedInitiumVi;
    }

    @Test
    @Transactional
    void createInitiumViWithExistingId() throws Exception {
        // Create the InitiumVi with an existing ID
        initiumVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInitiumViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi)))
            .andExpect(status().isBadRequest());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        initiumVi.setName(null);

        // Create the InitiumVi, which fails.

        restInitiumViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        initiumVi.setSlug(null);

        // Create the InitiumVi, which fails.

        restInitiumViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInitiumVis() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initiumVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isJelloSupported").value(hasItem(DEFAULT_IS_JELLO_SUPPORTED.booleanValue())));
    }

    @Test
    @Transactional
    void getInitiumVi() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get the initiumVi
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL_ID, initiumVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(initiumVi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isJelloSupported").value(DEFAULT_IS_JELLO_SUPPORTED.booleanValue()));
    }

    @Test
    @Transactional
    void getInitiumVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        Long id = initiumVi.getId();

        defaultInitiumViFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInitiumViFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInitiumViFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInitiumVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where name equals to
        defaultInitiumViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where name in
        defaultInitiumViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where name is not null
        defaultInitiumViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where name contains
        defaultInitiumViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where name does not contain
        defaultInitiumViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllInitiumVisBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where slug equals to
        defaultInitiumViFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumVisBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where slug in
        defaultInitiumViFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumVisBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where slug is not null
        defaultInitiumViFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumVisBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where slug contains
        defaultInitiumViFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumVisBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where slug does not contain
        defaultInitiumViFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllInitiumVisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where description equals to
        defaultInitiumViFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumVisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where description in
        defaultInitiumViFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllInitiumVisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where description is not null
        defaultInitiumViFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllInitiumVisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where description contains
        defaultInitiumViFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumVisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where description does not contain
        defaultInitiumViFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllInitiumVisByIsJelloSupportedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where isJelloSupported equals to
        defaultInitiumViFiltering(
            "isJelloSupported.equals=" + DEFAULT_IS_JELLO_SUPPORTED,
            "isJelloSupported.equals=" + UPDATED_IS_JELLO_SUPPORTED
        );
    }

    @Test
    @Transactional
    void getAllInitiumVisByIsJelloSupportedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where isJelloSupported in
        defaultInitiumViFiltering(
            "isJelloSupported.in=" + DEFAULT_IS_JELLO_SUPPORTED + "," + UPDATED_IS_JELLO_SUPPORTED,
            "isJelloSupported.in=" + UPDATED_IS_JELLO_SUPPORTED
        );
    }

    @Test
    @Transactional
    void getAllInitiumVisByIsJelloSupportedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        // Get all the initiumViList where isJelloSupported is not null
        defaultInitiumViFiltering("isJelloSupported.specified=true", "isJelloSupported.specified=false");
    }

    private void defaultInitiumViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInitiumViShouldBeFound(shouldBeFound);
        defaultInitiumViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInitiumViShouldBeFound(String filter) throws Exception {
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(initiumVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isJelloSupported").value(hasItem(DEFAULT_IS_JELLO_SUPPORTED.booleanValue())));

        // Check, that the count call also returns 1
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInitiumViShouldNotBeFound(String filter) throws Exception {
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInitiumViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInitiumVi() throws Exception {
        // Get the initiumVi
        restInitiumViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInitiumVi() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initiumVi
        InitiumVi updatedInitiumVi = initiumViRepository.findById(initiumVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInitiumVi are not directly saved in db
        em.detach(updatedInitiumVi);
        updatedInitiumVi
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);

        restInitiumViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInitiumVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInitiumVi))
            )
            .andExpect(status().isOk());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInitiumViToMatchAllProperties(updatedInitiumVi);
    }

    @Test
    @Transactional
    void putNonExistingInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, initiumVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(initiumVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(initiumVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInitiumViWithPatch() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initiumVi using partial update
        InitiumVi partialUpdatedInitiumVi = new InitiumVi();
        partialUpdatedInitiumVi.setId(initiumVi.getId());

        partialUpdatedInitiumVi.name(UPDATED_NAME).slug(UPDATED_SLUG);

        restInitiumViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInitiumVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInitiumVi))
            )
            .andExpect(status().isOk());

        // Validate the InitiumVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInitiumViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInitiumVi, initiumVi),
            getPersistedInitiumVi(initiumVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateInitiumViWithPatch() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the initiumVi using partial update
        InitiumVi partialUpdatedInitiumVi = new InitiumVi();
        partialUpdatedInitiumVi.setId(initiumVi.getId());

        partialUpdatedInitiumVi
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .description(UPDATED_DESCRIPTION)
            .isJelloSupported(UPDATED_IS_JELLO_SUPPORTED);

        restInitiumViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInitiumVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInitiumVi))
            )
            .andExpect(status().isOk());

        // Validate the InitiumVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInitiumViUpdatableFieldsEquals(partialUpdatedInitiumVi, getPersistedInitiumVi(partialUpdatedInitiumVi));
    }

    @Test
    @Transactional
    void patchNonExistingInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, initiumVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(initiumVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(initiumVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInitiumVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        initiumVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInitiumViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(initiumVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InitiumVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInitiumVi() throws Exception {
        // Initialize the database
        insertedInitiumVi = initiumViRepository.saveAndFlush(initiumVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the initiumVi
        restInitiumViMockMvc
            .perform(delete(ENTITY_API_URL_ID, initiumVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return initiumViRepository.count();
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

    protected InitiumVi getPersistedInitiumVi(InitiumVi initiumVi) {
        return initiumViRepository.findById(initiumVi.getId()).orElseThrow();
    }

    protected void assertPersistedInitiumViToMatchAllProperties(InitiumVi expectedInitiumVi) {
        assertInitiumViAllPropertiesEquals(expectedInitiumVi, getPersistedInitiumVi(expectedInitiumVi));
    }

    protected void assertPersistedInitiumViToMatchUpdatableProperties(InitiumVi expectedInitiumVi) {
        assertInitiumViAllUpdatablePropertiesEquals(expectedInitiumVi, getPersistedInitiumVi(expectedInitiumVi));
    }
}
