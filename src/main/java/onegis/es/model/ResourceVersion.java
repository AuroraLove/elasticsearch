package onegis.es.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import onegis.es.dict.ResourceTypeEnum;

import java.util.*;

/**
 * 资源（插件、App、模型、数据、服务）的版本
 * 
 * @author appleyk
 *
 */
public class ResourceVersion implements Comparable<ResourceVersion>{

	// 资源版本ID
	private Long id;

	// 资源ID
	private Long resourceId;

	private String baseVersion;

	// 资源版本创建用户
	private User user;

	// 资源类型
	private ResourceTypeEnum resourceType;

	// 版本名称（如v1.20）
	private String name;

	// 版本备注信息
	private String note;

	// 输入信息（JSON）
	private String input;

	// 输出信息（JSON）
	private String output;

	// 状态（审核状态：0-待审核 、1-审核中、 2-审核通过、 3-拒绝）
	private Integer status;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	private Map<String, Object> parameters;

	private List<ResourceVersionFile> versionFileList;
	private List<ExamineRecord> examineRecords;

	public ResourceVersion() {
		parameters = new HashMap<>();
		versionFileList = new ArrayList<>();
	}

	public ResourceVersion(Long id) {
		parameters = new HashMap<>();
		versionFileList = new ArrayList<>();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getBaseVersion() {
		return baseVersion;
	}

	public void setBaseVersion(String baseVersion) {
		this.baseVersion = baseVersion;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ResourceTypeEnum getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceTypeEnum resourceType) {
		this.resourceType = resourceType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void addParameter(String key, Object value) {
		this.parameters.put(key, value);
	}

	public List<ResourceVersionFile> getVersionFileList() {
		return versionFileList;
	}

	public void setVersionFileList(List<ResourceVersionFile> versionFileList) {
		this.versionFileList = versionFileList;
	}

	public void addVersionFile(ResourceVersionFile versionFile) {

		this.versionFileList.add(versionFile);
	}

	public List<ExamineRecord> getExamineRecords() {
		return examineRecords;
	}

	public void setExamineRecords(List<ExamineRecord> examineRecords) {
		this.examineRecords = examineRecords;
	}

	@Override
	public int compareTo(ResourceVersion o) {
		if (o.getCreateTime().getTime() > this.createTime.getTime()){
			return 1;
		}else if (o.getCreateTime().getTime() < this.createTime.getTime()){
			return -1;
		}
		return 0;
	}
}
