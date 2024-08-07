package anand.controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/play")
public class VideoController {
	
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
}
