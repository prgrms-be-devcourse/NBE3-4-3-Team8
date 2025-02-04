package com.ll.nbe342team8.domain.member.deliveryInformation.service;

import com.ll.nbe342team8.domain.member.deliveryInformation.dto.ReqDeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.entity.DeliveryInformation;
import com.ll.nbe342team8.domain.member.deliveryInformation.dto.DeliveryInformationDto;
import com.ll.nbe342team8.domain.member.deliveryInformation.repository.DeliveryInformationRepository;
import com.ll.nbe342team8.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryInformationService {

    private final DeliveryInformationRepository deliveryInformationRepository;

    @Transactional
    public void addDeliveryInformation(Member member, ReqDeliveryInformationDto dto) {

        //dto 데이터를 가져와 새로운 배송 정보 개체생성
        DeliveryInformation deliveryInformation=new DeliveryInformation(dto,member);

        //사용자 개체의 배송 정보 리스트에 생성한 배송 정보개체를 추가 등록한다.
        //더티 체킹을 이용해 사용자 개체를 갱신한다.
        member.addDeliveryInformaiton(deliveryInformation);

    }

    @Transactional
    public void deleteDeliveryInformation(Member member,Long id) {

        //사용자개체의 배송 정보 리스트에서 id에 해당하는 deliveryInformation을 찾아 삭제
        //더티 체킹을 이용해 개체 갱신
        member.deleteDeliveryInformaiton(id);
    }

    @Transactional
    public void modifyDeliveryInformation(DeliveryInformation deliveryInformation,ReqDeliveryInformationDto dto,Member member) {
        //기본 배송지 설정을 결정하는 dto의 isDefaultAddress 가 true인 경우 member의 deliveryinformation 데이터 들의
        //isDefaultAddress 값을 모두 false로 만든 후 deliveryInformation을 갱신 -> 언제나 기본 배송지 1개로 유지 가능하다.
        //dto의 isDefaultAddress가 false인 경우 그냥 데이터 갱신한다.
        //더티 체킹을 이용한 개체 갱신
        if(dto.getIsDefaultAddress()) {
            member.convertFalseDeliveryInformaitonsIsDefaultAddress();
        }
        deliveryInformation.updateDeliveryInfo(dto);
    }

    public Optional<DeliveryInformation> findById(Long id) {
        return deliveryInformationRepository.findById(id);
    }
}
