package kr.co.won.user.controller;

import kr.co.won.address.Address;
import kr.co.won.auth.AuthUser;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.form.CreateMemberForm;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateMemberValidation;
import kr.co.won.util.page.PageDto;
import kr.co.won.util.page.PageMaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;
    /**
     * user admin Service
     */
    @Resource(name = "adminUserService")
    private final UserService userService;

    /**
     * create member validation
     */
    private final CreateMemberValidation memberValidation;

    @InitBinder(value = "createMemberForm")
    public void memberValidationInit(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(memberValidation);
    }

    @GetMapping
    public String memberListPage(PageDto pageDto) {
        return "admin/users/listMemberPage";
    }

    @GetMapping(path = "/create")
    public String memberCreatePage(@AuthUser UserDomain user, Model model) {
        log.info("login user : {}", user);
        model.addAttribute(new CreateMemberForm());
        model.addAttribute("authUser", user);
        return "admin/users/createMemberPage";
    }

    @PostMapping(path = "/create")
    public String memberCreateDo(@AuthUser UserDomain authUser, @Validated CreateMemberForm form, Errors errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("authUser", authUser);
            return "admin/users/createMemberPage";
        }
        /** member address */
        Address memberAddress = new Address(form.getZipCode(), form.getRoadAddress(), form.getDetailAddress());
        UserDomain setMember = UserDomain.builder()
                .name(form.getName())
                .email(form.getEmail())
                .address(memberAddress)
                .build();
        // make random password

        Set<UserRoleType> formRoles = null;
        // user role form data is not null
        if (form.getRoles() != null) {
            formRoles = form.getRoles();
        }
        // save member
        UserDomain savedMember = userService.createUser(setMember, authUser, formRoles);
        // flash message setting
        flash.addFlashAttribute("msg", messageSource.getMessage("create.member",
                        new String[]{savedMember.getName()},
                LocaleContextHolder.getLocale()));
        return "redirect:/admin/users";
    }

    @GetMapping(path = "/info")
    public String memberInformationPage(@RequestParam(name = "email", required = true) String email, Model model) {
        log.info("get information ");
        UserDomain findUser = userService.findUserByEmail(email, null);
        // model setting
        model.addAttribute("member", findUser);
        return "admin/users/informationMemberPage";
    }

    @GetMapping(path = "/list")
    public String memberListPage(@AuthUser UserDomain authUser, PageDto pageDto, Model model) {
        Page pagingResult = userService.pagingUser(pageDto);
        // make paging result
        PageMaker paging = new PageMaker<>(pagingResult);
        model.addAttribute("page", paging);
        model.addAttribute("user", authUser);
        return "admin/users/listMemberPage";
    }
}
