package fr.abes.ongoingthesestodelete.sujets.repositories;

import fr.abes.ongoingthesestodelete.sujets.entities.DocumentSujets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SujetsRepository extends JpaRepository<DocumentSujets, Integer> {

    @Query("select s.doc from DocumentSujets s where s.iddoc like :x")
    String getTefByIddoc(@Param("x") int iddoc);

    @Transactional(transactionManager="sujetsTransactionManager")
    @Modifying
    @Query("delete from DocumentSujets s where s.iddoc like :x")
    int deleteByIddoc(@Param("x") int iddoc);

}