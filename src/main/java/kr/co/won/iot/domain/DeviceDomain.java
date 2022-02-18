package kr.co.won.iot.domain;

import lombok.*;
import net.bytebuddy.asm.Advice;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"idx"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_device")
public class DeviceDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;

    @Enumerated(EnumType.STRING)
    private DeviceType type;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /** Device Domain Function */

}
