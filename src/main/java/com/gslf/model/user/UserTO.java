package com.gslf.model.user;

import java.sql.Blob;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.web.multipart.MultipartFile;

import com.gslf.model.category.CategoryTO;


@Entity
@Table(name = "t_user")
public class UserTO {

    private Long id;
    private String username;
    private String email;
	private String passwordOld;
	private String passwordOldUI;
	private String password;
    private String passwordConfirm;
	private Boolean enabled = false;	
	private String confirmationToken;
    private Set<RoleTO> roles;
    private Set<CategoryTO> userCategories;
    private Boolean isUpdate = false;
	private Date createDate;
	private Date confirmedDate;
	private Date lastUpdate;
	private Integer numOfPosts;	
	private Integer rank;
    private String signature;      
    private MultipartFile icon;
    private String base64imageString;      

    @Lob
    @Basic(fetch = FetchType.LAZY)
	private Blob iconBlob;


	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
    @Transient
	public String getPasswordOld() {
		return passwordOld;
	}

	public void setPasswordOld(String passwordOld) {
		this.passwordOld = passwordOld;
	}

    @Transient
	public String getPasswordOldUI() {
		return passwordOldUI;
	}

	public void setPasswordOldUI(String passwordOldUI) {
		this.passwordOldUI = passwordOldUI;
	}
	
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    @ManyToMany
    @JoinTable(name = "t_user_t_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<RoleTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleTO> roles) {
        this.roles = roles;
    }
    
    @ManyToMany
    @JoinTable(name = "t_user_t_category", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	public Set<CategoryTO> getUserCategories() {
		return userCategories;
	}

	public void setUserCategories(Set<CategoryTO> userCategories) {
		this.userCategories = userCategories;
	}

	@Transient
	public Boolean getUpdate() {
		return isUpdate;
	}

	public void setUpdate(Boolean isUpdate) {
		this.isUpdate = isUpdate;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getConfirmedDate() {
		return confirmedDate;
	}

	public void setConfirmedDate(Date confirmedDate) {
		this.confirmedDate = confirmedDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	@Transient
	public Integer getNumOfPosts() {
		return numOfPosts;
	}

	public void setNumOfPosts(Integer numOfPosts) {
		this.numOfPosts = numOfPosts;
		if(numOfPosts == null) {
			this.rank = 1;
		} else {
			this.rank = (1 + numOfPosts / 10);
		}
	}
	
	@Transient
	public Integer getRank() {
		return rank;
	}

	@Transient
	public MultipartFile getIcon() {
		return icon;
	}

	public void setIcon(MultipartFile icon) {
		this.icon = icon;
	}
	
	@Transient
	public String getBase64imageString() {
		return base64imageString;
	}

	public void setBase64imageString(String base64imageString) {
		this.base64imageString = base64imageString;
	}

	public Blob getIconBlob() {
		return iconBlob;
	}

	public void setIconBlob(Blob iconBlob) {
		this.iconBlob = iconBlob;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getConfirmationToken() {
		return confirmationToken;
	}

	public void setConfirmationToken(String confirmationToken) {
		this.confirmationToken = confirmationToken;
	}
    
}