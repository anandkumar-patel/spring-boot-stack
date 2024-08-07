package anand.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest")
public class RestFileController {

	@Value("${file.upload-dir}")
	private String uploadDir;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("filename") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Please select a file to upload");
		}
		try {
			Path copyLocation = Paths
					.get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage());
		}
	}
	
	@GetMapping("/download/stream")
	public ResponseEntity<StreamingResponseBody> downloadFile1(@RequestParam("filename") String filename) {
		Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
		if (Files.exists(filePath)) {
			StreamingResponseBody responseBody = outputStream -> {
				Files.copy(filePath, outputStream);
			};
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
					.body(responseBody);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}

	@GetMapping("/download/resource")
	public ResponseEntity<Resource> downloadFile2(@RequestParam String filename) {
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

	@GetMapping("/download/byte")
	public ResponseEntity<byte[]> downloadFile3(@RequestParam String filename) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

			byte[] data = Files.readAllBytes(filePath);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

			return new ResponseEntity<>(data, headers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download/inputstream")
	public ResponseEntity<InputStreamResource> downloadFile4(@RequestParam String filename) throws IOException {
		File file = new File(uploadDir+File.separator+filename);
		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", filename);
		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@GetMapping("/download/void")
	public void downloadFile5(@RequestParam String filename, HttpServletResponse response) {
		try {
			Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
				response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + resource.getFilename() + "\"");
				resource.getInputStream().transferTo(response.getOutputStream());
				response.flushBuffer();
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found");
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
