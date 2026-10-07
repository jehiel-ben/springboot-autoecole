package com.autoecole;

import com.autoecole.entity.Eleve;
import com.autoecole.repository.EleveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AutoEcoleApplicationTests {

    @Autowired
    private EleveRepository eleveRepository;

    @Test
    void contextLoads() {
        assertNotNull(eleveRepository, "Le repository Eleve doit être injecté");
    }

    @Test
    void testCreerEtRecupererEleve() {
        Eleve eleve = Eleve.builder()
                .nom("Test")
                .prenom("JUnit")
                .email("junit.test@autoecole.com")
                .telephone("0600000000")
                .build();

        Eleve saved = eleveRepository.save(eleve);
        assertNotNull(saved.getId(), "L'ID doit être généré");

        Eleve found = eleveRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found, "L'élève doit être trouvable");
        assertEquals("junit.test@autoecole.com", found.getEmail());
        assertEquals(Boolean.FALSE, found.getSupprime());
    }
}