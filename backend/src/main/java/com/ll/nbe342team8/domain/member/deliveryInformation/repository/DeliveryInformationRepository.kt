package com.ll.nbe342team8.domain.member.deliveryInformation.repository

import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation
import com.ll.nbe342team8.domain.member.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface DeliveryInformationRepository : JpaRepository<DeliveryInformation?, Long?> {

    @Query(
        "SELECT COUNT(d) > 0 FROM DeliveryInformation d " +
                "WHERE d.member = :member " +
                "AND d.addressName = :addressName " +
                "AND d.postCode = :postCode " +
                "AND d.detailAddress = :detailAddress " +
                "AND d.recipient = :recipient " +
                "AND d.phone = :phone " +
                "AND d.createDate >= :cutoffTime"
    )
    fun existsDuplicateInShortTime(
        @Param("member") member: Member,
        @Param("addressName") addressName: String,
        @Param("postCode") postCode: String,
        @Param("detailAddress") detailAddress: String,
        @Param("recipient") recipient: String,
        @Param("phone") phone: String,
        @Param("cutoffTime") cutoffTime: LocalDateTime
    ): Boolean
}