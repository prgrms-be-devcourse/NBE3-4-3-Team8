// components/book/ExchangeReturnInfo.tsx
export const ExchangeReturnInfo = () => {
  return (
    <div className="p-6">
      <h2 className="text-xl font-bold mb-4">반품/교환안내</h2>
      <table className="w-full border-collapse border border-gray-300 text-sm">
        <tbody>
          <tr>
            <td className="border border-gray-300 bg-gray-50 p-3 w-[180px] font-semibold">
              반품/교환 방법
            </td>
            <td className="border border-gray-300 p-3">
              "마이페이지&gt;주문조회&gt;반품/교환신청", 1:1상담 &gt; 반품/교환 또는 고객센터
              <br />
              판매자 배송상품은 판매자와 반품/교환이 협의된 상품에 한해 가능
            </td>
          </tr>
          <tr>
            <td className="border border-gray-300 bg-gray-50 p-3 font-semibold">
              반품/교환 가능기간
            </td>
            <td className="border border-gray-300 p-3">
              ∘ 변심반품 수령 후 20일
              <br />∘ 파본 등 상품결함 시 "문제점 발견 후 30일(단, 수령일로부터 3개월)" 이내
            </td>
          </tr>
          <tr>
            <td className="border border-gray-300 bg-gray-50 p-3 font-semibold">반품/교환 비용</td>
            <td className="border border-gray-300 p-3">
              ∘ 변심 혹은 구매착오의 경우에만 반송료 고객 부담
              <br />
              ∘ 해외직배송도서의 변심 혹은 구매착오로 인한 취소/반품은 판매가의 20% 취소수수료로
              고객 부담
              <br />
              * 취소수수료: 수입제반비용(국내 까지의 운송비, 관세사비, 보세창고료, 내륙 운송비,
              통관비 등)과 재고리스크에 따른 비용 등<br />
              - 오늘 00시~06시 주문은 오늘 06시 이전 취소
              <br />- 오늘 06시 이후 주문 후 다음 날 06시 이전 취소
            </td>
          </tr>
          <tr>
            <td className="border border-gray-300 bg-gray-50 p-3 font-semibold">
              반품/교환 불가 사유
            </td>
            <td className="border border-gray-300 p-3">
              ∘ 소비자의 책임 있는 사유로 상품 등이 손실 또는 훼손된 경우
              <br />
              ∘ 소비자의 사용, 포장 개봉에 의해 상품 등의 가치가 현저히 감소한 경우
              <br />
              ∘ 복제가 가능 또는 단기간 내 완독 가능 상품의 자체 포장이나 래핑을 훼손한 경우
              <br />
              ∘ 소비자 요청에 의한 주문 제작 상품(북셀프도서, POD 도서 등)
              <br />
              ∘ 세트 상품 일부만 반품 불가(전체 반품 후 낱권 재구매)
              <br />
              ∘ 디지털 컨텐츠인 eBook, 오디오북 등을 1회 이상 다운로드 받았거나 웰북으로 보기 혹은
              듣기를 한 경우
              <br />
              ∘ 대여 기간이 종료된 eBook, 오디오북 대여제 상품
              <br />∘ 신선도 문제로 일정 기한 경과 시 상품 가치가 현저하게 감소하는 상품
            </td>
          </tr>
          <tr>
            <td className="border border-gray-300 bg-gray-50 p-3 font-semibold">
              소비자 피해보상
              <br />
              환불지연에 따른 배상
            </td>
            <td className="border border-gray-300 p-3">
              ∘ 상품의 불량에 의한 반품, 교환, A/S, 환불, 품질보증 및 피해보상 등에 관한 사항은
              소비자분쟁해결기준에 따라 처리됨
              <br />∘ 대금 환불 및 환불 지연에 따른 배상금 지급 조건, 절차 등은 전자상거래 등에서의
              소비자 보호에 관한 법률에 따라 처리됨
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  );
};
