package kr.co.won.iot.persistence;

import kr.co.won.iot.domain.SiteDomain;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * SitePersistence
 * Version:V0.0.1
 * author: Douckhee Won
 * DATE: 2022/06/19
 * <p>
 * DESCRIPTION
 * IoT Site Persistence
 */

public interface SitePersistence extends JpaRepository<SiteDomain, Long> {
}
