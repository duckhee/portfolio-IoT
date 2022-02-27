package kr.co.won.websocket.stomp.persistence;

import kr.co.won.websocket.stomp.domain.ChattingRoomDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomPersistence extends JpaRepository<ChattingRoomDomain, Long> {
}
