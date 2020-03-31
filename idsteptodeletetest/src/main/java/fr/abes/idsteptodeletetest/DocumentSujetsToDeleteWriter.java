package fr.abes.idsteptodeletetest;


import fr.abes.idsteptodeletetest.sujets.entities.DocumentSujets;
import fr.abes.idsteptodeletetest.sujets.repositories.SujetsRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
