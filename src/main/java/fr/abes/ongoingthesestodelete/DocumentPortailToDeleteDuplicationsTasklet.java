package fr.abes.ongoingthesestodelete;

import fr.abes.ongoingthesestodelete.portail.repositories.PortailRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Collections;


@Component
public class DocumentPortailToDeleteDuplicationsTasklet implements Tasklet {

    private Logger logger = LogManager.getLogger(DocumentPortailToDeleteDuplicationsTasklet.class);

    @Autowired
    private PortailRepository portailRepository;
    private int deletePortail;
    ArrayList<String> duplicatesNntList;
    ArrayList<Integer> duplicatesIddocList;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        RepeatStatus res = null;
        //first, we must get the list of duplicates nnt from Portail.Document
        duplicatesNntList = portailRepository.getDuplicatesNntList();
        logger.info("debut process suppression doublon");
        logger.info("nb doublons = " + duplicatesNntList.size());

        //then for each duplicate nnt in the list
        for (String nntDuplicate : duplicatesNntList) {
            logger.info("debut process suppression doublons nnt = " + nntDuplicate);
            //we must get the several iddoc relative to this nnt and we fill the duplicatesIddocList
            duplicatesIddocList = portailRepository.getDuplicatesIddocList(nntDuplicate);
            Collections.sort(duplicatesIddocList); //the iddoc values asc order
            duplicatesIddocList.remove(duplicatesIddocList.size() - 1); //we push out from the list the last one
            for (Integer duplicateIddocPortail : duplicatesIddocList) {
                deletePortail = portailRepository.deleteByIddoc(duplicateIddocPortail);
                if (deletePortail == 1)
                    logger.info("delete portail OK pour doublon iddoc = " + duplicateIddocPortail + " (doublon du Nnt = " + nntDuplicate + ")");
                else
                    logger.info("delete portail KO pour doublon iddoc = " + duplicateIddocPortail + " (doublon du Nnt = " + nntDuplicate + ")");
            }
        }return res;
    }
}

