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
class ReservationRestControllerTest {

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
        reservation.setEstValide(true); // The boolean field estValide is directly set
    }

    @Test
    void testGetReservations() {
        when(reservationService.retrieveAllReservations()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationRestController.getReservations();

        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isEqualTo(1);
        verify(reservationService, times(1)).retrieveAllReservations();
    }

    @Test
    void testRetrieveReservation() {
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        Reservation result = reservationRestController.retrieveReservation("1");

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void testRetrieveReservationNotFound() {
        when(reservationService.retrieveReservation("999")).thenReturn(null);  // Simuler l'absence de la réservation

        Reservation result = reservationRestController.retrieveReservation("999");

        assertThat(result).isNull();  // Vérifier que la réservation est null
        verify(reservationService, times(1)).retrieveReservation("999");
    }

    @Test
    void testAddReservation() {
        when(reservationService.addReservation(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.addReservation(reservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).addReservation(reservation);
    }

    @Test
    void testModifyReservation() {
        when(reservationService.modifyReservation(reservation)).thenReturn(reservation);

        Reservation result = reservationRestController.modifyReservation(reservation);

        assertThat(result).isNotNull();
        verify(reservationService, times(1)).modifyReservation(reservation);
    }

    @Test
    void testRemoveReservation() {
        doNothing().when(reservationService).removeReservation("1");

        reservationRestController.removeReservation("1");

        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() {
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), true);

        assertThat(result).isNotEmpty();
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(true));
    }

    @Test
    void testRetrieveReservationParDateEtStatusNoResults() {
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(false)))
                .thenReturn(Arrays.asList());  // Simuler aucune réservation

        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), false);

        assertThat(result).isEmpty();  // Vérifier que la liste est vide
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(false));
    }

    @Test
    void testAddReservationWithInvalidData() {
        reservation.setIdReservation(null);  // Id null pour simuler une mauvaise entrée

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
