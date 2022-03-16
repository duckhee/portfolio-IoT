package kr.co.won.chat.service;

import kr.co.won.chat.domain.ChattingRoomDomain;
import kr.co.won.chat.persistence.ChattingRoomPersistence;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.persistence.UserPersistence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service(value = "adminChattingService")
@RequiredArgsConstructor
public class ChattingServiceAdminImpl implements ChattingService {
    // user persistence add
    private final UserPersistence userPersistence;

    // chatting room persistence
    private final ChattingRoomPersistence chatRoomPersistence;

    @Transactional
    @Override
    public ChattingRoomDomain createChatRoom(ChattingRoomDomain room, UserDomain loginUser) {
        UserDomain findUser = userPersistence.findByEmail(loginUser.getEmail()).orElseThrow(() -> new IllegalArgumentException("login first."));
        // room owner setting
        room.setOwnerEmail(findUser.getEmail());
        room.generateRoomId();
        ChattingRoomDomain savedChattingRoom = chatRoomPersistence.save(room);
        return savedChattingRoom;
    }
}
