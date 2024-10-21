package xyz.jhmapstruct.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static xyz.jhmapstruct.domain.EmployeeViAsserts.*;
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
import xyz.jhmapstruct.domain.EmployeeVi;
import xyz.jhmapstruct.repository.EmployeeViRepository;

/**
 * Integration tests for the {@link EmployeeViResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeViResourceIT {

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

    private static final String ENTITY_API_URL = "/api/employee-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeViRepository employeeViRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeViMockMvc;

    private EmployeeVi employeeVi;

    private EmployeeVi insertedEmployeeVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmployeeVi createEntity() {
        return new EmployeeVi()
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
    public static EmployeeVi createUpdatedEntity() {
        return new EmployeeVi()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);
    }

    @BeforeEach
    public void initTest() {
        employeeVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedEmployeeVi != null) {
            employeeViRepository.delete(insertedEmployeeVi);
            insertedEmployeeVi = null;
        }
    }

    @Test
    @Transactional
    void createEmployeeVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmployeeVi
        var returnedEmployeeVi = om.readValue(
            restEmployeeViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmployeeVi.class
        );

        // Validate the EmployeeVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeViUpdatableFieldsEquals(returnedEmployeeVi, getPersistedEmployeeVi(returnedEmployeeVi));

        insertedEmployeeVi = returnedEmployeeVi;
    }

    @Test
    @Transactional
    void createEmployeeViWithExistingId() throws Exception {
        // Create the EmployeeVi with an existing ID
        employeeVi.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setFirstName(null);

        // Create the EmployeeVi, which fails.

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setLastName(null);

        // Create the EmployeeVi, which fails.

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        employeeVi.setEmail(null);

        // Create the EmployeeVi, which fails.

        restEmployeeViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployeeVis() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get all the employeeViList
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employeeVi.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hireDate").value(hasItem(DEFAULT_HIRE_DATE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        // Get the employeeVi
        restEmployeeViMockMvc
            .perform(get(ENTITY_API_URL_ID, employeeVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employeeVi.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hireDate").value(DEFAULT_HIRE_DATE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNonExistingEmployeeVi() throws Exception {
        // Get the employeeVi
        restEmployeeViMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi
        EmployeeVi updatedEmployeeVi = employeeViRepository.findById(employeeVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployeeVi are not directly saved in db
        em.detach(updatedEmployeeVi);
        updatedEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployeeVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeViToMatchAllProperties(updatedEmployeeVi);
    }

    @Test
    @Transactional
    void putNonExistingEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employeeVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi using partial update
        EmployeeVi partialUpdatedEmployeeVi = new EmployeeVi();
        partialUpdatedEmployeeVi.setId(employeeVi.getId());

        partialUpdatedEmployeeVi.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME).position(UPDATED_POSITION);

        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmployeeVi, employeeVi),
            getPersistedEmployeeVi(employeeVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmployeeViWithPatch() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employeeVi using partial update
        EmployeeVi partialUpdatedEmployeeVi = new EmployeeVi();
        partialUpdatedEmployeeVi.setId(employeeVi.getId());

        partialUpdatedEmployeeVi
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .hireDate(UPDATED_HIRE_DATE)
            .position(UPDATED_POSITION);

        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployeeVi))
            )
            .andExpect(status().isOk());

        // Validate the EmployeeVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeViUpdatableFieldsEquals(partialUpdatedEmployeeVi, getPersistedEmployeeVi(partialUpdatedEmployeeVi));
    }

    @Test
    @Transactional
    void patchNonExistingEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employeeVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employeeVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployeeVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employeeVi.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employeeVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmployeeVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployeeVi() throws Exception {
        // Initialize the database
        insertedEmployeeVi = employeeViRepository.saveAndFlush(employeeVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employeeVi
        restEmployeeViMockMvc
            .perform(delete(ENTITY_API_URL_ID, employeeVi.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeViRepository.count();
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

    protected EmployeeVi getPersistedEmployeeVi(EmployeeVi employeeVi) {
        return employeeViRepository.findById(employeeVi.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeViToMatchAllProperties(EmployeeVi expectedEmployeeVi) {
        assertEmployeeViAllPropertiesEquals(expectedEmployeeVi, getPersistedEmployeeVi(expectedEmployeeVi));
    }

    protected void assertPersistedEmployeeViToMatchUpdatableProperties(EmployeeVi expectedEmployeeVi) {
        assertEmployeeViAllUpdatablePropertiesEquals(expectedEmployeeVi, getPersistedEmployeeVi(expectedEmployeeVi));
    }
}
