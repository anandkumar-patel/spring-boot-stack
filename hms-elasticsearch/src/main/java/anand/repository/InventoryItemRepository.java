package anand.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import anand.entity.InventoryItem;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
	Optional<InventoryItem> findBySku(String sku);
	
	Optional<InventoryItem> findById(Long name);
}
