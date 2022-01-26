package kr.co.won.user.domain;

import kr.co.won.address.Address;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"idx"})
@ToString(exclude = {"roles"})
@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Builder.Default
    private boolean deleteFlag = false;

    @Builder.Default
    private boolean activeFlag = false;

    @Embedded
    private Address address;

    @Builder.Default
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRoleDomain> roles = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    /**
     * domain function
     */

    public void addRole(UserRoleDomain role) {
        this.roles.add(role);
        role.setUser(this);
    }

    /**
     * user role have check
     */
    public boolean roleCheck(UserRoleType roleType) {
        return this.roles.stream().anyMatch(roleDomain -> roleDomain.getRole().equals(roleType));
    }

}
