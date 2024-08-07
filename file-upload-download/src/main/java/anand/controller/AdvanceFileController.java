package anand.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import anand.entity.FileMetadata;
import anand.repository.FileMetadataRepository;
import jakarta.validation.constraints.NotNull;

@Controller
@Validated
@RequestMapping("/adv")
public class AdvanceFileController {

	@Value("${file.upload-dir}")
	private String uploadDir;
	
	@Autowired
	private FileMetadataRepository fileMetadataRepository;

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("filename") @NotNull MultipartFile file, RedirectAttributes redirectAttributes) {
		// File validation
		if (file.isEmpty() || !isValidFileType(file.getContentType())) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:/adv";
		}

		try {
			// Save file to disk
			byte[] bytes = file.getBytes();
			Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
			Files.write(path, bytes);

			FileMetadata metadata = new FileMetadata();
			metadata.setFileName(file.getOriginalFilename());
			metadata.setFileType(file.getContentType());
			metadata.setFileSize(file.getSize());
			metadata.setUploadDate(new Date());
			fileMetadataRepository.save(metadata);

		} catch (IOException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("message", "Failed to upload '" + file.getOriginalFilename() + "'");
		}

		return "redirect:/adv";
	}

	@GetMapping("/download")
	public ResponseEntity<StreamingResponseBody> downloadFile(@RequestParam("filename") String filename) {
		Path path = Paths.get(uploadDir + File.separator + filename);
		if (Files.exists(path)) {
			StreamingResponseBody responseBody = outputStream -> {
				Files.copy(path, outputStream);
			};
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(responseBody);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	private boolean isValidFileType(String contentType) {
		// Add logic to check for valid file types (e.g., image/png, image/jpeg)
		return contentType.equals("image/png") || contentType.equals("image/jpeg");
	}
}
