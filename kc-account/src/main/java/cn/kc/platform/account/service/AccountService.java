package cn.kc.platform.account.service;

import cn.kc.platform.User;
import cn.kc.platform.account.entity.Member;
import cn.kc.platform.account.entity.Role;
import cn.kc.platform.account.repository.MemberRepository;
import cn.kc.platform.account.repository.RoleRepository;
import cn.kc.util.Digests;
import cn.kc.util.Encodes;
import cn.oz.core.utils.DynamicSpecifications;
import cn.oz.core.utils.Hibernates;
import cn.oz.core.utils.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

public class AccountService {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    private static final int SALT_SIZE = 8;

    private static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private MemberRepository memberRepository;

    private RoleRepository roleRepository;

    /**
     * 在保存用户时,发送用户修改通知消息, 由消息接收者异步进行较为耗时的通知邮件发送.
     * <p/>
     * 如果企图修改超级用户,取出当前操作员用户,打印其信息然后抛出异常.
     */
    public Member saveUser(Member member) {

        if (isSupervisor(member)) {
            logger.warn("操作员{}尝试修改超级管理员用户", getCurrentUserName());
            throw new ServiceException("不能修改超级管理员用户");
        }

        // 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
        if (StringUtils.isNotBlank(member.getPlainPassword())) {
            entryptPassword(member);
        }

        memberRepository.save(member);
        return member;
    }

    /**
     * 按Id获得用户.
     */
    public Member getUser(Long id) {
        return memberRepository.findOne(id);
    }

    /**
     * 获取全部用户，并在返回前对用户的延迟加载关联角色进行初始化.
     */
    public List<Member> getAllUserInitialized() {
        List<Member> result = (List<Member>) memberRepository.findAll();
        for (Member member : result) {
            Hibernates.initLazyProperty(member.getRoleList());
        }
        return result;
    }

    /**
     * 按登录名查询用户.
     */
    public Member findUserByLoginName(String loginName) {
        return memberRepository.findByLoginName(loginName);
    }

    /**
     * 按名称查询用户, 并在返回前对用户的延迟加载关联角色进行初始化.
     */
    public Member findUserByNameInitialized(String name) {
        Member member = memberRepository.findByName(name);
        if (member != null) {
            Hibernates.initLazyProperty(member.getRoleList());
        }
        return member;
    }

    /**
     * 按页面传来的查询条件查询用户.
     */
    public List<Member> searchUser(Map<String, Object> searchParams) {
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Member> spec = DynamicSpecifications.bySearchFilter(filters.values(), Member.class);
        List<Member> memberList = memberRepository.findAll(spec);
        return memberList;
    }

    /**
     * 获取当前用户数量.
     */
    public Long getUserCount() {
        return memberRepository.count();
    }

    /**
     * 判断是否超级管理员.
     */
    private boolean isSupervisor(Member member) {
        return ((member.getId() != null) && (member.getId() == 1L));
    }

    /**
     * 设定安全的密码，生成随机的salt并经过1024次 sha-1 hash
     */
    private void entryptPassword(Member member) {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        member.setSalt(Encodes.encodeHex(salt));

        byte[] hashPassword = Digests.sha1(member.getPlainPassword().getBytes(), salt, HASH_INTERATIONS);
        member.setPassword(Encodes.encodeHex(hashPassword));
    }


    /**
     * 取出Shiro中的当前用户LoginName.
     */
    private String getCurrentUserName() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user.loginName;
    }

    // --------------------//
    // Role Management //
    // --------------------//

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    // -----------------//
    // Setter methods //
    // -----------------//

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}
