package com.anon.ecom.services.impl;

import com.anon.ecom.domain.dto.ItemCopyDto;
import com.anon.ecom.domain.dto.ItemDto;
import com.anon.ecom.domain.dto.UserDto;
import com.anon.ecom.domain.entities.ItemCopyEntity;
import com.anon.ecom.domain.entities.ItemEntity;
import com.anon.ecom.domain.entities.UserEntity;
import com.anon.ecom.mappers.Mapper;
import com.anon.ecom.repositories.ItemCopyRepository;
import com.anon.ecom.repositories.ItemRepository;
import com.anon.ecom.repositories.UserRepository;
import com.anon.ecom.services.ItemCopyService;
import com.anon.ecom.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemCopyServiceImpl implements ItemCopyService {

    private final UserService userService;
    private final ItemCopyRepository itemCopyRepository;
    private final UserRepository userRepository;
    private final Mapper<ItemCopyEntity, ItemCopyDto> itemCopyMapper;
    private final ItemRepository itemRepository;
    public ItemCopyServiceImpl(UserService userService, ItemCopyRepository itemCopyRepository, UserRepository userRepository, Mapper<ItemCopyEntity, ItemCopyDto> itemCopyMapper, ItemRepository itemRepository) {
        this.userService = userService;
        this.itemCopyRepository = itemCopyRepository;
        this.userRepository = userRepository;
        this.itemCopyMapper = itemCopyMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemCopyEntity save(ItemCopyEntity itemCopyEntity) {
        return itemCopyRepository.save(itemCopyEntity);
    }
    @Override
    public List<ItemCopyEntity> findAll() {
        return StreamSupport
                .stream(
                        itemCopyRepository.findAll().spliterator(),
                        false)
                .collect(Collectors.toList());
    }
    @Override
    public Page<ItemCopyEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemCopyEntity> findAllByUserId(Long userId) {
        return new ArrayList<>(itemCopyRepository.findAllSellOffersBySellerId(userId));
    }
    @Override
    public List<ItemCopyEntity> findAllByUserIdAndItemId(Long userId, Long itemId) {
        return new ArrayList<>(itemCopyRepository.findAllSellOffersBySellerIdAndItemId(userId, itemId));
    }
    @Override
    public Optional<ItemCopyEntity> findOne(Long id) {
        return itemCopyRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        itemCopyRepository.deleteById(id);
    }

    @Override
    public ItemCopyDto putItemCopyForSale(ItemCopyDto itemCopyDto) {

        UserEntity user = userService.getUser();

        String username = user.getUsername();
        UserEntity seller = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        ItemEntity itemEntity = itemRepository.findById(itemCopyDto.getItem().getId())
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemCopyDto.getItem().getId()));

        ItemCopyEntity itemCopyEntity = itemCopyMapper.mapFrom(itemCopyDto);

        itemCopyEntity.setItem(itemEntity);
        itemCopyEntity.setSeller(seller);
        itemCopyEntity.setStatus("for sale");
        itemCopyRepository.save(itemCopyEntity);

        return itemCopyMapper.mapTo(itemCopyEntity);
    }

}

