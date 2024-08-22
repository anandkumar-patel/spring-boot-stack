package anand.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import anand.document.InventoryItemDocument;
import anand.entity.InventoryItem;
import anand.repository.InventoryItemDocumentRepository;
import anand.repository.InventoryItemRepository;

@Service
public class InventoryService {

	@Autowired
	private InventoryItemRepository inventoryItemRepository;

	@Autowired
	private InventoryItemDocumentRepository inventoryItemDocumentRepository;

	@Transactional
	public InventoryItem addInventoryItem(InventoryItem item) {
		InventoryItem savedItem = inventoryItemRepository.save(item);
		InventoryItemDocument document = new InventoryItemDocument();
		document.setId(String.valueOf(savedItem.getId()));
		document.setName(savedItem.getName());
		document.setDescription(savedItem.getDescription());
		document.setQuantity(savedItem.getQuantity());
		document.setLocation(savedItem.getLocation());
		document.setSku(savedItem.getSku());
		inventoryItemDocumentRepository.save(document);
		return savedItem;
	}

	public List<InventoryItem> getAllInventoryItems() {
		return inventoryItemRepository.findAll();
	}

	public InventoryItem getInventoryItemBySku(String sku) {
		return inventoryItemRepository.findBySku(sku).orElse(null);
	}

	public void deleteInventoryItem(Long id) {
		inventoryItemRepository.deleteById(id);
		inventoryItemDocumentRepository.deleteById(String.valueOf(id));
	}
	
	public InventoryItem getInventoryDetails(Long id) {
	    return inventoryItemRepository.findById(id).orElse(null);
	}
	
	public List<InventoryItemDocument> searchInventory(String query) {
        if (!StringUtils.hasText(query)) {
            return StreamSupport
                    .stream(inventoryItemDocumentRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
        return inventoryItemDocumentRepository.findByNameContainingOrDescriptionContainingOrSkuContaining(query, query, query);
    }
	
	public List<String> searchInventorySuggestions(String query) {
	    return inventoryItemDocumentRepository.searchByNameDescriptionOrSku(query)
	            .stream()
	            .map(InventoryItemDocument::getName)
	            .collect(Collectors.toList());
	}

}
