package anand.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import anand.document.InventoryItemDocument;

public interface InventoryItemDocumentRepository extends ElasticsearchRepository<InventoryItemDocument, String> {
	Optional<InventoryItemDocument> findBySku(String sku);

	List<InventoryItemDocument> findByNameContainingOrDescriptionContainingOrSkuContaining(String name,
			String description, String sku);

	@Query("{\"bool\": {\"should\": [" 
			+ "{\"match\": {\"name\": \"?0\"}}," 
			+ "{\"match\": {\"description\": \"?0\"}},"
			+ "{\"match\": {\"sku\": \"?0\"}}" 
			+ "]}}")
	List<InventoryItemDocument> searchByNameDescriptionOrSku(String text);
}
