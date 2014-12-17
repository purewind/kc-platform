package cn.kc.platform;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by dhm on 14-12-13.
 */
@NoRepositoryBean
public interface ZRespository<T> extends JpaRepository<T, Long>,JpaSpecificationExecutor<T> {
}
