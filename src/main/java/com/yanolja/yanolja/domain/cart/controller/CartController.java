package com.yanolja.yanolja.domain.cart.controller;

import com.yanolja.yanolja.domain.auth.config.model.CustomUserDetails;
import com.yanolja.yanolja.domain.cart.model.request.CartDeleteRequest;
import com.yanolja.yanolja.domain.cart.model.request.CartRequest;
import com.yanolja.yanolja.domain.cart.model.response.CartCountResponse;
import com.yanolja.yanolja.domain.cart.model.response.CartListResponse;
import com.yanolja.yanolja.domain.cart.service.CartService;
import com.yanolja.yanolja.global.util.APIUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping("")
    public ResponseEntity createCart(
        @RequestBody @Valid CartRequest cartRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        cartService.createCart(cartRequest, customUserDetails.getUserId());
        return APIUtil.OK();
    }

    @GetMapping("")
    public ResponseEntity findCartListByUserId(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){

        CartListResponse response = cartService.findCartListByUserId(customUserDetails);
        return APIUtil.OK(response);
    }

    @DeleteMapping("")
    public ResponseEntity deleteCartByCartIdList(
        @ModelAttribute @Valid CartDeleteRequest cartDeleteRequest,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        cartService.deleteCartByCartIdList(cartDeleteRequest.cartList(),customUserDetails);
        return APIUtil.OK();

    }

    @GetMapping("/count")
    public ResponseEntity getCartCount(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ){
        CartCountResponse cartCountResponse = cartService.getCartCount(customUserDetails);
        return APIUtil.OK(cartCountResponse);

    }
}
