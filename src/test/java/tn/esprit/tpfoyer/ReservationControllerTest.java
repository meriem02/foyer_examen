package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    @InjectMocks
    private ReservationRestController reservationRestController;

    @Mock
    private ReservationServiceImpl reservationService;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    // ✅ Test pour récupérer toutes les réservations
    @Test
    void testGetReservations() {
        when(reservationService.retrieveAllReservations()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationRestController.getReservations();

        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isEqualTo(1);
        verify(reservationService, times(1)).retrieveAllReservations();
    }

    // ✅ Test pour vérifier les exceptions lors de la récupération des réservations
    @Test
    void testGetReservationsException() {
        when(reservationService.retrieveAllReservations()).thenThrow(new RuntimeException("Service unavailable"));

        try {
            reservationRestController.getReservations();
        } catch (Exception e) {
            assertThat(e).isInstanceOf(RuntimeException.class);
            assertThat(e.getMessage()).isEqualTo("Service unavailable");
        }

        verify(reservationService, times(1)).retrieveAllReservations();
    }

    // ✅ Test pour récupérer une réservation spécifique
    @Test
    void testRetrieveReservation() {
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        Reservation result = reservationRestController.retrieveReservation("1");

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).retrieveReservation("1");
    }

    // ✅ Test pour une réservation non trouvée
    @Test
    void testRetrieveReservationNotFound() {
        when(reservationService.retrieveReservation("999")).thenReturn(null);

        Reservation result = reservationRestController.retrieveReservation("999");

        assertThat(result).isNull();
        verify(reservationService, times(1)).retrieveReservation("999");
    }

    // ✅ Test pour ajouter une réservation
    @Test
    void testAddReservation() {
        when(reservationService.addReservation(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.addReservation(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).addReservation(reservation);
    }

    // ✅ Test pour ajouter une réservation avec une date null
    @Test
    void testAddReservationWithNullDate() {
        reservation.setAnneeUniversitaire(null);

        when(reservationService.addReservation(reservation)).thenThrow(new IllegalArgumentException("Date is required"));

        try {
            reservationRestController.addReservation(reservation);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e.getMessage()).isEqualTo("Date is required");
        }

        verify(reservationService, times(1)).addReservation(reservation);
    }

    // ✅ Test pour modifier une réservation
    @Test
    void testModifyReservation() {
        when(reservationService.modifyReservation(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.modifyReservation(reservation);

        assertThat(result).isNotNull();
        verify(reservationService, times(1)).modifyReservation(reservation);
    }

    // ✅ Test pour modifier une réservation avec une entrée nulle
    @Test
    void testModifyReservationWithNull() {
        when(reservationService.modifyReservation(null)).thenThrow(new IllegalArgumentException("Reservation cannot be null"));

        try {
            reservationRestController.modifyReservation(null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e.getMessage()).isEqualTo("Reservation cannot be null");
        }

        verify(reservationService, times(1)).modifyReservation(null);
    }

    // ✅ Test pour supprimer une réservation
    @Test
    void testRemoveReservation() {
        doNothing().when(reservationService).removeReservation("1");

        reservationRestController.removeReservation("1");

        verify(reservationService, times(1)).removeReservation("1");
    }

    // ✅ Test pour supprimer une réservation avec un ID invalide
    @Test
    void testRemoveReservationWithInvalidId() {
        doThrow(new IllegalArgumentException("Invalid ID")).when(reservationService).removeReservation("");

        try {
            reservationRestController.removeReservation("");
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e.getMessage()).isEqualTo("Invalid ID");
        }

        verify(reservationService, times(1)).removeReservation("");
    }

    // ✅ Test pour récupérer les réservations par date et statut
    @Test
    void testRetrieveReservationParDateEtStatus() {
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), true);

        assertThat(result).isNotEmpty();
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(true));
    }

    // ✅ Test pour récupérer les réservations par date et statut sans résultats
    @Test
    void testRetrieveReservationParDateEtStatusNoResults() {
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(false)))
                .thenReturn(Arrays.asList());

        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), false);

        assertThat(result).isEmpty();
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(false));
    }

    // ✅ Test pour ajouter une réservation avec des données invalides
    @Test
    void testAddReservationWithInvalidData() {
        reservation.setIdReservation(null);

        when(reservationService.addReservation(reservation)).thenThrow(new IllegalArgumentException("Invalid Reservation"));

        try {
            reservationRestController.addReservation(reservation);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
            assertThat(e.getMessage()).isEqualTo("Invalid Reservation");
        }

        verify(reservationService, times(1)).addReservation(reservation);
    }
}
