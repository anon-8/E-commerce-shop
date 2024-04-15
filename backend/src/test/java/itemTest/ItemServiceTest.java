package itemTest;

import com.anon.ecom.item.ItemRepository;
import com.anon.ecom.item.domain.ItemEntity;
import com.anon.ecom.item.services.ItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void shouldSaveItem() {
        ItemEntity item = new ItemEntity();
        when(itemRepository.save(item)).thenReturn(item);

        ItemEntity savedItem = itemService.save(item);

        assertNotNull(savedItem);
        assertEquals(item, savedItem);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void shouldCreateUpdateItem() {
        Long itemId = 1L;
        ItemEntity item = new ItemEntity();
        when(itemRepository.save(item)).thenReturn(item);

        ItemEntity createdUpdatedItem = itemService.createUpdateItem(itemId, item);

        assertNotNull(createdUpdatedItem);
        assertEquals(itemId, createdUpdatedItem.getId());
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void shouldFindAllItems() {
        List<ItemEntity> itemList = List.of(new ItemEntity(), new ItemEntity());
        when(itemRepository.findAll()).thenReturn(itemList);

        List<ItemEntity> foundItems = itemService.findAll();

        assertEquals(itemList.size(), foundItems.size());
        assertTrue(foundItems.containsAll(itemList));
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllItemsPageable() {
        Pageable pageable = Pageable.unpaged();
        Page<ItemEntity> itemPage = mock(Page.class);
        when(itemRepository.findAll(pageable)).thenReturn(itemPage);

        Page<ItemEntity> foundItems = itemService.findAll(pageable);

        assertNotNull(foundItems);
        assertEquals(itemPage, foundItems);
        verify(itemRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldFindOneItemById() {
        Long itemId = 1L;
        ItemEntity item = new ItemEntity();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        Optional<ItemEntity> foundItem = itemService.findOne(itemId);

        assertTrue(foundItem.isPresent());
        assertEquals(item, foundItem.get());
        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void shouldCheckIfItemExists() {
        Long itemId = 1L;
        when(itemRepository.existsById(itemId)).thenReturn(true);

        boolean exists = itemService.isExists(itemId);

        assertTrue(exists);
        verify(itemRepository, times(1)).existsById(itemId);
    }

    @Test
    void shouldPartialUpdateItem() {
        Long itemId = 1L;
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setTitle("New Title");
        ItemEntity existingItem = new ItemEntity();
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(existingItem)).thenReturn(existingItem);

        ItemEntity updatedItem = itemService.partialUpdate(itemId, itemEntity);

        assertNotNull(updatedItem);
        assertEquals("New Title", updatedItem.getTitle());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(existingItem);
    }

    @Test
    void shouldDeleteItemById() {
        Long itemId = 1L;

        itemService.delete(itemId);

        verify(itemRepository, times(1)).deleteById(itemId);
    }
}
