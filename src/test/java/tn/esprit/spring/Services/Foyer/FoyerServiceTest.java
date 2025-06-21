package tn.esprit.spring.Services.Foyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FoyerServiceTest {

    private FoyerRepository foyerRepository;
    private UniversiteRepository universiteRepository;
    private BlocRepository blocRepository;
    private FoyerService foyerService;

    @BeforeEach
    void setUp() {
        foyerRepository = mock(FoyerRepository.class);
        universiteRepository = mock(UniversiteRepository.class);
        blocRepository = mock(BlocRepository.class);

        // Le constructeur prend foyerRepository en double car `repo` est aussi utilisé.
        foyerService = new FoyerService(foyerRepository, foyerRepository, universiteRepository, blocRepository);
    }

    @Test
    void testAddOrUpdate() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addOrUpdate(foyer);
        assertEquals(foyer, result);
    }

    @Test
    void testFindAll() {
        List<Foyer> foyers = Arrays.asList(new Foyer(), new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.findAll();
        assertEquals(foyers, result);
    }

    @Test
    void testFindById() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);
        assertEquals(1L, result.getIdFoyer());
    }

    @Test
    void testDeleteById() {
        foyerService.deleteById(1L);
        verify(foyerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        Foyer foyer = new Foyer();
        foyerService.delete(foyer);
        verify(foyerRepository, times(1)).delete(foyer);
    }

    @Test
    void testAffecterFoyerAUniversite_NomUniversite() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        Universite universite = new Universite();

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.findByNomUniversite("ESPRIT")).thenReturn(universite);
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "ESPRIT");
        assertEquals(foyer, result.getFoyer());
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer foyer = new Foyer();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer.setBlocs(List.of(bloc1, bloc2));

        Universite universite = new Universite();

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(universiteRepository.findById(10L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 10L);
        verify(blocRepository, times(2)).save(any());
        assertEquals(foyer, result);
    }

    @Test
    void testAjoutFoyerEtBlocs() {
        Foyer foyer = new Foyer();
        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer.setBlocs(List.of(bloc1, bloc2));

        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);
        verify(blocRepository, times(2)).save(any());
        assertEquals(foyer, result);
    }

    @Test
    void testAffecterFoyerAUniversite_Ids() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();

        when(universiteRepository.findById(10L)).thenReturn(Optional.of(universite));
        when(foyerRepository.findById(5L)).thenReturn(Optional.of(foyer));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.affecterFoyerAUniversite(5L, 10L);
        assertEquals(foyer, result.getFoyer());
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite universite = new Universite();
        universite.setFoyer(new Foyer());

        when(universiteRepository.findById(10L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(universite)).thenReturn(universite);

        Universite result = foyerService.desaffecterFoyerAUniversite(10L);
        assertNull(result.getFoyer());
    }
}
