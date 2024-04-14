package com.anon.ecom.cart.services;
import com.anon.ecom.cart.CartRepository;
import com.anon.ecom.cart.domain.CartDto;
import com.anon.ecom.cart.domain.CartEntity;
import com.anon.ecom.item.domain.ItemDto;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.itemCopy.ItemCopyRepository;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.user.UserRepository;
import com.anon.ecom.item.exception.ItemNotFoundException;
import com.anon.ecom.user.exception.UserNotFoundException;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final Mapper<ItemEntity, ItemDto> itemMapper;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final Mapper<CartEntity, CartDto> cartItemMapper;
    private final ItemCopyRepository itemCopyRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, Mapper<ItemEntity, ItemDto> itemMapper, Mapper<UserEntity, UserDto> userMapper, Mapper<CartEntity, CartDto> cartItemMapper, ItemCopyRepository itemCopyRepository, ItemRepository itemRepository, UserService userService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
        this.cartItemMapper = cartItemMapper;
        this.itemCopyRepository = itemCopyRepository;
        this.itemRepository = itemRepository;
        this.userService = userService;
    }
    @Override
    public List<CartEntity> findAll() {
        List<CartEntity> cartEntities = new ArrayList<>();
        cartRepository.findAll().forEach(cartEntities::add);
        return cartEntities;
    }
    @Override
    public Optional<CartEntity> findOne(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public CartEntity saveOrPartialUpdate(CartEntity cartItemEntity) {
        if (cartItemEntity.getId() != null && cartRepository.existsByItemIdAndUserIdAndSellerIdAndPrice(cartItemEntity.getItem().getId(), cartItemEntity.getUser().getId(), cartItemEntity.getSeller().getId(), cartItemEntity.getPrice())) {
            cartRepository.partialUpdate(cartItemEntity);
            return cartRepository.findById(cartItemEntity.getId())
                    .orElseThrow(() -> new EntityNotFoundException("CartItemEntity not found with id: " + cartItemEntity.getId()));
        }
        if (cartItemEntity.getQuantity() == null) {
            return CartEntity.builder().build();
        }
        return cartRepository.save(cartItemEntity);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }
    @Override
    public CartDto cartManipulation(CartDto cartDto) {

        ItemEntity item = itemRepository.findById(cartDto.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException(cartDto.getItem().getId()));

        UserEntity seller = userRepository.findById(cartDto.getSeller().getId())
                .orElseThrow(() -> new UserNotFoundException(cartDto.getSeller().getUsername()));

        int itemCodesInStock = itemCopyRepository.findAllSellOffersByItemIdAndSellerIdAndPrice(seller.getId(), item.getId(), cartDto.getPrice()).size();
        if (itemCodesInStock < 1) {
            return CartDto.builder().build();
        }

        UserEntity user = userService.getUser();

        if (cartRepository.existsByItemIdAndUserIdAndSellerIdAndPrice(item.getId(), user.getId(), seller.getId(), cartDto.getPrice())) {

            CartEntity cartItemEntity = cartRepository.findByItemIdAndUserIdAndSellerIdAndPrice(item.getId(), user.getId(), seller.getId(), cartDto.getPrice());

            if (cartItemEntity.getQuantity() + cartDto.getQuantity() <= 0) {
                deleteCartItem(cartItemEntity.getId());
                return CartDto.builder().build();
            } else {
                cartItemEntity.setQuantity(Math.min(cartItemEntity.getQuantity() + cartDto.getQuantity(), itemCodesInStock));
            }

            return cartItemMapper.mapTo(cartItemEntity);
        } else {
            if (cartDto.getQuantity() > itemCodesInStock) {
                cartDto.setQuantity(itemCodesInStock);
            }
            cartDto.setUser(userMapper.mapTo(user));
            cartDto.setSeller(userMapper.mapTo(seller));
            cartDto.setItem(itemMapper.mapTo(item));

            return cartDto;
        }
    }

    @Override
    public List<CartEntity> findUserCartItems(Long userId) {
        return cartRepository.findUserCartItems(userId);
    }
}



