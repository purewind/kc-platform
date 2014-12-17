package cn.kc.platform.account.web.controller

        ;

import cn.kc.platform.account.entity.Member;
import cn.kc.platform.account.repository.MemberRepository;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@RequiresRoles("ROLE_USER")
class AccountController {

    private MemberRepository memberRepository;

    @Autowired
    public AccountController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @RequestMapping(value = "account/current", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Member accounts(Principal principal) {
        Assert.notNull(principal);
        return memberRepository.findByLoginName(principal.getName());
    }
}
