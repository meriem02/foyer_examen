package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        Etudiant etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1L);
        Etudiant etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2L);

        reservation.setEtudiants(new HashSet<>(Arrays.asList(etudiant1, etudiant2)));
    }

    // ✅ Test d'ajout de réservation dupliquée
    @Test
    void testAddDuplicateReservation() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation newReservation = new Reservation();
        newReservation.setIdReservation("1");

        // Vérifier que l'ajout d'une réservation avec un ID existant lance une exception
        assertThrows(IllegalArgumentException.class, () -> reservationService.addReservation(newReservation),
                "Reservation with the same ID already exists");

        // Vérifier que la méthode save n'est pas appelée
        verify(reservationRepository, never()).save(newReservation);
    }

    // ✅ Test de suppression de réservation inexistante
    @Test
    void testRemoveNonExistentReservation() {
        when(reservationRepository.findById("99")).thenReturn(Optional.empty());

        // Vérifier qu'une exception est lancée lorsqu'une réservation inexistante est supprimée
        assertThrows(NoSuchElementException.class, () -> reservationService.removeReservation("99"),
                "Reservation not found");

        // Vérifier que la méthode deleteById n'est pas appelée
        verify(reservationRepository, never()).deleteById("99");
    }

    // ✅ Test de modification avec données nulles
    @Test
    void testModifyReservationWithNullData() {
        // Vérifier que la méthode lève une exception si la réservation est null
        assertThrows(IllegalArgumentException.class, () -> reservationService.modifyReservation(null),
                "Reservation cannot be null");

        // Vérifier que la méthode save n'est pas appelée
        verify(reservationRepository, never()).save(any());
    }

    // ✅ Test de recherche avec ID null
    @Test
    void testRetrieveReservationWithNullId() {
        // Vérifier que la méthode lève une exception si l'ID est null
        assertThrows(IllegalArgumentException.class, () -> reservationService.retrieveReservation(null),
                "ID cannot be null");

        // Vérifier que la méthode findById n'est pas appelée
        verify(reservationRepository, never()).findById(null);
    }

    // ✅ Test de suppression avec ID null
    @Test
    void testRemoveReservationWithNullId() {
        // Vérifier que la méthode lève une exception si l'ID est null
        assertThrows(IllegalArgumentException.class, () -> reservationService.removeReservation(null),
                "ID cannot be null");

        // Vérifier que la méthode deleteById n'est pas appelée
        verify(reservationRepository, never()).deleteById(null);
    }

    // ✅ Test de recherche par date et statut avec une liste vide
    @Test
    void testTrouverResSelonDateEtStatusWithEmptyResult() {
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(Collections.emptyList());

        // Vérifier que la méthode retourne une liste vide si aucune réservation n'est trouvée
        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        assertThat(result).isEmpty();
        verify(reservationRepository, times(1))
                .findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    // ✅ Test de mise à jour d'une réservation inexistante
    @Test
    void testModifyNonExistentReservation() {
        Reservation nonExistentReservation = new Reservation();
        nonExistentReservation.setIdReservation("99");

        // Simuler l'enregistrement d'une réservation inexistante
        when(reservationRepository.save(nonExistentReservation)).thenReturn(nonExistentReservation);

        // Modifier la réservation et vérifier qu'elle est correctement retournée
        Reservation result = reservationService.modifyReservation(nonExistentReservation);

        assertThat(result).isNotNull();
        assertThat(result.getIdReservation()).isEqualTo("99");
        verify(reservationRepository, times(1)).save(nonExistentReservation);
    }

    // ✅ Test de validation de la liste d'étudiants vide
    @Test
    void testAddReservationWithEmptyEtudiants() {
        Reservation reservationWithNoEtudiants = new Reservation();
        reservationWithNoEtudiants.setIdReservation("2");
        reservationWithNoEtudiants.setEtudiants(new HashSet<>());

        // Vérifier que la méthode lève une exception si la réservation n'a pas d'étudiants
        assertThrows(IllegalArgumentException.class, () -> reservationService.addReservation(reservationWithNoEtudiants),
                "Reservation must have at least one student");

        // Vérifier que la méthode save n'est pas appelée
        verify(reservationRepository, never()).save(any());
    }

    // ✅ Test pour vérifier que la méthode findAll() est appelée une seule fois
    @Test
    void testRetrieveAllReservationsCalledOnce() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> reservations = reservationService.retrieveAllReservations();

        assertThat(reservations).isNotEmpty();
        // Vérifier que la méthode findAll est appelée une seule fois
        verify(reservationRepository, times(1)).findAll();
    }

    // ✅ Test avec suppression et vérification de l'interaction
    @Test
    void testRemoveReservationAndVerifyInteraction() {
        doNothing().when(reservationRepository).deleteById("1");

        // Supprimer la réservation et vérifier que deleteById est appelée
        reservationService.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }
}
