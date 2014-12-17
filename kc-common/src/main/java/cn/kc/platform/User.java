package cn.kc.platform;

import java.io.Serializable;
import java.util.Objects;

/**
 * 系统用户
 * Created by dhm on 14-12-13.
 */
public class User implements Serializable {
    private static final long serialVersionUID = -1373760761780840081L;
    public String loginName;
    public String name;

    public User(String loginName, String name) {
        this.loginName = loginName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
        return loginName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(loginName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        User other = (User) obj;
        if (loginName == null) {
            if (other.loginName != null) {
                return false;
            }
        } else if (!loginName.equals(other.loginName)) {
            return false;
        }
        return true;
    }
}
