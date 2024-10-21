package ai.realworld.web.rest;

import static ai.realworld.domain.AlProtyViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlAppleVi;
import ai.realworld.domain.AlProProVi;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.Metaverse;
import ai.realworld.domain.enumeration.PeteStatus;
import ai.realworld.repository.AlProtyViRepository;
import ai.realworld.service.AlProtyViService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AlProtyViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlProtyViResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_HEITIGA = "BBBBBBBBBB";

    private static final String DEFAULT_COORDINATE = "AAAAAAAAAA";
    private static final String UPDATED_COORDINATE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final PeteStatus DEFAULT_STATUS = PeteStatus.ACTIVE;
    private static final PeteStatus UPDATED_STATUS = PeteStatus.INACTIVE;

    private static final Boolean DEFAULT_IS_ENABLED = false;
    private static final Boolean UPDATED_IS_ENABLED = true;

    private static final String ENTITY_API_URL = "/api/al-proty-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlProtyViRepository alProtyViRepository;

    @Mock
    private AlProtyViRepository alProtyViRepositoryMock;

    @Mock
    private AlProtyViService alProtyViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlProtyViMockMvc;

    private AlProtyVi alProtyVi;

    private AlProtyVi insertedAlProtyVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProtyVi createEntity(EntityManager em) {
        AlProtyVi alProtyVi = new AlProtyVi()
            .name(DEFAULT_NAME)
            .descriptionHeitiga(DEFAULT_DESCRIPTION_HEITIGA)
            .coordinate(DEFAULT_COORDINATE)
            .code(DEFAULT_CODE)
            .status(DEFAULT_STATUS)
            .isEnabled(DEFAULT_IS_ENABLED);
        // Add required entity
        AlAppleVi alAppleVi;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alAppleVi = AlAppleViResourceIT.createEntity(em);
            em.persist(alAppleVi);
            em.flush();
        } else {
            alAppleVi = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        alProtyVi.setOperator(alAppleVi);
        return alProtyVi;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlProtyVi createUpdatedEntity(EntityManager em) {
        AlProtyVi updatedAlProtyVi = new AlProtyVi()
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);
        // Add required entity
        AlAppleVi alAppleVi;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alAppleVi = AlAppleViResourceIT.createUpdatedEntity(em);
            em.persist(alAppleVi);
            em.flush();
        } else {
            alAppleVi = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        updatedAlProtyVi.setOperator(alAppleVi);
        return updatedAlProtyVi;
    }

    @BeforeEach
    public void initTest() {
        alProtyVi = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlProtyVi != null) {
            alProtyViRepository.delete(insertedAlProtyVi);
            insertedAlProtyVi = null;
        }
    }

    @Test
    @Transactional
    void createAlProtyVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlProtyVi
        var returnedAlProtyVi = om.readValue(
            restAlProtyViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlProtyVi.class
        );

        // Validate the AlProtyVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAlProtyViUpdatableFieldsEquals(returnedAlProtyVi, getPersistedAlProtyVi(returnedAlProtyVi));

        insertedAlProtyVi = returnedAlProtyVi;
    }

    @Test
    @Transactional
    void createAlProtyViWithExistingId() throws Exception {
        // Create the AlProtyVi with an existing ID
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlProtyViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi)))
            .andExpect(status().isBadRequest());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alProtyVi.setName(null);

        // Create the AlProtyVi, which fails.

        restAlProtyViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlProtyVis() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProtyVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].coordinate").value(hasItem(DEFAULT_COORDINATE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlProtyVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(alProtyViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProtyViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alProtyViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlProtyVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alProtyViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlProtyViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alProtyViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlProtyVi() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get the alProtyVi
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL_ID, alProtyVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alProtyVi.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.descriptionHeitiga").value(DEFAULT_DESCRIPTION_HEITIGA))
            .andExpect(jsonPath("$.coordinate").value(DEFAULT_COORDINATE))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isEnabled").value(DEFAULT_IS_ENABLED.booleanValue()));
    }

    @Test
    @Transactional
    void getAlProtyVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        UUID id = alProtyVi.getId();

        defaultAlProtyViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where name equals to
        defaultAlProtyViFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where name in
        defaultAlProtyViFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where name is not null
        defaultAlProtyViFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where name contains
        defaultAlProtyViFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where name does not contain
        defaultAlProtyViFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByDescriptionHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where descriptionHeitiga equals to
        defaultAlProtyViFiltering(
            "descriptionHeitiga.equals=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.equals=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtyVisByDescriptionHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where descriptionHeitiga in
        defaultAlProtyViFiltering(
            "descriptionHeitiga.in=" + DEFAULT_DESCRIPTION_HEITIGA + "," + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.in=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtyVisByDescriptionHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where descriptionHeitiga is not null
        defaultAlProtyViFiltering("descriptionHeitiga.specified=true", "descriptionHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByDescriptionHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where descriptionHeitiga contains
        defaultAlProtyViFiltering(
            "descriptionHeitiga.contains=" + DEFAULT_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.contains=" + UPDATED_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtyVisByDescriptionHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where descriptionHeitiga does not contain
        defaultAlProtyViFiltering(
            "descriptionHeitiga.doesNotContain=" + UPDATED_DESCRIPTION_HEITIGA,
            "descriptionHeitiga.doesNotContain=" + DEFAULT_DESCRIPTION_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCoordinateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where coordinate equals to
        defaultAlProtyViFiltering("coordinate.equals=" + DEFAULT_COORDINATE, "coordinate.equals=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCoordinateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where coordinate in
        defaultAlProtyViFiltering("coordinate.in=" + DEFAULT_COORDINATE + "," + UPDATED_COORDINATE, "coordinate.in=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCoordinateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where coordinate is not null
        defaultAlProtyViFiltering("coordinate.specified=true", "coordinate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCoordinateContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where coordinate contains
        defaultAlProtyViFiltering("coordinate.contains=" + DEFAULT_COORDINATE, "coordinate.contains=" + UPDATED_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCoordinateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where coordinate does not contain
        defaultAlProtyViFiltering("coordinate.doesNotContain=" + UPDATED_COORDINATE, "coordinate.doesNotContain=" + DEFAULT_COORDINATE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where code equals to
        defaultAlProtyViFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where code in
        defaultAlProtyViFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where code is not null
        defaultAlProtyViFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where code contains
        defaultAlProtyViFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where code does not contain
        defaultAlProtyViFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where status equals to
        defaultAlProtyViFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where status in
        defaultAlProtyViFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where status is not null
        defaultAlProtyViFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByIsEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where isEnabled equals to
        defaultAlProtyViFiltering("isEnabled.equals=" + DEFAULT_IS_ENABLED, "isEnabled.equals=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByIsEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where isEnabled in
        defaultAlProtyViFiltering("isEnabled.in=" + DEFAULT_IS_ENABLED + "," + UPDATED_IS_ENABLED, "isEnabled.in=" + UPDATED_IS_ENABLED);
    }

    @Test
    @Transactional
    void getAllAlProtyVisByIsEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        // Get all the alProtyViList where isEnabled is not null
        defaultAlProtyViFiltering("isEnabled.specified=true", "isEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllAlProtyVisByParentIsEqualToSomething() throws Exception {
        AlProtyVi parent;
        if (TestUtil.findAll(em, AlProtyVi.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            parent = AlProtyViResourceIT.createEntity(em);
        } else {
            parent = TestUtil.findAll(em, AlProtyVi.class).get(0);
        }
        em.persist(parent);
        em.flush();
        alProtyVi.setParent(parent);
        alProtyViRepository.saveAndFlush(alProtyVi);
        UUID parentId = parent.getId();
        // Get all the alProtyViList where parent equals to parentId
        defaultAlProtyViShouldBeFound("parentId.equals=" + parentId);

        // Get all the alProtyViList where parent equals to UUID.randomUUID()
        defaultAlProtyViShouldNotBeFound("parentId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtyVisByOperatorIsEqualToSomething() throws Exception {
        AlAppleVi operator;
        if (TestUtil.findAll(em, AlAppleVi.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            operator = AlAppleViResourceIT.createEntity(em);
        } else {
            operator = TestUtil.findAll(em, AlAppleVi.class).get(0);
        }
        em.persist(operator);
        em.flush();
        alProtyVi.setOperator(operator);
        alProtyViRepository.saveAndFlush(alProtyVi);
        UUID operatorId = operator.getId();
        // Get all the alProtyViList where operator equals to operatorId
        defaultAlProtyViShouldBeFound("operatorId.equals=" + operatorId);

        // Get all the alProtyViList where operator equals to UUID.randomUUID()
        defaultAlProtyViShouldNotBeFound("operatorId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtyVisByPropertyProfileIsEqualToSomething() throws Exception {
        AlProProVi propertyProfile;
        if (TestUtil.findAll(em, AlProProVi.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            propertyProfile = AlProProViResourceIT.createEntity();
        } else {
            propertyProfile = TestUtil.findAll(em, AlProProVi.class).get(0);
        }
        em.persist(propertyProfile);
        em.flush();
        alProtyVi.setPropertyProfile(propertyProfile);
        alProtyViRepository.saveAndFlush(alProtyVi);
        UUID propertyProfileId = propertyProfile.getId();
        // Get all the alProtyViList where propertyProfile equals to propertyProfileId
        defaultAlProtyViShouldBeFound("propertyProfileId.equals=" + propertyProfileId);

        // Get all the alProtyViList where propertyProfile equals to UUID.randomUUID()
        defaultAlProtyViShouldNotBeFound("propertyProfileId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtyVisByAvatarIsEqualToSomething() throws Exception {
        Metaverse avatar;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            avatar = MetaverseResourceIT.createEntity();
        } else {
            avatar = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(avatar);
        em.flush();
        alProtyVi.setAvatar(avatar);
        alProtyViRepository.saveAndFlush(alProtyVi);
        Long avatarId = avatar.getId();
        // Get all the alProtyViList where avatar equals to avatarId
        defaultAlProtyViShouldBeFound("avatarId.equals=" + avatarId);

        // Get all the alProtyViList where avatar equals to (avatarId + 1)
        defaultAlProtyViShouldNotBeFound("avatarId.equals=" + (avatarId + 1));
    }

    @Test
    @Transactional
    void getAllAlProtyVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alProtyVi.setApplication(application);
        alProtyViRepository.saveAndFlush(alProtyVi);
        UUID applicationId = application.getId();
        // Get all the alProtyViList where application equals to applicationId
        defaultAlProtyViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alProtyViList where application equals to UUID.randomUUID()
        defaultAlProtyViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlProtyVisByImageIsEqualToSomething() throws Exception {
        Metaverse image;
        if (TestUtil.findAll(em, Metaverse.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            image = MetaverseResourceIT.createEntity();
        } else {
            image = TestUtil.findAll(em, Metaverse.class).get(0);
        }
        em.persist(image);
        em.flush();
        alProtyVi.addImage(image);
        alProtyViRepository.saveAndFlush(alProtyVi);
        Long imageId = image.getId();
        // Get all the alProtyViList where image equals to imageId
        defaultAlProtyViShouldBeFound("imageId.equals=" + imageId);

        // Get all the alProtyViList where image equals to (imageId + 1)
        defaultAlProtyViShouldNotBeFound("imageId.equals=" + (imageId + 1));
    }

    @Test
    @Transactional
    void getAllAlProtyVisByBookingIsEqualToSomething() throws Exception {
        AlPyuJokerVi booking;
        if (TestUtil.findAll(em, AlPyuJokerVi.class).isEmpty()) {
            alProtyViRepository.saveAndFlush(alProtyVi);
            booking = AlPyuJokerViResourceIT.createEntity();
        } else {
            booking = TestUtil.findAll(em, AlPyuJokerVi.class).get(0);
        }
        em.persist(booking);
        em.flush();
        alProtyVi.addBooking(booking);
        alProtyViRepository.saveAndFlush(alProtyVi);
        UUID bookingId = booking.getId();
        // Get all the alProtyViList where booking equals to bookingId
        defaultAlProtyViShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the alProtyViList where booking equals to UUID.randomUUID()
        defaultAlProtyViShouldNotBeFound("bookingId.equals=" + UUID.randomUUID());
    }

    private void defaultAlProtyViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlProtyViShouldBeFound(shouldBeFound);
        defaultAlProtyViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlProtyViShouldBeFound(String filter) throws Exception {
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alProtyVi.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].descriptionHeitiga").value(hasItem(DEFAULT_DESCRIPTION_HEITIGA)))
            .andExpect(jsonPath("$.[*].coordinate").value(hasItem(DEFAULT_COORDINATE)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isEnabled").value(hasItem(DEFAULT_IS_ENABLED.booleanValue())));

        // Check, that the count call also returns 1
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlProtyViShouldNotBeFound(String filter) throws Exception {
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlProtyViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlProtyVi() throws Exception {
        // Get the alProtyVi
        restAlProtyViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlProtyVi() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProtyVi
        AlProtyVi updatedAlProtyVi = alProtyViRepository.findById(alProtyVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlProtyVi are not directly saved in db
        em.detach(updatedAlProtyVi);
        updatedAlProtyVi
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProtyViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAlProtyVi.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAlProtyVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlProtyViToMatchAllProperties(updatedAlProtyVi);
    }

    @Test
    @Transactional
    void putNonExistingAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alProtyVi.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alProtyVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlProtyViWithPatch() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProtyVi using partial update
        AlProtyVi partialUpdatedAlProtyVi = new AlProtyVi();
        partialUpdatedAlProtyVi.setId(alProtyVi.getId());

        restAlProtyViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProtyVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProtyVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProtyVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProtyViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlProtyVi, alProtyVi),
            getPersistedAlProtyVi(alProtyVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlProtyViWithPatch() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alProtyVi using partial update
        AlProtyVi partialUpdatedAlProtyVi = new AlProtyVi();
        partialUpdatedAlProtyVi.setId(alProtyVi.getId());

        partialUpdatedAlProtyVi
            .name(UPDATED_NAME)
            .descriptionHeitiga(UPDATED_DESCRIPTION_HEITIGA)
            .coordinate(UPDATED_COORDINATE)
            .code(UPDATED_CODE)
            .status(UPDATED_STATUS)
            .isEnabled(UPDATED_IS_ENABLED);

        restAlProtyViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlProtyVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlProtyVi))
            )
            .andExpect(status().isOk());

        // Validate the AlProtyVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlProtyViUpdatableFieldsEquals(partialUpdatedAlProtyVi, getPersistedAlProtyVi(partialUpdatedAlProtyVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alProtyVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProtyVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alProtyVi))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlProtyVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alProtyVi.setId(UUID.randomUUID());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlProtyViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alProtyVi)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlProtyVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlProtyVi() throws Exception {
        // Initialize the database
        insertedAlProtyVi = alProtyViRepository.saveAndFlush(alProtyVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alProtyVi
        restAlProtyViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alProtyVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alProtyViRepository.count();
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

    protected AlProtyVi getPersistedAlProtyVi(AlProtyVi alProtyVi) {
        return alProtyViRepository.findById(alProtyVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlProtyViToMatchAllProperties(AlProtyVi expectedAlProtyVi) {
        assertAlProtyViAllPropertiesEquals(expectedAlProtyVi, getPersistedAlProtyVi(expectedAlProtyVi));
    }

    protected void assertPersistedAlProtyViToMatchUpdatableProperties(AlProtyVi expectedAlProtyVi) {
        assertAlProtyViAllUpdatablePropertiesEquals(expectedAlProtyVi, getPersistedAlProtyVi(expectedAlProtyVi));
    }
}
