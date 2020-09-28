package fr.abes.ongoingthesestodelete.star.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "DOCUMENT")
@NoArgsConstructor
@Getter
@Setter
public class DocumentStar implements Serializable{

    @Id
    @Column(name = "IDDOC")
    private Integer iddoc;

    @ColumnTransformer(read = "NVL2(DOC, (DOC).getClobVal(), NULL)", write = "NULLSAFE_XMLTYPE(?)")
    @Lob
    @Column(name = "DOC")
    //@Basic(fetch = FetchType.LAZY) ==> à voir si necessaire cf:https://dzone.com/articles/50-best-performance-practices-for-hibernate-5-amp
    private String doc;

    @Column(name = "TEXTE")
    private String texte;

    @Column(name = "CODEETAB")
    private String codeEtab;

    @Column(name = "ENVOISOLR")
    private Integer envoiSolr;

    public DocumentStar(Integer idDoc, String doc, String texte, String codeEtab, Integer envoiSolr) {
        this.iddoc = idDoc;
        this.doc = doc;
        this.texte = texte;
        this.codeEtab = codeEtab;
        this.envoiSolr = envoiSolr;
    }
}

