package entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name="User.findAll", query="SELECT e FROM User  e"),
        @NamedQuery(name = "User.findallBasic" , query = "SELECT e.name, e.username , e.email FROM User e WHERE e.del = false ")

})
public class User  implements Serializable{

    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "ROLE")
    private String role;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PHONENUM")
    private String phonenum;
    @Column(name = "DEL")
    private boolean del;

    public User(String name, String username, String password, String role, String email, String phonenum) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phonenum = phonenum;
        this.del = false;
    }

    public User(){};

    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "USERID"),
            inverseJoinColumns = @JoinColumn(name = "GROUPID"))
    List<Group> groups;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }




}
