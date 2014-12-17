package cn.kc.platform.account.service;

import javax.annotation.PostConstruct;

import cn.kc.platform.account.entity.Member;
import cn.kc.platform.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@PostConstruct	
	protected void initialize() {
		accountRepository.save(new Member("user", "demo", "ROLE_USER"));
		accountRepository.save(new Member("admin", "admin", "ROLE_ADMIN"));
	}

	
	public void signin(Member member) {

	}

	public Member findUserByLoginName(String loginName) {
		return accountRepository.findByEmail(loginName);
	}
}
