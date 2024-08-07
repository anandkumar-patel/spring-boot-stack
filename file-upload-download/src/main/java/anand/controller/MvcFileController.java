package anand.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mvc")
public class MvcFileController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@GetMapping("/status")
	public String uploadStatus() {
		return "uploadstatus";
	}

	@PostMapping("/upload")
	public String handleFileUpload(@RequestParam("filename") MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:status";
		}
		try {
			// Save the file
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
			System.out.println("file path : " + path.toString());
			Files.write(path, bytes);

			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + file.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "'");
		}

		return "redirect:status";
	}

	@GetMapping("/download")
	public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + resource.getFilename() + "\"")
						.body(resource);
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + filename);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Could not download the file: " + filename + "! " + e.getMessage());
		}
	}
}
