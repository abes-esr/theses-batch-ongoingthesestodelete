package fr.abes.idsteptodelete;

import fr.abes.idsteptodelete.sujets.entities.DocumentSujets;
import fr.abes.idsteptodelete.sujets.repositories.SujetsRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentSujetsToDeleteWriter implements ItemWriter<DocumentSujets> {

    @Autowired
    private SujetsRepository sujetsRepository;
    @Override
    public void write(List<? extends DocumentSujets> list) throws Exception {
        //sujetsRepository.saveAll(list);
        System.out.println("ok");

    }
}
