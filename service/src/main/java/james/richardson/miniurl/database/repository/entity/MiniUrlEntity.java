package james.richardson.miniurl.database.repository.entity;

import james.richardson.miniurl.service.model.MiniUrl;

import javax.persistence.*;

@Entity
@Table(name = "mini_url")
public class MiniUrlEntity {
    @Id
    @Column(name = "mini_uri_code")
    private String miniUrlCode;

    @Column(name = "long_url")
    private String longUrl;

    @Column(name = "open_count")
    private int openCount;

    public MiniUrlEntity() {

    }

    public MiniUrlEntity(String miniUrlCode, String longUrl, int openCount) {
        this.miniUrlCode = miniUrlCode;
        this.longUrl = longUrl;
        this.openCount = openCount;
    }

    public MiniUrlEntity(String miniUrlCode, String longUrl) {
        this.miniUrlCode = miniUrlCode;
        this.longUrl = longUrl;
    }

    public MiniUrl toModel() {
        return new MiniUrl(miniUrlCode, longUrl, openCount);
    }
}
