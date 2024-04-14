package com.anon.ecom.cart.services;
import com.anon.ecom.cart.CartRepository;
import com.anon.ecom.cart.domain.CartItemDto;
import com.anon.ecom.cart.domain.CartItemEntity;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final Mapper<ItemEntity, ItemDto> itemMapper;
    private final Mapper<UserEntity, UserDto> userMapper;
    private final Mapper<CartItemEntity, CartItemDto> cartItemMapper;
    private final ItemCopyRepository itemCopyRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, Mapper<ItemEntity, ItemDto> itemMapper, Mapper<UserEntity, UserDto> userMapper, Mapper<CartItemEntity, CartItemDto> cartItemMapper, ItemCopyRepository itemCopyRepository, ItemRepository itemRepository, UserService userService) {
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
    public List<CartItemEntity> findAll() {
        List<CartItemEntity> cartEntities = new ArrayList<>();
        cartRepository.findAll().forEach(cartEntities::add);
        return cartEntities;
    }
    @Override
    public Optional<CartItemEntity> findOne(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public CartItemEntity saveOrPartialUpdate(CartItemEntity cartItemEntity) {
        if (cartItemEntity.getId() != null && cartRepository.existsByItemIdAndUserIdAndSellerIdAndPrice(cartItemEntity.getItem().getId(), cartItemEntity.getUser().getId(), cartItemEntity.getSeller().getId(), cartItemEntity.getPrice())) {
            cartRepository.partialUpdate(cartItemEntity);
            return cartRepository.findById(cartItemEntity.getId())
                    .orElseThrow(() -> new EntityNotFoundException("CartItemEntity not found with id: " + cartItemEntity.getId()));
        }
        if (cartItemEntity.getQuantity() == null) {
            return CartItemEntity.builder().build();
        }
        return cartRepository.save(cartItemEntity);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartRepository.deleteById(id);
    }
    @Override
    public CartItemDto cartManipulation(CartItemDto cartItemDto) {

        ItemEntity item = itemRepository.findById(cartItemDto.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException(cartItemDto.getItem().getId()));

        UserEntity seller = userRepository.findById(cartItemDto.getSeller().getId())
                .orElseThrow(() -> new UserNotFoundException(cartItemDto.getSeller().getUsername()));

        int itemCodesInStock = itemCopyRepository.findAllSellOffersByItemIdAndSellerIdAndPrice(seller.getId(), item.getId(), cartItemDto.getPrice()).size();
        if (itemCodesInStock < 1) {
            return CartItemDto.builder().build();
        }

        UserEntity user = userService.getUser();

        if (cartRepository.existsByItemIdAndUserIdAndSellerIdAndPrice(item.getId(), user.getId(), seller.getId(), cartItemDto.getPrice())) {

            CartItemEntity cartItemEntity = cartRepository.findByItemIdAndUserIdAndSellerIdAndPrice(item.getId(), user.getId(), seller.getId(), cartItemDto.getPrice());

            if (cartItemEntity.getQuantity() + cartItemDto.getQuantity() <= 0) {
                deleteCartItem(cartItemEntity.getId());
                return CartItemDto.builder().build();
            } else {
                cartItemEntity.setQuantity(Math.min(cartItemEntity.getQuantity() + cartItemDto.getQuantity(), itemCodesInStock));
            }

            return cartItemMapper.mapTo(cartItemEntity);
        } else {
            if (cartItemDto.getQuantity() > itemCodesInStock) {
                cartItemDto.setQuantity(itemCodesInStock);
            }
            cartItemDto.setUser(userMapper.mapTo(user));
            cartItemDto.setSeller(userMapper.mapTo(seller));
            cartItemDto.setItem(itemMapper.mapTo(item));

            return cartItemDto;
        }
    }

    @Override
    public List<CartItemEntity> findUserCartItems(Long userId) {
        return cartRepository.findUserCartItems(userId);
    }


    @Override
    public CartItemEntity findUserCartItem(Long userId, Long itemId, Long sellerId, BigDecimal price){
        return cartRepository.findByItemIdAndUserIdAndSellerIdAndPrice(userId, itemId, sellerId, price);
    }
}



