package playground.minisns.timeline;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timeline")
public class TimelineController {
    private final TimelineService service;

    public TimelineController(TimelineService service) {
        this.service = service;
    }

    @GetMapping
    List<TimelineService.TimelinePost> timeline(
        Authentication auth,
        @RequestParam(defaultValue = "20") int limit,
        @RequestParam(defaultValue = "0") int offset
    ) {
        return service.timeline(auth.getName(), limit, offset);
    }
}
