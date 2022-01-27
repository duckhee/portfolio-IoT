package kr.co.won.user.domain;

import kr.co.won.address.Address;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public void addRole(UserRoleDomain... roles) {
        for (int i = 0; i < roles.length; i++) {
            this.addRole(roles[i]);
        }
    }

    /**
     * user role have check
     */
    public boolean roleCheck(UserRoleType roleType) {
        return this.roles.stream().anyMatch(roleDomain -> roleDomain.getRole().equals(roleType));
    }

    public void removeRole(UserRoleType role) {
//        this.roles.stream().filter(roleDomain -> roleDomain.getRole().equals(role)).collect(Collectors.toList());
        // remove role not default user role delete
        if (!role.equals(UserRoleType.USER)) {
//            roles.removeIf(roleDomain -> roleDomain.getRole().equals(role));

            List<UserRoleDomain> userRoles = this.roles.stream()
                    .filter(roleDomain -> roleDomain.getRole().equals(role))
                    .collect(Collectors.toList());
            // user role set user null
            userRoles.forEach(userRole -> {
                roles.remove(userRole);
                userRole.setUser(null);
            });

        }
    }

    public void removeRole(UserRoleType... roles) {
        for (int i = 0; i < roles.length; i++) {
            this.removeRole(roles[i]);
        }
    }

}
