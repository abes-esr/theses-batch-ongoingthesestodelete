package fr.abes.ongoingthesestodelete.portail.repositories;

import fr.abes.ongoingthesestodelete.portail.entities.DocumentPortail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Clob;
import java.util.ArrayList;

@Repository
public interface PortailRepository extends JpaRepository<DocumentPortail, Integer> {

    @Query("select p.doc from DocumentPortail p where p.iddoc like :x")
    public String getTefByIddoc(@Param("x") int iddoc);

    @Query("select p.iddoc from DocumentPortail p where p.numsujet like :x")
    Integer getIddocByNumsujet(@Param("x") String numsujet);

    @Query("select p.texte from DocumentPortail p where p.iddoc like :x")
    public Clob getTexteByIddoc(@Param("x") int iddoc);

    @Query("select case when count(p) > 0 then true else false end from DocumentPortail p where p.nnt is null and p.numsujet = :numSujet")
    boolean verifNntIsNull(@Param("numSujet") String numSujet);

    @Transactional(transactionManager="portailTransactionManager")
    @Modifying
    @Query("delete from DocumentPortail p where p.numsujet like :x")
    int deleteByNumSujet(@Param("x") String numSujet);

    @Query("select p.iddoc from DocumentPortail p where p.nnt like :x")
    ArrayList<Integer> getDuplicatesIddocList(@Param("x") String nnt);

    @Query(value ="SELECT p.NNT FROM Document p GROUP BY p.NNT HAVING COUNT(p.NNT) > 1", nativeQuery = true)
    ArrayList<String> getDuplicatesNntList();

    @Transactional(transactionManager="portailTransactionManager")
    @Modifying
    @Query("delete from DocumentPortail p where p.iddoc like :x")
    int deleteByIddoc(@Param("x") int iddoc);

}
