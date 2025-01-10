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
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;
    private Date currentDate;

    @BeforeEach
    public void setUp() {
        // Initialisation des données nécessaires pour le test
        currentDate = new Date();
        reservation = new Reservation();
        reservation.setAnneeUniversitaire(currentDate);
        reservation.setEstValide(true);

        // Enregistrer une réservation dans la base de données en mémoire
        reservationRepository.save(reservation);
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValide() {
        // Appel de la méthode du repository
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, true);

        // Vérifications
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }
}
