package pl.kedziora.emilek.roomies.database.objects;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 3191336626243990294L;

	@Id
	@GeneratedValue
	protected Long id;

    protected String uuid = UUID.randomUUID().toString();

    @Version
    protected Long version;

	public Long getId() {
		return id;
	}

    public String getUuid() {
        return uuid;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (!uuid.equals(that.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

}
