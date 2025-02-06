"use client"

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { MemberMyPageDto,DeliveryInformationDto, errorDto  } from "./types";
import { GetMyPage, PutMyPage, PutMyAddress, PostAddress, DeleteAddress } from "./api";


export default function Home() {

    const [responseBody, setResponseBody] = useState<MemberMyPageDto | null>(null);

    const [isEditing, setIsEditing] = useState(false);
    const [isAddingAddress, setIsAddingAddress] = useState(false);
    const [formData, setFormData] = useState<MemberMyPageDto>({
        name: "",
        phoneNumber: "",
        deliveryInformationDtos: [],
    });
    const [editingAddressId, setEditingAddressId] = useState<number | null>(null);
    const [editedAddress, setEditedAddress] = useState<DeliveryInformationDto | null>(null);
    const [newAddress, setNewAddress] = useState<DeliveryInformationDto>({
        id: 0,
        addressName: "",
        postCode: "",
        detailAddress: "",
        recipient: "",
        phone: "",
        isDefaultAddress: false,
      });

      useEffect(() => {
        GetMyPage()
            .then(async (response) => {
                
                if (!response.ok) {
                    
                    throw new Error(`서버 오류: ${response.status}`);
                }
                const resData = await response.json() as MemberMyPageDto; // 🔹 JSON 변환 후 타입 지정
                console.log(resData);
                setResponseBody(resData);
                setFormData(resData);
            })
            .catch((error) => console.error("데이터 불러오기 실패:", error));
    }, []);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    };

    // 배송지 추가 입력값 변경
  const handleNewAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setNewAddress((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

    // 수정 버튼 클릭 시 입력 모드 전환
    const handleEditMyPage = () => {
        setIsEditing(true);
    };

    // 배송지 추가하기 버튼 클릭 시 입력 폼 표시
    const handleAddAddress = () => {
        setIsAddingAddress(true);
    };

    const handleCancelAddAddress = () => {
        setIsAddingAddress(false);
    };

    const handleEditAddress = (address: DeliveryInformationDto) => {
      setEditingAddressId(address.id);
      setEditedAddress({ ...address }); // 기존 값으로 초기화
  };

  // 입력 값 변경 처리
  const handleEditedAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!editedAddress) return;
    const { name, value } = e.target;
    setEditedAddress((prevState) => ({
        ...prevState!,
        [name]: value,
    }));
  };

  const handleDefaultAddressChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!editedAddress) return;
    setEditedAddress((prevState) => ({
        ...prevState!,
        isDefaultAddress: e.target.checked, // 체크 여부에 따라 값 변경
    }));
};

  

  // 수정 완료 버튼 클릭 시 PUT 요청 전송
  const handleSaveEditedAddress = async () => {
    if (!editedAddress) return;
    try {
        const response = await PutMyAddress(editedAddress, editedAddress.id);

        if (response.ok) {
            let updatedData: MemberMyPageDto = await response.json();
            setResponseBody(updatedData);
            setEditingAddressId(null); // 수정 모드 종료
        } else {
            let errorData = (await response.json()) as errorDto;
            alert(errorData.message);
        }
    } catch (error) {
        console.error("수정 실패:", error);
    }
  };

    // 저장 버튼 클릭 시 PUT 요청 전송
    const handleSaveMyPage = async () => {
        try {
            const response = await PutMyPage(formData);
            
            if (response.ok) {
                let updatedData: MemberMyPageDto;
                updatedData = (await response.json()) as MemberMyPageDto;
                setResponseBody(updatedData); // 응답이 오면 상태 업데이트
                setIsEditing(false); // 입력 모드 종료
            } else {
                let errorData=(await response.json()) as errorDto;
                alert(errorData.message);
            }
        } catch (error) {
            console.error("수정 실패:", error);
        }

        
    };

    const handleDeleteAddress = async (id: number) => {
        try {
            const response = await DeleteAddress(id);
            
            if (response.ok) {
                let updatedData: MemberMyPageDto;
                updatedData = (await response.json()) as MemberMyPageDto;
                setResponseBody(updatedData); // 응답이 오면 상태 업데이트
                
            } else {
                let errorData=(await response.json()) as errorDto;
                alert(errorData.message);
            }
        } catch (error) {
            console.error("수정 실패:", error);
        }
       
    }

    // 새 배송지 정보 저장
    const handleSaveAddress = async () => {

        try {
        const response = await PostAddress(newAddress);
        
        if (response.ok) {
            let updatedData: MemberMyPageDto;
            updatedData = (await response.json()) as MemberMyPageDto;
            setResponseBody(updatedData);
            setNewAddress({
            id:0,
            addressName: "",
            postCode: "",
            detailAddress: "",
            recipient: "",
            phone: "",
            isDefaultAddress: false,
            });
            setIsAddingAddress(false);
        } else {
            let errorData=(await response.json()) as errorDto;
            alert(errorData.message);
        }
        
    }catch (error) {
      console.error("수정 실패:", error);
    }
  }
    

    //console.log("responseBody");
    //console.log(responseBody);

    return (
        <div className="container mx-auto p-4">
          <h1 className="text-2xl font-bold mb-4">마이페이지</h1>
    
          {responseBody ? (
            <div className="bg-white p-6 shadow-md rounded-md">
              {/* 사용자 정보 */}
              <div className="mb-4">
                <h2 className="text-xl font-semibold">사용자 정보</h2>
                {isEditing ? (
                  <>
                    <p>
                      <strong>이름:</strong>{" "}
                      <input
                        type="text"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        className="border p-2 rounded-md"
                      />
                    </p>
                    <p>
                      <strong>전화번호:</strong>{" "}
                      <input
                        type="text"
                        name="phoneNumber"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                        className="border p-2 rounded-md"
                      />
                    </p>
                    <button onClick={handleSaveMyPage} className="bg-blue-500 text-white px-4 py-2 rounded-md mt-2">
                      저장
                    </button>
                  </>
                ) : (
                  <>
                    <p>
                      <strong>이름:</strong> {responseBody.name}
                    </p>
                    <p>
                      <strong>전화번호:</strong> {responseBody.phoneNumber}
                    </p>
                    <button onClick={handleEditMyPage} className="bg-gray-500 text-white px-4 py-2 rounded-md mt-2">
                      수정
                    </button>
                  </>
                )}
              </div>
    
              {/* 배송지 정보 */}
              <div>
                <h2 className="text-xl font-semibold mb-2">배송지 정보</h2>
                {responseBody.deliveryInformationDtos.length > 0 ? (
                  <ul className="space-y-3">
                    {responseBody.deliveryInformationDtos.map((delivery, index) => (
                      <li key={index} className="p-4 border rounded-md">
                        {editingAddressId === delivery.id ? (
                          // ✨ 수정 모드일 때 입력 필드 표시
                          <div className="space-y-2">
                            <input type="text" name="addressName" value={editedAddress?.addressName || ""} onChange={handleEditedAddressChange} className="border p-2 rounded-md w-full" />
                            <input type="text" name="postCode" value={editedAddress?.postCode || ""} onChange={handleEditedAddressChange} className="border p-2 rounded-md w-full" />
                            <input type="text" name="detailAddress" value={editedAddress?.detailAddress || ""} onChange={handleEditedAddressChange} className="border p-2 rounded-md w-full" />
                            <input type="text" name="recipient" value={editedAddress?.recipient || ""} onChange={handleEditedAddressChange} className="border p-2 rounded-md w-full" />
                            <input type="text" name="phone" value={editedAddress?.phone || ""} onChange={handleEditedAddressChange} className="border p-2 rounded-md w-full" />
                            <div className="flex items-center mt-2">
                              <input
                              type="checkbox"
                              checked={editedAddress?.isDefaultAddress || false}
                              onChange={handleDefaultAddressChange}
                              className="mr-2"
                              />
                              <label className="text-sm">기본 배송지로 설정</label>
                            </div>
                            <button onClick={handleSaveEditedAddress} className="bg-blue-500 text-white px-4 py-2 rounded-md">
                             수정 완료
                            </button>
                            <button onClick={() => setEditingAddressId(null)} className="bg-gray-500 text-white px-4 py-2 rounded-md ml-2">
                              취소
                            </button>
                          </div>
                            ) : (
                            // ✨ 기본 보기 모드
                            <>
                              {/* ✅ 기본 배송지 여부 표시 */}
                              {delivery.isDefaultAddress && (
                              <p className="text-green-600 font-semibold">⭐ 기본 배송지</p>
                              )}
                              <p><strong>주소:</strong> {delivery.addressName}</p>
                              <p><strong>우편번호:</strong> {delivery.postCode}</p>
                              <p><strong>상세 주소:</strong> {delivery.detailAddress}</p>
                              <p><strong>수령인:</strong> {delivery.recipient}</p>
                              <p><strong>전화번호:</strong> {delivery.phone}</p>

                              <div className="flex gap-2 mt-2">
                              <button onClick={() => handleEditAddress(delivery)} className="bg-yellow-500 text-white px-4 py-2 rounded-md">
                                수정
                              </button>
                              <button onClick={() => handleDeleteAddress(delivery.id)} className="bg-red-500 text-white px-4 py-2 rounded-md">
                                삭제
                              </button>
                            </div>
                          </>
                        )}
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="text-gray-500">등록된 배송지가 없습니다.</p>
                )}
    
                {/* 배송지 추가 버튼 */}
                {!isAddingAddress ? (
                  
                  <button onClick={handleAddAddress} className="bg-green-500 text-white px-4 py-2 rounded-md mt-2">
                    배송지 추가하기
                  </button>
                  
                  
                ) : (
                  <div className="mt-4 p-4 border rounded-md">
                    <input type="text" name="addressName" placeholder="주소명" onChange={handleNewAddressChange} className="border p-2 rounded-md w-full mb-2" />
                    <input type="text" name="postCode" placeholder="우편번호" onChange={handleNewAddressChange} className="border p-2 rounded-md w-full mb-2" />
                    <input type="text" name="detailAddress" placeholder="상세주소" onChange={handleNewAddressChange} className="border p-2 rounded-md w-full mb-2" />
                    <input type="text" name="recipient" placeholder="수령인" onChange={handleNewAddressChange} className="border p-2 rounded-md w-full mb-2" />
                    <input type="text" name="phone" placeholder="전화번호" onChange={handleNewAddressChange} className="border p-2 rounded-md w-full mb-2" />
                    <div className="flex gap-2 mt-4">
                        <button onClick={handleSaveAddress} className="bg-blue-500 text-white px-4 py-2 rounded-md">
                        저장
                        </button>
                        <button onClick={handleCancelAddAddress} className="bg-gray-500 text-white px-4 py-2 rounded-md">
                        닫기
                        </button>
                    </div>
                  </div>
                )}
              </div>
            </div>
          ) : (
            <p className="text-gray-500">데이터를 불러오는 중...</p>
          )}
        </div>
      );
  }