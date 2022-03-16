package kr.co.won.websocket.stomp.persistence;

import kr.co.won.websocket.stomp.domain.ChattingRoomUserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomUserPersistence extends JpaRepository<ChattingRoomUserDomain, Long> {
}
