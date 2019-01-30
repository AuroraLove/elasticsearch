package onegis.es.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源版本文件（1.rar-版本V2.1）
 * 
 * @author appleyk
 *
 */
public class ResourceVersionFile {

	// 文件ID
	private Long id;
	
	// 文件对应的版本ID
	private Long versionId;

	//文件对应的资源ID
	private Long resourceId;
	
	//文件对应的onegis内部的版本标识【如：onegis-2.0-win-x64】
	private String onegisVersion;
	
	// 文件资源url标识
	private String fileUrl;
	
	// 文件名称
	private String fileName;


	// 文件依赖的环境【{"linux": "centOs7"}】
	Map<String, Object> environment;
	
	// 文件依赖的文件集合【如：文件A依赖文件B和C，那么B和C就是文件A依赖的文件列表】
	private List<ResourceVersionFile> dependencyList;

	public ResourceVersionFile() {
		environment = new HashMap<>();
		dependencyList = new ArrayList<>();
	}

	public ResourceVersionFile(Long id) {
		this.id = id;
		environment = new HashMap<>();
		dependencyList = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<ResourceVersionFile> getDependencyList() {
		return dependencyList;
	}

	public void setDependencyList(List<ResourceVersionFile> dependencyList) {
		this.dependencyList = dependencyList;
	}

	public void addDependency(ResourceVersionFile dependencyFile){
		this.dependencyList.add(dependencyFile);
	}

	public Map<String, Object> getEnvironment() {
		return environment;
	}

	public void setEnvironment(Map<String, Object> environment) {
		this.environment = environment;
	}

	public void addEnvironment(String key, Object value) {
		this.environment.put(key, value);
	}

	public Long getVersionId() {
		return versionId;
	}

	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getOnegisVersion() {
		return onegisVersion;
	}

	public void setOnegisVersion(String onegisVersion) {
		this.onegisVersion = onegisVersion;
	}
}
