/**
 * This file was auto-generated by openapi-typescript.
 * Do not make direct changes to the file.
 */

export interface paths {
    "/reviews/{reviewId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /** 리뷰 수정 */
        put: operations["updateReview"];
        post?: never;
        /** 리뷰 삭제 */
        delete: operations["deleteReview"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/deliveryInformation/{id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put: operations["putDeliveryInformation"];
        post?: never;
        delete: operations["deleteDeliveryInformation"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/cart/{bookId}/{memberId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        /** 장바구니 수정 */
        put: operations["updateCartItem"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/my": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getMyPage"];
        put: operations["putMyPage"];
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/reviews/{book-id}/{member-id}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 리뷰 등록 */
        post: operations["createReview"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/question": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["postQuesiton"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/deliveryInformation": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["postDeliveryInformation"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/cart/{memberId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 장바구니 조회 */
        get: operations["getCart"];
        put?: never;
        /** 장바구니 수정 json */
        post: operations["updateCartItems"];
        /** 장바구니 삭제 */
        delete: operations["deleteBook"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/cart/items": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        /** 장바구니 추가 */
        post: operations["addCart"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/books/admin/books": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["addBook"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/api/auth/logout": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["logout"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/login": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post: operations["adminLogin"];
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/books/admin/books/{bookId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch: operations["updateBookPart"];
        trace?: never;
    };
    "/reviews": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 전체 리뷰 조회 */
        get: operations["getAllReviews"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/reviews/{bookId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 특정 도서 리뷰 조회 */
        get: operations["getReviewsById"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/orders": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getOrders"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/orders/{orderId}/details": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getDetailOrders"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/event/banners": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get: operations["getBannerImages"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/books": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 전체 도서 조회 */
        get: operations["getAllBooks"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/books/{bookId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 특정 도서 조회 */
        get: operations["getBookById"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/books/search": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 도서 검색 (제목, 저자, ISBN13, 출판사 검색) */
        get: operations["searchBooks"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 전체 회원 주문 조회 */
        get: operations["getAllOrdersForAdmin"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/admin/orders/{orderId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        /** 주문 ID, 상세 주문 정보 조회 - 관리자 */
        get: operations["getOrderDetails"];
        put?: never;
        post?: never;
        delete?: never;
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
    "/my/orders/{orderId}": {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        get?: never;
        put?: never;
        post?: never;
        delete: operations["deleteOrder"];
        options?: never;
        head?: never;
        patch?: never;
        trace?: never;
    };
}
export type webhooks = Record<string, never>;
export interface components {
    schemas: {
        ReqDeliveryInformationDto: {
            /** Format: int64 */
            id?: number;
            addressName: string;
            postCode: string;
            detailAddress: string;
            recipient: string;
            phone: string;
            isDefaultAddress: boolean;
        };
        PutReqMemberMyPageDto: {
            name: string;
            phoneNumber: string;
        };
        Book: {
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            /** Format: int64 */
            id?: number;
            title: string;
            author: string;
            isbn?: string;
            isbn13: string;
            /** Format: date */
            pubDate: string;
            /** Format: int32 */
            priceStandard: number;
            /** Format: int32 */
            pricesSales: number;
            /** Format: int32 */
            stock: number;
            /** Format: int32 */
            status: number;
            /** Format: double */
            rating?: number;
            /** Format: double */
            averageRating?: number;
            toc?: string;
            coverImage?: string;
            description?: string;
            descriptionImage?: string;
            /** Format: int64 */
            salesPoint?: number;
            /** Format: int64 */
            reviewCount?: number;
            publisher?: string;
            review?: components["schemas"]["Review"][];
        };
        Cart: {
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            /** Format: int64 */
            id?: number;
            /** Format: int32 */
            quantity?: number;
        };
        DeliveryInformation: {
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            /** Format: int64 */
            id?: number;
            addressName?: string;
            postCode?: string;
            detailAddress?: string;
            isDefaultAddress?: boolean;
            recipient?: string;
            phone?: string;
            member?: components["schemas"]["Member"];
        };
        GrantedAuthority: {
            authority?: string;
        };
        Member: {
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            /** Format: int64 */
            id?: number;
            name?: string;
            phoneNumber?: string;
            /** @enum {string} */
            memberType?: "USER" | "ADMIN";
            email?: string;
            password?: string;
            deliveryInformations?: components["schemas"]["DeliveryInformation"][];
            carts?: components["schemas"]["Cart"][];
            username?: string;
            authorities?: components["schemas"]["GrantedAuthority"][];
            oauthId?: string;
        };
        Review: {
            /** Format: date-time */
            createDate?: string;
            /** Format: date-time */
            modifyDate?: string;
            /** Format: int64 */
            id?: number;
            book?: components["schemas"]["Book"];
            member?: components["schemas"]["Member"];
            content?: string;
            /** Format: double */
            rating?: number;
        };
        ReqQuestionDto: {
            content: string;
            title: string;
        };
        CartItemRequestDto: {
            /** Format: int64 */
            bookId?: number;
            /** Format: int32 */
            quantity?: number;
        };
        CartRequestDto: {
            cartItems?: components["schemas"]["CartItemRequestDto"][];
        };
        AdminLoginDto: {
            username?: string;
            password?: string;
        };
        BookPatchRequestDto: {
            title?: string;
            author?: string;
            isbn?: string;
            isbn13?: string;
            /** Format: date */
            pubDate?: string;
            /** Format: int32 */
            priceStandard?: number;
            /** Format: int32 */
            priceSales?: number;
            /** Format: int32 */
            stock?: number;
            /** Format: int32 */
            status?: number;
            /** Format: double */
            rating?: number;
            toc?: string;
            cover?: string;
            description?: string;
            descriptionImage?: string;
            categoryId?: components["schemas"]["Category"];
            validStatus?: boolean;
        };
        Category: {
            /** Format: int64 */
            id?: number;
            /** Format: int32 */
            categoryId: number;
            categoryName: string;
            mall: string;
            depth1: string;
            depth2?: string;
            depth3?: string;
            depth4?: string;
            depth5?: string;
            books?: components["schemas"]["Book"][];
            category?: string;
        };
        BookResponseDto: {
            /** Format: int64 */
            id?: number;
            title?: string;
            author?: string;
            isbn?: string;
            isbn13?: string;
            publisher?: string;
            /** Format: date */
            pubDate?: string;
            /** Format: int32 */
            priceStandard?: number;
            /** Format: int32 */
            priceSales?: number;
            /** Format: int64 */
            salesPoint?: number;
            /** Format: int32 */
            stock?: number;
            /** Format: int32 */
            status?: number;
            /** Format: double */
            rating?: number;
            toc?: string;
            /** Format: int64 */
            reviewCount?: number;
            coverImage?: string;
            /** Format: int32 */
            categoryId?: number;
            description?: string;
            descriptionImage?: string;
            /** Format: double */
            averageRating?: number;
        };
        PageReviewResponseDto: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["ReviewResponseDto"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            first?: boolean;
            last?: boolean;
            pageable?: components["schemas"]["PageableObject"];
            /** Format: int32 */
            numberOfElements?: number;
            empty?: boolean;
        };
        PageableObject: {
            /** Format: int64 */
            offset?: number;
            sort?: components["schemas"]["SortObject"];
            paged?: boolean;
            /** Format: int32 */
            pageNumber?: number;
            /** Format: int32 */
            pageSize?: number;
            unpaged?: boolean;
        };
        ReviewResponseDto: {
            /** Format: int64 */
            bookId?: number;
            /** Format: int64 */
            reviewId?: number;
            author?: string;
            content?: string;
            /** Format: double */
            rating?: number;
            /** Format: date-time */
            createDate?: string;
        };
        SortObject: {
            empty?: boolean;
            sorted?: boolean;
            unsorted?: boolean;
        };
        OrderDTO: {
            /** Format: int64 */
            memberId?: number;
            orderStatus?: string;
            /** Format: int64 */
            totalPrice?: number;
        };
        DetailOrderDto: {
            /** Format: int64 */
            orderId?: number;
            /** Format: int64 */
            bookId?: number;
            /** Format: int32 */
            bookQuantity?: number;
            /** @enum {string} */
            deliveryStatus?: "PENDING" | "SHIPPED" | "DELIVERED";
        };
        CartResponseDto: {
            /** Format: int64 */
            memberId?: number;
            /** Format: int64 */
            bookId?: number;
            /** Format: int32 */
            quantity?: number;
            title?: string;
            /** Format: int32 */
            price?: number;
            coverImage?: string;
        };
        PageBookResponseDto: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["BookResponseDto"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            first?: boolean;
            last?: boolean;
            pageable?: components["schemas"]["PageableObject"];
            /** Format: int32 */
            numberOfElements?: number;
            empty?: boolean;
        };
        AdminDetailOrderDTO: {
            /** Format: int64 */
            id?: number;
            /** Format: int64 */
            orderId?: number;
            /** Format: date-time */
            modifyDate?: string;
            bookTitle?: string;
            /** Format: int32 */
            bookQuantity?: number;
            deliveryStatus?: string;
        };
        AdminOrderDTO: {
            /** Format: int64 */
            orderId?: number;
            /** Format: date-time */
            createdDate?: string;
            /** Format: int64 */
            totalPrice?: number;
            status?: string;
            detailOrders?: components["schemas"]["AdminDetailOrderDTO"][];
        };
        PageAdminOrderDTO: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["AdminOrderDTO"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            first?: boolean;
            last?: boolean;
            pageable?: components["schemas"]["PageableObject"];
            /** Format: int32 */
            numberOfElements?: number;
            empty?: boolean;
        };
        PageAdminDetailOrderDTO: {
            /** Format: int64 */
            totalElements?: number;
            /** Format: int32 */
            totalPages?: number;
            /** Format: int32 */
            size?: number;
            content?: components["schemas"]["AdminDetailOrderDTO"][];
            /** Format: int32 */
            number?: number;
            sort?: components["schemas"]["SortObject"];
            first?: boolean;
            last?: boolean;
            pageable?: components["schemas"]["PageableObject"];
            /** Format: int32 */
            numberOfElements?: number;
            empty?: boolean;
        };
    };
    responses: never;
    parameters: never;
    requestBodies: never;
    headers: never;
    pathItems: never;
}
export type $defs = Record<string, never>;
export interface operations {
    updateReview: {
        parameters: {
            query: {
                content: string;
                rating: number;
            };
            header?: never;
            path: {
                reviewId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    deleteReview: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                reviewId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    putDeliveryInformation: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ReqDeliveryInformationDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    deleteDeliveryInformation: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                id: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    updateCartItem: {
        parameters: {
            query: {
                quantity: number;
            };
            header?: never;
            path: {
                bookId: number;
                memberId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    getMyPage: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    putMyPage: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["PutReqMemberMyPageDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    createReview: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                "book-id": number;
                "member-id": number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["Review"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    postQuesiton: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ReqQuestionDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    postDeliveryInformation: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["ReqDeliveryInformationDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    getCart: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                memberId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["CartResponseDto"][];
                };
            };
        };
    };
    updateCartItems: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                memberId: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CartRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    deleteBook: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                memberId: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CartRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    addCart: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["CartItemRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content?: never;
            };
        };
    };
    addBook: {
        parameters: {
            query?: {
                isbn13?: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    logout: {
        parameters: {
            query?: never;
            header: {
                Authorization: string;
            };
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    adminLogin: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["AdminLoginDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": Record<string, never>;
                };
            };
        };
    };
    updateBookPart: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                bookId: number;
            };
            cookie?: never;
        };
        requestBody: {
            content: {
                "application/json": components["schemas"]["BookPatchRequestDto"];
            };
        };
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["BookResponseDto"];
                };
            };
        };
    };
    getAllReviews: {
        parameters: {
            query?: {
                page?: number;
                pageSize?: number;
                reviewSortType?: "CREATE_AT_DESC" | "CREATE_AT_ASC" | "RATING_DESC" | "RATING_ASC" | "PUBLISHED_DATE" | "SALES_POINT" | "RATING" | "REVIEW_COUNT";
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageReviewResponseDto"];
                };
            };
        };
    };
    getReviewsById: {
        parameters: {
            query?: {
                page?: number;
                pageSize?: number;
                reviewSortType?: "CREATE_AT_DESC" | "CREATE_AT_ASC" | "RATING_DESC" | "RATING_ASC" | "PUBLISHED_DATE" | "SALES_POINT" | "RATING" | "REVIEW_COUNT";
            };
            header?: never;
            path: {
                bookId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageReviewResponseDto"];
                };
            };
        };
    };
    getOrders: {
        parameters: {
            query: {
                memberId: number;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["OrderDTO"][];
                };
            };
        };
    };
    getDetailOrders: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                orderId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["DetailOrderDto"][];
                };
            };
        };
    };
    getBannerImages: {
        parameters: {
            query?: never;
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": string[];
                };
            };
        };
    };
    getAllBooks: {
        parameters: {
            query?: {
                page?: number;
                pageSize?: number;
                bookSortType?: "PUBLISHED_DATE" | "SALES_POINT" | "RATING" | "REVIEW_COUNT";
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageBookResponseDto"];
                };
            };
        };
    };
    getBookById: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                bookId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["BookResponseDto"];
                };
            };
        };
    };
    searchBooks: {
        parameters: {
            query: {
                page?: number;
                pageSize?: number;
                bookSortType?: "PUBLISHED_DATE" | "SALES_POINT" | "RATING" | "REVIEW_COUNT";
                searchType?: "TITLE" | "AUTHOR" | "ISBN13" | "PUBLISHER";
                keyword: string;
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageBookResponseDto"];
                };
            };
        };
    };
    getAllOrdersForAdmin: {
        parameters: {
            query?: {
                page?: number;
                pageSize?: number;
                sortType?: "ORDER_DATE" | "TOTAL_PRICE" | "STATUS";
            };
            header?: never;
            path?: never;
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageAdminOrderDTO"];
                };
            };
        };
    };
    getOrderDetails: {
        parameters: {
            query?: {
                page?: number;
                size?: number;
                sort?: string;
            };
            header?: never;
            path: {
                orderId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": components["schemas"]["PageAdminDetailOrderDTO"];
                };
            };
        };
    };
    deleteOrder: {
        parameters: {
            query?: never;
            header?: never;
            path: {
                orderId: number;
            };
            cookie?: never;
        };
        requestBody?: never;
        responses: {
            /** @description OK */
            200: {
                headers: {
                    [name: string]: unknown;
                };
                content: {
                    "application/json;charset=UTF-8": string;
                };
            };
        };
    };
}
