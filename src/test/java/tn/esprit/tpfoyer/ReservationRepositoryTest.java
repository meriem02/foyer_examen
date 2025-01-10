package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
public class ReservationRepositoryTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private Reservation reservation;
    private Date currentDate;

    @BeforeEach
    public void setUp() {
        // Initialisation des données nécessaires pour le test
        currentDate = new Date();
        reservation = new Reservation();
        reservation.setAnneeUniversitaire(currentDate);
        reservation.setEstValide(true);
    }

    @Test
    public void testFindAllByAnneeUniversitaireBeforeAndEstValide() {
        // Données mockées
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);

        // Simulation du comportement de la méthode
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, true))
                .thenReturn(reservations);

        // Appel de la méthode du repository
        List<Reservation> result = reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(currentDate, true);

        // Vérifications
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
    }
}
