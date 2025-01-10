package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Initialiser une réservation de test
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        // Sauvegarder dans la base de données de test
        reservationRepository.save(reservation);
    }

    @Test
    void testFindAllByAnneeUniversitaireBeforeAndEstValide() {
        // Given
        Date date = new Date();
        boolean estValide = true;

        // When
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, estValide);

        // Then
        assertNotNull(result);
        assertTrue(result.size() > 0);  // Si la date et le statut sont corrects, la liste ne devrait pas être vide
        assertEquals("1", result.get(0).getIdReservation());  // Vérifier que la réservation enregistrée est bien dans les résultats
    }

    @Test
    void testFindAllByAnneeUniversitaireBeforeAndEstValideWhenNoResults() {
        // Given
        Date date = new Date(System.currentTimeMillis() - 10000000); // Date très ancienne
        boolean estValide = false;

        // When
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, estValide);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());  // Aucune réservation ne correspond aux critères donnés
    }
}
