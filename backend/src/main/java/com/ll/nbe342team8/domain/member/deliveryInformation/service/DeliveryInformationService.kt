package com.ll.nbe342team8.domain.member.deliveryInformation.service

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation
import com.ll.nbe342team8.domain.member.deliveryInformation.repository.DeliveryInformationRepository
import com.ll.nbe342team8.domain.member.member.entity.Member
import com.ll.nbe342team8.global.exceptions.ServiceException
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

@Service
@RequiredArgsConstructor
class DeliveryInformationService(
    private val deliveryInformationRepository: DeliveryInformationRepository
) {


    @Transactional
    fun addDeliveryInformation(member: Member, dto: ReqDeliveryInformationDto) {
        //dto 데이터를 가져와 새로운 배송 정보 개체생성

        val deliveryInformation = DeliveryInformation(dto, member)

        //사용자 개체의 배송 정보 리스트에 생성한 배송 정보개체를 추가 등록한다.
        //더티 체킹을 이용해 사용자 개체를 갱신한다.
        member.addDeliveryInformation(deliveryInformation)
    }

    @Transactional
    fun deleteDeliveryInformation(deliveryInformation: DeliveryInformation, member: Member) {
        //사용자개체의 배송 정보 리스트에서 id에 해당하는 deliveryInformation을 찾아 삭제
        //더티 체킹을 이용해 개체 갱신

        member.deliveryInformations.remove(deliveryInformation)
        deliveryInformationRepository.delete(deliveryInformation)
    }

    @Transactional
    fun modifyDeliveryInformation(
        deliveryInformation: DeliveryInformation,
        dto: ReqDeliveryInformationDto,
        member: Member
    ) {
        //기본 배송지 설정을 결정하는 dto의 isDefaultAddress 가 true인 경우 member의 deliveryinformation 데이터 들의
        //isDefaultAddress 값을 모두 false로 만든 후 deliveryInformation을 갱신 -> 언제나 기본 배송지 1개로 유지 가능하다.
        //dto의 isDefaultAddress가 false인 경우 그냥 데이터 갱신한다.
        //더티 체킹을 이용한 개체 갱신
        if (dto.isDefaultAddress) {
            member.convertFalseDeliveryInformationsIsDefaultAddress()
        }
        deliveryInformation.updateDeliveryInfo(dto)
    }

    fun findById(id: Long): Optional<DeliveryInformation?> {
        return deliveryInformationRepository.findById(id)
    }

    //수정, 삭제하려는 게시글을 사용자가 작성한지 학인
    fun isDeliveryInformationOwner(member: Member, deliveryInformation: DeliveryInformation): Boolean {
        return deliveryInformation.member.id == member.id
    }

    fun existsDuplicateDeliveryInformationInShortTime(
        dto: ReqDeliveryInformationDto,
        member: Member,
        duration: Duration
    ): Boolean {
        val cutoffTime = LocalDateTime.now().minus(duration)
        return deliveryInformationRepository.existsDuplicateInShortTime(
            member,
            dto.addressName,
            dto.postCode,
            dto.detailAddress,
            dto.recipient,
            dto.phone,
            cutoffTime
        )
    }

    //사용자 권한 확인, 관리자 계정이여도 접근 가능
    fun validateDeliveryInformationOwner(member: Member, deliveryInformation: DeliveryInformation) {
        require(
            isDeliveryInformationOwner(member, deliveryInformation) || member.checkAdmin()
        ) { throw ServiceException(HttpStatus.FORBIDDEN.value(), "권한이 없습니다.") }
    }


    fun validateExistsDuplicateDeliveryInformationInShortTime(member: Member, dto : ReqDeliveryInformationDto, duration: Duration) {
        require(!existsDuplicateDeliveryInformationInShortTime(dto, member , duration)) {
            throw ServiceException(HttpStatus.TOO_MANY_REQUESTS.value(), "너무 빠르게 동일한 답변을 등록할 수 없습니다.")
        }
    }
}