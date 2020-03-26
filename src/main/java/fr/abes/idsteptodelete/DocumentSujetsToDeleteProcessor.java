package fr.abes.idsteptodelete;

import fr.abes.idsteptodelete.portail.repositories.PortailRepository;
import fr.abes.idsteptodelete.sujets.entities.DocumentSujets;
import fr.abes.idsteptodelete.sujets.repositories.SujetsRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//si on a un seul processor et qu'on le definit pas dans la classe de config
//si on a un composite de processor, et qu'on instancie le composite dans la classe de config, pas d'annotation @Component
@Component
public class DocumentSujetsToDeleteProcessor implements ItemProcessor<DocumentSujets,DocumentSujets> {

    private int iddocStep;
    private String numSujetPortail;
    @Autowired
    private PortailRepository portailRepository;
    @Autowired
    private SujetsRepository sujetsRepository;

    @Override
    public DocumentSujets process(DocumentSujets documentSujets) throws Exception {

        iddocStep = documentSujets.getIddoc();
        numSujetPortail ="s"+ iddocStep;
        if (portailRepository.verifNntIsNull(numSujetPortail)) {
            portailRepository.deleteByNumSujet(numSujetPortail);
            sujetsRepository.deleteByIddoc(iddocStep);
        }
        return documentSujets;
    }
}