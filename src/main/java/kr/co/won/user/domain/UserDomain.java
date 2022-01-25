package kr.co.won.user.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"idx"})
@ToString()
@Entity
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
public class UserDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
}
