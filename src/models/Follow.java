package models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Table(name = "follow")

@NamedQueries({
    @NamedQuery(
        name = "getMyfollowlist",
        query = "SELECT f.FollowerID FROM Follow AS f WHERE f.FollowedID =:id ORDER BY f.FollowerID"
    ),
    @NamedQuery(
            name = "getfolowerID",
            query = "SELECT f FROM Follow AS f WHERE f.FollowerID =:FollowerID AND f.FollowedID =:FollowedID"
        )
    })



@Entity
public class Follow{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //フォローする側のID
    @ManyToOne
    @JoinColumn(name = "FollowedID", nullable = false)
    private Employee FollowedID;

    //フォローされている側のID
    @ManyToOne
    @JoinColumn(name = "FollowerID", nullable = false)
    private Employee FollowerID;

    public Integer getPrimaryKey(){
        return this.id;
    }

    public Employee getFollowedID(){
        return FollowedID;
    }

    public Employee getFollowerID(){
        return FollowerID;
    }

    public void setFollowedID(Employee FollowedID){
        this.FollowedID=FollowedID;
    }

    public void setFollowerID(Employee FollowerID){
        this.FollowerID=FollowerID;
    }

}

