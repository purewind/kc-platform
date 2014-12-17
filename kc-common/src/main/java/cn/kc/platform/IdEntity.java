package cn.kc.platform;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by dhm on 14-12-13.
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable,Persistable<Long> {

    protected Long id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return false;
    }
}
