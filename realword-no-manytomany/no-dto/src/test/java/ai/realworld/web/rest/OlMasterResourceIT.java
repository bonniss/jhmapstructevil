package ai.realworld.web.rest;

import static ai.realworld.domain.OlMasterAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AndreiRightHand;
import ai.realworld.domain.OlMaster;
import ai.realworld.domain.enumeration.OlBakeryType;
import ai.realworld.repository.OlMasterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.UUID;
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
 * Integration tests for the {@link OlMasterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OlMasterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final OlBakeryType DEFAULT_BUSINESS_TYPE = OlBakeryType.CLIENT_COMPANY;
    private static final OlBakeryType UPDATED_BUSINESS_TYPE = OlBakeryType.CLIENT_INDIVIDUAL;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_HOTLINE = "AAAAAAAAAA";
    private static final String UPDATED_HOTLINE = "BBBBBBBBBB";

    private static final String DEFAULT_TAX_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TAX_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTS_JASON = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTS_JASON = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION_JASON = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION_JASON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/ol-masters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OlMasterRepository olMasterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOlMasterMockMvc;

    private OlMaster olMaster;

    private OlMaster insertedOlMaster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OlMaster createEntity() {
        return new OlMaster()
            .name(DEFAULT_NAME)
            .slug(DEFAULT_SLUG)
            .descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA)
            .businessType(DEFAULT_BUSINESS_TYPE)
            .email(DEFAULT_EMAIL)
            .hotline(DEFAULT_HOTLINE)
            .taxCode(DEFAULT_TAX_CODE)
            .contactsJason(DEFAULT_CONTACTS_JASON)
            .extensionJason(DEFAULT_EXTENSION_JASON)
            .isEnabled(DEFAULT_IS_ENABLED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OlMaster createUpdatedEntity() {
        return new OlMaster()
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .businessType(UPDATED_BUSINESS_TYPE)
            .email(UPDATED_EMAIL)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);
    }

    @BeforeEach
    public void initTest() {
        olMaster = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOlMaster != null) {
            olMasterRepository.delete(insertedOlMaster);
            insertedOlMaster = null;
        }
    }

    @Test
    @Transactional
    void createOlMaster() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OlMaster
        var returnedOlMaster = om.readValue(
            restOlMasterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OlMaster.class
        );

        // Validate the OlMaster in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOlMasterUpdatableFieldsEquals(returnedOlMaster, getPersistedOlMaster(returnedOlMaster));

        insertedOlMaster = returnedOlMaster;
    }

    @Test
    @Transactional
    void createOlMasterWithExistingId() throws Exception {
        // Create the OlMaster with an existing ID
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOlMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster)))
            .andExpect(status().isBadRequest());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        olMaster.setName(null);

        // Create the OlMaster, which fails.

        restOlMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        olMaster.setSlug(null);

        // Create the OlMaster, which fails.

        restOlMasterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOlMasters() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(olMaster.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hotline").value(hasItem(DEFAULT_HOTLINE)))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].extensionJason").value(hasItem(DEFAULT_EXTENSION_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @Test
    @Transactional
    void getOlMaster() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get the olMaster
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL_ID, olMaster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(olMaster.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA))
            .andExpect(jsonPath("$.businessType").value(DEFAULT_BUSINESS_TYPE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.hotline").value(DEFAULT_HOTLINE))
            .andExpect(jsonPath("$.taxCode").value(DEFAULT_TAX_CODE))
            .andExpect(jsonPath("$.contactsJason").value(DEFAULT_CONTACTS_JASON))
            .andExpect(jsonPath("$.extensionJason").value(DEFAULT_EXTENSION_JASON))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getOlMastersByIdFiltering() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        UUID id = olMaster.getId();

        defaultOlMasterFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllOlMastersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where name equals to
        defaultOlMasterFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlMastersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where name in
        defaultOlMasterFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlMastersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where name is not null
        defaultOlMasterFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where name contains
        defaultOlMasterFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllOlMastersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where name does not contain
        defaultOlMasterFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllOlMastersBySlugIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where slug equals to
        defaultOlMasterFiltering("slug.equals=" + DEFAULT_SLUG, "slug.equals=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllOlMastersBySlugIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where slug in
        defaultOlMasterFiltering("slug.in=" + DEFAULT_SLUG + "," + UPDATED_SLUG, "slug.in=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllOlMastersBySlugIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where slug is not null
        defaultOlMasterFiltering("slug.specified=true", "slug.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersBySlugContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where slug contains
        defaultOlMasterFiltering("slug.contains=" + DEFAULT_SLUG, "slug.contains=" + UPDATED_SLUG);
    }

    @Test
    @Transactional
    void getAllOlMastersBySlugNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where slug does not contain
        defaultOlMasterFiltering("slug.doesNotContain=" + UPDATED_SLUG, "slug.doesNotContain=" + DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void getAllOlMastersByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where descriptionHeitiga equals to
        defaultOlMasterFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where descriptionHeitiga in
        defaultOlMasterFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where descriptionHeitiga is not null
        defaultOlMasterFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where descriptionHeitiga contains
        defaultOlMasterFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where descriptionHeitiga does not contain
        defaultOlMasterFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByBusinessTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where businessType equals to
        defaultOlMasterFiltering("businessType.equals=" + DEFAULT_BUSINESS_TYPE, "businessType.equals=" + UPDATED_BUSINESS_TYPE);
    }

    @Test
    @Transactional
    void getAllOlMastersByBusinessTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where businessType in
        defaultOlMasterFiltering(
            "businessType.in=" + DEFAULT_BUSINESS_TYPE + "," + UPDATED_BUSINESS_TYPE,
            "businessType.in=" + UPDATED_BUSINESS_TYPE
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByBusinessTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where businessType is not null
        defaultOlMasterFiltering("businessType.specified=true", "businessType.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where email equals to
        defaultOlMasterFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOlMastersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where email in
        defaultOlMasterFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOlMastersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where email is not null
        defaultOlMasterFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where email contains
        defaultOlMasterFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllOlMastersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where email does not contain
        defaultOlMasterFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllOlMastersByHotlineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where hotline equals to
        defaultOlMasterFiltering("hotline.equals=" + DEFAULT_HOTLINE, "hotline.equals=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllOlMastersByHotlineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where hotline in
        defaultOlMasterFiltering("hotline.in=" + DEFAULT_HOTLINE + "," + UPDATED_HOTLINE, "hotline.in=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllOlMastersByHotlineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where hotline is not null
        defaultOlMasterFiltering("hotline.specified=true", "hotline.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByHotlineContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where hotline contains
        defaultOlMasterFiltering("hotline.contains=" + DEFAULT_HOTLINE, "hotline.contains=" + UPDATED_HOTLINE);
    }

    @Test
    @Transactional
    void getAllOlMastersByHotlineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where hotline does not contain
        defaultOlMasterFiltering("hotline.doesNotContain=" + UPDATED_HOTLINE, "hotline.doesNotContain=" + DEFAULT_HOTLINE);
    }

    @Test
    @Transactional
    void getAllOlMastersByTaxCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where taxCode equals to
        defaultOlMasterFiltering("taxCode.equals=" + DEFAULT_TAX_CODE, "taxCode.equals=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllOlMastersByTaxCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where taxCode in
        defaultOlMasterFiltering("taxCode.in=" + DEFAULT_TAX_CODE + "," + UPDATED_TAX_CODE, "taxCode.in=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllOlMastersByTaxCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where taxCode is not null
        defaultOlMasterFiltering("taxCode.specified=true", "taxCode.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByTaxCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where taxCode contains
        defaultOlMasterFiltering("taxCode.contains=" + DEFAULT_TAX_CODE, "taxCode.contains=" + UPDATED_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllOlMastersByTaxCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where taxCode does not contain
        defaultOlMasterFiltering("taxCode.doesNotContain=" + UPDATED_TAX_CODE, "taxCode.doesNotContain=" + DEFAULT_TAX_CODE);
    }

    @Test
    @Transactional
    void getAllOlMastersByContactsJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where contactsJason equals to
        defaultOlMasterFiltering("contactsJason.equals=" + DEFAULT_CONTACTS_JASON, "contactsJason.equals=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllOlMastersByContactsJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where contactsJason in
        defaultOlMasterFiltering(
            "contactsJason.in=" + DEFAULT_CONTACTS_JASON + "," + UPDATED_CONTACTS_JASON,
            "contactsJason.in=" + UPDATED_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByContactsJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where contactsJason is not null
        defaultOlMasterFiltering("contactsJason.specified=true", "contactsJason.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByContactsJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where contactsJason contains
        defaultOlMasterFiltering("contactsJason.contains=" + DEFAULT_CONTACTS_JASON, "contactsJason.contains=" + UPDATED_CONTACTS_JASON);
    }

    @Test
    @Transactional
    void getAllOlMastersByContactsJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where contactsJason does not contain
        defaultOlMasterFiltering(
            "contactsJason.doesNotContain=" + UPDATED_CONTACTS_JASON,
            "contactsJason.doesNotContain=" + DEFAULT_CONTACTS_JASON
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByExtensionJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where extensionJason equals to
        defaultOlMasterFiltering("extensionJason.equals=" + DEFAULT_EXTENSION_JASON, "extensionJason.equals=" + UPDATED_EXTENSION_JASON);
    }

    @Test
    @Transactional
    void getAllOlMastersByExtensionJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where extensionJason in
        defaultOlMasterFiltering(
            "extensionJason.in=" + DEFAULT_EXTENSION_JASON + "," + UPDATED_EXTENSION_JASON,
            "extensionJason.in=" + UPDATED_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByExtensionJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where extensionJason is not null
        defaultOlMasterFiltering("extensionJason.specified=true", "extensionJason.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByExtensionJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where extensionJason contains
        defaultOlMasterFiltering(
            "extensionJason.contains=" + DEFAULT_EXTENSION_JASON,
            "extensionJason.contains=" + UPDATED_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByExtensionJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where extensionJason does not contain
        defaultOlMasterFiltering(
            "extensionJason.doesNotContain=" + UPDATED_EXTENSION_JASON,
            "extensionJason.doesNotContain=" + DEFAULT_EXTENSION_JASON
        );
    }

    @Test
    @Transactional
    void getAllOlMastersByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where isEnabled equals to
        defaultOlMasterFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllOlMastersByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where isEnabled in
        defaultOlMasterFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllOlMastersByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        // Get all the olMasterList where isEnabled is not null
        defaultOlMasterFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllOlMastersByAddressIsEqualToSomething() throws Exception {
        AndreiRightHand address;
        if (TestUtil.findAll(em, AndreiRightHand.class).isEmpty()) {
            olMasterRepository.saveAndFlush(olMaster);
            address = AndreiRightHandResourceIT.createEntity();
        } else {
            address = TestUtil.findAll(em, AndreiRightHand.class).get(0);
        }
        em.persist(address);
        em.flush();
        olMaster.setAddress(address);
        olMasterRepository.saveAndFlush(olMaster);
        Long addressId = address.getId();
        // Get all the olMasterList where address equals to addressId
        defaultOlMasterShouldBeFound("addressId.equals=" + addressId);

        // Get all the olMasterList where address equals to (addressId + 1)
        defaultOlMasterShouldNotBeFound("addressId.equals=" + (addressId + 1));
    }

    private void defaultOlMasterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOlMasterShouldBeFound(shouldBeFound);
        defaultOlMasterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOlMasterShouldBeFound(String filter) throws Exception {
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(olMaster.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].businessType").value(hasItem(DEFAULT_BUSINESS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].hotline").value(hasItem(DEFAULT_HOTLINE)))
            .andExpect(jsonPath("$.[*].taxCode").value(hasItem(DEFAULT_TAX_CODE)))
            .andExpect(jsonPath("$.[*].contactsJason").value(hasItem(DEFAULT_CONTACTS_JASON)))
            .andExpect(jsonPath("$.[*].extensionJason").value(hasItem(DEFAULT_EXTENSION_JASON)))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOlMasterShouldNotBeFound(String filter) throws Exception {
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOlMasterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOlMaster() throws Exception {
        // Get the olMaster
        restOlMasterMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOlMaster() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olMaster
        OlMaster updatedOlMaster = olMasterRepository.findById(olMaster.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOlMaster are not directly saved in db
        em.detach(updatedOlMaster);
        updatedOlMaster
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .businessType(UPDATED_BUSINESS_TYPE)
            .email(UPDATED_EMAIL)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restOlMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOlMaster.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOlMaster))
            )
            .andExpect(status().isOk());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOlMasterToMatchAllProperties(updatedOlMaster);
    }

    @Test
    @Transactional
    void putNonExistingOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, olMaster.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(olMaster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOlMasterWithPatch() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olMaster using partial update
        OlMaster partialUpdatedOlMaster = new OlMaster();
        partialUpdatedOlMaster.setId(olMaster.getId());

        partialUpdatedOlMaster.name(UPDATED_NAME).extensionJason(UPDATED_EXTENSION_JASON).isEnabled(UPDATED_IS_ENABLED);

        restOlMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOlMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOlMaster))
            )
            .andExpect(status().isOk());

        // Validate the OlMaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOlMasterUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOlMaster, olMaster), getPersistedOlMaster(olMaster));
    }

    @Test
    @Transactional
    void fullUpdateOlMasterWithPatch() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the olMaster using partial update
        OlMaster partialUpdatedOlMaster = new OlMaster();
        partialUpdatedOlMaster.setId(olMaster.getId());

        partialUpdatedOlMaster
            .name(UPDATED_NAME)
            .slug(UPDATED_SLUG)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .businessType(UPDATED_BUSINESS_TYPE)
            .email(UPDATED_EMAIL)
            .hotline(UPDATED_HOTLINE)
            .taxCode(UPDATED_TAX_CODE)
            .contactsJason(UPDATED_CONTACTS_JASON)
            .extensionJason(UPDATED_EXTENSION_JASON)
            .isEnabled(UPDATED_IS_ENABLED);

        restOlMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOlMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOlMaster))
            )
            .andExpect(status().isOk());

        // Validate the OlMaster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOlMasterUpdatableFieldsEquals(partialUpdatedOlMaster, getPersistedOlMaster(partialUpdatedOlMaster));
    }

    @Test
    @Transactional
    void patchNonExistingOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, olMaster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(olMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(olMaster))
            )
            .andExpect(status().isBadRequest());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOlMaster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        olMaster.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOlMasterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(olMaster)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OlMaster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOlMaster() throws Exception {
        // Initialize the database
        insertedOlMaster = olMasterRepository.saveAndFlush(olMaster);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the olMaster
        restOlMasterMockMvc
            .perform(delete(ENTITY_API_URL_ID, olMaster.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return olMasterRepository.count();
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

    protected OlMaster getPersistedOlMaster(OlMaster olMaster) {
        return olMasterRepository.findById(olMaster.getId()).orElseThrow();
    }

    protected void assertPersistedOlMasterToMatchAllProperties(OlMaster expectedOlMaster) {
        assertOlMasterAllPropertiesEquals(expectedOlMaster, getPersistedOlMaster(expectedOlMaster));
    }

    protected void assertPersistedOlMasterToMatchUpdatableProperties(OlMaster expectedOlMaster) {
        assertOlMasterAllUpdatablePropertiesEquals(expectedOlMaster, getPersistedOlMaster(expectedOlMaster));
    }
}
