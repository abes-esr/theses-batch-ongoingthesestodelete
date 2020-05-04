package fr.abes.ongoingthesestodelete;

import fr.abes.ongoingthesestodelete.portail.repositories.PortailRepository;
import fr.abes.ongoingthesestodelete.sujets.entities.DocumentSujets;
import fr.abes.ongoingthesestodelete.sujets.repositories.SujetsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;


//si on a un seul processor et qu'on le definit pas dans la classe de config
//si on a un composite de processor, et qu'on instancie le composite dans la classe de config, pas d'annotation @Component
@Component
public class DocumentSujetsToDeleteProcessor implements ItemProcessor<DocumentSujets,DocumentSujets> {

    private Logger logger = LogManager.getLogger(DocumentSujetsToDeleteProcessor.class);

    private int iddocStep;
    private int iddocPortail;
    private String numSujetPortail;
    @Autowired
    private PortailRepository portailRepository;
    @Autowired
    private SujetsRepository sujetsRepository;
    private boolean verifNntIsNull;
    private String tef;
    private int deleteSujets;
    private int deletePortail;
    private String statutThese;

    @Override
    public DocumentSujets process(DocumentSujets documentSujets) throws Exception {

        iddocStep = documentSujets.getIddoc();
        numSujetPortail ="s"+ iddocStep;
        logger.info("debut process pour iddocStep = " + iddocStep);
        //logger.info("numSujetPortail = " + numSujetPortail);
        tef = sujetsRepository.getTefByIddoc(iddocStep);
        //logger.info("tef = " + tef);
        statutThese = StringUtils.substringBetween(tef, "stepEtat=\"", "\">");
        //logger.info("statutThese = " + statutThese);
        if(tef!=null) {
            if (!tef.contains("stepEtat=\"these\"")) {
                logger.info("tef en statut " +  statutThese + " pour iddocStep = " + iddocStep);
                deleteSujets = sujetsRepository.deleteByIddoc(iddocStep);
                if (deleteSujets == 1) logger.info("delete sujets OK pour iddocStep = " + iddocStep);
                else logger.info("delete sujets KO pour iddocStep = " + iddocStep);
                try{
                    iddocPortail = portailRepository.getIddocByNumsujet(numSujetPortail);
                    logger.info("these dans bdd portail trouvee pour numsujetPortail = " + numSujetPortail);
                    deletePortail = portailRepository.deleteByNumSujet(numSujetPortail);
                    if (deletePortail == 1) logger.info("delete portail OK pour numsujetPortail = " + numSujetPortail + " (iddocPortail = " + iddocPortail + ")");
                    else logger.info("delete portail KO pour numsujetPortail = " + numSujetPortail + " (iddocPortail = " + iddocPortail + ")");
                } catch (Exception e)
                {
                    logger.info("these dans bdd portail non trouvee pour numsujetPortail = " + numSujetPortail);
                }
            } else {
                logger.info("tef en statut these pour iddocStep = " + iddocStep);
            }
        }
        else {
            logger.info("tef = null ou iddocStep " + iddocStep + " inexistant en bdd." );
        }




        //va voir le stef etat
        //these dans bdd sujets trouvée ou non trouvée
        //si stefetat = pas these
            //delete sujet => delete sujets ko ou delete sujets ok
            //delete portail => these dans bdd portail trouvée ou non trouvée
            //delete portail => delete portail ko ou delete portail ok
        //sinon stefetat = these donc pas de delete
        /*verifNntIsNull = portailRepository.verifNntIsNull(numSujetPortail);
        logger.info("verifNntIsNull = " + verifNntIsNull);
        if (verifNntIsNull) {
            portailRepository.deleteByNumSujet(numSujetPortail);
            logger.info("delete portail ok ");
            sujetsRepository.deleteByIddoc(iddocStep);
            logger.info("delete sujets ok ");
        }*/
        return documentSujets;
    }
}
