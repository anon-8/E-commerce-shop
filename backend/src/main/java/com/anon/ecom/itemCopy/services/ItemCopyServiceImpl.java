package com.anon.ecom.itemCopy.services;

import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.item.exception.ItemNotFoundException;
import com.anon.ecom.itemCopy.ItemCopyRepository;
import com.anon.ecom.itemCopy.domain.ItemCopyDto;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.user.UserRepository;
import com.anon.ecom.user.exception.UserNotFoundException;
import com.anon.ecom.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public List<ItemCopyEntity> findAll() {
        return itemCopyRepository.findAll();
    }
    @Override
    public Page<ItemCopyEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<ItemCopyEntity> findAllSellOffersByUserId(Long userId) {
        return new ArrayList<>(itemCopyRepository.findAllSellOffersBySellerId(userId));
    }

    @Override
    public List<ItemCopyEntity> findLatestSellOffersByItemIdAndSellerIdAndPrice(Long itemId, Long sellerId, BigDecimal price) {
        return itemCopyRepository.findLatestSellOffersByItemIdAndSellerIdAndPrice(itemId, sellerId, price);
    }

    @Override
    public List<ItemCopyEntity> findAllSellOffersByUserIdAndItemId(Long userId, Long itemId) {
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
    public ItemCopyEntity save(ItemCopyEntity itemCopyEntity) { return itemCopyRepository.save(itemCopyEntity); }

    @Override
    public ItemCopyEntity putItemCopyForSale(ItemCopyDto itemCopyDto) {

        UserEntity user = userService.getUser();

        String username = user.getUsername();
        UserEntity seller = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UserNotFoundException(username));

        ItemEntity itemEntity = itemRepository.findById(itemCopyDto.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException(itemCopyDto.getItem().getId()));

        ItemCopyEntity itemCopyEntity = itemCopyMapper.mapFrom(itemCopyDto);

        itemCopyEntity.setItem(itemEntity);
        itemCopyEntity.setSeller(seller);
        itemCopyEntity.setStatus("for sale");
        itemCopyRepository.save(itemCopyEntity);

        return itemCopyEntity;
    }

}

