package pl.kedziora.emilek.roomies.database.objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = 3191336626243990294L;

	@Id
	@GeneratedValue
	protected Long id;

    protected String uuid = UUID.randomUUID().toString();

	public Long getId() {
		return id;
	}

    public String getUuid() {
        return uuid;
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
