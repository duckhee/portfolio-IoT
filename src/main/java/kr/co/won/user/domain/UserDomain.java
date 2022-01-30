package kr.co.won.user.domain;

import kr.co.won.address.Address;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * entity Graph
 */
@NamedEntityGraph(name = "user.withRole",
        attributeNodes = {
                @NamedAttributeNode(value = "roles")
        }
)

@Slf4j
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
    @Column(nullable = false)
    private boolean deleteFlag = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean activeFlag = false;

    @Embedded
    private Address address;

    private String job;

    private String emailCheckToken;

    private LocalDateTime emailCheckTokenGeneratedTime;

    /**
     * email verified check
     */
    @Builder.Default
    @Column(nullable = false)
    private boolean emailVerified = false;

    private LocalDateTime joinTime;


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

    /**
     * user role add
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

    public void addRole(Set<UserRoleDomain> roles) {
        roles.forEach(role -> this.addRole(role));
    }

    /**
     * user role have check
     */
    public boolean hasRole(UserRoleType roleType) {
        return this.roles.stream().anyMatch(roleDomain -> roleDomain.getRole().equals(roleType));
    }

    /**
     * user mulit role check
     */
    public boolean hasRole(UserRoleType... roleTypes) {
        boolean flag = false;
        for (int i = 0; i < roleTypes.length; i++) {
            flag = flag || this.hasRole(roleTypes[i]);
            log.info("get user role check flag ::: {}", this.hasRole(roleTypes[i]));
            log.info("role check flag ::: {}", flag);
        }
        return flag;
    }


    /**
     * user mulit role check strong
     */
    public boolean hasAllMathRole(UserRoleType... roleTypes) {
        boolean flag = true;
        for (int i = 0; i < roleTypes.length; i++) {
            flag = flag && this.hasRole(roleTypes[i]);
            log.info("get user role check flag ::: {}", this.hasRole(roleTypes[i]));
            log.info("role check flag ::: {}", flag);
        }
        return flag;
    }

    /**
     * user role delete
     */
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

    /**
     * email check token create
     */
    public String makeEmailToken() {
        String token = UUID.randomUUID().toString();
        this.emailCheckToken = token;
        this.emailCheckTokenGeneratedTime = LocalDateTime.now();
        return token;
    }

    public boolean isValidToken(String token) {
        return emailCheckToken.equals(token);
    }

    /**
     * user join and active account
     */
    public void join() {
        this.emailVerified = true;
        this.activeFlag = true;
        this.setJoinTime(LocalDateTime.now());
    }

    /**
     * User Email Send Possible
     */
    public boolean canSendConfirmEmail() {
        return this.emailCheckTokenGeneratedTime.isBefore(LocalDateTime.now().minusHours(1));
    }

}
