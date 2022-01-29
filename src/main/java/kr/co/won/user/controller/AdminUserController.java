package kr.co.won.user.controller;

import kr.co.won.address.Address;
import kr.co.won.user.domain.UserDomain;
import kr.co.won.user.domain.UserRoleType;
import kr.co.won.user.form.CreateMemberForm;
import kr.co.won.user.service.UserService;
import kr.co.won.user.validation.CreateMemberValidation;
import kr.co.won.util.page.PageDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Set;

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
    public String memberCreatePage(Model model) {
        model.addAttribute(new CreateMemberForm());
        return "admin/users/createMemberPage";
    }

    @PostMapping(path = "/create")
    public String memberCreateDo(@Validated CreateMemberForm form, Errors errors, Model model) {
        if (errors.hasErrors()) {
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
        if (formRoles.size() > 0) {
            formRoles = form.getRoles();
        }

        userService.createUser(setMember, null, formRoles);

        return "redirect:/admin/users";
    }


}
