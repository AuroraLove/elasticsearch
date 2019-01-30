package onegis.es.model;

public class User {

	/**
	 * 用户没有读写权限
	 */
	public final static int NULL = 0;

	/**
	 * 用户具有读权限
	 */
	public final static int READ = 1;

	/**
	 * 用户具有写权限
	 */
	public final static int WRITE = 2;

	/**
	 * 普通用户角色
	 */
	public static final int USER = 4;

	/**
	 * 管理员角色，可以管理普通用户
	 */
	public static final int MANAGER = 16;

	/**
	 * 超户角色，可以管理任何用户
	 */
	public static final int SUPER = 64;

	/**
	 * 用户唯一标识
	 */
	private Long uid;

	/**
	 * 用户昵称
	 */
	private String userNickName;

	/**
	 * 用户头像
	 */
	private String userAvatar;

	private String email;

	/**
	 * 用户角色，其值为USER|MANAGER|SUPER任意组合
	 */
	private Integer role;

	public User() {

	}

	public User(Long uID) {
		this.uid = uID;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}