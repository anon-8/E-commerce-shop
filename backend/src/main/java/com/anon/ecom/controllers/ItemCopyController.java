package com.anon.ecom.controllers;

import com.anon.ecom.domain.dto.ItemCopyDto;
import com.anon.ecom.domain.entities.ItemCopyEntity;
import com.anon.ecom.domain.entities.UserEntity;
import com.anon.ecom.mappers.Mapper;
import com.anon.ecom.services.ItemCopyService;
import com.anon.ecom.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class ItemCopyController {
    private final ItemCopyService itemCopyService;
    private final UserService userService;
    private final Mapper<ItemCopyEntity, ItemCopyDto> itemCopyMapper;
    public ItemCopyController(Mapper<ItemCopyEntity, ItemCopyDto> itemCopyMapper, ItemCopyService itemCopyService, UserService userService) {
        this.itemCopyMapper = itemCopyMapper;
        this.itemCopyService = itemCopyService;
        this.userService = userService;
    }
    @PostMapping(path = "/put-digital-key-for-sale")
    public ResponseEntity<ItemCopyDto> putItemCopyForSale(@RequestBody ItemCopyDto itemCopyDto) {

        ItemCopyDto savedItemCopyDto = itemCopyService.putItemCopyForSale(itemCopyDto);

        return new ResponseEntity<>(savedItemCopyDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/admin/copies")
    public List<ItemCopyDto> listItemCopies() {
        List<ItemCopyEntity> itemCopies = itemCopyService.findAll();
        return itemCopies.stream()
                .map(itemCopyMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/copies")
    public List<ItemCopyDto> listUserCopies() {

        UserEntity user = userService.getUser();

        List<ItemCopyEntity> itemCopies = itemCopyService.findAllByUserId(user.getId());

        return itemCopies.stream()
                .map(itemCopyMapper::mapTo)
                .collect(Collectors.toList());
    }

    @GetMapping(path = "/copies/{itemId}")
    public List<ItemCopyDto> listUserItemCopies(@PathVariable("itemId") Long itemID) {

        UserEntity user = userService.getUser();

        List<ItemCopyEntity> itemCopies = itemCopyService.findAllByUserIdAndItemId(user.getId(), itemID);

        return itemCopies.stream()
                .map(itemCopyMapper::mapTo)
                .collect(Collectors.toList());
    }
    @GetMapping(path = "/admin/copy/{id}")
    public ResponseEntity<ItemCopyDto> getItemCopy(@PathVariable("id") Long id) {
        Optional<ItemCopyEntity> foundItemCopy = itemCopyService.findOne(id);
        return foundItemCopy.map(itemCopyEntity -> {
            ItemCopyDto itemCopyDto = itemCopyMapper.mapTo(itemCopyEntity);
            return new ResponseEntity<>(itemCopyDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(path = "/admin/copy/{id}")
    public ResponseEntity deleteItemCopy(@PathVariable("id") Long id) {
        itemCopyService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
