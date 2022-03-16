package kr.co.won.chat.persistence;

import kr.co.won.chat.domain.ChattingRoomUserDomain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomUserPersistence extends JpaRepository<ChattingRoomUserDomain, Long> {
}
