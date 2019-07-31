package Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.json.Json;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
@Entity
@Table(name = "auditlog")@NamedQueries({
        @NamedQuery(name="AuditLog.findAll", query="SELECT e FROM AuditLog   e")


})
public class AuditLog implements Serializable{

    @Id
    @Column(name = "LOGID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "ENTITY")
    private  String entity;
    @Column(name = "LOG_TIME")
    private Timestamp logtime;

    public AuditLog(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Timestamp getLogtime() {
        return logtime;
    }

    public void setLogtime(Timestamp logtime) {
        this.logtime = logtime;
    }

}
