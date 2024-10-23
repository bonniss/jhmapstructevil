package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryMiMiAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

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
import xyz.jhmapstruct.IntegrationTest;
import xyz.jhmapstruct.domain.MasterTenant;
import xyz.jhmapstruct.domain.NextCategoryMiMi;
import xyz.jhmapstruct.repository.NextCategoryMiMiRepository;

/**
 * Integration tests for the {@link NextCategoryMiMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryMiMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-mi-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryMiMiRepository nextCategoryMiMiRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryMiMiMockMvc;

    private NextCategoryMiMi nextCategoryMiMi;

    private NextCategoryMiMi insertedNextCategoryMiMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryMiMi createEntity() {
        return new NextCategoryMiMi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryMiMi createUpdatedEntity() {
        return new NextCategoryMiMi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryMiMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryMiMi != null) {
            nextCategoryMiMiRepository.delete(insertedNextCategoryMiMi);
            insertedNextCategoryMiMi = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryMiMi
        var returnedNextCategoryMiMi = om.readValue(
            restNextCategoryMiMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiMi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryMiMi.class
        );

        // Validate the NextCategoryMiMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertNextCategoryMiMiUpdatableFieldsEquals(returnedNextCategoryMiMi, getPersistedNextCategoryMiMi(returnedNextCategoryMiMi));

        insertedNextCategoryMiMi = returnedNextCategoryMiMi;
    }

    @Test
    @Transactional
    void createNextCategoryMiMiWithExistingId() throws Exception {
        // Create the NextCategoryMiMi with an existing ID
        nextCategoryMiMi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiMi)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryMiMi.setName(null);

        // Create the NextCategoryMiMi, which fails.

        restNextCategoryMiMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiMi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMis() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryMiMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get the nextCategoryMiMi
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryMiMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryMiMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryMiMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        Long id = nextCategoryMiMi.getId();

        defaultNextCategoryMiMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryMiMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryMiMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where name equals to
        defaultNextCategoryMiMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where name in
        defaultNextCategoryMiMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where name is not null
        defaultNextCategoryMiMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where name contains
        defaultNextCategoryMiMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where name does not contain
        defaultNextCategoryMiMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where description equals to
        defaultNextCategoryMiMiFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where description in
        defaultNextCategoryMiMiFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where description is not null
        defaultNextCategoryMiMiFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where description contains
        defaultNextCategoryMiMiFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        // Get all the nextCategoryMiMiList where description does not contain
        defaultNextCategoryMiMiFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryMiMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryMiMi.setTenant(tenant);
        nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryMiMiList where tenant equals to tenantId
        defaultNextCategoryMiMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryMiMiList where tenant equals to (tenantId + 1)
        defaultNextCategoryMiMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryMiMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryMiMiShouldBeFound(shouldBeFound);
        defaultNextCategoryMiMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryMiMiShouldBeFound(String filter) throws Exception {
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryMiMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryMiMiShouldNotBeFound(String filter) throws Exception {
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryMiMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryMiMi() throws Exception {
        // Get the nextCategoryMiMi
        restNextCategoryMiMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryMiMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMiMi
        NextCategoryMiMi updatedNextCategoryMiMi = nextCategoryMiMiRepository.findById(nextCategoryMiMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryMiMi are not directly saved in db
        em.detach(updatedNextCategoryMiMi);
        updatedNextCategoryMiMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNextCategoryMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedNextCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryMiMiToMatchAllProperties(updatedNextCategoryMiMi);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryMiMi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMiMi using partial update
        NextCategoryMiMi partialUpdatedNextCategoryMiMi = new NextCategoryMiMi();
        partialUpdatedNextCategoryMiMi.setId(nextCategoryMiMi.getId());

        partialUpdatedNextCategoryMiMi.description(UPDATED_DESCRIPTION);

        restNextCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryMiMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryMiMi, nextCategoryMiMi),
            getPersistedNextCategoryMiMi(nextCategoryMiMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryMiMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMiMi using partial update
        NextCategoryMiMi partialUpdatedNextCategoryMiMi = new NextCategoryMiMi();
        partialUpdatedNextCategoryMiMi.setId(nextCategoryMiMi.getId());

        partialUpdatedNextCategoryMiMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryMiMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMiMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryMiMiUpdatableFieldsEquals(
            partialUpdatedNextCategoryMiMi,
            getPersistedNextCategoryMiMi(partialUpdatedNextCategoryMiMi)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryMiMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryMiMi))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryMiMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMiMi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryMiMi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryMiMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryMiMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMiMi = nextCategoryMiMiRepository.saveAndFlush(nextCategoryMiMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryMiMi
        restNextCategoryMiMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryMiMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryMiMiRepository.count();
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

    protected NextCategoryMiMi getPersistedNextCategoryMiMi(NextCategoryMiMi nextCategoryMiMi) {
        return nextCategoryMiMiRepository.findById(nextCategoryMiMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryMiMiToMatchAllProperties(NextCategoryMiMi expectedNextCategoryMiMi) {
        assertNextCategoryMiMiAllPropertiesEquals(expectedNextCategoryMiMi, getPersistedNextCategoryMiMi(expectedNextCategoryMiMi));
    }

    protected void assertPersistedNextCategoryMiMiToMatchUpdatableProperties(NextCategoryMiMi expectedNextCategoryMiMi) {
        assertNextCategoryMiMiAllUpdatablePropertiesEquals(
            expectedNextCategoryMiMi,
            getPersistedNextCategoryMiMi(expectedNextCategoryMiMi)
        );
    }
}
