package kr.co.won.iot.persistence;

import kr.co.won.iot.domain.DeviceDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DevicePersistence extends JpaRepository<DeviceDomain, Long> {
}
