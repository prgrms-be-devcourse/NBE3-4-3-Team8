export interface DeliveryInformationDto {
    id: number;
    addressName: string;
    postCode: string;
    detailAddress: string;
    recipient: string;
    phone: string;
    isDefaultAddress: boolean;
  }



export interface MemberMyPageDto {
    name: string;
    phoneNumber: string;
    deliveryInformationDtos: DeliveryInformationDto[];
  }

  export interface errorDto {
    message : string;
  }