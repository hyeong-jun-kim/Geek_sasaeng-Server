package shop.geeksasang.repository.chat;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import shop.geeksasang.domain.chat.ChatRoom;
import shop.geeksasang.domain.chat.PartyChatRoom;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartyChatRoomRepository extends MongoRepository<PartyChatRoom, String> {
    @Query("{_id:'?0'}") // 0번째 파라미터 조건
    Optional<PartyChatRoom> findByPartyChatRoomId(String id);

    @Query("{ chief: ?0, 'status' : 'ACTIVE'}")
    Optional<PartyChatRoom> findPartyChatRoomByChiefId(ObjectId chiefId);

    @Query("{ _id : ?0 }")
    //@Update("{ $pull: { 'participants': { _id : ?1 } } }")
    @Update("{ $pull : { 'participants': { $in : [?1] } } }")
    void deleteParticipant(ObjectId roomId, ObjectId participantId);
}
