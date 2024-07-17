package com.yanolja.yanolja.domain.cart.model.response;

import com.yanolja.yanolja.domain.cart.model.entity.Cart;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record CartListResponse (
    List<CartResponse> cartList
) {

    public static CartListResponse createCartListResponse(List<Cart> cartList, List<Boolean> isBookingList) {
        List<CartResponse> cartResponseList = new ArrayList<>();
        for(int i=0; i< cartList.size(); i++){
            Cart cart = cartList.get(i);
            cartResponseList.add(new CartResponse(
                cart.getId(),
                cart.getRoom().getAccommodation().getId(),
                cart.getRoom().getAccommodation().getTitle(),
                cart.getPeople(),
                cart.getRoom().getId(),
                cart.getRoom().getTitle(),
                cart.getCheckInDateTime(),
                cart.getCheckOutDateTime(),
                cart.getRoom().getMinPeople(),
                cart.getRoom().getMaxPeople(),
                cart.getTotalPrice(),
                cart.getRoom().getImages().get(1),
                isBookingList.get(i)
                ));
        }
        return new CartListResponse(cartResponseList);
    }

    public record CartResponse (
        Long cartId,
        Long accommodationId,
        String accommodationTitle,
        int people,
        Long roomId,
        String roomTitle,
        LocalDateTime checkInDatetime,
        LocalDateTime checkOutDatetime,
        int minPeople,
        int maxPeople,
        Long totalPrice,
        String roomImg,
        Boolean isBooking
    ){

    }
}
