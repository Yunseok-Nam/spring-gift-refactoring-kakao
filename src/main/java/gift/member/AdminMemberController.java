package gift.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/members")
public class AdminMemberController {
    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("members", memberService.findAll());
        return "member/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "member/new";
    }

    @PostMapping
    public String create(
        @RequestParam String email,
        @RequestParam String password,
        Model model
    ) {
        try {
            memberService.create(email, password);
        } catch (IllegalArgumentException e) {
            populateNewFormError(model, email, e.getMessage());
            return "member/new";
        }
        return "redirect:/admin/members";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var member = memberService.findById(id);
        model.addAttribute("member", member);
        return "member/edit";
    }

    @PostMapping("/{id}/edit")
    public String update(
        @PathVariable Long id,
        @RequestParam String email,
        @RequestParam String password
    ) {
        memberService.update(id, email, password);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/charge-point")
    public String chargePoint(
        @PathVariable Long id,
        @RequestParam int amount
    ) {
        memberService.chargePoint(id, amount);
        return "redirect:/admin/members";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/admin/members";
    }

    private void populateNewFormError(Model model, String email, String error) {
        model.addAttribute("error", error);
        model.addAttribute("email", email);
    }
}
