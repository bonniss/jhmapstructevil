package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.NextCategoryMiAsserts.*;
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
import xyz.jhmapstruct.domain.NextCategoryMi;
import xyz.jhmapstruct.repository.NextCategoryMiRepository;
import xyz.jhmapstruct.service.dto.NextCategoryMiDTO;
import xyz.jhmapstruct.service.mapper.NextCategoryMiMapper;

/**
 * Integration tests for the {@link NextCategoryMiResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextCategoryMiResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-category-mis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NextCategoryMiRepository nextCategoryMiRepository;

    @Autowired
    private NextCategoryMiMapper nextCategoryMiMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextCategoryMiMockMvc;

    private NextCategoryMi nextCategoryMi;

    private NextCategoryMi insertedNextCategoryMi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryMi createEntity() {
        return new NextCategoryMi().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextCategoryMi createUpdatedEntity() {
        return new NextCategoryMi().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        nextCategoryMi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedNextCategoryMi != null) {
            nextCategoryMiRepository.delete(insertedNextCategoryMi);
            insertedNextCategoryMi = null;
        }
    }

    @Test
    @Transactional
    void createNextCategoryMi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);
        var returnedNextCategoryMiDTO = om.readValue(
            restNextCategoryMiMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NextCategoryMiDTO.class
        );

        // Validate the NextCategoryMi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNextCategoryMi = nextCategoryMiMapper.toEntity(returnedNextCategoryMiDTO);
        assertNextCategoryMiUpdatableFieldsEquals(returnedNextCategoryMi, getPersistedNextCategoryMi(returnedNextCategoryMi));

        insertedNextCategoryMi = returnedNextCategoryMi;
    }

    @Test
    @Transactional
    void createNextCategoryMiWithExistingId() throws Exception {
        // Create the NextCategoryMi with an existing ID
        nextCategoryMi.setId(1L);
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        nextCategoryMi.setName(null);

        // Create the NextCategoryMi, which fails.
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        restNextCategoryMiMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextCategoryMis() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getNextCategoryMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get the nextCategoryMi
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL_ID, nextCategoryMi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextCategoryMi.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNextCategoryMisByIdFiltering() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        Long id = nextCategoryMi.getId();

        defaultNextCategoryMiFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNextCategoryMiFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNextCategoryMiFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where name equals to
        defaultNextCategoryMiFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where name in
        defaultNextCategoryMiFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where name is not null
        defaultNextCategoryMiFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where name contains
        defaultNextCategoryMiFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where name does not contain
        defaultNextCategoryMiFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where description equals to
        defaultNextCategoryMiFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where description in
        defaultNextCategoryMiFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where description is not null
        defaultNextCategoryMiFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where description contains
        defaultNextCategoryMiFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        // Get all the nextCategoryMiList where description does not contain
        defaultNextCategoryMiFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllNextCategoryMisByTenantIsEqualToSomething() throws Exception {
        MasterTenant tenant;
        if (TestUtil.findAll(em, MasterTenant.class).isEmpty()) {
            nextCategoryMiRepository.saveAndFlush(nextCategoryMi);
            tenant = MasterTenantResourceIT.createEntity();
        } else {
            tenant = TestUtil.findAll(em, MasterTenant.class).get(0);
        }
        em.persist(tenant);
        em.flush();
        nextCategoryMi.setTenant(tenant);
        nextCategoryMiRepository.saveAndFlush(nextCategoryMi);
        Long tenantId = tenant.getId();
        // Get all the nextCategoryMiList where tenant equals to tenantId
        defaultNextCategoryMiShouldBeFound("tenantId.equals=" + tenantId);

        // Get all the nextCategoryMiList where tenant equals to (tenantId + 1)
        defaultNextCategoryMiShouldNotBeFound("tenantId.equals=" + (tenantId + 1));
    }

    private void defaultNextCategoryMiFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNextCategoryMiShouldBeFound(shouldBeFound);
        defaultNextCategoryMiShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNextCategoryMiShouldBeFound(String filter) throws Exception {
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextCategoryMi.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        // Check, that the count call also returns 1
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNextCategoryMiShouldNotBeFound(String filter) throws Exception {
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNextCategoryMiMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNextCategoryMi() throws Exception {
        // Get the nextCategoryMi
        restNextCategoryMiMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNextCategoryMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMi
        NextCategoryMi updatedNextCategoryMi = nextCategoryMiRepository.findById(nextCategoryMi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNextCategoryMi are not directly saved in db
        em.detach(updatedNextCategoryMi);
        updatedNextCategoryMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(updatedNextCategoryMi);

        restNextCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryMiDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNextCategoryMiToMatchAllProperties(updatedNextCategoryMi);
    }

    @Test
    @Transactional
    void putNonExistingNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextCategoryMiDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(nextCategoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(nextCategoryMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextCategoryMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMi using partial update
        NextCategoryMi partialUpdatedNextCategoryMi = new NextCategoryMi();
        partialUpdatedNextCategoryMi.setId(nextCategoryMi.getId());

        partialUpdatedNextCategoryMi.name(UPDATED_NAME);

        restNextCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryMiUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNextCategoryMi, nextCategoryMi),
            getPersistedNextCategoryMi(nextCategoryMi)
        );
    }

    @Test
    @Transactional
    void fullUpdateNextCategoryMiWithPatch() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the nextCategoryMi using partial update
        NextCategoryMi partialUpdatedNextCategoryMi = new NextCategoryMi();
        partialUpdatedNextCategoryMi.setId(nextCategoryMi.getId());

        partialUpdatedNextCategoryMi.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restNextCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextCategoryMi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNextCategoryMi))
            )
            .andExpect(status().isOk());

        // Validate the NextCategoryMi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNextCategoryMiUpdatableFieldsEquals(partialUpdatedNextCategoryMi, getPersistedNextCategoryMi(partialUpdatedNextCategoryMi));
    }

    @Test
    @Transactional
    void patchNonExistingNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextCategoryMiDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(nextCategoryMiDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextCategoryMi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        nextCategoryMi.setId(longCount.incrementAndGet());

        // Create the NextCategoryMi
        NextCategoryMiDTO nextCategoryMiDTO = nextCategoryMiMapper.toDto(nextCategoryMi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextCategoryMiMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(nextCategoryMiDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextCategoryMi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextCategoryMi() throws Exception {
        // Initialize the database
        insertedNextCategoryMi = nextCategoryMiRepository.saveAndFlush(nextCategoryMi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the nextCategoryMi
        restNextCategoryMiMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextCategoryMi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return nextCategoryMiRepository.count();
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

    protected NextCategoryMi getPersistedNextCategoryMi(NextCategoryMi nextCategoryMi) {
        return nextCategoryMiRepository.findById(nextCategoryMi.getId()).orElseThrow();
    }

    protected void assertPersistedNextCategoryMiToMatchAllProperties(NextCategoryMi expectedNextCategoryMi) {
        assertNextCategoryMiAllPropertiesEquals(expectedNextCategoryMi, getPersistedNextCategoryMi(expectedNextCategoryMi));
    }

    protected void assertPersistedNextCategoryMiToMatchUpdatableProperties(NextCategoryMi expectedNextCategoryMi) {
        assertNextCategoryMiAllUpdatablePropertiesEquals(expectedNextCategoryMi, getPersistedNextCategoryMi(expectedNextCategoryMi));
    }
}
