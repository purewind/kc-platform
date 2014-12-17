package cn.kc.platform.account.repository;

import cn.kc.platform.ZRespository;
import cn.kc.platform.account.entity.Member;

/**
 * Created by dhm on 14-12-13.
 */
public interface MemberRepository extends ZRespository<Member> {
    Member findByName(String name);

    Member findByLoginName(String loginName);
}
