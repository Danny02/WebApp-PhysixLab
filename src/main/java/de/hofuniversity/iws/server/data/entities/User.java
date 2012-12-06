package de.hofuniversity.iws.server.data.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table
public class User implements Serializable, GenericEntity {

    @Transient
    private boolean detached = false;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String userName;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Column
    private Timestamp birthDate;
    @Column
    private String city;
    @Lob
    private byte[] userPic;
    @OneToMany(mappedBy = "user")
    private List<NetworkAccount> networkAccountList;
    @OneToMany(mappedBy = "user")
    private List<GameResult> gameResultList;
    @OneToMany(mappedBy = "user")
    private List<LessonProgress> lessonProgressList;
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "USER_FRIENDS",
    joinColumns = {
        @JoinColumn(name = "USER_ID")},
    inverseJoinColumns = {
        @JoinColumn(name = "FRIEND_ID")})
    private List<User> friends = new ArrayList<User>();
    @ManyToMany(mappedBy = "friends")
    private List<User> users = new ArrayList<User>();

    public boolean isDetached() {
        return detached;
    }

    public void setDetached(boolean detached) {
        this.detached = detached;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public byte[] getUserPic() {
        return userPic;
    }

    public void setUserPic(byte[] userPic) {
        this.userPic = userPic;
    }

    public List<NetworkAccount> getNetworkAccountList() {
        return networkAccountList;
    }

    public void setNetworkAccountList(List<NetworkAccount> networkAccountList) {
        this.networkAccountList = networkAccountList;
    }

    public List<GameResult> getGameResultList() {
        return gameResultList;
    }

    public void setGameResultList(List<GameResult> gameResultList) {
        this.gameResultList = gameResultList;
    }

    public List<LessonProgress> getLessonProgressList() {
        return lessonProgressList;
    }

    public void setLessonProgressList(List<LessonProgress> lessonProgressList) {
        this.lessonProgressList = lessonProgressList;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
