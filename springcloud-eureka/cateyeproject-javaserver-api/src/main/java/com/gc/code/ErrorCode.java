package com.gc.code;

public enum ErrorCode {

    CODE_SUCEESS(0),//成功
    CODE_UPLOAD_VISITOR(301),//上传访客图片错误
    CODE_INSERT_REDIS_VISITOR(302),//保存访客图片地址到redis错误
    CODE_FIND_REDIS_VISITOR_URL(303),//查询redis中visitor地址错误
    CODE_UPLOAD_WARNING(304),//上传warning错误
    CODE_INSERT_REDIS_WARNING(305),//保存warning到redis错误
    CODE_FIND_REDIS_WARNING_URL(306),//保存warning到redis错误
    CODE_FIND_REDIS_VISITOR_COUNT(307),
    CODE_FIND_REDIS_WARNING_COUNT(308),//查询redis总条数 错误 查看服务器是否是否正常启动
    CODE_FIND_REDIS_EXITS_DAY(307),//查询redis中报警或访客存在天报错
    CODE_FIND_REDIS_DELETE(308),//删除失败


    CODE_DATE_FORMAT(308);
    private final Integer value;
    ErrorCode(Integer value) {
        this.value = value;
    }
    public final Integer getValue() {
        return value;
    }
}
