package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testGetReservations() throws Exception {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.retrieveAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/reservation/retrieve-all-reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("1"));

        verify(reservationService, times(1)).retrieveAllReservations();
    }

    @Test
    void testRetrieveReservation() throws Exception {
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        mockMvc.perform(get("/reservation/retrieve-reservation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void testAddReservation() throws Exception {
        when(reservationService.addReservation(reservation)).thenReturn(reservation);

        mockMvc.perform(post("/reservation/add-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idReservation\":\"1\",\"anneeUniversitaire\":\"2025-01-01\",\"estValide\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).addReservation(any(Reservation.class));
    }

    @Test
    void testModifyReservation() throws Exception {
        when(reservationService.modifyReservation(reservation)).thenReturn(reservation);

        mockMvc.perform(put("/reservation/modify-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idReservation\":\"1\",\"anneeUniversitaire\":\"2025-01-01\",\"estValide\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).modifyReservation(any(Reservation.class));
    }

    @Test
    void testRemoveReservation() throws Exception {
        doNothing().when(reservationService).removeReservation("1");

        mockMvc.perform(delete("/reservation/remove-reservation/1"))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void testRetrieveReservationByDateAndStatus() throws Exception {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(true)))
                .thenReturn(reservations);

        mockMvc.perform(get("/reservation/retrieve-reservation-date-status/2025-01-01/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("1"));

        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(true));
    }
}
