package onegis.es.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import onegis.es.dict.ResourceTypeEnum;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class ResourceInfo {

	private Long id;
	private String name;
	private String title;
	private String icon;
	private String website;
	private String keywords;
	private String description;
	private ResourceTypeEnum type;
	@NotNull
	private Classify classify;
	private ResourceVersion lasterVersion;
	private String license;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;
	private Integer status;
	private User user;
	private List<ResourceVersion> versionList;
	private List<ExamineRecord> examineRecords;

	public ResourceInfo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ResourceTypeEnum getType() {
		return type;
	}

	public void setType(ResourceTypeEnum type) {
		this.type = type;
	}

	public Classify getClassify() {
		return classify;
	}

	public void setClassify(Classify classify) {
		this.classify = classify;
	}

	public ResourceVersion getLasterVersion() {
		return lasterVersion;
	}

	public void setLasterVersion(ResourceVersion lasterVersion) {
		this.lasterVersion = lasterVersion;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<ResourceVersion> getVersionList() {
		return versionList;
	}

	public void setVersionList(List<ResourceVersion> versionList) {
		this.versionList = versionList;
	}

	public List<ExamineRecord> getExamineRecords() {
		return examineRecords;
	}

	public void setExamineRecords(List<ExamineRecord> examineRecords) {
		this.examineRecords = examineRecords;
	}
}
