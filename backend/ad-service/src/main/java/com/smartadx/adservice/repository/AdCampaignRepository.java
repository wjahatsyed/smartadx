package com.smartadx.adservice.repository;

import com.smartadx.adservice.model.AdCampaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdCampaignRepository extends JpaRepository<AdCampaign, Long> {
    @Query("SELECT c FROM AdCampaign c LEFT JOIN FETCH c.interestTags")
    List<AdCampaign> findAllWithInterests();


}
