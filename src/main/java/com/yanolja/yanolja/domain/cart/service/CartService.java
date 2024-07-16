package com.yanolja.yanolja.domain.cart.service;

import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.auth.model.entity.User;
import com.yanolja.yanolja.domain.booking.exception.BookingException;
import com.yanolja.yanolja.domain.booking.exception.errorcode.BookingErrorCode;
import com.yanolja.yanolja.domain.booking.repository.BookingRepository;
import com.yanolja.yanolja.domain.cart.exception.CartException;
import com.yanolja.yanolja.domain.cart.exception.errorcode.CartErrorCode;
import com.yanolja.yanolja.domain.cart.model.entity.Cart;
import com.yanolja.yanolja.domain.cart.model.request.CartRequest;
import com.yanolja.yanolja.domain.cart.model.response.CartCountResponse;
import com.yanolja.yanolja.domain.cart.model.response.CartListResponse;
import com.yanolja.yanolja.domain.cart.repository.CartRepository;
import com.yanolja.yanolja.domain.room.exception.RoomException;
import com.yanolja.yanolja.domain.room.exception.errorcode.RoomErrorCode;
import com.yanolja.yanolja.domain.room.model.entity.Room;
import com.yanolja.yanolja.domain.room.repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final RoomRepository roomRepository;

    private final CartRepository cartRepository;

    private final BookingRepository bookingRepository;

    public void createCart(CartRequest cartRequest, Long userId) {

        // room 이 올바른지 확인 후 price 가져오기
        Room room = roomRepository.findRoomAndAccommodationById(cartRequest.roomId())
            .orElseThrow(() -> new RoomException(RoomErrorCode.ROOM_NOT_FOUND));

        LocalDateTime checkInDatetime = LocalDateTime.of(cartRequest.checkInDate(), room.getAccommodation().getCheckIn());
        LocalDateTime checkOutDatetime = LocalDateTime.of(cartRequest.checkOutDate(), room.getAccommodation().getCheckOut());

        // 같은 요청의 장바구니가 이미 있는지 확인
        checkEqualsCartInDB(cartRequest.roomId(),userId,checkInDatetime,checkOutDatetime);

        // 예약 가능한 장바구니 인지
        if(isConflictingBookings(cartRequest.roomId(),checkInDatetime,checkOutDatetime)){
            throw new BookingException(BookingErrorCode.CONFLICT_BOOKING);
        }

        // 장바구니에 저장
        Cart cart = Cart.builder()
            .user(User.builder().id(userId).build())
            .room(room)
            .totalPrice(ChronoUnit.DAYS.between(checkInDatetime.toLocalDate(), checkOutDatetime.toLocalDate()) * room.getPrice())
            .people(cartRequest.people())
            .checkInDateTime(checkInDatetime)
            .checkOutDateTime(checkOutDatetime)
            .build();

        cartRepository.save(cart);
    }

    private void checkEqualsCartInDB(Long roomId, Long userId, LocalDateTime checkInDatetime, LocalDateTime checkOutDatetime){
        if (cartRepository.existsByRoomIdAndUserIdAndCheckInDateTimeAndCheckOutDateTime(roomId,userId,checkInDatetime,checkOutDatetime)){
            throw new CartException(CartErrorCode.EQUALS_CART_IN_DB);
        }
    }

    private boolean isConflictingBookings(Long roomId, LocalDateTime checkInDateTime, LocalDateTime checkOutDateTime){
        long conflictBookingCount = bookingRepository.checkConflictingBookings(
            roomId,
            checkInDateTime,
            checkOutDateTime
        );

        if (conflictBookingCount > 0) {
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public CartListResponse findCartListByUserId(CustomUserDetails customUserDetails) {
        //cart 정보 가져오기
        List<Cart> cartList = cartRepository.findByUserId(customUserDetails.getUserId());

        List<Boolean> isBookingList = cartList.stream()
            .map(cart -> (!cart.getCheckInDateTime().toLocalDate().isBefore(LocalDate.now()))
                && !isConflictingBookings(cart.getRoom().getId(), cart.getCheckInDateTime(), cart.getCheckOutDateTime())
            ).toList();
        return CartListResponse.createCartListResponse(cartList,isBookingList);
    }

    @Transactional(readOnly = true)
    public CartCountResponse getCartCount(CustomUserDetails customUserDetails) {
        return new CartCountResponse(cartRepository.countByUserId(customUserDetails.getUserId()));
    }

    public void deleteCartByCartIdList(List<Long> cartIdList, CustomUserDetails customUserDetails) {

        int deleteCount = cartRepository.deleteAllByIds(cartIdList, customUserDetails.getUserId());

        if (deleteCount == 0) {
            throw new CartException(CartErrorCode.FAIL_DELETE);
        }
    }
}
