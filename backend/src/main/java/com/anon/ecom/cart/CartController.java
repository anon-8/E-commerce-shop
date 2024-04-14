package com.anon.ecom.cart;

import com.anon.ecom.cart.domain.CartDto;
import com.anon.ecom.cart.domain.CartEntity;
import com.anon.ecom.cart.services.CartService;
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
public class CartController {
    private final CartService cartService;

    private final Mapper<CartEntity, CartDto> cartItemMapper;

    public CartController(Mapper<CartEntity, CartDto> cartItemMapper, CartService cartService) {
        this.cartItemMapper = cartItemMapper;
        this.cartService = cartService;
    }
    @PostMapping(path = "/manipulate-cart")
    public ResponseEntity<CartDto> addItemToCart(@RequestBody CartDto cartDto) {

        CartEntity cartItemEntity = cartItemMapper.mapFrom(cartService.cartManipulation(cartDto));
        CartEntity savedCartItemEntity = cartService.saveOrPartialUpdate(cartItemEntity);

        return new ResponseEntity<>(cartItemMapper.mapTo(savedCartItemEntity), HttpStatus.CREATED);
    }


    @GetMapping(path = "/in-cart")
    public List<CartDto> listCartItems(){
        List<CartEntity> cartItems = cartService.findAll();
        cartItems.sort(Comparator.comparing(CartEntity::getPrice).reversed());
        return cartItems.stream()
                .map(cartItemMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/in-cart/{id}")
    public ResponseEntity<CartDto> getCartItem(@PathVariable("id") Long id) {
        Optional<CartEntity> foundItem = cartService.findOne(id);
        return foundItem.map(cartItemEntity -> {
            CartDto cartDto = cartItemMapper.mapTo(cartItemEntity);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    }


