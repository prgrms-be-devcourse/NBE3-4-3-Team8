package com.ll.nbe342team8.domain.member.deliveryInformation.repository;

import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryInformationRepository extends JpaRepository<DeliveryInformation, Long> {
}
