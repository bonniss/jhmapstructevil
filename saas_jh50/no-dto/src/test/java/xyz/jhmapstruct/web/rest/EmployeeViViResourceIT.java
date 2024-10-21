package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeViViAsserts.*;
import static xyz.jhmapstruct.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import xyz.jhmapstruct.domain.EmployeeViVi;
import xyz.jhmapstruct.repository.EmployeeViViRepository;

/**
 * Integration tests for the {@link EmployeeViViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeViViResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Instant DEFAULT_HIRE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HIRE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/employee-vi-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeViViRepository employeeViViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeViViMockMvc;

    private EmployeeViVi employeeViVi;

    private EmployeeViVi insertedEmployeeViVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeViVi createEntity() {
        return new EmployeeViVi()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .hireDate(DEFAULT_HIRE_DATE)
            .position(DEFAULT_POSITION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeViVi createUpdatedEntity() {
        return new EmployeeViVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeViVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeViVi != null) {
            employeeViViRepository.delete(insertedEmployeeViVi);
            insertedEmployeeViVi = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeViVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeViVi
        var returnedEmployeeViVi = om.readValue(
            restEmployeeViViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeViVi.class
        );

        // Validate the EmployeeViVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeViViUpdatableFieldsEquals(returnedEmployeeViVi, getPersistedEmployeeViVi(returnedEmployeeViVi));

        insertedEmployeeViVi = returnedEmployeeViVi;
    }

    @Test
    @Transactional
    void createEmployeeViViWithExistingId() throws Exception {
        // Create the EmployeeViVi with an existing ID
        employeeViVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeViVi.setFirstName(null);

        // Create the EmployeeViVi, which fails.

        restEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeViVi.setLastName(null);

        // Create the EmployeeViVi, which fails.

        restEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeViVi.setEmail(null);

        // Create the EmployeeViVi, which fails.

        restEmployeeViViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeViVis() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        // Get all the employeeViViList
        restEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeViVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeViVi() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        // Get the employeeViVi
        restEmployeeViViMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeViVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeViVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeViVi() throws Exception {
        // Get the employeeViVi
        restEmployeeViViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeViVi() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeViVi
        EmployeeViVi updatedEmployeeViVi = employeeViViRepository.findById(employeeViVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeViVi are not directly saved in db
        em.detach(updatedEmployeeViVi);
        updatedEmployeeViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeViVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeViViToMatchAllProperties(updatedEmployeeViVi);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeViVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeViViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeViVi using partial update
        EmployeeViVi partialUpdatedEmployeeViVi = new EmployeeViVi();
        partialUpdatedEmployeeViVi.setId(employeeViVi.getId());

        partialUpdatedEmployeeViVi.email(UPDATED_EMAIL);

        restEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeViVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeViVi, employeeViVi),
            getPersistedEmployeeViVi(employeeViVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeViViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeViVi using partial update
        EmployeeViVi partialUpdatedEmployeeViVi = new EmployeeViVi();
        partialUpdatedEmployeeViVi.setId(employeeViVi.getId());

        partialUpdatedEmployeeViVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeViVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeViVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViViUpdatableFieldsEquals(partialUpdatedEmployeeViVi, getPersistedEmployeeViVi(partialUpdatedEmployeeViVi));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeViVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeViVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeViVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeViVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeViVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeViVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeViVi() throws Exception {
        // Initialize the database
        insertedEmployeeViVi = employeeViViRepository.saveAndFlush(employeeViVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeViVi
        restEmployeeViViMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeViVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeViViRepository.count();
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

    protected EmployeeViVi getPersistedEmployeeViVi(EmployeeViVi employeeViVi) {
        return employeeViViRepository.findById(employeeViVi.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeViViToMatchAllProperties(EmployeeViVi expectedEmployeeViVi) {
        assertEmployeeViViAllPropertiesEquals(expectedEmployeeViVi, getPersistedEmployeeViVi(expectedEmployeeViVi));
    }

    protected void assertPersistedEmployeeViViToMatchUpdatableProperties(EmployeeViVi expectedEmployeeViVi) {
        assertEmployeeViViAllUpdatablePropertiesEquals(expectedEmployeeViVi, getPersistedEmployeeViVi(expectedEmployeeViVi));
    }
}
