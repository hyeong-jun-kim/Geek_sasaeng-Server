package shop.geeksasang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.geeksasang.domain.DeliveryParty;
import shop.geeksasang.domain.Member;
import shop.geeksasang.domain.University;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPartyRepository extends JpaRepository<DeliveryParty,Integer> {

    Optional<Member> findMembersById(int id);
    List<DeliveryParty> findDeliveryPartiesByDomitoryId(int domitoryId);

}
