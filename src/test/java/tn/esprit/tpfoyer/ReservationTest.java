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
    private ReservationRestController reservationRestController; // Injecter le contrôleur avec le service mocké

    @Mock
    private ReservationServiceImpl reservationService;  // Mock du service de réservation

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Préparation de l'objet Reservation pour les tests
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void testGetReservations() {
        // Arrange: Le service renvoie une liste avec la réservation
        when(reservationService.retrieveAllReservations()).thenReturn(Arrays.asList(reservation));

        // Act: Appel de la méthode du contrôleur
        List<Reservation> reservations = reservationRestController.getReservations();

        // Assert: Vérification des résultats et des interactions
        assertThat(reservations).isNotEmpty();
        assertThat(reservations.size()).isEqualTo(1); // Vérifie qu'il y a une réservation
        verify(reservationService, times(1)).retrieveAllReservations(); // Vérifie que le service a été appelé une seule fois
    }

    @Test
    void testRetrieveReservation() {
        // Arrange: Le service renvoie une réservation spécifique
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        // Act: Appel de la méthode du contrôleur
        Reservation result = reservationRestController.retrieveReservation("1");

        // Assert: Vérification des résultats et des interactions
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void testAddReservation() {
        // Arrange: Le service renvoie la réservation après ajout
        when(reservationService.addReservation(reservation)).thenReturn(reservation);

        // Act: Appel de la méthode du contrôleur pour ajouter la réservation
        Reservation result = reservationRestController.addReservation(reservation);

        // Assert: Vérification des résultats et des interactions
        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("1");
        verify(reservationService, times(1)).addReservation(reservation);
    }

    @Test
    void testModifyReservation() {
        // Arrange: Le service renvoie la réservation modifiée
        when(reservationService.modifyReservation(reservation)).thenReturn(reservation);

        // Act: Appel de la méthode du contrôleur pour modifier la réservation
        Reservation result = reservationRestController.modifyReservation(reservation);

        // Assert: Vérification des résultats et des interactions
        assertThat(result).isNotNull();
        verify(reservationService, times(1)).modifyReservation(reservation);
    }

    @Test
    void testRemoveReservation() {
        // Arrange: Le service ne fait rien lors de la suppression
        doNothing().when(reservationService).removeReservation("1");

        // Act: Appel de la méthode du contrôleur pour supprimer la réservation
        reservationRestController.removeReservation("1");

        // Assert: Vérification que la suppression a bien été appelée une seule fois
        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() {
        // Arrange: Le service renvoie une liste de réservations filtrées
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), eq(true)))
                .thenReturn(Arrays.asList(reservation));

        // Act: Appel de la méthode du contrôleur
        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(new Date(), true);

        // Assert: Vérification des résultats et des interactions
        assertThat(result).isNotEmpty();
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), eq(true));
    }
}
