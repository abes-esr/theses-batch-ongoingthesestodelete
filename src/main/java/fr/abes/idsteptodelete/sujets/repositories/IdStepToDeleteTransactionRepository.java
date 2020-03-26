package fr.abes.idsteptodelete.sujets.repositories;

import fr.abes.idsteptodelete.sujets.entities.IdStepToDeleteTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdStepToDeleteTransactionRepository extends JpaRepository<IdStepToDeleteTransaction,Long> {
}
