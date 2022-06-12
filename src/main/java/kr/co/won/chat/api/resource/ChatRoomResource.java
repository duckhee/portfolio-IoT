package kr.co.won.chat.api.resource;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import kr.co.won.blog.api.BlogApiController;
import kr.co.won.blog.api.resource.BlogCollectResources;
import kr.co.won.blog.api.resource.dto.BlogReadResourcesDto;
import kr.co.won.blog.domain.BlogDomain;
import kr.co.won.chat.api.dto.ChatRoomDto;
import kr.co.won.chat.domain.redis.ChatRoomDomain;
import kr.co.won.user.domain.UserDomain;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomResource extends CollectionModel<ChatRoomDomain> {

    @JsonUnwrapped
    private List<ChatRoomDomain> chatRoomList;

}
