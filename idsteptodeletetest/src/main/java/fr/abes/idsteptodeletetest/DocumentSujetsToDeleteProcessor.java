package fr.abes.idsteptodeletetest;

//import fr.abes.idsteptodeletetest.portail.repositories.PortailRepository;
import fr.abes.idsteptodeletetest.portail.repositories.PortailRepository;
import fr.abes.idsteptodeletetest.sujets.entities.DocumentSujets;
import fr.abes.idsteptodeletetest.sujets.repositories.SujetsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


//si on a un seul processor et qu'on le definit pas dans la classe de config
//si on a un composite de processor, et qu'on instancie le composite dans la classe de config, pas d'annotation @Component
@Component
public class DocumentSujetsToDeleteProcessor implements ItemProcessor<DocumentSujets,DocumentSujets> {

    private Logger logger = LogManager.getLogger(DocumentSujetsToDeleteProcessor.class);

    private int iddocStep;
    private String numSujetPortail;
    @Autowired
    private PortailRepository portailRepository;
    @Autowired
    private SujetsRepository sujetsRepository;
    private boolean verifNntIsNull;

    @Override
    public DocumentSujets process(DocumentSujets documentSujets) throws Exception {

        iddocStep = documentSujets.getIddoc();
        numSujetPortail ="s"+ iddocStep;
        logger.info("numSujetPortail = " + numSujetPortail);
        verifNntIsNull = portailRepository.verifNntIsNull(numSujetPortail);
        logger.info("verifNntIsNull = " + verifNntIsNull);
        if (verifNntIsNull) {
            portailRepository.deleteByNumSujet(numSujetPortail);
            logger.info("delete portail ok ");
            sujetsRepository.deleteByIddoc(iddocStep);
            logger.info("delete sujets ok ");
        }
        return documentSujets;
    }
}
