package kr.co.won.iot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.won.auth.AuthUser;
import kr.co.won.iot.form.CreateAdminSiteForm;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping(path = "/admin/IoT")
@RequiredArgsConstructor
public class AdminIoTController {

    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;


    /**
     * Create IoT Site Create Page
     */
    @GetMapping(path = "/site/create")
    public String iotSiteCreatePage(@AuthUser UserDomain loginUser, Model model) {
        log.info("login user information ::: {}", loginUser);
        // model setting create site form
        model.addAttribute(new CreateAdminSiteForm());
        return "admin/iot/site/IoTSiteCreatePage";
    }

    /**
     * Create IoT Site List Do
     */
    @PostMapping(path = "/site/create")
    public String iotSiteCreateDo(@AuthUser UserDomain loginUser, Model model, RedirectAttributes flash) {
        log.info("login user information ::: {}", loginUser);
        return "";
    }

    /**
     * Create IoT Site List Page
     */
    @GetMapping(path = "/site/list")
    public String iotSiteListPage(@AuthUser UserDomain loginUser, PageDto pageDto, Model model) {
        log.info("login user information ::: {}", loginUser);
        return "admin/iot/site/IoTSiteListPage";
    }


    /**
     * Create IoT Site Create Page
     */
    @GetMapping(path = "/device/create")
    public String iotDeviceCreatePage(@AuthUser UserDomain loginUser, Model model) {
        log.info("login user information ::: {}", loginUser);
        return "admin/iot/device/IoTDeviceCreatePage";
    }

    /**
     * Create IoT Site List Do
     */
    @PostMapping(path = "/device/create")
    public String iotDeviceCreateDo(@AuthUser UserDomain loginUser, Model model, RedirectAttributes flash) {
        log.info("login user information ::: {}", loginUser);
        return "";
    }

    /**
     * Create IoT Site List Page
     */
    @GetMapping(path = "/device/list")
    public String iotDeviceListPage(@AuthUser UserDomain loginUser, PageDto pageDto, Model model) {
        log.info("login user information ::: {}", loginUser);
        return "admin/iot/device/IoTDeviceListPage";
    }
}
