package anand.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/play")
public class VideoController {
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	private final Path videoDir = Paths.get(uploadDir);
	
	@GetMapping("/")
	public String getVideoPlayPage() {
		return "videoplay";
	}

    @GetMapping("/video")
    public StreamingResponseBody streamVideo(HttpServletResponse response, 
                                             @RequestHeader(value = "Range", required = false) String rangeHeader) {
        try {
            //ClassPathResource videoFile = new ClassPathResource("/home/surya/uploaded/bunndle-machish.mp4");
            File videoFile = new File("/home/surya/uploaded/bunndle-machish.mp4");
            long rangeStart = 0;
            long rangeEnd;
            long fileSize = videoFile.length();
            
            if (rangeHeader != null) {
                String[] ranges = rangeHeader.split("=")[1].split("-");
                rangeStart = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                } else {
                    rangeEnd = fileSize - 1;
                }
            } else {
                rangeEnd = fileSize - 1;
            }

            final long rangeLength = rangeEnd - rangeStart + 1;

            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(rangeLength));
            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + fileSize);

            InputStream inputStream = new FileInputStream(videoFile);
            inputStream.skip(rangeStart);

            return outputStream -> {
                byte[] buffer = new byte[1024];
                long bytesRead = 0;
                int read;
                while ((read = inputStream.read(buffer)) != -1 && bytesRead < rangeLength) {
                    outputStream.write(buffer, 0, read);
                    bytesRead += read;
                }
                inputStream.close();
            };

        } catch (Exception e) {
            throw new RuntimeException("Error while streaming video", e);
        }
    }
    
    /*
    @GetMapping("/video/{filename}")
    public ResponseEntity<Resource> getVideo(
            @PathVariable String filename,
            @RequestHeader(value = "Range", required = false) String rangeHeader) {
        
        try {
            Path videoPath = videoDir.resolve(filename).normalize();
            Resource video = new UrlResource(videoPath.toUri());

            if (!video.exists() || !video.isReadable()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            long fileLength = Files.size(videoPath);
            List<HttpRange> ranges = HttpRange.parseRanges(rangeHeader);

            if (ranges.isEmpty()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                        .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength))
                        .body(video);
            }

            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(fileLength);
            long end = range.getRangeEnd(fileLength);
            long rangeLength = end - start + 1;

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(rangeLength))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength)
                    .body(new InputStreamResource(Files.newInputStream(videoPath, start, rangeLength)));

        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }*/
}
