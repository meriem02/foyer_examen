package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation1;
    private Reservation reservation2;
    private Date currentDate;

    @BeforeEach
    public void setUp() {
        // Initialisation des données avant chaque test
        currentDate = new Date();

        reservation1 = new Reservation();
        reservation1.setAnneeUniversitaire(new Date(currentDate.getTime() - 10000000)); // Une date avant la date actuelle
        reservation1.setEstValide(true);

        reservation2 = new Reservation();
        reservation2.setAnneeUniversitaire(new Date(currentDate.getTime() + 10000000)); // Une date après la date actuelle
        reservation2.setEstValide(false);

        // Sauvegarde dans la base de données en mémoire
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValide() {
        // Test de la méthode findAllByAnneeUniversitaireBeforeAndEstValide
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, true);

        // Vérification que la taille de la liste correspond à l'attente
        assertNotNull(result);
        assertEquals(1, result.size());

        // Vérification que l'élément retourné est bien le premier (celui qui correspond aux critères)
        assertTrue(result.contains(reservation1));
        assertFalse(result.contains(reservation2)); // Vérification que reservation2 n'est pas incluse
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValideWithNoResults() {
        // Cas où il n'y a pas de résultats correspondant aux critères
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, false);

        // Vérification que la liste est vide
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValideWithMultipleResults() {
        // Sauvegarde d'une troisième réservation valide
        Reservation reservation3 = new Reservation();
        reservation3.setAnneeUniversitaire(new Date(currentDate.getTime() - 5000000)); // Une date avant la date actuelle
        reservation3.setEstValide(true);
        reservationRepository.save(reservation3);

        // Test de la méthode findAllByAnneeUniversitaireBeforeAndEstValide avec plusieurs résultats
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, true);

        // Vérification que la taille de la liste correspond à l'attente
        assertNotNull(result);
        assertEquals(2, result.size());

        // Vérification que toutes les réservations valides avant la date actuelle sont retournées
        assertTrue(result.contains(reservation1));
        assertTrue(result.contains(reservation3));
        assertFalse(result.contains(reservation2)); // reservation2 ne doit pas être incluse
    }
}
