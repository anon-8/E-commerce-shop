package com.anon.ecom.item;

import com.anon.ecom.item.domain.ItemDto;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.itemCopy.domain.SellOffersDto;
import com.anon.ecom.item.service.ItemService;
import com.anon.ecom.user.domain.dto.UserDto;
import com.anon.ecom.itemCopy.domain.ItemCopyEntity;
import com.anon.ecom.user.domain.entity.UserEntity;
import com.anon.ecom.config.Mapper;
import com.anon.ecom.itemCopy.ItemCopyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;
    private final Mapper<ItemEntity, ItemDto> itemMapper;

    private final Mapper<UserEntity, UserDto> userMapper;

    private final ItemCopyRepository itemCopyRepository;

    public ItemController(Mapper<ItemEntity, ItemDto> itemMapper, ItemService itemService, Mapper<UserEntity, UserDto> userMapper, ItemCopyRepository itemCopyRepository) {
        this.itemMapper = itemMapper;
        this.itemService = itemService;
        this.userMapper = userMapper;
        this.itemCopyRepository = itemCopyRepository;
    }
    @PostMapping(path = "/auth/management/items")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto) {
        ItemEntity itemEntity = itemMapper.mapFrom(itemDto);
        ItemEntity savedItemEntity = itemService.save(itemEntity);
        return new ResponseEntity<>(itemMapper.mapTo(savedItemEntity), HttpStatus.CREATED);
    }
    @PutMapping(path = "/auth/management/items/{id}")
    public ResponseEntity<ItemDto> createUpdateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        ItemEntity itemEntity = itemMapper.mapFrom(itemDto);
        boolean itemExists = itemService.isExists(id);
        ItemEntity savedItemEntity = itemService.createUpdateItem(id, itemEntity);
        ItemDto savedUpdatedItemDto = itemMapper.mapTo(savedItemEntity);

        if(itemExists){
            return new ResponseEntity(savedUpdatedItemDto, HttpStatus.OK);
        } else {
            return new ResponseEntity(savedUpdatedItemDto, HttpStatus.CREATED);
        }
    }
    @PatchMapping(path = "/auth/management/items/{id}")
    public ResponseEntity<ItemDto> partialUpdateItem(
            @PathVariable("id") Long id,
            @RequestBody ItemDto itemDto
    ){
        if(!itemService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ItemEntity itemEntity = itemMapper.mapFrom(itemDto);
        ItemEntity updatedItemEntity = itemService.partialUpdate(id, itemEntity);
        return new ResponseEntity<>(
                itemMapper.mapTo(updatedItemEntity),
                HttpStatus.OK);
    }
    @GetMapping(path = "/items")
    public List<ItemDto> listItems() {
        List<ItemEntity> items = itemService.findAll();
        return items.stream()
                .map(itemMapper::mapTo)
                .collect(Collectors.toList());
    }
    @GetMapping(path = "/item/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable("id") Long id) {
        Optional<ItemEntity> foundItem = itemService.findOne(id);
        return foundItem.map(itemEntity -> {
            ItemDto itemDto = itemMapper.mapTo(itemEntity);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("item/{id}/sell-offers")
    public ResponseEntity<List<SellOffersDto>> getSellOffers(@PathVariable("id") Long id) {
        Optional<ItemEntity> foundItem = itemService.findOne(id);

        if (foundItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<ItemCopyEntity> sellOffers = itemCopyRepository.findAllSellOffersByItemId(foundItem.get().getId());

        Set<String> uniqueOfferKeys = new HashSet<>();
        List<SellOffersDto> sellOffersDtoList = sellOffers.stream()
                .filter(itemCopy -> {
                    String offerKey = itemCopy.getSeller().getId() + "-" + itemCopy.getPrice();
                    boolean isNewOffer = uniqueOfferKeys.add(offerKey);
                    return isNewOffer;
                })
                .map(itemCopy -> {
                    SellOffersDto sellOffersDto = new SellOffersDto();
                    sellOffersDto.setId(itemCopy.getId());
                    sellOffersDto.setItem(itemMapper.mapTo(foundItem.get()));
                    sellOffersDto.setSeller(userMapper.mapTo(itemCopy.getSeller()));
                    sellOffersDto.setPrice(itemCopy.getPrice());
                    sellOffersDto.setQuantity(getQuantityOfOffer(sellOffers, itemCopy));
                    return sellOffersDto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(sellOffersDtoList);
    }

    private int getQuantityOfOffer(List<ItemCopyEntity> sellOffers, ItemCopyEntity offer) {
        return (int) sellOffers.stream()
                .filter(o -> o.getSeller().getId().equals(offer.getSeller().getId()) && o.getPrice().equals(offer.getPrice()))
                .count();
    }

    @DeleteMapping(path = "/auth/admin/item/{id}")
    public ResponseEntity deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
