package tn.esprit.tpfoyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IReservationService reservationService;

    @InjectMocks
    private ReservationRestController reservationRestController;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Initialiser une réservation de test
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testGetReservations() throws Exception {
        // Given
        List<Reservation> reservations = List.of(reservation);
        when(reservationService.retrieveAllReservations()).thenReturn(reservations);

        // When & Then
        mockMvc.perform(get("/reservation/retrieve-all-reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idReservation").value("1"))
                .andExpect(jsonPath("$[0].estValide").value(true));

        verify(reservationService, times(1)).retrieveAllReservations();
    }

    @Test
    void testRetrieveReservation() throws Exception {
        // Given
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        // When & Then
        mockMvc.perform(get("/reservation/retrieve-reservation/{reservation-id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() throws Exception {
        // Given
        Date date = new Date();
        boolean estValide = true;
        List<Reservation> reservations = List.of(reservation);
        when(reservationService.trouverResSelonDateEtStatus(date, estValide)).thenReturn(reservations);

        // When & Then
        mockMvc.perform(get("/reservation/retrieve-reservation-date-status/{d}/{v}", date, estValide))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idReservation").value("1"));

        verify(reservationService, times(1)).trouverResSelonDateEtStatus(date, estValide);
    }

    @Test
    void testAddReservation() throws Exception {
        // Given
        when(reservationService.addReservation(any(Reservation.class))).thenReturn(reservation);

        // When & Then
        mockMvc.perform(post("/reservation/add-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).addReservation(any(Reservation.class));
    }

    @Test
    void testRemoveReservation() throws Exception {
        // When & Then
        mockMvc.perform(delete("/reservation/remove-reservation/{reservation-id}", "1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void testModifyReservation() throws Exception {
        // Given
        when(reservationService.modifyReservation(any(Reservation.class))).thenReturn(reservation);

        // When & Then
        mockMvc.perform(put("/reservation/modify-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).modifyReservation(any(Reservation.class));
    }
}
