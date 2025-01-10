package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest  // This annotation is used for JPA tests (it auto-configures an in-memory database)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Create a new Reservation object before each test
        reservation = new Reservation("1", new Date(), true, null);
    }

    @Test
    void testSaveReservation() {
        // Save the reservation entity to the in-memory database
        Reservation savedReservation = reservationRepository.save(reservation);

        // Assert that the reservation has been saved and the ID is not null
        assertNotNull(savedReservation);
        assertNotNull(savedReservation.getIdReservation());
        assertEquals("1", savedReservation.getIdReservation());
    }

    @Test
    void testFindReservationById() {
        // Save the reservation first
        reservationRepository.save(reservation);

        // Retrieve the reservation by its ID
        Optional<Reservation> foundReservation = reservationRepository.findById("1");

        // Assert that the reservation is found and has the correct ID
        assertTrue(foundReservation.isPresent());
        assertEquals("1", foundReservation.get().getIdReservation());
    }

    @Test
    void testDeleteReservation() {
        // Save the reservation
        reservationRepository.save(reservation);

        // Delete the reservation
        reservationRepository.deleteById("1");

        // Assert that the reservation no longer exists in the database
        Optional<Reservation> deletedReservation = reservationRepository.findById("1");
        assertFalse(deletedReservation.isPresent());
    }

    @Test
    void testUpdateReservation() {
        // Save the reservation
        Reservation savedReservation = reservationRepository.save(reservation);

        // Modify the reservation
        savedReservation.setEstValide(false);

        // Save the updated reservation
        Reservation updatedReservation = reservationRepository.save(savedReservation);

        // Assert that the reservation was updated successfully
        assertEquals(false, updatedReservation.isEstValide());
    }
}
