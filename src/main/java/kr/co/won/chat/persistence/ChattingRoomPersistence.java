package kr.co.won.chat.persistence;

import kr.co.won.chat.domain.ChattingRoomDomain;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChattingRoomPersistence extends JpaRepository<ChattingRoomDomain, Long> {
}
