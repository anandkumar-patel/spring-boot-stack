package anand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import anand.model.User;

@Controller
@RequestMapping("/users")
public class UserController {
		
	@GetMapping("/")
	public String getPage(Model model) {
		System.out.println("Developed BY :" + model.getAttribute("developer"));
		model.addAttribute("user", new User());
		return "register";
	}
	
	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, Model model) {
		System.out.println("Developed BY :" + model.getAttribute("developer"));
        model.addAttribute("message", "User registered successfully!");
		return "result";
	}
	
	
	@ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("developer", "anand");
    }

}
