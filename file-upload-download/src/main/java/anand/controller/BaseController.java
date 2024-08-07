package anand.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BaseController {

	@RequestMapping({ "/mvc", "/mvc/" })
	public String mvcPage() {
		return "mvcpage";
	}

	@RequestMapping({ "/rest", "/rest/" })
	public String restPage() {
		return "restpage";
	}

	@RequestMapping({ "/video", "/video/" })
	public String videoPage() {
		return "videopage";
	}

	@RequestMapping({ "/adv", "/adv/" })
	public String advancePage() {
		return "advpage";
	}

}
