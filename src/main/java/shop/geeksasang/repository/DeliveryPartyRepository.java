package shop.geeksasang.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import shop.geeksasang.config.domain.OrderTimeCategoryType;
import shop.geeksasang.domain.DeliveryParty;


@Repository
public interface DeliveryPartyRepository extends JpaRepository<DeliveryParty,Integer> {

    @Query("select dp from DeliveryParty dp where dp.dormitory.id = :dormitoryId")
    Slice<DeliveryParty> findDeliveryPartiesByDormitoryId(int dormitoryId, Pageable pageable);

    @Query("select dp from DeliveryParty dp where dp.dormitory.id = :dormitoryId and dp.maxMatching <= :maxMatching")
    Slice<DeliveryParty> findDeliveryPartiesByMaxMatching(int dormitoryId, int maxMatching, Pageable pageable);

    @Query("select dp from DeliveryParty dp where dp.dormitory.id = :dormitoryId and dp.orderTimeCategory = :orderTimeCategory")
    Slice<DeliveryParty> findDeliveryPartiesByOrderTime(int dormitoryId, @Param("orderTimeCategory") OrderTimeCategoryType orderTimeCategory, Pageable pageable);

    @Query("select dp from DeliveryParty dp where dp.dormitory.id = :dormitoryId and dp.title LIKE :keyword")
    Slice<DeliveryParty> findDeliveryPartiesByKeyword(int dormitoryId, String keyword, Pageable pageable);
}
