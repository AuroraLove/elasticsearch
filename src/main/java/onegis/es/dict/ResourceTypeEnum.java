package onegis.es.dict;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceTypeEnum {

	ALL("全部", 0),
	APP("App",1),
	PLUGIN("插件",2),
	SERVICE("服务",3),
	DATA("数据",4),
	MODEL("模型",5),
	OTHER("第三方库",6);

	private final String name;
	private Integer value;

	private ResourceTypeEnum(String name, Integer value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 根据value获取枚举对象
	 * 
	 * @param value
	 * @return
	 */
	@JsonCreator
	public static ResourceTypeEnum getEnum(int value) {
		for (ResourceTypeEnum rtypeEnum : ResourceTypeEnum.values()) {
			if (rtypeEnum.getValue() == value) {
				return rtypeEnum;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	@JsonValue
	public int getValue() {
		return value;
	}

}
