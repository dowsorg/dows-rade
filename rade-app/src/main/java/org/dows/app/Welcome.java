package org.dows.app;

import org.dows.core.annotation.TokenIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class Welcome {

    @RequestMapping("/")
    @TokenIgnore
    public String welcome() {
        return "welcome";
    }
}
