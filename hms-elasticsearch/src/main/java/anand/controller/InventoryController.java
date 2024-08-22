package anand.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import anand.document.InventoryItemDocument;
import anand.entity.InventoryItem;
import anand.service.InventoryService;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;

	@GetMapping
	public String listInventory(Model model) {
		List<InventoryItem> items = inventoryService.getAllInventoryItems();
		model.addAttribute("items", items);
		return "inventory/list";
	}

	@GetMapping("/add")
	public String showAddForm(Model model) {
		model.addAttribute("item", new InventoryItem());
		return "inventory/add";
	}

	@PostMapping("/add")
	public String addInventoryItem(@ModelAttribute InventoryItem item) {
		inventoryService.addInventoryItem(item);
		return "redirect:/inventory";
	}

	@GetMapping("/delete/{id}")
	public String deleteInventoryItem(@PathVariable Long id) {
		inventoryService.deleteInventoryItem(id);
		return "redirect:/inventory";
	}

	@GetMapping("/search-page")
	public String searchPage() {
		return "inventory/search";
	}

	@GetMapping("/search")
	public String listInventory(@RequestParam(required = false) String query, Model model) {
		List<InventoryItemDocument> items;
		if (query != null && !query.isEmpty()) {
			items = inventoryService.searchInventory(query);
		} else {
			items = inventoryService.searchInventory("");
		}
		model.addAttribute("headerData", "Searched Inventory List");
		model.addAttribute("items", items);
		model.addAttribute("query", query);
		return "inventory/list";
	}

	@GetMapping("/ajax-search-page")
	public String ajaxSearchPage() {
		return "inventory/ajaxsearch";
	}

	@GetMapping("/ajax")
	@ResponseBody
	public List<String> searchInventory(@RequestParam("query") String query) {
		return inventoryService.searchInventorySuggestions(query);
	}

	@GetMapping("/details")
	@ResponseBody
	public InventoryItem getInventoryDetails(@RequestParam("id") Long id) {
		return inventoryService.getInventoryDetails(id);
	}
}
