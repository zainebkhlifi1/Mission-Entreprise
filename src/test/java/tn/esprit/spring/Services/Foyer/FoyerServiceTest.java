package tn.esprit.spring.Services.Foyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FoyerServiceTest {

    private FoyerRepository foyerRepository;
    private FoyerService foyerService;

    @BeforeEach
    void setUp() {
        foyerRepository = mock(FoyerRepository.class);
        foyerService = new FoyerService(foyerRepository, foyerRepository, null, null);
    }

    @Test
    void testFindAll() {
        // Arrange
        Foyer foyer1 = new Foyer();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer A");

        Foyer foyer2 = new Foyer();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer B");

        List<Foyer> expectedFoyers = Arrays.asList(foyer1, foyer2);
        when(foyerRepository.findAll()).thenReturn(expectedFoyers);

        // Act
        List<Foyer> actualFoyers = foyerService.findAll();

        // Assert
        assertEquals(expectedFoyers, actualFoyers);
        verify(foyerRepository, times(1)).findAll();
    }
}
