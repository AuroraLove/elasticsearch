package onegis.es.model;

import java.util.List;

public class Classify {

	private Long id;

	private String name;

	private List<Classify> childs;

	public Classify() {

	}

	public Classify(Long id) {
		this.id = id;
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

	public List<Classify> getChilds() {
		return childs;
	}

	public void setChilds(List<Classify> childs) {
		this.childs = childs;
	}

}
