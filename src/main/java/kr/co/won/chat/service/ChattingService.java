package kr.co.won.chat.service;

import kr.co.won.chat.domain.ChattingRoomDomain;
import kr.co.won.user.domain.UserDomain;

public interface ChattingService {

    /**
     * create chatting room
     */
    public default ChattingRoomDomain createChatRoom(ChattingRoomDomain room, UserDomain loginUser) {
        return null;
    }

    /**
     * find chatting room
     */
    public default ChattingRoomDomain findChatRoom(Long roomIdx, UserDomain loginUser) {
        return null;
    }

    /**
     * find use chatting room UUID
     */
    public default ChattingRoomDomain findChatRoom(String roomIdx, UserDomain loginUser) {
        return null;
    }

}
