package com.liuqi.tools.codelife.exceptions;

/**
 * 异常对象集合
 *
 * @author LiuQI 2018/5/29 16:35
 * @version V1.0
 **/
public enum ErrorCodes {
    /**
     * 参数为空异常
     */
    COMM_PARAMETER_EMPTY("comm.parameter.empty"),

    /**
     * 登录失败
     */
    COMM_LOGIN_ERROR("comm.login.error"),

    /**
     * 参数长度超过限制
     */
    COMM_PARAMETER_TOO_LONG("comm.parameter.tooLong"),

    /**
     * 创建文件失败
     */
    COMM_FILE_CREATE_FAILED("comm.file.create.failed"),

    /**
     * 保存文件失败
     */
    COMM_FILE_SAVE_FAILED("comm.file.save.failed"),

    /**
     * 文件不存在
     */
    COMM_FILE_NOT_EXISTS("comm.file.not.exists"),

    /**
     * 读取文件内容失败
     */
    COMM_FILE_READ_FAILED("comm.file.read.failed"),

    /**
     * 写文件失败
     */
    COMM_FILE_WRITE_FAILED("comm.file.read.failed"),

    /**
     * 指定编号的文章不存在
     */
    ARTICLE_NOT_EXISTS("article.notExists"),

    /**
     * 不能修改其它人写的文章
     */
    ARTICLE_MANAGER_EDIT_NOT_AUTHOR("article.manager.edit.notAuthor"),

    /**
     * 不能删除其它人写的文章
     */
    ARTICLE_MANAGER_DELETE_NOT_AUTHOR("article.manager.delete.notAuthor"),

    /**
     * 文章内容不存在
     */
    ARTICLE_CONTENT_NOT_EXISTS("article.content.not.exists"),

    /**
     * 专题不存在
     */
    TOPIC_NOT_EXISTS("topic.notExists"),

    /**
     * 非专题管理员不能修改专题
     */
    TOPIC_MANAGER_EDIT_NOT_MANAGER("topic.manager.edit.notManager"),

    /**
     * 专题名称重复
     */
    TOPIC_NAME_EXISTS("topic.name.exists"),

    /**
     * 文章分类已存在
     */
    ARTICLE_TYPE_EXISTS("article.type.exists"),

    /**
     * 文章分类不存在
     */
    ARTICLE_TYPE_NOT_EXISTS("article.type.notExists"),

    /**
     * 角色名称已存在
     */
    ROLE_EXISTS("role.exists"),

    /**
     * 指定名称的角色不存在
     */
    ROLE_NOT_EXISTS("role.not.exists"),

    /**
     * 用户名不存在
     */
    USER_USERNAME_NOT_EXISTS("user.username.not.exits"),

    /**
     * 用户名已存在
     */
    USER_USERNAME_EXISTS("user.username.exists"),

    /**
     * 指定编号的用户不存在
     */
    USER_ID_NOT_EXISTS("user.id.not.exists"),

    /**
     * 用户状态不是锁定状态，无需解锁
     */
    USER_STATUS_NOT_LOCKED("user.status.not.locked"),

    /**
     * 用户状态不是待审批状态，不能被审批
     */
    USER_STATUS_NOT_APPROVING("user.status.not.approving"),

    /**
     * 管理员不能被注销
     */
    USER_ADMIN_CANNOT_BE_REGISTED("user.admin.cannot.beUnregisted");

    private String errorName;
    ErrorCodes(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorName() {
        return this.errorName;
    }
}
