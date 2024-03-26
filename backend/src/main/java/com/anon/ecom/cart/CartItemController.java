package com.anon.ecom.cart;

import com.anon.ecom.cart.domain.CartItemDto;
import com.anon.ecom.cart.domain.CartItemEntity;
import com.anon.ecom.cart.services.CartItemService;
import com.anon.ecom.config.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class CartItemController {
    private final CartItemService cartItemService;

    private final Mapper<CartItemEntity, CartItemDto> cartItemMapper;

    public CartItemController(Mapper<CartItemEntity, CartItemDto> cartItemMapper, CartItemService cartItemService) {
        this.cartItemMapper = cartItemMapper;
        this.cartItemService = cartItemService;
    }
    @PostMapping(path = "/manipulate-cart")
    public ResponseEntity<CartItemDto> addItemToCart(@RequestBody CartItemDto cartItemDto) {

        CartItemEntity cartItemEntity = cartItemMapper.mapFrom(cartItemService.cartManipulation(cartItemDto));
        CartItemEntity savedCartItemEntity = cartItemService.saveOrPartialUpdate(cartItemEntity);

        return new ResponseEntity<>(cartItemMapper.mapTo(savedCartItemEntity), HttpStatus.CREATED);
    }


    @GetMapping(path = "/in-cart")
    public List<CartItemDto> listCartItems(){
        List<CartItemEntity> cartItems = cartItemService.findAll();
        cartItems.sort(Comparator.comparing(CartItemEntity::getPrice).reversed());
        return cartItems.stream()
                .map(cartItemMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/in-cart/{id}")
    public ResponseEntity<CartItemDto> getCartItem(@PathVariable("id") Long id) {
        Optional<CartItemEntity> foundItem = cartItemService.findOne(id);
        return foundItem.map(cartItemEntity -> {
            CartItemDto cartItemDto = cartItemMapper.mapTo(cartItemEntity);
            return new ResponseEntity<>(cartItemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    }


