package kr.co.won.user.controller;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.form.CreateUserForm;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateUserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    /**
     * user service
     */
    private final UserService userService;

    /**
     * user registe validation add
     */
    private final CreateUserValidation createUserValidation;

    /**
     * Create User Form vaildation registe
     */
    @InitBinder(value = {"createUserForm"})
    public void createUserValidationBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(createUserValidation);
    }

    @GetMapping(path = "/create")
    public String createUserPage(Model model) {
        // user form model add
        model.addAttribute(new CreateUserForm());
        return "users/createPage";
    }

    @PostMapping(path = "/create")
    public String createDo(@Validated CreateUserForm form, Errors errors, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            return "users/createPage";
        }
        // mapping form
        UserDomain mappedUser = modelMapper.map(form, UserDomain.class);
        log.info("user mapping ::: {}", mappedUser);
        Address userAddress = new Address(form.getZipCode(), form.getRoadAddress(), form.getDetailAddress());
        mappedUser.setAddress(userAddress);
        UserDomain savedUser = userService.createUser(mappedUser);
        // flash message user email
        flash.addFlashAttribute("msg",
                messageSource.getMessage("create.user",
                        new String[]{savedUser.getName()},
                        LocaleContextHolder.getLocale()));
        // redirect login page
        return "redirect:/login";
    }
}