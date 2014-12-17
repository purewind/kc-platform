package cn.kc.platform.account.entity;

import cn.kc.platform.IdEntity;
import com.google.common.collect.Lists;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by dhm on 14-12-13.
 */
@Entity
@Table(name = "ss_team")
public class Team extends IdEntity {

    private String name;
    private Member master;
    private List<Member> memberList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @OneToOne
    @JoinColumn(name = "master_id")
    public Member getMaster() {
        return master;
    }

    public void setMaster(Member master) {
        this.master = master;
    }

    @OneToMany(mappedBy = "team")
    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
