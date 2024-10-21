package ai.realworld.web.rest;

import static ai.realworld.domain.AlPyuJokerViAsserts.*;
import static ai.realworld.web.rest.TestUtil.createUpdateProxyForBean;
import static ai.realworld.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ai.realworld.IntegrationTest;
import ai.realworld.domain.AlPacino;
import ai.realworld.domain.AlProtyVi;
import ai.realworld.domain.AlPyuJokerVi;
import ai.realworld.domain.EdSheeranVi;
import ai.realworld.domain.JohnLennon;
import ai.realworld.domain.enumeration.BenedictType;
import ai.realworld.domain.enumeration.PaulBargainStatus;
import ai.realworld.repository.AlPyuJokerViRepository;
import ai.realworld.service.AlPyuJokerViService;
import ai.realworld.service.dto.AlPyuJokerViDTO;
import ai.realworld.service.mapper.AlPyuJokerViMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link AlPyuJokerViResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AlPyuJokerViResourceIT {

    private static final String DEFAULT_BOOKING_NO = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_NO = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE_HEITIGA = "AAAAAAAAAA";
    private static final String UPDATED_NOTE_HEITIGA = "BBBBBBBBBB";

    private static final BenedictType DEFAULT_PERIOD_TYPE = BenedictType.NIGHT;
    private static final BenedictType UPDATED_PERIOD_TYPE = BenedictType.DAYLIGHT;

    private static final Instant DEFAULT_FROM_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_IN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_IN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CHECK_OUT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CHECK_OUT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_NUMBER_OF_ADULTS = 1;
    private static final Integer UPDATED_NUMBER_OF_ADULTS = 2;
    private static final Integer SMALLER_NUMBER_OF_ADULTS = 1 - 1;

    private static final Integer DEFAULT_NUMBER_OF_PRESCHOOLERS = 0;
    private static final Integer UPDATED_NUMBER_OF_PRESCHOOLERS = 1;
    private static final Integer SMALLER_NUMBER_OF_PRESCHOOLERS = 0 - 1;

    private static final Integer DEFAULT_NUMBER_OF_CHILDREN = 0;
    private static final Integer UPDATED_NUMBER_OF_CHILDREN = 1;
    private static final Integer SMALLER_NUMBER_OF_CHILDREN = 0 - 1;

    private static final BigDecimal DEFAULT_BOOKING_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BOOKING_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_BOOKING_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_EXTRA_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_EXTRA_FEE = new BigDecimal(2);
    private static final BigDecimal SMALLER_EXTRA_FEE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final PaulBargainStatus DEFAULT_BOOKING_STATUS = PaulBargainStatus.PENDING;
    private static final PaulBargainStatus UPDATED_BOOKING_STATUS = PaulBargainStatus.RESERVED;

    private static final String DEFAULT_HISTORY_REF_JASON = "AAAAAAAAAA";
    private static final String UPDATED_HISTORY_REF_JASON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/al-pyu-joker-vis";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlPyuJokerViRepository alPyuJokerViRepository;

    @Mock
    private AlPyuJokerViRepository alPyuJokerViRepositoryMock;

    @Autowired
    private AlPyuJokerViMapper alPyuJokerViMapper;

    @Mock
    private AlPyuJokerViService alPyuJokerViServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlPyuJokerViMockMvc;

    private AlPyuJokerVi alPyuJokerVi;

    private AlPyuJokerVi insertedAlPyuJokerVi;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuJokerVi createEntity() {
        return new AlPyuJokerVi()
            .bookingNo(DEFAULT_BOOKING_NO)
            .noteHeitiga(DEFAULT_NOTE_HEITIGA)
            .periodType(DEFAULT_PERIOD_TYPE)
            .fromDate(DEFAULT_FROM_DATE)
            .toDate(DEFAULT_TO_DATE)
            .checkInDate(DEFAULT_CHECK_IN_DATE)
            .checkOutDate(DEFAULT_CHECK_OUT_DATE)
            .numberOfAdults(DEFAULT_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(DEFAULT_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(DEFAULT_NUMBER_OF_CHILDREN)
            .bookingPrice(DEFAULT_BOOKING_PRICE)
            .extraFee(DEFAULT_EXTRA_FEE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .bookingStatus(DEFAULT_BOOKING_STATUS)
            .historyRefJason(DEFAULT_HISTORY_REF_JASON);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlPyuJokerVi createUpdatedEntity() {
        return new AlPyuJokerVi()
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);
    }

    @BeforeEach
    public void initTest() {
        alPyuJokerVi = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAlPyuJokerVi != null) {
            alPyuJokerViRepository.delete(insertedAlPyuJokerVi);
            insertedAlPyuJokerVi = null;
        }
    }

    @Test
    @Transactional
    void createAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);
        var returnedAlPyuJokerViDTO = om.readValue(
            restAlPyuJokerViMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJokerViDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlPyuJokerViDTO.class
        );

        // Validate the AlPyuJokerVi in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlPyuJokerVi = alPyuJokerViMapper.toEntity(returnedAlPyuJokerViDTO);
        assertAlPyuJokerViUpdatableFieldsEquals(returnedAlPyuJokerVi, getPersistedAlPyuJokerVi(returnedAlPyuJokerVi));

        insertedAlPyuJokerVi = returnedAlPyuJokerVi;
    }

    @Test
    @Transactional
    void createAlPyuJokerViWithExistingId() throws Exception {
        // Create the AlPyuJokerVi with an existing ID
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlPyuJokerViMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJokerViDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVis() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuJokerVi.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].noteHeitiga").value(hasItem(DEFAULT_NOTE_HEITIGA)))
            .andExpect(jsonPath("$.[*].periodType").value(hasItem(DEFAULT_PERIOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].bookingPrice").value(hasItem(sameNumber(DEFAULT_BOOKING_PRICE))))
            .andExpect(jsonPath("$.[*].extraFee").value(hasItem(sameNumber(DEFAULT_EXTRA_FEE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].historyRefJason").value(hasItem(DEFAULT_HISTORY_REF_JASON)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlPyuJokerVisWithEagerRelationshipsIsEnabled() throws Exception {
        when(alPyuJokerViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlPyuJokerViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(alPyuJokerViServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAlPyuJokerVisWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(alPyuJokerViServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAlPyuJokerViMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(alPyuJokerViRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAlPyuJokerVi() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get the alPyuJokerVi
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL_ID, alPyuJokerVi.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alPyuJokerVi.getId().toString()))
            .andExpect(jsonPath("$.bookingNo").value(DEFAULT_BOOKING_NO))
            .andExpect(jsonPath("$.noteHeitiga").value(DEFAULT_NOTE_HEITIGA))
            .andExpect(jsonPath("$.periodType").value(DEFAULT_PERIOD_TYPE.toString()))
            .andExpect(jsonPath("$.fromDate").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.toDate").value(DEFAULT_TO_DATE.toString()))
            .andExpect(jsonPath("$.checkInDate").value(DEFAULT_CHECK_IN_DATE.toString()))
            .andExpect(jsonPath("$.checkOutDate").value(DEFAULT_CHECK_OUT_DATE.toString()))
            .andExpect(jsonPath("$.numberOfAdults").value(DEFAULT_NUMBER_OF_ADULTS))
            .andExpect(jsonPath("$.numberOfPreschoolers").value(DEFAULT_NUMBER_OF_PRESCHOOLERS))
            .andExpect(jsonPath("$.numberOfChildren").value(DEFAULT_NUMBER_OF_CHILDREN))
            .andExpect(jsonPath("$.bookingPrice").value(sameNumber(DEFAULT_BOOKING_PRICE)))
            .andExpect(jsonPath("$.extraFee").value(sameNumber(DEFAULT_EXTRA_FEE)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.bookingStatus").value(DEFAULT_BOOKING_STATUS.toString()))
            .andExpect(jsonPath("$.historyRefJason").value(DEFAULT_HISTORY_REF_JASON));
    }

    @Test
    @Transactional
    void getAlPyuJokerVisByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        UUID id = alPyuJokerVi.getId();

        defaultAlPyuJokerViFiltering("id.equals=" + id, "id.notEquals=" + id);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingNo equals to
        defaultAlPyuJokerViFiltering("bookingNo.equals=" + DEFAULT_BOOKING_NO, "bookingNo.equals=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingNo in
        defaultAlPyuJokerViFiltering("bookingNo.in=" + DEFAULT_BOOKING_NO + "," + UPDATED_BOOKING_NO, "bookingNo.in=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingNo is not null
        defaultAlPyuJokerViFiltering("bookingNo.specified=true", "bookingNo.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingNoContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingNo contains
        defaultAlPyuJokerViFiltering("bookingNo.contains=" + DEFAULT_BOOKING_NO, "bookingNo.contains=" + UPDATED_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingNoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingNo does not contain
        defaultAlPyuJokerViFiltering("bookingNo.doesNotContain=" + UPDATED_BOOKING_NO, "bookingNo.doesNotContain=" + DEFAULT_BOOKING_NO);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNoteHeitigaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where noteHeitiga equals to
        defaultAlPyuJokerViFiltering("noteHeitiga.equals=" + DEFAULT_NOTE_HEITIGA, "noteHeitiga.equals=" + UPDATED_NOTE_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNoteHeitigaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where noteHeitiga in
        defaultAlPyuJokerViFiltering(
            "noteHeitiga.in=" + DEFAULT_NOTE_HEITIGA + "," + UPDATED_NOTE_HEITIGA,
            "noteHeitiga.in=" + UPDATED_NOTE_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNoteHeitigaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where noteHeitiga is not null
        defaultAlPyuJokerViFiltering("noteHeitiga.specified=true", "noteHeitiga.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNoteHeitigaContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where noteHeitiga contains
        defaultAlPyuJokerViFiltering("noteHeitiga.contains=" + DEFAULT_NOTE_HEITIGA, "noteHeitiga.contains=" + UPDATED_NOTE_HEITIGA);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNoteHeitigaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where noteHeitiga does not contain
        defaultAlPyuJokerViFiltering(
            "noteHeitiga.doesNotContain=" + UPDATED_NOTE_HEITIGA,
            "noteHeitiga.doesNotContain=" + DEFAULT_NOTE_HEITIGA
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByPeriodTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where periodType equals to
        defaultAlPyuJokerViFiltering("periodType.equals=" + DEFAULT_PERIOD_TYPE, "periodType.equals=" + UPDATED_PERIOD_TYPE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByPeriodTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where periodType in
        defaultAlPyuJokerViFiltering(
            "periodType.in=" + DEFAULT_PERIOD_TYPE + "," + UPDATED_PERIOD_TYPE,
            "periodType.in=" + UPDATED_PERIOD_TYPE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByPeriodTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where periodType is not null
        defaultAlPyuJokerViFiltering("periodType.specified=true", "periodType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByFromDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where fromDate equals to
        defaultAlPyuJokerViFiltering("fromDate.equals=" + DEFAULT_FROM_DATE, "fromDate.equals=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByFromDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where fromDate in
        defaultAlPyuJokerViFiltering("fromDate.in=" + DEFAULT_FROM_DATE + "," + UPDATED_FROM_DATE, "fromDate.in=" + UPDATED_FROM_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByFromDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where fromDate is not null
        defaultAlPyuJokerViFiltering("fromDate.specified=true", "fromDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByToDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where toDate equals to
        defaultAlPyuJokerViFiltering("toDate.equals=" + DEFAULT_TO_DATE, "toDate.equals=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByToDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where toDate in
        defaultAlPyuJokerViFiltering("toDate.in=" + DEFAULT_TO_DATE + "," + UPDATED_TO_DATE, "toDate.in=" + UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByToDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where toDate is not null
        defaultAlPyuJokerViFiltering("toDate.specified=true", "toDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckInDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkInDate equals to
        defaultAlPyuJokerViFiltering("checkInDate.equals=" + DEFAULT_CHECK_IN_DATE, "checkInDate.equals=" + UPDATED_CHECK_IN_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckInDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkInDate in
        defaultAlPyuJokerViFiltering(
            "checkInDate.in=" + DEFAULT_CHECK_IN_DATE + "," + UPDATED_CHECK_IN_DATE,
            "checkInDate.in=" + UPDATED_CHECK_IN_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckInDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkInDate is not null
        defaultAlPyuJokerViFiltering("checkInDate.specified=true", "checkInDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckOutDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkOutDate equals to
        defaultAlPyuJokerViFiltering("checkOutDate.equals=" + DEFAULT_CHECK_OUT_DATE, "checkOutDate.equals=" + UPDATED_CHECK_OUT_DATE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckOutDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkOutDate in
        defaultAlPyuJokerViFiltering(
            "checkOutDate.in=" + DEFAULT_CHECK_OUT_DATE + "," + UPDATED_CHECK_OUT_DATE,
            "checkOutDate.in=" + UPDATED_CHECK_OUT_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCheckOutDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where checkOutDate is not null
        defaultAlPyuJokerViFiltering("checkOutDate.specified=true", "checkOutDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults equals to
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.equals=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.equals=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults in
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.in=" + DEFAULT_NUMBER_OF_ADULTS + "," + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.in=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults is not null
        defaultAlPyuJokerViFiltering("numberOfAdults.specified=true", "numberOfAdults.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThanOrEqual=" + UPDATED_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults is less than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.lessThanOrEqual=" + DEFAULT_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThanOrEqual=" + SMALLER_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults is less than
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.lessThan=" + UPDATED_NUMBER_OF_ADULTS,
            "numberOfAdults.lessThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfAdultsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfAdults is greater than
        defaultAlPyuJokerViFiltering(
            "numberOfAdults.greaterThan=" + SMALLER_NUMBER_OF_ADULTS,
            "numberOfAdults.greaterThan=" + DEFAULT_NUMBER_OF_ADULTS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers equals to
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.equals=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.equals=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers in
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.in=" + DEFAULT_NUMBER_OF_PRESCHOOLERS + "," + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.in=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers is not null
        defaultAlPyuJokerViFiltering("numberOfPreschoolers.specified=true", "numberOfPreschoolers.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThanOrEqual=" + UPDATED_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers is less than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.lessThanOrEqual=" + DEFAULT_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThanOrEqual=" + SMALLER_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers is less than
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.lessThan=" + UPDATED_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.lessThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfPreschoolersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfPreschoolers is greater than
        defaultAlPyuJokerViFiltering(
            "numberOfPreschoolers.greaterThan=" + SMALLER_NUMBER_OF_PRESCHOOLERS,
            "numberOfPreschoolers.greaterThan=" + DEFAULT_NUMBER_OF_PRESCHOOLERS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren equals to
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.equals=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.equals=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren in
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.in=" + DEFAULT_NUMBER_OF_CHILDREN + "," + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.in=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren is not null
        defaultAlPyuJokerViFiltering("numberOfChildren.specified=true", "numberOfChildren.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.greaterThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThanOrEqual=" + UPDATED_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren is less than or equal to
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.lessThanOrEqual=" + DEFAULT_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThanOrEqual=" + SMALLER_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren is less than
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.lessThan=" + UPDATED_NUMBER_OF_CHILDREN,
            "numberOfChildren.lessThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByNumberOfChildrenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where numberOfChildren is greater than
        defaultAlPyuJokerViFiltering(
            "numberOfChildren.greaterThan=" + SMALLER_NUMBER_OF_CHILDREN,
            "numberOfChildren.greaterThan=" + DEFAULT_NUMBER_OF_CHILDREN
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice equals to
        defaultAlPyuJokerViFiltering("bookingPrice.equals=" + DEFAULT_BOOKING_PRICE, "bookingPrice.equals=" + UPDATED_BOOKING_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice in
        defaultAlPyuJokerViFiltering(
            "bookingPrice.in=" + DEFAULT_BOOKING_PRICE + "," + UPDATED_BOOKING_PRICE,
            "bookingPrice.in=" + UPDATED_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice is not null
        defaultAlPyuJokerViFiltering("bookingPrice.specified=true", "bookingPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "bookingPrice.greaterThanOrEqual=" + DEFAULT_BOOKING_PRICE,
            "bookingPrice.greaterThanOrEqual=" + UPDATED_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice is less than or equal to
        defaultAlPyuJokerViFiltering(
            "bookingPrice.lessThanOrEqual=" + DEFAULT_BOOKING_PRICE,
            "bookingPrice.lessThanOrEqual=" + SMALLER_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice is less than
        defaultAlPyuJokerViFiltering("bookingPrice.lessThan=" + UPDATED_BOOKING_PRICE, "bookingPrice.lessThan=" + DEFAULT_BOOKING_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingPrice is greater than
        defaultAlPyuJokerViFiltering(
            "bookingPrice.greaterThan=" + SMALLER_BOOKING_PRICE,
            "bookingPrice.greaterThan=" + DEFAULT_BOOKING_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee equals to
        defaultAlPyuJokerViFiltering("extraFee.equals=" + DEFAULT_EXTRA_FEE, "extraFee.equals=" + UPDATED_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee in
        defaultAlPyuJokerViFiltering("extraFee.in=" + DEFAULT_EXTRA_FEE + "," + UPDATED_EXTRA_FEE, "extraFee.in=" + UPDATED_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee is not null
        defaultAlPyuJokerViFiltering("extraFee.specified=true", "extraFee.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "extraFee.greaterThanOrEqual=" + DEFAULT_EXTRA_FEE,
            "extraFee.greaterThanOrEqual=" + UPDATED_EXTRA_FEE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee is less than or equal to
        defaultAlPyuJokerViFiltering("extraFee.lessThanOrEqual=" + DEFAULT_EXTRA_FEE, "extraFee.lessThanOrEqual=" + SMALLER_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee is less than
        defaultAlPyuJokerViFiltering("extraFee.lessThan=" + UPDATED_EXTRA_FEE, "extraFee.lessThan=" + DEFAULT_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByExtraFeeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where extraFee is greater than
        defaultAlPyuJokerViFiltering("extraFee.greaterThan=" + SMALLER_EXTRA_FEE, "extraFee.greaterThan=" + DEFAULT_EXTRA_FEE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice equals to
        defaultAlPyuJokerViFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice in
        defaultAlPyuJokerViFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice is not null
        defaultAlPyuJokerViFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice is greater than or equal to
        defaultAlPyuJokerViFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice is less than or equal to
        defaultAlPyuJokerViFiltering(
            "totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice is less than
        defaultAlPyuJokerViFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where totalPrice is greater than
        defaultAlPyuJokerViFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingStatus equals to
        defaultAlPyuJokerViFiltering("bookingStatus.equals=" + DEFAULT_BOOKING_STATUS, "bookingStatus.equals=" + UPDATED_BOOKING_STATUS);
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingStatus in
        defaultAlPyuJokerViFiltering(
            "bookingStatus.in=" + DEFAULT_BOOKING_STATUS + "," + UPDATED_BOOKING_STATUS,
            "bookingStatus.in=" + UPDATED_BOOKING_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByBookingStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where bookingStatus is not null
        defaultAlPyuJokerViFiltering("bookingStatus.specified=true", "bookingStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByHistoryRefJasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where historyRefJason equals to
        defaultAlPyuJokerViFiltering(
            "historyRefJason.equals=" + DEFAULT_HISTORY_REF_JASON,
            "historyRefJason.equals=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByHistoryRefJasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where historyRefJason in
        defaultAlPyuJokerViFiltering(
            "historyRefJason.in=" + DEFAULT_HISTORY_REF_JASON + "," + UPDATED_HISTORY_REF_JASON,
            "historyRefJason.in=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByHistoryRefJasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where historyRefJason is not null
        defaultAlPyuJokerViFiltering("historyRefJason.specified=true", "historyRefJason.specified=false");
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByHistoryRefJasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where historyRefJason contains
        defaultAlPyuJokerViFiltering(
            "historyRefJason.contains=" + DEFAULT_HISTORY_REF_JASON,
            "historyRefJason.contains=" + UPDATED_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByHistoryRefJasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        // Get all the alPyuJokerViList where historyRefJason does not contain
        defaultAlPyuJokerViFiltering(
            "historyRefJason.doesNotContain=" + UPDATED_HISTORY_REF_JASON,
            "historyRefJason.doesNotContain=" + DEFAULT_HISTORY_REF_JASON
        );
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByCustomerIsEqualToSomething() throws Exception {
        AlPacino customer;
        if (TestUtil.findAll(em, AlPacino.class).isEmpty()) {
            alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
            customer = AlPacinoResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, AlPacino.class).get(0);
        }
        em.persist(customer);
        em.flush();
        alPyuJokerVi.setCustomer(customer);
        alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
        UUID customerId = customer.getId();
        // Get all the alPyuJokerViList where customer equals to customerId
        defaultAlPyuJokerViShouldBeFound("customerId.equals=" + customerId);

        // Get all the alPyuJokerViList where customer equals to UUID.randomUUID()
        defaultAlPyuJokerViShouldNotBeFound("customerId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByPersonInChargeIsEqualToSomething() throws Exception {
        EdSheeranVi personInCharge;
        if (TestUtil.findAll(em, EdSheeranVi.class).isEmpty()) {
            alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
            personInCharge = EdSheeranViResourceIT.createEntity(em);
        } else {
            personInCharge = TestUtil.findAll(em, EdSheeranVi.class).get(0);
        }
        em.persist(personInCharge);
        em.flush();
        alPyuJokerVi.setPersonInCharge(personInCharge);
        alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
        Long personInChargeId = personInCharge.getId();
        // Get all the alPyuJokerViList where personInCharge equals to personInChargeId
        defaultAlPyuJokerViShouldBeFound("personInChargeId.equals=" + personInChargeId);

        // Get all the alPyuJokerViList where personInCharge equals to (personInChargeId + 1)
        defaultAlPyuJokerViShouldNotBeFound("personInChargeId.equals=" + (personInChargeId + 1));
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByApplicationIsEqualToSomething() throws Exception {
        JohnLennon application;
        if (TestUtil.findAll(em, JohnLennon.class).isEmpty()) {
            alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
            application = JohnLennonResourceIT.createEntity();
        } else {
            application = TestUtil.findAll(em, JohnLennon.class).get(0);
        }
        em.persist(application);
        em.flush();
        alPyuJokerVi.setApplication(application);
        alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
        UUID applicationId = application.getId();
        // Get all the alPyuJokerViList where application equals to applicationId
        defaultAlPyuJokerViShouldBeFound("applicationId.equals=" + applicationId);

        // Get all the alPyuJokerViList where application equals to UUID.randomUUID()
        defaultAlPyuJokerViShouldNotBeFound("applicationId.equals=" + UUID.randomUUID());
    }

    @Test
    @Transactional
    void getAllAlPyuJokerVisByPropertyIsEqualToSomething() throws Exception {
        AlProtyVi property;
        if (TestUtil.findAll(em, AlProtyVi.class).isEmpty()) {
            alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
            property = AlProtyViResourceIT.createEntity(em);
        } else {
            property = TestUtil.findAll(em, AlProtyVi.class).get(0);
        }
        em.persist(property);
        em.flush();
        alPyuJokerVi.addProperty(property);
        alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);
        UUID propertyId = property.getId();
        // Get all the alPyuJokerViList where property equals to propertyId
        defaultAlPyuJokerViShouldBeFound("propertyId.equals=" + propertyId);

        // Get all the alPyuJokerViList where property equals to UUID.randomUUID()
        defaultAlPyuJokerViShouldNotBeFound("propertyId.equals=" + UUID.randomUUID());
    }

    private void defaultAlPyuJokerViFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlPyuJokerViShouldBeFound(shouldBeFound);
        defaultAlPyuJokerViShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlPyuJokerViShouldBeFound(String filter) throws Exception {
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alPyuJokerVi.getId().toString())))
            .andExpect(jsonPath("$.[*].bookingNo").value(hasItem(DEFAULT_BOOKING_NO)))
            .andExpect(jsonPath("$.[*].noteHeitiga").value(hasItem(DEFAULT_NOTE_HEITIGA)))
            .andExpect(jsonPath("$.[*].periodType").value(hasItem(DEFAULT_PERIOD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fromDate").value(hasItem(DEFAULT_FROM_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDate").value(hasItem(DEFAULT_TO_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkInDate").value(hasItem(DEFAULT_CHECK_IN_DATE.toString())))
            .andExpect(jsonPath("$.[*].checkOutDate").value(hasItem(DEFAULT_CHECK_OUT_DATE.toString())))
            .andExpect(jsonPath("$.[*].numberOfAdults").value(hasItem(DEFAULT_NUMBER_OF_ADULTS)))
            .andExpect(jsonPath("$.[*].numberOfPreschoolers").value(hasItem(DEFAULT_NUMBER_OF_PRESCHOOLERS)))
            .andExpect(jsonPath("$.[*].numberOfChildren").value(hasItem(DEFAULT_NUMBER_OF_CHILDREN)))
            .andExpect(jsonPath("$.[*].bookingPrice").value(hasItem(sameNumber(DEFAULT_BOOKING_PRICE))))
            .andExpect(jsonPath("$.[*].extraFee").value(hasItem(sameNumber(DEFAULT_EXTRA_FEE))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].bookingStatus").value(hasItem(DEFAULT_BOOKING_STATUS.toString())))
            .andExpect(jsonPath("$.[*].historyRefJason").value(hasItem(DEFAULT_HISTORY_REF_JASON)));

        // Check, that the count call also returns 1
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlPyuJokerViShouldNotBeFound(String filter) throws Exception {
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlPyuJokerViMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlPyuJokerVi() throws Exception {
        // Get the alPyuJokerVi
        restAlPyuJokerViMockMvc.perform(get(ENTITY_API_URL_ID, UUID.randomUUID().toString())).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlPyuJokerVi() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJokerVi
        AlPyuJokerVi updatedAlPyuJokerVi = alPyuJokerViRepository.findById(alPyuJokerVi.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlPyuJokerVi are not directly saved in db
        em.detach(updatedAlPyuJokerVi);
        updatedAlPyuJokerVi
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(updatedAlPyuJokerVi);

        restAlPyuJokerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuJokerViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuJokerViDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlPyuJokerViToMatchAllProperties(updatedAlPyuJokerVi);
    }

    @Test
    @Transactional
    void putNonExistingAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alPyuJokerViDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuJokerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alPyuJokerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alPyuJokerViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlPyuJokerViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJokerVi using partial update
        AlPyuJokerVi partialUpdatedAlPyuJokerVi = new AlPyuJokerVi();
        partialUpdatedAlPyuJokerVi.setId(alPyuJokerVi.getId());

        partialUpdatedAlPyuJokerVi
            .bookingNo(UPDATED_BOOKING_NO)
            .periodType(UPDATED_PERIOD_TYPE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE);

        restAlPyuJokerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuJokerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuJokerVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJokerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuJokerViUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlPyuJokerVi, alPyuJokerVi),
            getPersistedAlPyuJokerVi(alPyuJokerVi)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlPyuJokerViWithPatch() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alPyuJokerVi using partial update
        AlPyuJokerVi partialUpdatedAlPyuJokerVi = new AlPyuJokerVi();
        partialUpdatedAlPyuJokerVi.setId(alPyuJokerVi.getId());

        partialUpdatedAlPyuJokerVi
            .bookingNo(UPDATED_BOOKING_NO)
            .noteHeitiga(UPDATED_NOTE_HEITIGA)
            .periodType(UPDATED_PERIOD_TYPE)
            .fromDate(UPDATED_FROM_DATE)
            .toDate(UPDATED_TO_DATE)
            .checkInDate(UPDATED_CHECK_IN_DATE)
            .checkOutDate(UPDATED_CHECK_OUT_DATE)
            .numberOfAdults(UPDATED_NUMBER_OF_ADULTS)
            .numberOfPreschoolers(UPDATED_NUMBER_OF_PRESCHOOLERS)
            .numberOfChildren(UPDATED_NUMBER_OF_CHILDREN)
            .bookingPrice(UPDATED_BOOKING_PRICE)
            .extraFee(UPDATED_EXTRA_FEE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .bookingStatus(UPDATED_BOOKING_STATUS)
            .historyRefJason(UPDATED_HISTORY_REF_JASON);

        restAlPyuJokerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlPyuJokerVi.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlPyuJokerVi))
            )
            .andExpect(status().isOk());

        // Validate the AlPyuJokerVi in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlPyuJokerViUpdatableFieldsEquals(partialUpdatedAlPyuJokerVi, getPersistedAlPyuJokerVi(partialUpdatedAlPyuJokerVi));
    }

    @Test
    @Transactional
    void patchNonExistingAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alPyuJokerViDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuJokerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alPyuJokerViDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlPyuJokerVi() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alPyuJokerVi.setId(UUID.randomUUID());

        // Create the AlPyuJokerVi
        AlPyuJokerViDTO alPyuJokerViDTO = alPyuJokerViMapper.toDto(alPyuJokerVi);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlPyuJokerViMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alPyuJokerViDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlPyuJokerVi in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlPyuJokerVi() throws Exception {
        // Initialize the database
        insertedAlPyuJokerVi = alPyuJokerViRepository.saveAndFlush(alPyuJokerVi);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alPyuJokerVi
        restAlPyuJokerViMockMvc
            .perform(delete(ENTITY_API_URL_ID, alPyuJokerVi.getId().toString()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alPyuJokerViRepository.count();
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

    protected AlPyuJokerVi getPersistedAlPyuJokerVi(AlPyuJokerVi alPyuJokerVi) {
        return alPyuJokerViRepository.findById(alPyuJokerVi.getId()).orElseThrow();
    }

    protected void assertPersistedAlPyuJokerViToMatchAllProperties(AlPyuJokerVi expectedAlPyuJokerVi) {
        assertAlPyuJokerViAllPropertiesEquals(expectedAlPyuJokerVi, getPersistedAlPyuJokerVi(expectedAlPyuJokerVi));
    }

    protected void assertPersistedAlPyuJokerViToMatchUpdatableProperties(AlPyuJokerVi expectedAlPyuJokerVi) {
        assertAlPyuJokerViAllUpdatablePropertiesEquals(expectedAlPyuJokerVi, getPersistedAlPyuJokerVi(expectedAlPyuJokerVi));
    }
}
