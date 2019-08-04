package entities;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name = "workgroup")@NamedQueries({
        @NamedQuery(name="Group.findAll", query="SELECT e FROM Group  e")


})

public class Group implements Serializable {

    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "GROUPNAME")
    private String name;
    @Column(name = "DESCRIPTION")
    private String desc;

    public Group(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "GROUPID"),
            inverseJoinColumns = @JoinColumn(name = "USERID"))
    List<User> users;


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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Group(){}

}
